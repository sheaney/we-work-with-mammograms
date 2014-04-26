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
    	  
    	  $scope.patient = data.ownPatients[0];
    
    // Personal Info
    	  $scope.fullName = data.ownPatients[0].personalInfo.name + " " + data.ownPatients[0].personalInfo.firstLastName + " " + data.ownPatients[0].personalInfo.secondLastName;
    	  $scope.address = data.ownPatients[0].personalInfo.address;
    	  $scope.email = data.ownPatients[0].personalInfo.email;
    	  $scope.telephone = data.ownPatients[0].personalInfo.telephone;
    	  $scope.birthdate = data.ownPatients[0].personalInfo.birthdate;
    	    
    // Medical Info
    	  $scope.sexualActivityStartAge = data.ownPatients[0].medicalInfo.sexualActivityStartAge;
    	  $scope.pregnancies = data.ownPatients[0].medicalInfo.pregnancies;
    	  $scope.cSections = data.ownPatients[0].medicalInfo.cSections;
    	  $scope.naturalDeliveries = data.ownPatients[0].medicalInfo.naturalDeliveries;
    	  $scope.abortions = data.ownPatients[0].medicalInfo.abortions;
    	  $scope.menopauseStartAge = data.ownPatients[0].medicalInfo.menopauseStartAge;
    	  $scope.menstrualPeriodStartAge = data.ownPatients[0].medicalInfo.menstrualPeriodStartAge;

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

staffApp.config(['$routeProvider',
                 function($routeProvider) {
                      $routeProvider.
                        when('/staff', {
                          templateUrl: 'staff.scala.html',
                          controller: 'PatientsCtrl'
                        }).
                        when('/staff/patient/:id', {
                            templateUrl: 'showPatient.scala.html',
                            controller: 'PatientCtrl'
                          }).
                        otherwise({
                          redirectTo: '/staff'
                        });
                    }]);

staffApp.run(function(editableOptions) {
	  editableOptions.theme = 'bs3';
	});


var patientApp = angular.module('patientApp', ['ngRoute', 'patientServices', 'CookieCtrl']);

patientApp.controller('PatientController', function($scope, Patient, id) {
	
	// Session Information
	  $scope.id = id;
	 	  	
    Patient.query({id: id}, function(data) {	
      $scope.patient = data;
    
    // Personal Information
  	  $scope.fullName = data.personalInfo.name + " " + data.personalInfo.firstLastName + " " + data.personalInfo.secondLastName;
  	  $scope.address = data.personalInfo.address;
  	  $scope.email = data.personalInfo.email;
  	  $scope.telephone = data.personalInfo.telephone;
  	  $scope.birthdate = data.personalInfo.birthdate;
  	  
  	// Medical Information
  	  $scope.sexualActivityStartAge = data.medicalInfo.sexualActivityStartAge;
  	  $scope.pregnancies = data.medicalInfo.pregnancies;
  	  $scope.cSections = data.medicalInfo.cSections;
  	  $scope.naturalDeliveries = data.medicalInfo.naturalDeliveries;
  	  $scope.abortions = data.medicalInfo.abortions;
  	  $scope.menopauseStartAge = data.medicalInfo.menopauseStartAge;
  	  $scope.familyPredisposition = data.medicalInfo.familyPredisposition;
  	  $scope.hormonalReplacementTherapy = data.medicalInfo.hormonalReplacementTherapy;
  	  $scope.previousMammaryDiseases = data.medicalInfo.previousMammaryDiseases;
  	  $scope.menstrualPeriodStartAge = data.medicalInfo.menstrualPeriodStartAge;
  	  $scope.breastfedChildren = data.medicalInfo.breastfedChildren; 	  
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


patientApp.config(['$routeProvider',
                 function($routeProvider) {
                      $routeProvider.
                        when('/patient', {
                          templateUrl: 'patient.scala.html',
                          controller: 'PatientController'
                        }).
                        otherwise({
                          redirectTo: '/patient'
                        });
                    }]);

/*
   $scope.showPregnancy = function() {
    var selected = $filter('filter')($scope.options, {value: $scope.patient.pregnancy});
    return ($scope.patient.pregnancy && selected.length) ? selected[0].text : null;
  };


    $scope.showRoles = function() {
    var selected = $filter('filter')($scope.roles, {value: $scope.staff.role});
    return ($scope.staff.role && selected.length) ? selected[0].text : null;
  };
*/