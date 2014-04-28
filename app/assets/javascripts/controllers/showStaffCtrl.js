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
    return patient.personalInfo.name + " " + patient.personalInfo.firstLastName + " " + patient.personalInfo.secondLastName;
  };

  $scope.numberOfStudiesText = function(patient) {
    var numberOfStudies = patient.studies.length;
    return numberOfStudies > 0 ? numberOfStudies : 'No tiene estudios';
  };

  $scope.setPatientId = function(id) {
    $scope.patientId = id;
  };

  $scope.orderProp = 'personalInfo.name';
});
