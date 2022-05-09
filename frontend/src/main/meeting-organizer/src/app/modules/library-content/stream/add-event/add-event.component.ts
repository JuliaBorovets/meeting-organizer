import {COMMA, ENTER} from '@angular/cdk/keycodes';
import {Component, ElementRef, Inject, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {FormControl} from '@angular/forms';
import {MatAutocompleteSelectedEvent} from '@angular/material/autocomplete';
import {MatChipInputEvent} from '@angular/material/chips';
import {iif, Observable, of, Subscription} from 'rxjs';
import {map, startWith, mergeMap} from 'rxjs/operators';
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from '@angular/material/dialog';
import {EventService} from '../../../../services/event/event.service';
import {EventModel} from '../../../../models/event/event.model';
import {StreamService} from '../../../../services/stream/stream.service';
import {StorageService} from '../../../../services/auth/storage.service';

@Component({
  selector: 'app-add-event',
  templateUrl: './add-event.component.html',
  styleUrls: ['./add-event.component.scss']
})
export class AddEventComponent implements OnInit, OnDestroy {

  separatorKeysCodes: number[] = [ENTER, COMMA];
  eventsFormCtrl = new FormControl();
  filteredEvents: Observable<EventModel[]>;
  eventsToAdd: EventModel[] = [];
  allEvents: EventModel[] = [];
  isUpdateFailed = false;
  libraryId: number;
  streamId: number;
  userId: number;
  name = '';
  subscription: Subscription;

  @ViewChild('eventsInput') eventsInput: ElementRef<HTMLInputElement>;

  constructor(public dialogRef: MatDialogRef<AddEventComponent>,
              public dialog: MatDialog,
              @Inject(MAT_DIALOG_DATA) public data: any,
              private streamService: StreamService,
              private eventService: EventService,
              private storageService: StorageService) {
    this.libraryId = data.libraryId;
    this.streamId = data.streamId;
    this.userId = this.storageService.getUser.userId;
    this.subscription = new Subscription();
    this.eventsFormCtrl.valueChanges.pipe(
      startWith(null),
      map((event: string | null) => {
        return this.findEvents(event)
          .pipe(mergeMap(e => iif(() => event != null, of(e.slice()), of(e.slice()))));
      }),
    ).subscribe(result => this.filteredEvents = result,
      err => console.log(err));
  }

  ngOnInit(): void {
    this.findEvents('');
  }

  findEvents(event: string): Observable<EventModel[]> {
    return this.eventService.findAllByStreamNotContaining(this.userId, this.libraryId, this.streamId, event);
  }

  add(event: MatChipInputEvent): void {
    const value = (event.value || '').trim();
    const foundValue = this.allEvents
      .find(e => e.name === value);
    if (value) {
      this.eventsToAdd.push(foundValue);
    }
    if (event.input) {
      event.input.value = '';
    }
    this.eventsFormCtrl.setValue(null);
  }

  remove(event: EventModel): void {
    const index = this.eventsToAdd.indexOf(event);
    if (index >= 0) {
      this.eventsToAdd.splice(index, 1);
    }
  }

  selected(event: MatAutocompleteSelectedEvent): void {
    this.eventsToAdd.push(event.option.value);
    this.eventsInput.nativeElement.value = '';
    this.eventsFormCtrl.setValue(null);
  }

  deleteFromMainList(): void {

  }

  onSubmit(): void {
    const eventsIdToAdd = this.eventsToAdd
      .map(e => String(e.eventId));
    this.subscription.add(
      this.streamService.addEventToStream(this.streamId, eventsIdToAdd).subscribe(
        () => {
          console.log('onSubmit');
          this.dialogRef.close();
        },
        () => console.log('error')
      ));
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }
}
