delete from annotation;
delete from mammogram;
delete from comment;
delete from study;
delete from admin;
delete from shared_patient;
delete from patient;
delete from personal_info;
delete from medical_info;
delete from staff;
delete from service_auth;

drop sequence if exists admin_seq; 
drop sequence if exists annotation_seq; 
drop sequence if exists comment_seq; 
drop sequence if exists mammogram_seq; 
drop sequence if exists medical_info_seq; 
drop sequence if exists patient_seq; 
drop sequence if exists personal_info_seq; 
drop sequence if exists service_auth_seq; 
drop sequence if exists shared_patient_seq; 
drop sequence if exists staff_seq;
drop sequence if exists study_seq;
