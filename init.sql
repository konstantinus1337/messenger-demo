create table user_profile
(
    id           int primary key generated by default as identity,
    username     varchar(60) unique not null,
    password     varchar(64)        not null,
    nickname     varchar(60) unique not null,
    status       varchar            not null, --
    phone_number varchar(10) unique not null,
    email        varchar(50) unique not null,
    avatar   varchar,
    created_at   timestamp          not null
);
create table friendlist
(
    id        int primary key generated by default as identity,
    user_id   int references user_profile (id) on delete cascade,
    friend_id int references user_profile (id) on delete cascade,
    added_at  timestamp not null
);
create table private_chat
(
    id          int primary key generated by default as identity,
    sender_id int references user_profile (id),
    receiver_id int references user_profile (id),
    created_at  timestamp
);
create table private_chat_messages
(
    id              int primary key generated by default as identity,
    private_chat_id int references private_chat (id) on delete cascade,
    sender_id       int references user_profile (id),
    receiver_id    int references user_profile (id),
    sent_at         timestamp,
    status          varchar,
    message         text
);
create table private_chat_files
(
    id              int primary key generated by default as identity,
    private_chat_id int references private_chat (id) on delete cascade,
    sender_id       int references user_profile (id),
    receiver_id    int references user_profile (id),
    sent_at         timestamp,
    file_name       varchar,
    type            varchar(10)
);
create table group_chat
(
    id          int primary key generated by default as identity,
    name        varchar(60) not null,
    description varchar,
    created_at  timestamp
);
create table group_chat_messages
(
    id            int primary key generated by default as identity,
    group_chat_id int references group_chat (id) on delete cascade,
    sender_id     int references user_profile (id),
    sent_at       timestamp,
    status          varchar,
    message       text
);
create table group_chat_files
(
    id            int primary key generated by default as identity,
    group_chat_id int references group_chat (id) on delete cascade,
    sender_id     int references user_profile (id),
    sent_at       timestamp,
    file_name     varchar,
    type          varchar(10)
);
create table group_chat_members
(
    id            int primary key generated by default as identity,
    group_chat_id int references group_chat (id) on delete cascade,
    member_id     int references user_profile (id),
    role          varchar(10) not null
);
