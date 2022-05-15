import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {EventModel} from '../../../models/event/event.model';
import {EventService} from '../../../services/event/event.service';
import {Subscription} from 'rxjs';

@Component({
  selector: 'app-info',
  templateUrl: './info.component.html',
  styleUrls: ['./info.component.scss']
})
export class InfoComponent implements OnInit, OnDestroy {

  eventModel: EventModel;
  eventId: number;
  private subscription: Subscription = new Subscription();
  libraryContent = false;
  calendarContent = false;

  constructor(private router: Router,
              private eventService: EventService,
              private route: ActivatedRoute) {
    this.route.queryParams.subscribe(params => {
      if (params.libraryContent) {
        this.libraryContent = true;
      }
      if (params.calendarContent) {
        this.calendarContent = true;
      }
    });
    this.route.params.subscribe(params => {
      this.eventId = +params.id;
    });
  }

  ngOnInit(): void {
    this.subscription.add(
      this.eventService.findById(this.eventId)
        .subscribe(
          (result) => {
            this.eventModel = result;
          }
        )
    );
  }

  navigateToListView(): void {
    if (this.libraryContent) {
      this.router.navigateByUrl('library').then();
    } else if (this.calendarContent) {
      this.router.navigateByUrl('calendar').then();
    } else {
      this.router.navigateByUrl('meeting').then();
    }
  }

  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

}
