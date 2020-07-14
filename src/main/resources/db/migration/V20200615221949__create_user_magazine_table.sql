create table `user_magazine`
(
    `facebook_id` varchar(100) not null,
    `house_id`    bigint not null,
    primary key (`facebook_id`, `house_id`),
    foreign key (`house_id`) references `house` (`id`),
    foreign key (`facebook_id`) references `user` (`facebook_id`)
)