'use strict';

/* Controllers */

var staffApp = angular.module('staffApp', ['ngRoute', 'staffServices']);

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

staffApp.controller('PatientCtrl', function($scope, Staff) {
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
   	  
$scope.setPatientId = function(id) {
$scope.patientId = id;
   	  };
   	  
 });

staffApp.config(['$routeProvider',
                 function($routeProvider) {
                      $routeProvider.
                        when('/staff', {
                          templateUrl: 'staff.scala.html',
                          controller: 'PatientCtrl'
                        }).
                        otherwise({
                          redirectTo: '/staff'
                        });
                    }]);

var form_app = angular.module("form_app", ["xeditable"]);

form_app.run(function(editableOptions) {
  editableOptions.theme = 'bs3';
});

form_app.controller('FormCtrl', function($scope) {
  $scope.patient = {
    patientName: 'Juan Pérez',
    patientAdress: 'Eugenio Garza Sada #123',
    email: 'juan@hotmail.com',
    phone: '123-3431',
    bd: '28/07/1982',

    sexAct: '22',
    pregnancy: 0,
    caesarea: 0,
    labour: 0,
    abortion: 0,
    menopause: '50',
    period: '14'

  }; 

   $scope.staff = {
    staffName: 'Juan Pérez',
    staffAdress: 'Eugenio Garza Sada #123',
    email: 'juan@hotmail.com',
    phone: '123-3431',
    bd: '28/07/1982',
    role: 'Doctor',
    rfc: 'AF43L0P343',
    cedula: '1233456'
  };  

 

 $scope.options = [
    {value: 0, text: '0'},    
    {value: 1, text: '1'},
    {value: 2, text: '2'},
    {value: 3, text: '3'},
    {value: 4, text: '4'},
    {value: "5 o más", text: '5 o más'}
  ]; 

   $scope.showPregnancy = function() {
    var selected = $filter('filter')($scope.options, {value: $scope.patient.pregnancy});
    return ($scope.patient.pregnancy && selected.length) ? selected[0].text : null;
  };


 $scope.roles = [
    {value: "Doctor", text: 'Doctor'},    
    {value: "Radiólogo", text: 'Radiólogo'},
    {value: "Enfermero", text: 'Enfermero'}
  ]; 

    $scope.showRoles = function() {
    var selected = $filter('filter')($scope.roles, {value: $scope.staff.role});
    return ($scope.staff.role && selected.length) ? selected[0].text : null;
  };


});
