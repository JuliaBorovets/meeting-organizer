package com.meeting.organizer.client.webex.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WebexMeeting {

    private String id;

    private String meetingNumber;

    private String title;

    private String agenda;

    private String password;

    private String phoneAndVideoSystemPassword;

    private String timezone;

    private String start;

    private String end;

    private String recurrence;

    private String hostUserId;

    private String hostDisplayName;

    private String hostEmail;

    private String meetingType;

    private String state;

    private String hostKey;

    private String siteUrl;

    private String webLink;

    private String sipAddress;

    private String dialInIpAddress;

    private Boolean enabledAutoRecordMeeting;

    private Boolean allowAnyUserToBeCoHost;

    private Boolean enabledJoinBeforeHost;

    private Boolean enableConnectAudioBeforeHost;

    private Long joinBeforeHostMinutes;

    private Boolean excludePassword;

    private Boolean publicMeeting;

    private Long reminderTime;

    private String unlockedMeetingJoinSecurity;

    private Long sessionTypeId;

    private Boolean enableAutomaticLock;

    private Long automaticLockMinutes;

    private Boolean allowFirstUserToBeCoHost;

    private Boolean allowAuthenticatedDevices;

    private String scheduledType;

    private WebexRegistration registration;

}
