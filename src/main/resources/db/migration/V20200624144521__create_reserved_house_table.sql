create table `reserve_house`
(
    `id`          bigint auto_increment not null,
    `facebook_id` varchar(100)          not null,
    `house_id`    bigint                not null,
    `date_from`   datetime              not null,
    `date_to`     datetime              not null,
    primary key (`id`, `facebook_id`, `house_id`),
    foreign key (`house_id`) references `house` (`id`),
    foreign key (`facebook_id`) references `user` (`facebook_id`)
)