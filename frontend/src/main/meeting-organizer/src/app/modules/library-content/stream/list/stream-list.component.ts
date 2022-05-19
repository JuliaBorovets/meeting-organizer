import {Component, OnDestroy, OnInit} from '@angular/core';
import {StreamFilterModel} from '../../../../models/stream/stream-filter.model';
import {StreamService} from '../../../../services/stream/stream.service';
import {ActivatedRoute} from '@angular/router';
import {PageEvent} from '@angular/material/paginator';
import {Subscription} from 'rxjs';
import {StreamModel} from '../../../../models/stream/stream.model';
import {FormBuilder, FormGroup} from '@angular/forms';
import {debounceTime} from 'rxjs/operators';

@Component({
  selector: 'app-stream-list',
  templateUrl: './stream-list.component.html',
  styleUrls: ['./stream-list.component.scss']
})
export class StreamListComponent implements OnInit, OnDestroy {

  streamCount = 0;
  isError = false;
  pageSizeOptions: number[] = [5, 10, 25, 50];
  public streamFilter: StreamFilterModel = new StreamFilterModel();
  private libraryId: number;
  public isLoading = true;
  pageEvent: PageEvent;
  streamList: StreamModel[] = [];
  private subscription: Subscription = new Subscription();
  searchForm: FormGroup;
  submitted = false;

  constructor(private streamService: StreamService,
              private formBuilder: FormBuilder,
              private route: ActivatedRoute) {
    this.route.params.subscribe(params => {
      this.libraryId = +params.libraryId;
      this.streamFilter.libraryId = this.libraryId;
    });
  }

  ngOnInit(): void {
    this.findStreamListByLibraryId();
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
        this.streamFilter.streamName = dataValue;
        this.findStreamListByLibraryId();
      });
  }

  get f() {
    return this.searchForm.controls;
  }

  findStreamListByLibraryId(): void {
    this.isLoading = true;
    this.streamCount = 0;
    this.subscription.add(
      this.streamService.findAllByLibraryId(this.streamFilter)
        .subscribe(result => {
            this.streamList = result.list;
            this.streamCount = result.totalItems;
            this.isLoading = false;
          },
          () => this.isError = true)
    );
  }

  onPaginateChange(event: PageEvent): void {
    this.streamFilter.pageNumber = event.pageIndex;
    this.streamFilter.pageSize = event.pageSize;
    this.streamFilter.pageNumber = this.streamFilter.pageNumber + 1;

    this.findStreamListByLibraryId();
  }

  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }
}
