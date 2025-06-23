create table suggestions
(
    message            varchar(20) not null
        primary key,
    message_channel    varchar(20) null,
    discussion_message varchar(20) null,
    popular_message    varchar(20) null,
    author_id          varchar(20) null,
    date               timestamp   null,
    upvotes            int         null,
    downvotes          int         null,
    special_reactions  text        null
) charset = latin1;
