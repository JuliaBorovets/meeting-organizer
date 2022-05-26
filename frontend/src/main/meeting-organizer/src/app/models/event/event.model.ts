import {EventType} from './event-type.model';
import {State} from './state.model';
import {MeetingType} from './meeting-type.model';
import {ZoomMeetingCreateSetting} from './event-create.model';
import {UserModel} from "../user/user.model";

export class EventModel {
  eventId?: number;
  startDate?: string;
  endDate?: string;
  maxNumberParticipants?: number;
  durationInMinutes?: number;
  name?: string;
  description?: string;
  imagePath?: string;
  eventType?: EventType;
  state?: State;
  meetingType?: MeetingType;
  externalMeetingId?: string;
  streamId?: number;
  meetingEntity?: MeetingEntity;
  isFavorite?: boolean;
  joinUrl?: string;
  isPrivate?: boolean;
  participantCount?: number;
  user?: UserModel;
}

export class MeetingEntity {
  // Zoom
  agenda?: string;
  password?: string;
  startTime?: string;
  timezone?: string;
  topic?: string;
  settings?: ZoomMeetingCreateSetting;
  start_url?: string;
  title?: string;
  hostEmail?: string;
  allowMultipleDevices?: boolean;
  hostVideo?: boolean;
  meetingAuthentication?: boolean;
  muteUponEntry?: boolean;
  participantVideo?: boolean;
  waitingRoom?: boolean;
  isPrivate?: boolean;
  start?: any;
  durationInMinutes?: any;
  enabledAutoRecordMeeting?: any;
  enabledJoinBeforeHost?: any;
  autoAcceptRequest?: any;
  requireFirstName?: any;
  requireLastName?: any;
  requireEmail?: any;
}
