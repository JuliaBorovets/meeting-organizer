INSERT INTO roles (role_id, name) VALUES (1, 'USER');

INSERT INTO users (user_id, email, username, password, first_name, last_name, birthday, bio, image, phone_number, creation_date, last_modified_date, user_google2fa, google2fa_secret, account_non_expired, account_non_locked, credentials_non_expired, enabled, location_id) VALUES (3, 'juliaborovets2001@gmail.com', 'juliaborovets', '$2a$10$f0g9p/XQwoMQct/GNDF.G.EMJ1q1.DCkcm0Pioc/keEHML1.CbM9C', 'Julia', 'Borovets', null, null, null, null, '2022-05-15 11:41:31.972000', '2022-05-15 11:42:02.428000', false, null, true, true, true, true, null);

INSERT INTO user_role (user_id, role_id) VALUES (3, 1);
