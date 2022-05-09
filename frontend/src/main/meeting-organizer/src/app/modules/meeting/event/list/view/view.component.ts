import {Component, EventEmitter, Input, OnDestroy, OnInit, Output} from '@angular/core';
import {EventModel} from '../../../../../models/event/event.model';
import {Subscription} from 'rxjs';
import {MatDialog} from '@angular/material/dialog';
import {DialogService} from '../../../../../services/confirm-dialog.service';
import {ToastrService} from 'ngx-toastr';
import {ActivatedRoute, Router} from '@angular/router';
import {EventService} from '../../../../../services/event/event.service';
import {UpdateEventComponent} from '../../update/update-event.component';
import {StreamService} from '../../../../../services/stream/stream.service';
import {StorageService} from '../../../../../services/auth/storage.service';

@Component({
  selector: 'app-event-view',
  templateUrl: './view.component.html',
  styleUrls: ['./view.component.scss']
})
export class ViewComponent implements OnInit, OnDestroy {

  @Input() eventModel: EventModel;
  @Output() updateEventEvent = new EventEmitter();

  private subscription: Subscription = new Subscription();
  public isError = false;
  libraryId: number;
  userId: number;

  constructor(public dialog: MatDialog,
              private dialogService: DialogService,
              private eventService: EventService,
              private toastrService: ToastrService,
              private route: ActivatedRoute,
              private streamService: StreamService,
              private storageService: StorageService,
              private router: Router) {
    this.route.params.subscribe(params => {
      this.libraryId = +params.libraryId;
    });
    this.userId = this.storageService.getUser.userId;
  }

  ngOnInit(): void {
  }

  openUpdateDialog(): void {
    const updateDialogRef = this.dialog.open(UpdateEventComponent, {
      height: 'auto',
      width: '65vh',
      data: {
        eventModel: this.eventModel,
        libraryId: this.libraryId
      }
    });

    this.subscription.add(
      updateDialogRef.afterClosed().subscribe(
        () => this.updateEventEvent.emit(),
        () => this.isError = true)
    );
  }

  deleteEvent(): void {
    this.dialogService.confirmDialog({
      title: 'Confirm deletion',
      message: 'Do you want to confirm this action?',
      confirmCaption: 'Confirm',
      cancelCaption: 'Cancel',
    })
      .subscribe((confirmed) => {
        if (confirmed) {
          this.eventService.deleteEvent(this.eventModel.eventId).subscribe(
            () => {
              this.toastrService.success('Success!', 'Deleted!');
              this.updateEventEvent.emit();
            },
            () => this.toastrService.error('Error!', 'Deletion failed!')
          );
        }
      });
  }

  deleteEventFormStream(): void {
    this.dialogService.confirmDialog({
      title: 'Confirm deletion from stream',
      message: 'Do you want to confirm this action?',
      confirmCaption: 'Confirm',
      cancelCaption: 'Cancel',
    })
      .subscribe((confirmed) => {
        if (confirmed) {
          this.streamService.deleteEventFromStream(this.eventModel.eventId, this.eventModel.streamId).subscribe(
            () => {
              this.toastrService.success('Success!', 'Deleted!');
              this.updateEventEvent.emit();
            },
            () => this.toastrService.error('Error!', 'Deletion failed!')
          );
        }
      });
  }

  addToFavorites(): void {
    this.subscription.add(
      this.eventService.addEventToFavorites(this.eventModel.eventId, this.userId)
        .subscribe(
          () => {
            this.toastrService.success('Success!', 'Added!');
            this.updateEventEvent.emit();
          },
          () => this.toastrService.error('Error!', 'Failed to add!')
        )
    );
  }

  deleteFromFavorites(): void {
    this.subscription.add(
      this.eventService.deleteEventFromFavorites(this.eventModel.eventId, this.userId)
        .subscribe(
          () => {
            this.toastrService.success('Success!', 'Deleted!');
            this.updateEventEvent.emit();
          },
          () => this.toastrService.error('Error!', 'Failed to delete!')
        )
    );
  }

  navigateToInfoPage(): void {
    if (this.libraryId) {
      this.router.navigateByUrl(`meeting/info/${this.eventModel.eventId}?libraryContent=true`).then(() => {});
    } else {
      this.router.navigateByUrl(`meeting/info/${this.eventModel.eventId}`).then(() => {});
    }
  }

  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

}
