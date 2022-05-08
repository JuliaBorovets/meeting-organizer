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
import {AddAccessCodeComponent} from "./add-access-code/add-access-code.component";

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
      label: 'All'
    },
    {
      label: 'My'
    },
    {
      label: 'Given Access'
    }
  ];
  activeLink = this.links[0];

  constructor(private libraryService: LibraryService,
              public dialog: MatDialog,
              private storageService: StorageService) {
    this.userId = this.storageService.getUser.userId;
  }

  ngOnInit(): void {
    this.filter.userId = this.storageService.getUser.userId;
    this.searchLibraries();
  }

  searchLibraries() {
    this.isLoading = true;
    this.libraryCount = 0;
    let observable: Observable<LibraryResponseModel>;

    if (this.isFindAllSelected()) {
      observable = this.libraryService.findAll(this.filter);
    } else if (this.isFindMySelected()) {
      observable = this.libraryService.findAllUser(this.filter);
    } else if (this.isFindAccessSelected()) {
      observable = this.libraryService.findAllUserAccess(this.filter);
    }

    this.subscription.add(
      observable.subscribe(
        result => {
          this.resultList = result.list;
          this.libraryCount = result.totalItems;
          this.isLoading = false;
        },
        () => this.isError = true
      )
    );
  }

  openCreateDialog(): void {

    const createDialogRef = this.dialog.open(CreateComponent, {
      height: 'auto',
      width: '65vh',
      data: {creator: this.storageService.getUser}
    });

    this.subscription.add(
      createDialogRef.afterClosed().subscribe(
        () => this.searchLibraries(),
        () => this.isError = true)
    );
  }

  openAddAccessCodeDialog(): void {
    const addAccessDialogRef = this.dialog.open(AddAccessCodeComponent, {
      height: 'auto',
      width: '65vh'
    });

    this.subscription.add(
      addAccessDialogRef.afterClosed().subscribe(
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

  isFindAllSelected(): boolean {
    return this.activeLink === this.links[0];
  }

  isFindMySelected(): boolean {
    return this.activeLink === this.links[1];
  }

  isFindAccessSelected(): boolean {
    return this.activeLink === this.links[2];
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }
}
