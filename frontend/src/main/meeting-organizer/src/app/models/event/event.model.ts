import {EventType} from './event-type.model';
import {State} from './state.model';
import {MeetingType} from './meeting-type.model';
import {ZoomMeetingCreateSetting} from './event-create.model';

export class EventModel {
  eventId?: number;
  startDate?: string;
  maxNumberParticipants?: string;
  name?: string;
  image?: Blob;
  eventType?: EventType;
  state?: State;
  meetingType?: MeetingType;
  externalMeetingId?: string;
  streamId?: number;
  meetingEntity?: MeetingEntity;
  isFavorite?: boolean;
  joinUrl?: string;
  isPrivate?: boolean;
}

export class MeetingEntity {
  // Zoom
  agenda?: string;
  password?: string;
  startTime?: string;
  timezone?: string;
  topic?: string;
  settings?: ZoomMeetingCreateSetting;
}
