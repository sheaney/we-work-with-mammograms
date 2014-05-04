'use strict';

var sharePatientApp = angular.module('sharePatientApp', ['sharePatientServices']);

sharePatientApp.controller('StaffListCtrl', function($scope, $http, Staff) {
  // Patient id
  $scope.patientId = $('#staff-selector').data('patient-id');
	
	Staff.query(function(data) {	
    $scope.staff = data;
  });	
	
  $scope.selectedMember = null;

  $scope.displayMember = function(member) {
    $scope.selectedMember = member;
  };

  $scope.fullName = function(member) {
    return member.name + " " + member.firstLastName + " " + member.secondLastName;
  };

  $scope.orderName = 'name';

  // Triggers createSharedPatient action in server
  $scope.sharePatient = function(borrowerId) {
    var route  = jsRoutes.controllers.Staffs.createSharedPatient($scope.patientId, borrowerId);
    var method = route.method.toLowerCase();
    var url    = route.url;
    $http[method](url, $scope.permissions).
      success(function(data, status, headers, config) {
        console.log(data); // Success
      }).
      error(function(error, status, headers, config) {
        console.log(error);
      });
  };

  $scope.permissions = {
    viewPersonalInfo: false,
    editPersonalInfo: false,
    viewMedicalInfo: false,
    editMedicalInfo: false,
    viewStudies: false,
    editStudies: false
  };

});
