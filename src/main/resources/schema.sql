create table term (
    term_id  int primary key,
    tyear     int not null check (tyear between 2000 and 2030),
    semester varchar(10) not null check (semester in ('Spring', 'Fall')),
    add_date Date not null,
    add_deadline Date not null,
    drop_deadline Date not null,
    start_date Date not null,
    end_date Date not null
);

create table course (
    course_id varchar(10) primary key,
    title varchar(100) not null,
    credits int not null check (credits >= 0)
);

create sequence sec_seq;
ALTER SEQUENCE sec_seq RESTART WITH 1000;

create table section (
    section_no int default next value for sec_seq  primary key,
    course_id varchar(10) not null,
    sec_id int not null not null,
    term_id int not null not null,
    building varchar(10),
    room varchar(10),
    times varchar(25),
    instructor_email varchar(50),
    foreign key(course_id) references course(course_id),
    foreign key(term_id) references term(term_id)
);

create sequence user_seq;
ALTER SEQUENCE user_seq RESTART WITH 7000;

create table user_table (
	id integer  default next value for user_seq primary key,
    name varchar(50) not null,
    email varchar(50) not null unique,
    password varchar(100) not null,
    type varchar(10) not null  check (type in ('STUDENT', 'ADMIN', 'INSTRUCTOR'))
);

create sequence enroll_seq;
ALTER SEQUENCE enroll_seq RESTART WITH 10000;

create table enrollment (
    enrollment_id integer default next value for enroll_seq primary key,
    grade varchar(5),
    section_no int not null,
    user_id int not null,
    foreign key(section_no) references section(section_no),
    foreign key(user_id) references user_table(id)
);
