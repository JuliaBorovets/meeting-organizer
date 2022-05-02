import {CollectionViewer, SelectionChange, DataSource} from '@angular/cdk/collections';
import {FlatTreeControl} from '@angular/cdk/tree';
import {Component, Injectable, OnInit} from '@angular/core';
import {BehaviorSubject, merge, Observable, Subscription} from 'rxjs';
import {map} from 'rxjs/operators';
import {LibraryService} from '../../../services/library/library.service';
import {LibraryResponseModel} from '../../../models/library/library-response.model';
import {LibraryFilterModel} from '../../../models/library/library-filter.model';
import {EventModel} from '../../../models/event/event.model';
import {EventService} from '../../../services/event/event.service';
import {LibraryModel} from '../../../models/library/library.model';

/** Flat node with expandable and level information */
export class DynamicFlatNode {
  constructor(
    public item: any,
    public level = 1,
    public expandable = false,
    public isLoading = false,
  ) {
  }
}

@Injectable({providedIn: 'root'})
export class DynamicDatabase {

  private subscription: Subscription = new Subscription();
  public filter: LibraryFilterModel = new LibraryFilterModel();

  libraryNodes: LibraryModel[] = [];

  constructor(private libraryService: LibraryService,
              private eventService: EventService) {
     this.libraryNodes = this.searchLibraries();
  }

  initialData(): DynamicFlatNode[] {
    return this.libraryNodes.map(name => new DynamicFlatNode(name, 0, true));
  }

  searchLibraries(): LibraryModel[] {
    const observable: Observable<LibraryResponseModel> = this.libraryService.findAll(this.filter);
    const resultList = [];

    this.subscription.add(
      observable.subscribe(
        result => {
          result.list.forEach(t => resultList.push(t));
        },
        () => console.log('error')
      )
    );
    return resultList;
  }

  getChildren(node: LibraryModel | EventModel): any[] {
    const resultList = [];
    if (!(node instanceof EventModel)) {
      // this.subscription.add(
      //   this.eventService.findAllByLibraryModelId(node.libraryId)
      //     .subscribe(result => {
      //         result.list.forEach(t => resultList.push(t.name));
      //       },
      //       () => console.log('error'))
      // );
    }
    return resultList;
  }

  isExpandable(node: LibraryModel | EventModel): boolean {
    return node instanceof LibraryModel;
  }
}

export class DynamicDataSource implements DataSource<DynamicFlatNode> {
  dataChange = new BehaviorSubject<DynamicFlatNode[]>([]);

  get data(): DynamicFlatNode[] {
    return this.dataChange.value;
  }

  set data(value: DynamicFlatNode[]) {
    this._treeControl.dataNodes = value;
    this.dataChange.next(value);
  }

  constructor(
    private _treeControl: FlatTreeControl<DynamicFlatNode>,
    private _database: DynamicDatabase,
  ) {
  }

  connect(collectionViewer: CollectionViewer): Observable<DynamicFlatNode[]> {
    this._treeControl.expansionModel.changed.subscribe(change => {
       if (
         (change as SelectionChange<DynamicFlatNode>).added ||
         (change as SelectionChange<DynamicFlatNode>).removed
       ) {
        this.handleTreeControl(change as SelectionChange<DynamicFlatNode>);
       }
    });

    return merge(collectionViewer.viewChange, this.dataChange).pipe(map(() => this.data));
  }

  disconnect(collectionViewer: CollectionViewer): void {
  }

  /** Handle expand/collapse behaviors */
  handleTreeControl(change: SelectionChange<DynamicFlatNode>) {
    if (change.added) {
      change.added.forEach(node => this.toggleNode(node, true));
    }
    if (change.removed) {
      change.removed
        .slice()
        .reverse()
        .forEach(node => this.toggleNode(node, false));
    }
  }

  /**
   * Toggle the node, remove from display list
   */
  toggleNode(node: DynamicFlatNode, expand: boolean) {
    const children = this._database.getChildren(node.item);
    const index = this.data.indexOf(node);
    if (!children || index < 0) {
      // If no children, or cannot find the node, no op
      return;
    }

    node.isLoading = true;

    setTimeout(() => {
      if (expand) {
        const nodes = children.map(
          name => new DynamicFlatNode(name, node.level + 1, this._database.isExpandable(name)),
        );
        this.data.splice(index + 1, 0, ...nodes);
      } else {
        let count = 0;
        for (
          let i = index + 1;
          i < this.data.length && this.data[i].level > node.level;
          i++, count++
        ) {
        }
        this.data.splice(index + 1, count);
      }

      // notify the change
      this.dataChange.next(this.data);
      node.isLoading = false;
    }, 1000);
  }
}

/**
 * @title Tree with dynamic data
 */
@Component({
  selector: 'app-test',
  templateUrl: './test.component.html',
  styleUrls: ['./test.component.scss']
})
export class TestComponent implements OnInit {
  constructor(private database: DynamicDatabase) {
    this.treeControl = new FlatTreeControl<DynamicFlatNode>(this.getLevel, this.isExpandable);
    this.dataSource = new DynamicDataSource(this.treeControl, database);

    this.dataSource.data = database.initialData();
  }

  ngOnInit(): void {
  }

  treeControl: FlatTreeControl<DynamicFlatNode>;

  dataSource: DynamicDataSource;

  getLevel = (node: DynamicFlatNode) => node.level;

  isExpandable = (node: DynamicFlatNode) => node.expandable;

  hasChild = (_: number, _nodeData: DynamicFlatNode) => _nodeData.expandable;
}
