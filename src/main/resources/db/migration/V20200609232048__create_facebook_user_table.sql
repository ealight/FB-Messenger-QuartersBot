create table `user`
(
    `id`           bigint auto_increment not null,
    `facebook_id`  varchar(100)          not null,
    `first_name`   varchar(32)           not null,
    `last_name`    varchar(32)           not null,
    `phone_number` varchar(16),
    primary key (`id`),
    unique index `unique_facebook_id` (`facebook_id` asc),
    unique index `unique_phone_number` (`phone_number` desc)
)