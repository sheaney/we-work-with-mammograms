# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table admin (
  id                        bigint not null,
  email                     varchar(255),
  password                  varchar(255),
  constraint pk_admin primary key (id))
;

create table annotation (
  id                        bigint not null,
  content                   varchar(255),
  created_at                date,
  mammogram_id              bigint,
  staff_id                  bigint,
  constraint pk_annotation primary key (id))
;

create table comment (
  id                        bigint not null,
  content                   varchar(255),
  created_at                date,
  staff_id                  bigint,
  study_id                  bigint,
  constraint pk_comment primary key (id))
;

create table mammogram (
  id                        bigint not null,
  created_at                date,
  url                       varchar(255),
  study_id                  bigint,
  constraint pk_mammogram primary key (id))
;

create table medical_info (
  id                        bigint not null,
  sexual_activity_start_age integer,
  pregnancies               integer,
  c_sections                integer,
  natural_deliveries        integer,
  abortions                 integer,
  menopause_start_age       integer,
  family_predisposition     boolean,
  hormonal_replacement_therapy boolean,
  previous_mammary_diseases boolean,
  menstrual_period_start_age integer,
  breastfed_children        boolean,
  constraint pk_medical_info primary key (id))
;

create table patient (
  id                        bigint not null,
  personal_info_id          bigint,
  medical_info_id           bigint,
  staff_id                  bigint,
  constraint pk_patient primary key (id))
;

create table permission (
  id                        bigint not null,
  value                     tinyint,
  staff_id                  bigint,
  patient_id                bigint,
  constraint pk_permission primary key (id))
;

create table personal_info (
  id                        bigint not null,
  name                      varchar(255),
  first_last_name           varchar(255),
  second_last_name          varchar(255),
  address                   varchar(255),
  email                     varchar(255),
  telephone                 varchar(255),
  birthdate                 date,
  constraint pk_personal_info primary key (id))
;

create table shared_patient (
  id                        bigint not null,
  created_at                date,
  staff_id                  bigint,
  patient_id                bigint,
  constraint pk_shared_patient primary key (id))
;

create table staff (
  id                        bigint not null,
  email                     varchar(255),
  password                  varchar(255),
  role                      varchar(255),
  name                      varchar(255),
  first_last_name           varchar(255),
  second_last_name          varchar(255),
  address                   varchar(255),
  telephone                 varchar(255),
  birthdate                 timestamp,
  cedula                    varchar(255),
  rfc                       varchar(255),
  constraint pk_staff primary key (id))
;

create table study (
  id                        bigint not null,
  created_at                date,
  patient_id                bigint,
  constraint pk_study primary key (id))
;

create sequence admin_seq;

create sequence annotation_seq;

create sequence comment_seq;

create sequence mammogram_seq;

create sequence medical_info_seq;

create sequence patient_seq;

create sequence permission_seq;

create sequence personal_info_seq;

create sequence shared_patient_seq;

create sequence staff_seq;

create sequence study_seq;

alter table annotation add constraint fk_annotation_annotated_1 foreign key (mammogram_id) references mammogram (id) on delete restrict on update restrict;
create index ix_annotation_annotated_1 on annotation (mammogram_id);
alter table annotation add constraint fk_annotation_annotator_2 foreign key (staff_id) references staff (id) on delete restrict on update restrict;
create index ix_annotation_annotator_2 on annotation (staff_id);
alter table comment add constraint fk_comment_commenter_3 foreign key (staff_id) references staff (id) on delete restrict on update restrict;
create index ix_comment_commenter_3 on comment (staff_id);
alter table comment add constraint fk_comment_commented_4 foreign key (study_id) references study (id) on delete restrict on update restrict;
create index ix_comment_commented_4 on comment (study_id);
alter table mammogram add constraint fk_mammogram_study_5 foreign key (study_id) references study (id) on delete restrict on update restrict;
create index ix_mammogram_study_5 on mammogram (study_id);
alter table patient add constraint fk_patient_personalInfo_6 foreign key (personal_info_id) references personal_info (id) on delete restrict on update restrict;
create index ix_patient_personalInfo_6 on patient (personal_info_id);
alter table patient add constraint fk_patient_medicalInfo_7 foreign key (medical_info_id) references medical_info (id) on delete restrict on update restrict;
create index ix_patient_medicalInfo_7 on patient (medical_info_id);
alter table patient add constraint fk_patient_owner_8 foreign key (staff_id) references staff (id) on delete restrict on update restrict;
create index ix_patient_owner_8 on patient (staff_id);
alter table permission add constraint fk_permission_ownsPermission_9 foreign key (staff_id) references staff (id) on delete restrict on update restrict;
create index ix_permission_ownsPermission_9 on permission (staff_id);
alter table permission add constraint fk_permission_patientBoundedB_10 foreign key (patient_id) references patient (id) on delete restrict on update restrict;
create index ix_permission_patientBoundedB_10 on permission (patient_id);
alter table shared_patient add constraint fk_shared_patient_sharer_11 foreign key (staff_id) references staff (id) on delete restrict on update restrict;
create index ix_shared_patient_sharer_11 on shared_patient (staff_id);
alter table shared_patient add constraint fk_shared_patient_shared_12 foreign key (patient_id) references staff (id) on delete restrict on update restrict;
create index ix_shared_patient_shared_12 on shared_patient (patient_id);
alter table study add constraint fk_study_owner_13 foreign key (patient_id) references patient (id) on delete restrict on update restrict;
create index ix_study_owner_13 on study (patient_id);



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists admin;

drop table if exists annotation;

drop table if exists comment;

drop table if exists mammogram;

drop table if exists medical_info;

drop table if exists patient;

drop table if exists permission;

drop table if exists personal_info;

drop table if exists shared_patient;

drop table if exists staff;

drop table if exists study;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists admin_seq;

drop sequence if exists annotation_seq;

drop sequence if exists comment_seq;

drop sequence if exists mammogram_seq;

drop sequence if exists medical_info_seq;

drop sequence if exists patient_seq;

drop sequence if exists permission_seq;

drop sequence if exists personal_info_seq;

drop sequence if exists shared_patient_seq;

drop sequence if exists staff_seq;

drop sequence if exists study_seq;

