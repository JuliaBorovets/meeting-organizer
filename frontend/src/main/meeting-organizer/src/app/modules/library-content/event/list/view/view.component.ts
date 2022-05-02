import {Component, EventEmitter, Input, OnDestroy, OnInit, Output} from '@angular/core';
import {EventModel} from '../../../../../models/event/event.model';
import {Subscription} from 'rxjs';
import {MatDialog} from '@angular/material/dialog';
import {DialogService} from '../../../../../services/confirm-dialog.service';
import {ToastrService} from 'ngx-toastr';
import {ActivatedRoute} from '@angular/router';
import {EventService} from '../../../../../services/event/event.service';
import {UpdateEventComponent} from '../../update/update-event.component';
import {StreamService} from "../../../../../services/stream/stream.service";
import {InfoComponent} from "../info/info.component";

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

  constructor(public dialog: MatDialog,
              private dialogService: DialogService,
              private eventService: EventService,
              private toastrService: ToastrService,
              private route: ActivatedRoute,
              private streamService: StreamService) {
    this.route.params.subscribe(params => {
      this.libraryId = +params.libraryId;
    });
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

  openInfoDialog(): void {
    const infoDialogRef = this.dialog.open(InfoComponent, {
      height: 'auto',
      width: '65vh',
      data: {
        eventModel: this.eventModel
      }
    });

    this.subscription.add(
      infoDialogRef.afterClosed().subscribe()
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

  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

}
