import {Component, OnDestroy, OnInit} from '@angular/core';
import {Observable, Subscription} from 'rxjs';
import {PageEvent} from '@angular/material/paginator';
import {LibraryModel} from '../../../models/library/library.model';
import {LibraryFilterModel} from '../../../models/library/library-filter.model';
import {MatDialog} from '@angular/material/dialog';
import {StorageService} from '../../../services/auth/storage.service';
import {LibraryResponseModel} from '../../../models/library/library-response.model';
import {CreateComponent} from '../create/create.component';
import {LibraryService} from '../../../services/library/library.service';

@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.scss']
})
export class ListComponent implements OnInit, OnDestroy {

  public resultList: LibraryModel[] = [];
  public isLoading = true;
  public isError = false;
  private subscription: Subscription = new Subscription();
  public filter: LibraryFilterModel = new LibraryFilterModel();
  pageEvent: PageEvent;
  libraryCount = 0;
  pageSizeOptions: number[] = [5, 10, 25, 50];
  userId: number;

  links = [
    {
      label: 'all',
      findAll: true
    },
    {
      label: 'my',
      findAll: false
    }
  ];
  activeLink = this.links[0];

  constructor(private libraryService: LibraryService,
              public dialog: MatDialog,
              private storageService: StorageService) {
   // this.userId = this.storageService.getUser.userId;
  }

  ngOnInit(): void {
  //  this.filter.userId = this.storageService.getUser.userId;
    this.searchLibraries();
  }

  searchLibraries() {
    this.isLoading = true;
    this.libraryCount = 0;
    // const observable: Observable<LibraryResponseModel> = this.activeLink.findAll
    //   ? this.libraryService.findAll(this.filter)
    //   : this.libraryService.findAll(this.filter);
    //
    // this.subscription.add(
    //   observable.subscribe(
    //     result => {
    //       this.resultList = result.list;
    //       this.libraryCount = result.totalItems;
    //       this.isLoading = false;
    //     },
    //     () => this.isError = true
    //   )
    // );

    const test: LibraryModel = {
      name: 'name',
      description: 'desc'
    };
    this.isLoading = false;
    this.resultList.push(test);
    this.libraryCount = 1;
  }

  openCreateDialog(): void {

    const createDialogRef = this.dialog.open(CreateComponent, {
      height: 'auto',
      width: '70%',
      data: {creator: this.storageService.getUser}
    });

    this.subscription.add(
      createDialogRef.afterClosed().subscribe(
        () => this.searchLibraries(),
        () => this.isError = true)
    );
  }

  onPaginateChange(event: PageEvent): void {
    this.filter.pageNumber = event.pageIndex;
    this.filter.pageSize = event.pageSize;
    this.filter.pageNumber = this.filter.pageNumber + 1;

    this.searchLibraries();
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }
}
