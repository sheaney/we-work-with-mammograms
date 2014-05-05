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
        // Redirect to staff home
        window.location.href = jsRoutes.controllers.Staffs.staff().url;
      }).
      error(function(error, status, headers, config) {
        console.log(error);
      });
  };

  $scope.toggle = function(permission) {
    $scope.permissions[permission] = !$scope.permissions[permission];
  };

  $scope.permissions = {
    viewPersonalInfo: false,
    updatePersonalInfo: false,
    viewMedicalInfo: false,
    updateMedicalInfo: false,
    viewStudies: false,
    updateStudies: false
  };

});
