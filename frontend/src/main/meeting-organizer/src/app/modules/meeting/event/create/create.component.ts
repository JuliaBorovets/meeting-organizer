import {Component, Inject, OnDestroy, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup} from '@angular/forms';
import {Subscription} from 'rxjs';
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from '@angular/material/dialog';
import {EventService} from '../../../../services/event/event.service';
import {EventType} from '../../../../models/event/event-type.model';
import {State} from '../../../../models/event/state.model';
import {MeetingType} from '../../../../models/event/meeting-type.model';
import {StorageService} from '../../../../services/auth/storage.service';

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
  hidePasswordField = true;
  userId: number;

  generateMeeting = true;

  private subscription: Subscription;
  range = new FormGroup({
    start: new FormControl(),
    end: new FormControl(),
  });

  selectedType: MeetingType;
  meetingTypes: MeetingType[] = [
    MeetingType.ZOOM,
    MeetingType.WEBEX
  ];

  eventTypes: EventType[] = [
    EventType.CONFERENCE,
    EventType.SEMINAR,
    EventType.COLLOQUIUM,
    EventType.WORKSHOP
  ];

  constructor(public dialogRef: MatDialogRef<CreateEventComponent>,
              private formBuilder: FormBuilder,
              public dialog: MatDialog,
              @Inject(MAT_DIALOG_DATA) public data: any,
              private eventService: EventService,
              private storageService: StorageService) {
    this.libraryId = data.libraryId;
    this.streamId = data.streamId;
    this.subscription = new Subscription();
    this.userId = this.storageService.getUser.userId;
  }

  ngOnInit(): void {
    this.createCreateForm();
  }

  createCreateForm(): void {
    this.createForm = this.formBuilder.group({
      name: ['', null],
      description: ['', null],
      startDate: ['', null],
      durationInMinutes: [30, null],

      maxNumberParticipants: [10, null],
      eventType: [EventType.CONFERENCE, null],

      meetingType: [MeetingType.ZOOM, null],
      photo: [null, null],

      agenda: ['', null],
      password: ['', null],
      allowMultipleDevices: [true, null],
      hostVideo: [false, null],
      meetingAuthentication: [false, null],

      muteUponEntry: [true, null],
      participantVideo: [false, null],
      waitingRoom: [false, null],

      generateMeeting: [false, null],
      isPrivate: [false, null],

      joinUrl: ['', null],

      webexTitle: ['', null],
      webexAgenda: ['', null],
      webexPassword: ['', null],
      webexStart: ['', null],
      webexDurationInMinutes: [30, null],
      webexHostEmail: ['', null],
      webexEnabledAutoRecordMeeting: [false, null],
      webexEnabledJoinBeforeHost: [true, null],
      webexAutoAcceptRequest: [true, null],
      webexRequireFirstName: [true, null],
      webexRequireLastName: [true, null],
      webexRequireEmail: [false, null],
    });
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
      startDate: new Date(this.f.startDate.value).toISOString(),
      durationInMinutes: this.f.durationInMinutes.value,
      maxNumberParticipants: this.f.maxNumberParticipants.value,
      name: this.f.name.value,
      description: this.f.description.value,
      photo: this.f.photo.value,
      eventType: this.f.eventType.value,
      state: State.NOT_STARTED,
      meetingType: this.f.meetingType.value,
      libraryId: this.libraryId,
      streamId: this.streamId,
      generateMeeting: this.f.generateMeeting.value,
      isPrivate: this.f.isPrivate.value,
      userId: this.userId
    };

    const addExistingEventRequest = {
      ...baseCreateRequest,
      joinUrl: this.f.joinUrl.value,
    };

    const zoomCreateRequest = {
      ...baseCreateRequest,
      meetingEntity: {
        agenda: this.f.agenda.value,
        topic: this.f.name.value,
        password: this.f.password.value,
        start_time: this.f.startDate.value,
        settings: {
          allow_multiple_devices: this.f.allowMultipleDevices.value,
          host_video: this.f.hostVideo.value,
          meeting_authentication: this.f.meetingAuthentication.value,
          mute_upon_entry: this.f.muteUponEntry.value,
          participant_video: this.f.participantVideo.value,
          waiting_room: this.f.waitingRoom.value
        }
      },
    };

    const webexCreateRequest = {
      ...baseCreateRequest,
      meetingEntity: {
        title: this.f.webexTitle.value,
        agenda: this.f.webexAgenda.value,
        password: this.f.webexPassword.value,
        start: this.f.startDate.value,
        durationInMinutes: this.f.durationInMinutes.value,
        hostEmail: this.f.webexHostEmail.value,
        enabledAutoRecordMeeting: this.f.webexEnabledAutoRecordMeeting.value,
        enabledJoinBeforeHost: this.f.webexEnabledJoinBeforeHost.value
      },
    };

    let request;

    if (this.f.generateMeeting.value) {
      if (this.f.meetingType.value === MeetingType.ZOOM) {
        request = zoomCreateRequest;
      } else if (this.f.meetingType.value === MeetingType.WEBEX) {
        request = webexCreateRequest;
      } else {
        request = baseCreateRequest;
      }
    } else {
      request = addExistingEventRequest;
    }

    this.subscription = this.eventService.create(request).subscribe(
      () => {
        this.dialogRef.close();
      },
      () => {
        this.isError = true;
      }
    );
  }

  isZoomTypeSelected(): boolean {
    return this.f.meetingType.value === MeetingType.ZOOM;
  }

  isWebexTypeSelected(): boolean {
    console.log('===', this.f.meetingType.value);
    return this.f.meetingType.value === MeetingType.WEBEX;
  }

  isGenerateNewEventSelected(): boolean {
    return this.f.generateMeeting.value;
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
