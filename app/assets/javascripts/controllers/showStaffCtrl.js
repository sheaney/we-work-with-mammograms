'use strict';

var staffApp = angular.module('staffApp', ['staffServices', 'xeditable']);
staffApp.controller('ShowStaffCtrl', function($scope, Staff) {

  Staff.query({id: '1'}, function(data) {
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

staffApp.run(function(editableOptions) { editableOptions.theme = 'bs3'; });

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
