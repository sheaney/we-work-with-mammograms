'use strict';

/* Controllers */

var staffApp = angular.module('staffApp', ['ngRoute', 'staffServices', 'xeditable']);

staffApp.controller('StaffCtrl', function($scope) {
  $scope.selectedMember = null;

  $scope.displayMember = function(member) {
    $scope.selectedMember = member;
  };

  $scope.fullName = function(member) {
    return member.name + " " + member.firstLastName + " " + member.secondLastName;
  };

  $scope.orderName = 'name';	  
});

staffApp.controller('PatientsCtrl', function($scope, Staff) {
    Staff.query({id: '1'}, function(data) {
  	  $scope.staff = data;
  	  console.log($scope.staff);
    }
); 
          
$scope.fullName = function(patient) {
return patient.personalInfo.name + " " + patient.personalInfo.firstLastName + " " + patient.personalInfo.secondLastName;
 	  };
 	  
$scope.numberOfStudiesText = function(patient) {
 	    var numberOfStudies = patient.studies.length;
 	    return numberOfStudies > 0 ? numberOfStudies : 'No tiene estudios';
 	  };
 	  
$scope.cssClass = function(ownPatient) {
 		    return ownPatient ? 'info' : 'success';
 		  };
 	  
$scope.setPatientId = function(id) {
$scope.patientId = id;
 	  };
 	  
$scope.show= function(patient) {
  window.location = jsRoutes.controllers.Staffs.showPatient(patient.id).url;
   };
 	  
});

staffApp.controller('PatientCtrl', function($scope, Staff) {
      Staff.query({id: '1'}, function(data) {
    	  
    	 
    	  $scope.options = [
    	                    {value: 0, text: '0'},    
    	                    {value: 1, text: '1'},
    	                    {value: 2, text: '2'},
    	                    {value: 3, text: '3'},
    	                    {value: 4, text: '4'},
    	                    {value: "5 o más", text: '5 o más'}
    	                  ];
    	  
    	  $scope.roles = [
    	                  {value: "Doctor", text: 'Doctor'},    
    	                  {value: "Radiólogo", text: 'Radiólogo'},
    	                  {value: "Enfermero", text: 'Enfermero'}
    	                ]; 

    // Studies
    	  $scope.studyDate = function(study) {
    	  	return study.createdAt;
    	  		   	  };
    	  		   	  
    	  $scope.mammogramId = function(mammogram) {
    	  	return mammogram.id;
    	  		   	 };
    	  		   	 
    	  $scope.comments = function(comment) {
    	  	return comment.content;
    	  		   	 };
      }
      
    
); 
           
  	  
 });


staffApp.run(function(editableOptions) {
	  editableOptions.theme = 'bs3';
	});


var patientApp = angular.module('patientApp', ['ngRoute', 'patientServices', 'CookieCtrl']);

patientApp.controller('PatientController', function($scope, Patient, id) {
	
	// Session Information
	  $scope.id = id;
	 	  	
    Patient.query({id: id}, function(data) {	
      $scope.patient = data;
    
  	  $scope.fullName = data.personalInfo.name + " " + data.personalInfo.firstLastName + " " + data.personalInfo.secondLastName;
  	  	  
    }
     
); 
     
 	// Studies
$scope.studyDate = function(study) {
	return study.createdAt;
		   	  };
		   	  
$scope.mammogramId = function(mammogram) {
	return mammogram.id;
		   	 };
		   	 
$scope.comments = function(comment) {
	return comment.content;
		   	 };

});