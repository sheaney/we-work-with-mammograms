var patientApp = angular.module('patientApp', ['patientServices', 'CookieCtrl']);

patientApp.controller('PatientController', function($scope, Patient, id) {
	
	// Session Information
  $scope.id = id;

  Patient.query({id: id}, function(data) {	
    $scope.patient = data;
    $scope.fullName = data.personalInfo.name + " " + data.personalInfo.firstLastName + " " + data.personalInfo.secondLastName;
  });

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
