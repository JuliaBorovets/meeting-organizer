import {Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {MatDialog} from '@angular/material/dialog';
import {StorageService} from '../../../../services/auth/storage.service';
import {CreateEventComponent} from '../../event/create/create.component';
import {Subscription} from 'rxjs';
import {EventListComponent} from "../../event/list/event-list.component";
import {StreamListComponent} from "../list/stream-list.component";
import {AddEventComponent} from "../add-event/add-event.component";

@Component({
  selector: 'app-content',
  templateUrl: './content.component.html',
  styleUrls: ['./content.component.scss']
})
export class ContentComponent implements OnInit, OnDestroy {

  isLoading = false;
  libraryId: number;
  streamId: number;
  subscription: Subscription;
  @ViewChild(EventListComponent, {static: false})
  private eventListComponent: EventListComponent;

  constructor(private route: ActivatedRoute,
              private router: Router,
              public dialog: MatDialog,
              private storageService: StorageService) {
    this.route.params.subscribe(params => {
      this.libraryId = +params.libraryId;
      this.streamId = +params.streamId;
    });
    this.subscription = new Subscription();
  }

  ngOnInit(): void {
  }

  openCreateEventView(): void {
    const creatEventDialogRef = this.dialog.open(CreateEventComponent, {
      height: '90vh',
      width: 'auto',
      data: {
        libraryId: this.libraryId,
        streamId: this.streamId
      }
    });

    this.subscription.add(
      creatEventDialogRef.afterClosed().subscribe(
        () => this.updateContent()
      ));
  }

  openAddEventView(): void {
    const creatEventDialogRef = this.dialog.open(AddEventComponent, {
      height: '90vh',
      width: 'auto',
      data: {
        libraryId: this.libraryId,
        streamId: this.streamId,
      }
    });

    this.subscription.add(
      creatEventDialogRef.afterClosed().subscribe(
        () => this.updateContent()
      ));
  }

  navigateToListView(): void {
    this.router.navigate(['library-content/view/' + this.libraryId]).then();
  }

  updateContent(): void {
    this.eventListComponent.findEventListByLibraryId();
  }

  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }
}
