import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {PageEvent} from '@angular/material/paginator';
import {Observable, Subscription} from 'rxjs';
import {ActivatedRoute} from '@angular/router';
import {EventFilterModel} from '../../../../models/event/event-filter.model';
import {EventModel} from '../../../../models/event/event.model';
import {EventService} from '../../../../services/event/event.service';
import {StorageService} from '../../../../services/auth/storage.service';
import {LibraryResponseModel} from '../../../../models/library/library-response.model';
import {CreateEventComponent} from '../create/create.component';
import {MatDialog} from '@angular/material/dialog';
import {FormBuilder, FormGroup} from '@angular/forms';
import {debounceTime} from 'rxjs/operators';
import {AddEventAccessCodeComponent} from '../../add-access-code/add-access-code.component';

@Component({
  selector: 'app-event-list',
  templateUrl: './event-list.component.html',
  styleUrls: ['./event-list.component.scss']
})
export class EventListComponent implements OnInit, OnDestroy {

  @Input() showTabs = false;
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
  searchForm: FormGroup;
  submitted = false;

  constructor(private eventService: EventService,
              private route: ActivatedRoute,
              private storageService: StorageService,
              private formBuilder: FormBuilder,
              public dialog: MatDialog) {
    this.route.params.subscribe(params => {
      this.libraryId = +params.libraryId;
      this.streamId = +params.streamId;
      this.eventFilter.libraryId = this.libraryId;
      this.eventFilter.streamId = this.streamId;
      this.eventFilter.userId = this.storageService.getUser.userId;
    });
  }

  ngOnInit(): void {
    this.findEvents();
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
        this.eventFilter.eventName = dataValue;
        this.findEvents();
      });
  }

  get f() {
    return this.searchForm.controls;
  }

  findEvents(): void {
    this.isLoading = true;
    this.eventCount = 0;
    let observable: Observable<LibraryResponseModel>;

    if (this.libraryId) {
      observable = this.eventService.findAllByLibraryId(this.eventFilter);
    } else {
      if (this.isFindAllSelected()) {
        observable = this.eventService.findAll(this.eventFilter);
      } else if (this.isFindMySelected()) {
        observable = this.eventService.findAllUser(this.eventFilter);
      } else if (this.isFindAccessSelected()) {
        observable = this.eventService.findAllUserAccess(this.eventFilter);
      }
    }

    this.subscription.add(
      observable
        .subscribe(result => {
            this.eventList = result.list;
            this.eventCount = result.totalItems;
            this.isLoading = false;
          },
          () => this.isError = true)
    );
  }

  openCreateEventView(): void {
    const creatEventDialogRef = this.dialog.open(CreateEventComponent, {
      height: '80vh',
      width: 'auto',
      data: {libraryId: this.libraryId}
    });

    this.subscription.add(
      creatEventDialogRef.afterClosed().subscribe(
        () => this.findEvents()
      ));
  }

  onPaginateChange(event: PageEvent): void {
    this.eventFilter.pageNumber = event.pageIndex;
    this.eventFilter.pageSize = event.pageSize;
    this.eventFilter.pageNumber = this.eventFilter.pageNumber + 1;

    this.findEvents();
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

  openAddAccessCodeDialog(): void {
    const addAccessDialogRef = this.dialog.open(AddEventAccessCodeComponent, {
      height: 'auto',
      width: '65vh'
    });

    this.subscription.add(
      addAccessDialogRef.afterClosed().subscribe(
        () => this.findEvents(),
        () => this.isError = true)
    );
  }

  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }
}
