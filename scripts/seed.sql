insert into admin(id, email, password) values (1,'admin@wwwm.com','123');

insert into staff(id, email, password, role, name, first_last_name, second_last_name, address, telephone, birthdate, cedula, RFC)
  values (1,'jestefano@wwwm.com','wwwm','doctor','Juan Estefano','Rodríguez','González','Paseo de los Leones 445, Col. Cumbres, Mty. N.L.', '811653735', '1985-07-29','AQWERTYGSDGN','83473847asfdjklj');
insert into staff(id, email, password, role, name, first_last_name, second_last_name, address, telephone, birthdate, cedula, RFC)
  values (2,'cquintero@wwwm.com','wwwm','doctor','Carlos','Garza','Quintero','Jupiter 9383, Col. Mitras, Mty. N.L.', '811684723', '1981-01-22','ASIELKXJKLL','asdlfkj837kjl');

insert into personal_info(id, name, first_last_name, second_last_name, address, email, telephone, password, birthdate)
  values(1,'María','Dávalos','González','Bosque Mágico 123, Col. Robledo, Mty, N.L.','mdavalos@xyz.com', '85234725', 'wwwm','1978-02-21');
insert into personal_info(id, name, first_last_name, second_last_name, address, email, telephone, password, birthdate)
  values(2,'Alejandra Fernanda','Martínez','García','Bosque Mágico 129, Col. Robledo, Mty, N.L.','amtz@xyz.com', '81124239','wwwm','1973-08-09');
insert into personal_info(id, name, first_last_name, second_last_name, address, email, telephone, password, birthdate)
  values(3,'Kristina','Montejano','De La Garza','Bosque Mágico 187, Col. Robledo, Mty, N.L.','kmontejano@xyz.com', '81128473','wwwm','1979-11-19');

insert into medical_info(id, sexual_activity_start_age, pregnancies, c_sections, natural_deliveries, abortions,
  menopause_start_age, family_predisposition, hormonal_replacement_therapy, previous_mammary_diseases, menstrual_period_start_age, breastfed_children)
  values (1,18,1,0,1,0,34,true,true,false,15,true);
insert into medical_info(id, sexual_activity_start_age, pregnancies, c_sections, natural_deliveries, abortions,
  menopause_start_age, family_predisposition, hormonal_replacement_therapy, previous_mammary_diseases, menstrual_period_start_age, breastfed_children)
  values (2,18,3,1,2,0,45,true,false,true,16,true);
insert into medical_info(id, sexual_activity_start_age, pregnancies, c_sections, natural_deliveries, abortions,
  menopause_start_age, family_predisposition, hormonal_replacement_therapy, previous_mammary_diseases, menstrual_period_start_age, breastfed_children)
  values (3,19,2,0,2,0,50,false,false,false,14,true);

insert into patient(id, personal_info_id, medical_info_id, staff_id, view_comments, view_annotations)
  values (1,1,1,1,true,true);
insert into patient(id, personal_info_id, medical_info_id, staff_id, view_comments, view_annotations)
  values (2,2,2,1,true,true);
insert into patient(id, personal_info_id, medical_info_id, staff_id, view_comments, view_annotations)
  values (3,3,3,2,true,true);

insert into shared_patient(id, created_at, sharer_id, borrower_id, patient_id, access_privileges)
  values (1, '2014-04-21', 2, 1, 3, 15);

insert into study(id, created_at, patient_id)
  values (1, '2014-04-24', 1);

insert into comment(id, content, created_at, staff_id, study_id)
  values(1, 'Toma2 y Toma3 no salen muy bien y requieren una segunda opinión.', '2014-04-25', 1, 1);

insert into mammogram(id, created_at, study_id)
  values(1, '2014-04-24', 1);

insert into annotation(id, content, created_at, mammogram_id, staff_id)
  values(1, 'Coordenadas (234, 82) se observa algo extraño', '2014-04-25', 1, 1);

