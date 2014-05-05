'use strict';

var staffApp = angular.module('staffApp', ['staffServices', 'CookieCtrl']);
staffApp.controller('ShowStaffCtrl', function($scope, Staff, id) {

  Staff.query({id: id}, function(data) {
    $scope.staff = data;
  });

  $scope.sharedTag = function(patient) {
    return patient['shared'] ? 'PRESTADO' : '';
  };

  $scope.setPatientId = function(id) {
    $scope.patientId = id;
  };

  $scope.cssClass = function(patient) {
    return $scope.borrowedPatient(patient) ? 'success' : 'info';
  };

  $scope.borrowedPatient = function(patient) {
    return patient['shared'] === undefined;
  };

  $scope.show = function(patient) {
    window.location = jsRoutes.controllers.Staffs.showPatient(patient.id).url;
  };

  $scope.fullName = function(patient) {
    if (patient.personalInfo) {
      return patient.personalInfo.name + " " + patient.personalInfo.firstLastName + " " + patient.personalInfo.secondLastName;
    } else {
      return defaults.fullName;
    }
  };

  $scope.numberOfStudiesText = function(patient) {
    if (patient.studies) {
      var numberOfStudies = patient.studies.length;
      return numberOfStudies > 0 ? numberOfStudies : 'No tiene estudios';
    } else {
      return defaults.nbrStudies;
    }
  };

  $scope.setPatientId = function(id) {
    $scope.patientId = id;
  };

  // Default values when access to information is denied
  var defaults = {
    fullName: 'Paciente compartido',
    nbrStudies: 'No se sabe'
  };

  $scope.orderProp = 'personalInfo.name';
});
