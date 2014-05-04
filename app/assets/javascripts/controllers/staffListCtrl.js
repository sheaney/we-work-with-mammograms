'use strict';

var sharePatientApp = angular.module('sharePatientApp', ['sharePatientServices']);

sharePatientApp.controller('StaffListCtrl', function($scope, $http, Staff) {
	
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
  
  $scope.submit = function(id){
	console.log("Selected Member ID: " + id);  
	$scope.id = id; 
	$http.post("@routes.Staffs.showPatient(id)", "id=" + $scope.id);

  };
	  
});
