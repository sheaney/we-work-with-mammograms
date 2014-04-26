'use strict';

var sharePatientApp = angular.module('sharePatientApp', ['staffServices', 'xeditable']);

sharePatientApp.controller('StaffListCtrl', function($scope) {
  $scope.selectedMember = null;

  $scope.displayMember = function(member) {
    $scope.selectedMember = member;
  };

  $scope.fullName = function(member) {
    return member.name + " " + member.firstLastName + " " + member.secondLastName;
  };

  $scope.orderName = 'name';
});
