import {Component, Inject, OnDestroy, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup} from '@angular/forms';
import {Subscription} from 'rxjs';
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from '@angular/material/dialog';
import {EventService} from '../../../../services/event/event.service';
import {EventType} from '../../../../models/event/event-type.model';
import {State} from '../../../../models/event/state.model';
import {MeetingType} from '../../../../models/event/meeting-type.model';

@Component({
  selector: 'app-create',
  templateUrl: './create.component.html',
  styleUrls: ['./create.component.scss']
})
export class CreateEventComponent implements OnInit, OnDestroy {

  createForm: FormGroup;
  isCreateFailed = false;
  errorMessage = '';
  submitted = false;
  libraryId: number;
  streamId: number;
  isError = false;
  isInValid = false;
  private subscription: Subscription;
  range = new FormGroup({
    start: new FormControl(),
    end: new FormControl(),
  });

  constructor(public dialogRef: MatDialogRef<CreateEventComponent>,
              private formBuilder: FormBuilder,
              public dialog: MatDialog,
              @Inject(MAT_DIALOG_DATA) public data: any,
              private eventService: EventService) {
    this.libraryId = data.libraryId;
    this.streamId = data.streamId;
    this.subscription = new Subscription();
  }

  ngOnInit(): void {
    this.createCreateForm();
  }

  createCreateForm(): void {
    const config: Map<string, any> = new Map<string, any>();
    config.set('name', ['', null]);
    config.set('startDate', ['', null]);
    config.set('endDate', ['', null]);
    config.set('maxNumberParticipants', [1, null]);
    config.set('photo', [null, null]);
    config.set('eventType', [EventType.CONFERENCE, null]);
    config.set('state', [State.NOT_STARTED, null]);
    config.set('meetingType', [MeetingType.ZOOM, null]);
    // Zoom
    config.set('agenda', ['', null]);
    config.set('password', ['', null]);
    config.set('startTime', ['', null]);
    config.set('timezone', ['', null]);
    config.set('topic', ['', null]);
    // Zoom settings
    config.set('allowMultipleDevices', [true, null]);
    config.set('hostVideo', [true, null]);
    config.set('meetingAuthentication', [false, null]);
    config.set('muteUponEntry', [true, null]);
    config.set('participantVideo', [false, null]);
    config.set('waitingRoom', [false, null]);

    this.createForm = this.formBuilder.group(config);
  }

  get f() {
    return this.createForm.controls;
  }

  onSubmit(): void {
    this.submitted = true;
    if (this.createForm.invalid) {
      return;
    }

    const baseCreateRequest = {
      startDate: this.f.startDate.value,
      endDate: this.f.endDate.value,
      maxNumberParticipants: this.f.maxNumberParticipants.value,
      name: this.f.name.value,
      photo: this.f.photo.value,
      eventType: this.f.eventType.value,
      state: this.f.state.value,
      meetingType: this.f.meetingType.value,
      libraryId: this.libraryId,
      streamId: this.streamId,
    };

    const zoomCreateRequest = {
      ...baseCreateRequest,
      meetingEntity: {
        agenda: this.f.agenda.value,
        password: this.f.password.value,
        startTime: this.f.startTime.value,
        timezone: this.f.timezone.value,
        topic: this.f.topic.value,
        settings: {
          allowMultipleDevices: this.f.allowMultipleDevices.value,
          hostVideo: this.f.hostVideo.value,
          meetingAuthentication: this.f.meetingAuthentication.value,
          muteUponEntry: this.f.muteUponEntry.value,
          participantVideo: this.f.participantVideo.value,
          waitingRoom: this.f.waitingRoom.value
        }
      },
    };

    let response;
    if (this.f.meetingType.value === MeetingType.ZOOM) {
      response = this.eventService.create(zoomCreateRequest);
    }

    this.subscription = response.subscribe(
      () => {
        this.dialogRef.close();
      },
      () => {
        this.isError = true;
      }
    );
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
