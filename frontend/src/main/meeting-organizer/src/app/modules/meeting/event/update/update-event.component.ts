import {Component, Inject, OnDestroy, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup} from '@angular/forms';
import {Subscription} from 'rxjs';
import {MeetingType} from '../../../../models/event/meeting-type.model';
import {EventType} from '../../../../models/event/event-type.model';
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from '@angular/material/dialog';
import {EventService} from '../../../../services/event/event.service';
import {StorageService} from '../../../../services/auth/storage.service';
import {State} from '../../../../models/event/state.model';
import {EventModel} from '../../../../models/event/event.model';

@Component({
  selector: 'app-event-update',
  templateUrl: './update-event.component.html',
  styleUrls: ['./update-event.component.scss']
})
export class UpdateEventComponent implements OnInit, OnDestroy {

  updateForm: FormGroup;
  isUpdateFailed = false;
  errorMessage = '';
  submitted = false;
  isError = false;
  isInValid = false;
  userId: number;
  eventId: number;
  eventItem: any;
  hidePasswordField = true;
  isLoading = true;

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

  constructor(public dialogRef: MatDialogRef<UpdateEventComponent>,
              private formBuilder: FormBuilder,
              public dialog: MatDialog,
              @Inject(MAT_DIALOG_DATA) public data: any,
              private eventService: EventService,
              private storageService: StorageService) {
    this.subscription = new Subscription();
    this.userId = this.storageService.getUser.userId;
    this.eventId = data.eventModel.eventId;
  }

  ngOnInit(): void {
    this.subscription.add(
      this.eventService.findById(this.eventId).subscribe(
        data => {
          this.eventItem = data;
          this.isLoading = false;
          this.createUpdateForm();
        },
        () => this.isError = true)
    );
  }

  createUpdateForm(): void {
    console.log(this.eventItem);
    this.updateForm = this.formBuilder.group({
      name: [this.eventItem.name, null],
      description: [this.eventItem.description, null],
      startDate: [this.eventItem.startDate, null],
      durationInMinutes: [this.eventItem.durationInMinutes, null],

      maxNumberParticipants: [this.eventItem.maxNumberParticipants, null],
      eventType: [this.eventItem.eventType, null],

      meetingType: [this.eventItem.meetingType, null],
      photo: [null, null],

      agenda: [this.eventItem.meetingEntity?.agenda, null],
      password: [this.eventItem.meetingEntity?.password, null],
      allowMultipleDevices: [this.eventItem.meetingEntity?.settings?.allow_multiple_devices, null],
      hostVideo: [this.eventItem.meetingEntity?.settings?.host_video, null],
      meetingAuthentication: [this.eventItem.meetingEntity?.settings?.meeting_authentication, null],

      muteUponEntry: [this.eventItem.meetingEntity?.settings?.mute_upon_entry, null],
      participantVideo: [this.eventItem.meetingEntity?.settings?.participant_video, null],
      waitingRoom: [this.eventItem.meetingEntity?.settings?.waiting_room, null],

      isPrivate: [this.eventItem.isPrivate, null],

      joinUrl: [this.eventItem.joinUrl, null],

      webexTitle: [this.eventItem.meetingEntity?.title, null],
      webexAgenda: [this.eventItem.meetingEntity?.agenda, null],
      webexPassword: [this.eventItem.meetingEntity?.password, null],
      webexStart: [this.eventItem.meetingEntity?.start, null],
      webexDurationInMinutes: [this.eventItem.meetingEntity?.durationInMinutes, null],
      webexHostEmail: [this.eventItem.meetingEntity?.hostEmail, null],
      webexEnabledAutoRecordMeeting: [this.eventItem.meetingEntity?.enabledAutoRecordMeeting, null],
      webexEnabledJoinBeforeHost: [this.eventItem.meetingEntity?.enabledJoinBeforeHost, null],
    });
  }

  get f() {
    return this.updateForm.controls;
  }

  onSubmit(): void {
    this.submitted = true;
    if (this.updateForm.invalid) {
      return;
    }

    const baseUpdateRequest = {
      eventId: this.eventItem.eventId,
      startDate: new Date(this.f.startDate.value).toISOString(),
      durationInMinutes: this.f.durationInMinutes.value,
      maxNumberParticipants: this.f.maxNumberParticipants.value,
      name: this.f.name.value,
      description: this.f.description.value,
      photo: this.f.photo.value,
      eventType: this.f.eventType.value,
      state: State.NOT_STARTED,
      meetingType: this.f.meetingType.value,
      isPrivate: this.f.isPrivate.value
    };

    const addExistingEventUpdateRequest = {
      ...baseUpdateRequest,
      joinUrl: this.f.joinUrl.value,
    };

    const zoomUpdateRequest = {
      ...baseUpdateRequest,
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
      ...baseUpdateRequest,
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

    if (this.eventItem.externalMeetingId) {
      if (this.f.meetingType.value === MeetingType.ZOOM) {
        request = zoomUpdateRequest;
      } else if (this.f.meetingType.value === MeetingType.WEBEX) {
        request = webexCreateRequest;
      } else {
        request = baseUpdateRequest;
      }
    } else {
      request = addExistingEventUpdateRequest;
    }

    this.subscription = this.eventService.updateEvent(request).subscribe(
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
    return this.f.meetingType.value === MeetingType.WEBEX;
  }

  isGenerateNewEventSelected(): boolean {
    return this.eventItem.externalMeetingId !== null;
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
