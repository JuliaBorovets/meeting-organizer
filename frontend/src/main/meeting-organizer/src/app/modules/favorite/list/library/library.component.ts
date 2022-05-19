import {Component, OnDestroy, OnInit} from '@angular/core';
import {LibraryService} from '../../../../services/library/library.service';
import {LibraryModel} from '../../../../models/library/library.model';
import {Subscription} from 'rxjs';
import {LibraryFilterModel} from '../../../../models/library/library-filter.model';
import {PageEvent} from '@angular/material/paginator';
import {StorageService} from '../../../../services/auth/storage.service';
import {FormBuilder, FormGroup} from '@angular/forms';
import {debounceTime} from "rxjs/operators";

@Component({
  selector: 'app-library',
  templateUrl: './library.component.html',
  styleUrls: ['./library.component.scss']
})
export class LibraryComponent implements OnInit, OnDestroy {
  public resultList: LibraryModel[] = [];
  public isLoading = true;
  public isError = false;
  private subscription: Subscription = new Subscription();
  public filter: LibraryFilterModel = new LibraryFilterModel();
  pageEvent: PageEvent;
  libraryCount = 0;
  pageSizeOptions: number[] = [5, 10, 25, 50];
  searchForm: FormGroup;
  submitted = false;

  constructor(private libraryService: LibraryService,
              private formBuilder: FormBuilder,
              private storageService: StorageService) {
    this.filter.userId = this.storageService.getUser.userId;
  }

  ngOnInit(): void {
    this.findFavorites();
    this.createSearchForm();
  }


  createSearchForm() {
    this.searchForm = this.formBuilder.group({
      search: [null, null],
    });

    this.searchForm.get('search')
      .valueChanges
      .pipe(debounceTime(500))
      .subscribe(dataValue => {
        this.filter.libraryName = dataValue;
        this.findFavorites();
      });
  }

  get f() {
    return this.searchForm.controls;
  }

  findFavorites(): void {
    this.isLoading = true;
    this.libraryCount = 0;

    this.subscription.add(
      this.libraryService.findUserFavorites(this.filter).subscribe(
        result => {
          this.resultList = result.list;
          this.libraryCount = result.totalItems;
          this.isLoading = false;
        },
        () => this.isError = true
      )
    );
  }

  onPaginateChange(event: PageEvent): void {
    this.filter.pageNumber = event.pageIndex;
    this.filter.pageSize = event.pageSize;
    this.filter.pageNumber = this.filter.pageNumber + 1;

    this.findFavorites();
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

}
