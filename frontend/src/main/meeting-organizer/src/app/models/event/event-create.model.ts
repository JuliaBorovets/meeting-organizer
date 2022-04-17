import {EventType} from './event-type.model';
import {State} from './state.model';
import {MeetingType} from './meeting-type.model';

export class EventCreateModel {
  startDate = '';
  endDate = '';
  maxNumberParticipants: number;
  name: string;
  photo: any;
  eventType: EventType;
  state: State;
  meetingType: MeetingType;
  libraryId: number;
  streamId: number;
  meetingEntity: MeetingCreateEntity;
}

export class MeetingCreateEntity {
  // Zoom
  agenda?: string;
  password?: string;
  startTime?: string;
  timezone?: string;
  topic?: string;
  settings?: ZoomMeetingCreateSetting;
}

export class ZoomMeetingCreateSetting {
  allowMultipleDevices: boolean;
  hostVideo: boolean;
  meetingAuthentication: boolean;
  muteUponEntry: boolean;
  participantVideo: boolean;
  waitingRoom: boolean;
}
