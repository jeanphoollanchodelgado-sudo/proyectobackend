create database dbds220261;

create table tuser(
idUser char(36) not null,
firstName varchar(70) not null,
surName varchar(40) not null,
email varchar(100) not null,
password varchar(2000) not null,
role varchar(1000) not null,/*Súper usuario,Administrador*/
createdAt datetime not null,
updatedAt datetime not null,
primary key(idUser)
) engine=innodb;

create table tprofessor(
idProfessor char(36) not null,
firstName varchar(70) not null,
surName varchar(40) not null,
email varchar(100) not null,
createdAt datetime not null,
updatedAt datetime not null,
primary key(idProfessor)
) engine=innodb;

create table toffice(
idOffice char(36) not null,
name varchar(700) not null,
createdAt datetime not null,
updatedAt datetime not null,
primary key(idOffice)
) engine=innodb;

create table tsuggestion(
idSuggestion char(36) not null,
idOffice char(36) not null,
code char(7) not null,
personFullName varchar(110) not null,
description text not null,
status varchar(70) not null,/*Pendiente de revisión, Visto, Rechazado, En coordinación, Cerrado*/
createdAt datetime not null,
updatedAt datetime not null,
primary key(idSuggestion),
foreign key(idOffice) references toffice(idOffice) on delete cascade on update cascade
) engine=innodb;

create table tsuggestionfile(
idSuggestionfile char(36) not null,
idSuggestion char(36) not null,
name varchar(2000) not null,
extension varchar(5) not null,
createdAt datetime not null,
updatedAt datetime not null,
primary key(idSuggestionfile),
foreign key(idSuggestion) references tsuggestion(idSuggestion) on delete cascade on update cascade
) engine=innodb;

create table tcomplaint(
idComplaint char(36) not null,
idOffice char(36) not null,
idProfessor char(36) null,
code char(7) not null,
complaintFullName varchar(110) not null,
personFullName varchar(110) not null,
description text not null,
issueDate datetime null,
status varchar(70) not null,/*Pendiente de revisión, Visto, Rechazado, En coordinación, Cerrado*/
createdAt datetime not null,
updatedAt datetime not null,
primary key(idComplaint),
foreign key(idOffice) references toffice(idOffice) on delete cascade on update cascade,
foreign key(idProfessor) references tprofessor(idProfessor) on delete cascade on update cascade
) engine=innodb;

create table tcomplaintfile(
idComplaintfile char(36) not null,
idComplaint char(36) not null,
name varchar(2000) not null,
extension varchar(5) not null,
createdAt datetime not null,
updatedAt datetime not null,
primary key(idComplaintfile),
foreign key(idComplaint) references tcomplaint(idComplaint) on delete cascade on update cascade
) engine=innodb;

create table tcomplaintcomment(
idComplaintcomment char(36) not null,
idComplaint char(36) not null,
idUser char(36) null,
description varchar(2000) not null,
createdAt datetime not null,
updatedAt datetime not null,
primary key(idComplaintcomment),
foreign key(idComplaint) references tcomplaint(idComplaint) on delete cascade on update cascade,
foreign key(idUser) references tuser(idUser) on delete cascade on update cascade
) engine=innodb;