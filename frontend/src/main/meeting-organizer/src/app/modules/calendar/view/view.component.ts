import {Component, OnDestroy, OnInit} from '@angular/core';
import {CalendarOptions, EventClickArg, EventInput} from '@fullcalendar/angular';
import {Subscription} from 'rxjs';
import {StorageService} from '../../../services/auth/storage.service';
import {CalendarService} from '../../../services/calendar.service';
import {ToastrService} from 'ngx-toastr';
import {Router} from '@angular/router';

@Component({
  selector: 'app-calendar-view',
  templateUrl: './view.component.html',
  styleUrls: ['./view.component.scss']
})
export class ViewComponent implements OnInit, OnDestroy {

  viewDate: Date = new Date();

  public resultList: EventInput[] = [];
  public isLoading = true;
  private subscription: Subscription = new Subscription();
  private userId: number;

  calendarVisible = true;
  calendarOptions: CalendarOptions = {
    headerToolbar: {
      left: 'prev,next today',
      center: 'title',
      right: 'dayGridMonth,timeGridWeek,timeGridDay,listWeek'
    },
    initialView: 'dayGridMonth',
    initialEvents: this.resultList,
    weekends: true,
    editable: true,
    selectable: true,
    selectMirror: true,
    dayMaxEvents: true,
    eventClick: this.handleEventClick.bind(this),
  };

  constructor(private calendarService: CalendarService,
              private storageService: StorageService,
              private toastrService: ToastrService,
              private router: Router) {
    this.userId = this.storageService.getUser.userId;
  }

  ngOnInit(): void {
    this.searchCalendarEvents();
  }

  searchCalendarEvents() {
    this.isLoading = true;

    this.subscription.add(
      this.calendarService.findAllByUser(this.userId).subscribe(
        result => {
          this.isLoading = false;
          this.calendarOptions.events = result;
        },
        () => this.toastrService.error('Error!', 'Fetch failed!')
      )
    );
  }

  handleEventClick(clickInfo: EventClickArg) {
    clickInfo.jsEvent.preventDefault();
    this.router.navigateByUrl(`meeting/info/${clickInfo.event.id}?calendarContent=true`).then(() => {
    });
  }

  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

}
