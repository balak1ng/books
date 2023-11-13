create table if not exists books
(
    id
    bigserial
    primary
    key,
    author
    varchar
(
    255
) not null,
    title varchar
(
    255
) not null,
    num_pages int not null
    );