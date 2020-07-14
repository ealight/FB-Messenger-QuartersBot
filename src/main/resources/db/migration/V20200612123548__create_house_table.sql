create table `house`
(
    `id`                 bigint auto_increment not null,
    `name`               varchar(40)           not null,
    `description`        varchar(200)          not null,
    `detail_info`        varchar(300)          not null,
    `owner_requirements` varchar(200)          not null,
    `owner_phone_number` varchar(17)           not null default (''),
    `additional_service` varchar(200)          not null,
    `seats`              int(11)               not null,
    `price`              int(11)               not null,
    `swimming_pool`      boolean               not null,
    `bath`               boolean               not null,
    primary key (`id`)
)