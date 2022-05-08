import {Component, EventEmitter, Input, OnDestroy, OnInit, Output} from '@angular/core';
import {Subscription} from 'rxjs';
import {ToastrService} from 'ngx-toastr';
import {StorageService} from '../../../../../services/auth/storage.service';
import {EventModel} from '../../../../../models/event/event.model';
import {EventService} from '../../../../../services/event/event.service';

@Component({
  selector: 'app-event-view-fav',
  templateUrl: './view.component.html',
  styleUrls: ['./view.component.scss']
})
export class EventViewComponent implements OnInit, OnDestroy {

  @Input() eventItem: EventModel;
  @Output() updateEventEvent = new EventEmitter();
  private subscription: Subscription = new Subscription();
  private userId: number;

  constructor(private eventService: EventService,
              private toastrService: ToastrService,
              private storageService: StorageService) {
    this.userId = this.storageService.getUser.userId;
  }

  ngOnInit(): void {
  }

  deleteFromFavorites(): void {
    this.subscription.add(
      this.eventService.deleteEventFromFavorites(this.eventItem.eventId, this.userId)
        .subscribe(
          () => {
            this.toastrService.success('Success!', 'Deleted!');
            this.updateEventEvent.emit();
          },
          () => this.toastrService.error('Error!', 'Failed to delete!')
        )
    );
  }

  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }
}
