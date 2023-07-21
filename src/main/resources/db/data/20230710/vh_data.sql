insert into vh.roles (id, title, created_on, updated_on)
values  (1, 'user', '2023-07-12 08:05:22.731451 +00:00', null);

insert into vh.users (id, role_id, login_name, secret, encryption, first_name, last_name, birthday, gender, timezone, locale, status, login_count, created_on, updated_on)
values  (100000, 1, 'user', '123', 'None', 'New', 'User', null, null, 'GMT', 'en_GB', 1, 0, '2023-07-12 08:07:38.371824 +00:00', null);
