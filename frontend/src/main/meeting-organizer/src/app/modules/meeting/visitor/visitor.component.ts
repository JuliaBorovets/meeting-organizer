import {Component, EventEmitter, Input, OnDestroy, OnInit, Output} from '@angular/core';
import {UserModel} from '../../../models/user/user.model';
import {EventService} from '../../../services/event/event.service';
import {ToastrService} from 'ngx-toastr';
import {Subscription} from 'rxjs';
import {StorageService} from '../../../services/auth/storage.service';
import {EventModel} from '../../../models/event/event.model';
import {DialogService} from '../../../services/confirm-dialog.service';

@Component({
  selector: 'app-visitor',
  templateUrl: './visitor.component.html',
  styleUrls: ['./visitor.component.scss']
})
export class VisitorComponent implements OnInit, OnDestroy {

  @Input() user: UserModel;
  @Input() event: EventModel;
  @Output() updateEvent = new EventEmitter();
  userId: number;
  subscription: Subscription = new Subscription();

  constructor(private eventService: EventService,
              private toastrService: ToastrService,
              private dialogService: DialogService,
              private storageService: StorageService) {
    this.userId = storageService.getUser.userId;
  }

  ngOnInit(): void {
  }

  isUserHost(): boolean {
    return this.event.user.userId === this.userId;
  }

  deleteEventFromVisited(): void {
    this.dialogService.confirmDialog({
      title: 'Confirm deletion',
      message: 'Do you want to confirm deletion?',
      confirmCaption: 'Confirm',
      cancelCaption: 'Cancel',
    })
      .subscribe((confirmed) => {
        if (confirmed) {
          this.subscription.add(
            this.eventService.deleteVisitorFromEvent(this.event.eventId, this.userId)
              .subscribe(
                () => {
                  this.updateEvent.emit();
                  this.toastrService.success('Success!', 'Deleted!');
                },
                () => this.toastrService.error('Error!', 'Failed to delete!')
              )
          );
        }});
  }

  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }
}
