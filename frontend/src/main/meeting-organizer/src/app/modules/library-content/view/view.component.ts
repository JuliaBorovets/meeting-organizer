import {Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {Router, ActivatedRoute} from '@angular/router';
import {Subscription} from 'rxjs';
import {MatDialog} from '@angular/material/dialog';
import {CreateStreamComponent} from '../stream/create/create.component';
import {CreateEventComponent} from '../event/create/create.component';
import {StreamListComponent} from '../stream/list/stream-list.component';
import {EventListComponent} from '../event/list/event-list.component';

@Component({
  selector: 'app-view',
  templateUrl: './view.component.html',
  styleUrls: ['./view.component.scss']
})
export class ViewComponent implements OnInit, OnDestroy {

  isLoading = false;
  isError = false;
  private subscription: Subscription = new Subscription();
  private libraryId: number;
  @ViewChild(StreamListComponent, {static: false})
  private streamListComponent: StreamListComponent;
  @ViewChild(EventListComponent, {static: false})
  private eventListComponent: EventListComponent;

  links = [
    {
      label: 'Streams'
    },
    {
      label: 'Events'
    }
  ];
  activeLink = this.links[0];

  constructor(private router: Router,
              public dialog: MatDialog,
              private route: ActivatedRoute) {
    this.route.params.subscribe(params => {
      this.libraryId = +params.libraryId;
    });
  }

  ngOnInit(): void {
  }

  updateStreamList(): void {
    this.streamListComponent.findStreamListByLibraryId();
  }

  updateEventList(): void {
    this.eventListComponent.findEventListByLibraryId();
  }

  navigateToListView(): void {
    this.router.navigateByUrl('library').then();
  }

  openCreateStreamView(): void {
    const creatStreamDialogRef = this.dialog.open(CreateStreamComponent, {
      height: 'auto',
      width: '65vh',
      data: {libraryId: this.libraryId}
    });

    this.subscription.add(
      creatStreamDialogRef.afterClosed().subscribe(
        () => this.updateStreamList()
      ));
  }

  openCreateEventView(): void {
    const creatEventDialogRef = this.dialog.open(CreateEventComponent, {
      height: '80vh',
      width: 'auto',
      data: {libraryId: this.libraryId}
    });

    this.subscription.add(
      creatEventDialogRef.afterClosed().subscribe(
        () => this.updateEventList()
      ));
  }

  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }
}
