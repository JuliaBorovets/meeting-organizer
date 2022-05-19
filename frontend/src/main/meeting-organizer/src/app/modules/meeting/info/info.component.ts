import {Component, Inject, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {EventModel} from '../../../models/event/event.model';
import {EventService} from '../../../services/event/event.service';
import {of, Subscription} from 'rxjs';
import {CommentModel} from '../../../models/reaction/comment.model';
import {ReactionService} from '../../../services/reaction/reaction.service';
import {CommentFilterModel} from '../../../models/reaction/comment-filter.model';
import {ToastrService} from 'ngx-toastr';
import {PageEvent} from '@angular/material/paginator';
import {FormBuilder, FormGroup} from '@angular/forms';
import {StorageService} from '../../../services/auth/storage.service';
import {UserModel} from '../../../models/user/user.model';
import {AttendeesFilterModel} from '../../../models/event/attendees-filter.model';
import {debounceTime, map, mergeMap} from 'rxjs/operators';
import {DOCUMENT} from '@angular/common';

@Component({
  selector: 'app-info',
  templateUrl: './info.component.html',
  styleUrls: ['./info.component.scss']
})
export class InfoComponent implements OnInit, OnDestroy {

  eventModel: EventModel;
  eventId: number;
  userId: number;
  subscription: Subscription = new Subscription();
  libraryContent = false;
  calendarContent = false;
  favoritesContent = false;
  pageEvent: PageEvent;
  comments: CommentModel[] = [];
  attendees: UserModel[] = [];
  isLoading = true;
  commentsCount = 0;
  attendeesCount = 0;
  pageSizeOptions: number[] = [5, 10, 25, 50];
  filter: CommentFilterModel = new CommentFilterModel();
  attendeesFilter: AttendeesFilterModel = new AttendeesFilterModel();
  searchForm: FormGroup;
  createCommentForm: FormGroup;
  submitted = false;

  constructor(private router: Router,
              private eventService: EventService,
              private route: ActivatedRoute,
              private formBuilder: FormBuilder,
              private reactionService: ReactionService,
              @Inject(DOCUMENT) private document: Document,
              private storageService: StorageService,
              private toastrService: ToastrService) {
    this.route.queryParams.subscribe(params => {
      if (params.libraryContent) {
        this.libraryContent = true;
      }
      if (params.calendarContent) {
        this.calendarContent = true;
      }
      if (params.eventFavorite) {
        this.favoritesContent = true;
      }
    });
    this.route.params.subscribe(params => {
      this.eventId = +params.id;
      this.filter.eventId = this.eventId;
    });
    this.userId = storageService.getUser.userId;
    this.attendeesFilter.eventId = this.eventId;
  }

  ngOnInit(): void {
    this.isLoading = true;
    this.findEvent();
    this.searchComments();
    this.searchAttendees();
    this.createCreateCommentForm();
    this.createSearchForm();
  }

  findEvent() {
    this.subscription.add(
      this.eventService.findById(this.eventId)
        .subscribe(
          (result) => {
            this.eventModel = result;
            this.isLoading = false;
          }
        )
    );
  }

  createSearchForm() {
    this.searchForm = this.formBuilder.group({
      search: [null, null],
    });

    this.searchForm.get('search')
      .valueChanges
      .pipe(debounceTime(500))
      .subscribe(dataValue => {
        this.attendeesFilter.username = dataValue;
        this.searchAttendees();
      });
  }

  get attendersF() {
    return this.searchForm.controls;
  }

  createCreateCommentForm(): void {
    this.createCommentForm = this.formBuilder.group({
      text: ['', null]
    });
  }

  get f() {
    return this.createCommentForm.controls;
  }

  searchComments() {
    this.subscription.add(
      this.reactionService.findAllByEvent(this.filter)
        .subscribe(
          result => {
            this.commentsCount = result.totalItems;
            this.comments = result.list;
          },
          () => this.toastrService.error('Error!', 'Fetch failed!')
        )
    );
  }

  searchAttendees() {
    this.subscription.add(
      this.eventService.findAttendeesByEvent(this.attendeesFilter)
        .subscribe(
          result => {
            this.attendeesCount = result.totalItems;
            this.attendees = result.list;
          },
          () => this.toastrService.error('Error!', 'Fetch failed!')
        )
    );
  }

  createComment() {
    this.submitted = true;
    if (this.createCommentForm.invalid || this.f.text.value.trim() === '') {
      return;
    }

    const createRequest = {
      userId: this.userId,
      eventId: this.eventId,
      text: this.f.text.value
    };

    this.subscription.add(
      this.reactionService.createComment(createRequest)
        .pipe(mergeMap(() => this.reactionService.findAllByEvent(this.filter)))
        .subscribe(
          (result) => {
            this.commentsCount = result.totalItems;
            this.comments = result.list;
            this.createCommentForm.reset();
          },
          () => this.toastrService.error('Error!', 'Failed to add a comment!')
        ));
  }

  onPaginateChange(event: PageEvent): void {
    this.filter.pageNumber = event.pageIndex;
    this.filter.pageSize = event.pageSize;
    this.filter.pageNumber = this.filter.pageNumber + 1;

    this.searchComments();
  }

  navigateToListView(): void {
    if (this.libraryContent) {
      this.router.navigateByUrl('library').then();
    } else if (this.calendarContent) {
      this.router.navigateByUrl('calendar').then();
    } else if (this.favoritesContent) {
      this.router.navigateByUrl('favorite').then();
    } else {
      this.router.navigateByUrl('meeting').then();
    }
  }

  showDisabledJoinButton(): boolean {
    return this.eventModel.participantCount >= this.eventModel.maxNumberParticipants;
  }

  joinEvent() {
    this.subscription.add(
      this.eventService.addVisitorToEvent(this.eventId, this.userId)
        .pipe(mergeMap(() => this.eventService.findAttendeesByEvent(this.filter).pipe(
          map((res) => {
            this.attendeesCount = res.totalItems;
            this.attendees = res.list;
            return of({});
          }))))
        .pipe(mergeMap(() => this.eventService.findById(this.eventId)))
        .subscribe(
          (res) => {
            this.eventModel = res;
            this.toastrService.success('Success!', 'Added!');
          },
          () => this.toastrService.error('Error!', 'Failed to add!')
        )
    );
  }

  deleteEventFromVisited(): void {
    this.subscription.add(
      this.eventService.deleteVisitorFromEvent(this.eventId, this.userId)
        .pipe(mergeMap(() => this.eventService.findAttendeesByEvent(this.filter).pipe(
          map((res) => {
            this.attendeesCount = res.totalItems;
            this.attendees = res.list;
            return of({});
          }))))
        .pipe(mergeMap(() => this.eventService.findById(this.eventId)))
        .subscribe(
          (res) => {
            this.eventModel = res;
            this.toastrService.success('Success!', 'Deleted!');
          },
          () => this.toastrService.error('Error!', 'Failed to delete!')
        )
    );
  }

  isUserAlreadyJoinedEvent(): boolean {
    return this.attendees.map(a => a.userId).includes(this.userId);
  }

  openMeeting(): void {
    const link = this.document.createElement('a');
    link.target = '_blank';
    link.href = this.eventModel.joinUrl;
    link.click();
    link.remove();
  }

  startMeeting(): void {
    const link = this.document.createElement('a');
    link.target = '_blank';
    link.href = this.eventModel.meetingEntity.start_url;
    link.click();
    link.remove();
  }

  isUserHost(): boolean {
    return this.eventModel.user.userId === this.userId;
  }

  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

}
