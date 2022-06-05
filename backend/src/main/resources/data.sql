-- roles
INSERT INTO roles (role_id, name) VALUES (1, 'USER');

-- users
INSERT INTO users (user_id, email, username, password, first_name, last_name, birthday, bio, image_path, phone_number, creation_date, last_modified_date, user_google2fa, google2fa_secret, account_non_expired, account_non_locked, credentials_non_expired, enabled) VALUES (1, 'juliaborovets2001@gmail.com', 'JuliaBorovets', '$2a$10$F6fkgj/62.6gOnk1jdtg8uSfDBD2L/WEg5xmUJdIdMT/Dq5BtxQvq', 'Julia', 'Borovets', null, null, '8223b429-85a9-457d-badd-2d1d65ed481ashotlandskaya-shinshilla-variacii-okrasa-harakter-i-usloviya-soderzhaniya-koshek.jpg', null, '2022-06-02 20:08:42.243000', '2022-06-02 21:01:15.862000', false, null, true, true, true, true);
INSERT INTO users (user_id, email, username, password, first_name, last_name, birthday, bio, image_path, phone_number, creation_date, last_modified_date, user_google2fa, google2fa_secret, account_non_expired, account_non_locked, credentials_non_expired, enabled) VALUES (4, 'isnnsndjjjjjj2020@gmail.com', 'username', '$2a$10$dgpM1m9Rnp9WUUAt2hT6H.k4EjUGr/8dlnw.eHAbPjoi9Bi/Km.py', 'Inna', 'Perenko', null, null, null, null, '2022-06-02 23:55:47.953000', '2022-06-02 23:55:47.953000', false, null, true, true, true, true);

-- user_role
INSERT INTO user_role (user_id, role_id) VALUES (1, 1);
INSERT INTO user_role (user_id, role_id) VALUES (4, 1);

-- libraries
INSERT INTO libraries (library_id, name, description, access_token, image, user_id, is_private) VALUES (1, 'Java patterns', 'Java design patterns', '5c22e0a4-32d1-47cf-93c9-7af2f9882164', null, 1, false);
INSERT INTO libraries (library_id, name, description, access_token, image, user_id, is_private) VALUES (2, 'Go Concurrency', 'Go Concurrency зфееукті', 'cb0519a3-1e16-40d2-9599-78cda7b2fdf9', null, 1, true);

-- streams
INSERT INTO streams (stream_id, name, library_id) VALUES (1, 'Golang', 1);
INSERT INTO streams (stream_id, name, library_id) VALUES (2, 'Kafka', 1);
INSERT INTO streams (stream_id, name, library_id) VALUES (3, 'Java', 1);

-- events
INSERT INTO events (event_id, start_date, end_date, max_number_participants, name, description, image_path, event_type, state, meeting_type, external_meeting_id, library_id, user_id, stream_id, join_url, access_token, creation_date, is_private) VALUES (1, '2022-06-15 23:31:02.000000', '2022-06-16 00:16:02.000000', 5, 'Java patterns', 'Creational, structural, and behavioral design patterns', null, 'SEMINAR', 'NOT_STARTED', 'ZOOM', '71503406321', null, 1, null, 'https://us04web.zoom.us/j/71503406321?pwd=YVHM7kxRpC0cC1fGpjzD451KsRPzTB.1', '2edba2cc-7ac2-4716-af50-6048c57c2d03', '2022-06-02 23:32:00.554000', false);
INSERT INTO events (event_id, start_date, end_date, max_number_participants, name, description, image_path, event_type, state, meeting_type, external_meeting_id, library_id, user_id, stream_id, join_url, access_token, creation_date, is_private) VALUES (3, '2022-06-03 23:52:37.000000', '2022-06-04 00:22:37.000000', 10, 'Adapter Design Pattern in Java', 'Implementation of Adapter Design Pattern in Java', null, 'CONFERENCE', 'NOT_STARTED', 'ZOOM', null, null, 1, null, 'https://us04web.zoom.us/s/71503406321?pwd=YVHM7kxRpC0cC1fGpjzD451KsRPzTB.1', '565d58d0-7ef0-4b07-9b66-59dce7ddf363', '2022-06-02 23:54:01.214000', true);
INSERT INTO events (event_id, start_date, end_date, max_number_participants, name, description, image_path, event_type, state, meeting_type, external_meeting_id, library_id, user_id, stream_id, join_url, access_token, creation_date, is_private) VALUES (2, '2022-06-21 23:35:02.000000', '2022-06-22 00:05:02.000000', 30, 'Apache Kafka tutorial', 'It includes a look at Kafka architecture, core concepts, and the connector ecosystem.', '2a81b44f-6dc4-4603-9e9c-89576be8fbbcindex.png', 'WORKSHOP', 'NOT_STARTED', 'WEBEX', '7cc19fc317604bcf8f6e53b988e61075', null, 1, null, 'https://meet198.webex.com/meet198/j.php?MTID=m25e23c68cc095bf48282ea7e4e984c6f', '6fd9b06e-d64b-4850-83c0-60ae226d6341', '2022-06-02 23:36:14.671000', false);
INSERT INTO events (event_id, start_date, end_date, max_number_participants, name, description, image_path, event_type, state, meeting_type, external_meeting_id, library_id, user_id, stream_id, join_url, access_token, creation_date, is_private) VALUES (4, '2022-06-03 01:18:15.000000', '2022-06-03 01:48:15.000000', 10, 'Go Concurrency Patterns', 'Go Concurrency Patterns: Pipelines and cancellation', null, 'SEMINAR', 'NOT_STARTED', 'WEBEX', 'ff89a682705e442eb822b403dae68ef1', 1, 1, 3, 'https://meet198.webex.com/meet198/j.php?MTID=mf2121d1b71920c858f02613bc16d8ddd', '47247c4f-ec23-439f-8682-67a09bc69eb4', '2022-06-03 00:27:19.256000', false);

-- event_user_access
INSERT INTO event_user_access (event_id, user_id) VALUES (3, 4);

-- event_user_fav
INSERT INTO event_user_fav (event_id, user_id) VALUES (2, 1);
INSERT INTO event_user_fav (event_id, user_id) VALUES (4, 1);

-- event_user_visitor
INSERT INTO event_user_visitor (event_id, user_id) VALUES (1, 1);
INSERT INTO event_user_visitor (event_id, user_id) VALUES (3, 1);
INSERT INTO event_user_visitor (event_id, user_id) VALUES (4, 1);
INSERT INTO event_user_visitor (event_id, user_id) VALUES (2, 1);
INSERT INTO event_user_visitor (event_id, user_id) VALUES (2, 4);

-- comments
INSERT INTO comments (comment_id, username, text, creation_date, last_modified_date, event_id, user_id) VALUES (2, 'InnaUsername', 'Awesome!', '2022-06-03 00:49:39.986000', '2022-06-03 00:49:39.986000', 2, 4);
INSERT INTO comments (comment_id, username, text, creation_date, last_modified_date, event_id, user_id) VALUES (3, null, 'Cool', '2022-06-03 00:52:33.242000', '2022-06-03 00:52:33.242000', 2, 1);
