import {Component, Input, OnInit} from '@angular/core';
import {EventModel} from '../../../../../models/event/event.model';

@Component({
  selector: 'app-event-view',
  templateUrl: './view.component.html',
  styleUrls: ['./view.component.scss']
})
export class ViewComponent implements OnInit {

  @Input() eventModel: EventModel;

  constructor() { }

  ngOnInit(): void {
  }

}
