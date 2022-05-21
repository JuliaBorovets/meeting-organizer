import {Component, ElementRef, Inject, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {COMMA, ENTER} from '@angular/cdk/keycodes';
import {FormControl} from '@angular/forms';
import {iif, Observable, of, Subscription} from 'rxjs';
import {EventModel} from '../../../models/event/event.model';
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from '@angular/material/dialog';
import {EventService} from '../../../services/event/event.service';
import {StorageService} from '../../../services/auth/storage.service';
import {map, mergeMap, startWith} from 'rxjs/operators';
import {MatChipInputEvent} from '@angular/material/chips';
import {MatAutocompleteSelectedEvent} from '@angular/material/autocomplete';
import {LibraryService} from '../../../services/library/library.service';

@Component({
  selector: 'app-add-event',
  templateUrl: './add-event.component.html',
  styleUrls: ['./add-event.component.scss']
})
export class AddEventToLibraryComponent implements OnInit, OnDestroy {

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

  constructor(public dialogRef: MatDialogRef<AddEventToLibraryComponent>,
              public dialog: MatDialog,
              @Inject(MAT_DIALOG_DATA) public data: any,
              private libraryService: LibraryService,
              private eventService: EventService,
              private storageService: StorageService) {
    this.libraryId = data.libraryId;
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
    return this.eventService.findAllByLibraryNotContaining(this.userId, this.libraryId, event, '');
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
      this.libraryService.addEventToLibrary(this.libraryId, eventsIdToAdd).subscribe(
        () => {
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
