import {Component, OnDestroy, OnInit} from '@angular/core';
import {PageEvent} from '@angular/material/paginator';
import {Subscription} from 'rxjs';
import {ActivatedRoute} from '@angular/router';
import {EventFilterModel} from '../../../../models/event/event-filter.model';
import {EventModel} from '../../../../models/event/event.model';
import {EventService} from '../../../../services/event/event.service';
import {StorageService} from '../../../../services/auth/storage.service';

@Component({
  selector: 'app-event-list',
  templateUrl: './event-list.component.html',
  styleUrls: ['./event-list.component.scss']
})
export class EventListComponent implements OnInit, OnDestroy {

  eventCount = 0;
  isError = false;
  pageSizeOptions: number[] = [5, 10, 25, 50];
  public eventFilter: EventFilterModel = new EventFilterModel();
  private libraryId: number;
  private streamId: number;
  public isLoading = true;
  pageEvent: PageEvent;
  eventList: EventModel[] = [];
  private subscription: Subscription = new Subscription();

  constructor(private eventService: EventService,
              private route: ActivatedRoute,
              private storageService: StorageService) {
    this.route.params.subscribe(params => {
      this.libraryId = +params.libraryId;
      this.streamId = +params.streamId;
      this.eventFilter.libraryId = this.libraryId;
      this.eventFilter.streamId = this.streamId;
      this.eventFilter.userId = this.storageService.getUser.userId;
    });
  }

  ngOnInit(): void {
    this.findEventListByLibraryId();
  }

  findEventListByLibraryId(): void {
    this.isLoading = true;
    this.eventCount = 0;
    this.subscription.add(
      this.eventService.findAllByLibraryId(this.eventFilter)
        .subscribe(result => {
            this.eventList = result.list;
            this.eventCount = result.totalItems;
            this.isLoading = false;
          },
          () => this.isError = true)
    );
  }

  onPaginateChange(event: PageEvent): void {
    this.eventFilter.pageNumber = event.pageIndex;
    this.eventFilter.pageSize = event.pageSize;
    this.eventFilter.pageNumber = this.eventFilter.pageNumber + 1;

    this.findEventListByLibraryId();
  }

  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }
}
