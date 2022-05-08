import {Component, OnDestroy, OnInit} from '@angular/core';
import {Subscription} from 'rxjs';
import {PageEvent} from '@angular/material/paginator';
import {StorageService} from '../../../../services/auth/storage.service';
import {EventModel} from '../../../../models/event/event.model';
import {EventFilterModel} from '../../../../models/event/event-filter.model';
import {EventService} from '../../../../services/event/event.service';

@Component({
  selector: 'app-event',
  templateUrl: './event.component.html',
  styleUrls: ['./event.component.scss']
})
export class EventComponent implements OnInit, OnDestroy {

  public resultList: EventModel[] = [];
  public isLoading = true;
  public isError = false;
  private subscription: Subscription = new Subscription();
  public filter: EventFilterModel = new EventFilterModel();
  pageEvent: PageEvent;
  eventCount = 0;
  pageSizeOptions: number[] = [5, 10, 25, 50];

  constructor(private eventService: EventService,
              private storageService: StorageService) {
    this.filter.userId = this.storageService.getUser.userId;
  }

  ngOnInit(): void {
    this.findFavorites();
  }

  findFavorites(): void {
    this.isLoading = true;
    this.eventCount = 0;

    this.subscription.add(
      this.eventService.findUserFavorites(this.filter).subscribe(
        result => {
          this.resultList = result.list;
          this.eventCount = result.totalItems;
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
