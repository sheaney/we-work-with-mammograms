insert into admin(id, email, password) values (1,'admin@wwwm.com','123');
insert into staff(id, email, password, role, name, first_last_name, second_last_name, address, telephone, birthdate, cedula, RFC)
  values (1,'jestefano@wwwm.com','wwwm','doctor','Juan Estefano','Rodríguez','González','Paseo de los Leones 445, Col. Cumbres, Mty. N.L.', '811653735', '1985-07-29','AQWERTYGSDGN','83473847asfdjklj');
insert into personal_info(id, name, first_last_name, second_last_name, address, email, password, birthdate)
  values(1,'María','Dávalos','González','Bosque Mágico 123, Col. Robledo, Mty, N.L.','mdavalos@xyz.com','wwwm','1978-02-21');
insert into personal_info(id, name, first_last_name, second_last_name, address, email, password, birthdate)
  values(2,'Alejandra Fernanda','Martínez','García','Bosque Mágico 129, Col. Robledo, Mty, N.L.','amtz@xyz.com','wwwm','1973-08-09');
insert into medical_info(id, sexual_activity_start_age, pregnancies, c_sections, natural_deliveries, abortions,
  menopause_start_age, family_predisposition, hormonal_replacement_therapy, previous_mammary_diseases, menstrual_period_start_age, breastfed_children)
  values (1,18,1,0,1,0,34,true,true,false,15,true);
insert into medical_info(id, sexual_activity_start_age, pregnancies, c_sections, natural_deliveries, abortions,
  menopause_start_age, family_predisposition, hormonal_replacement_therapy, previous_mammary_diseases, menstrual_period_start_age, breastfed_children)
  values (2,18,3,1,2,0,45,true,false,true,16,true);
insert into patient(id, personal_info_id, medical_info_id, staff_id) values (1,1,1,1);
insert into patient(id, personal_info_id, medical_info_id, staff_id) values (2,2,2,1);
