var patientInfoApp = angular.module('patientInfoApp', ['patientInfoServices', 'CookieCtrl']);

patientInfoApp.controller('PatientInfoCtrl', function($scope, PatientInfo, id) {
	
	// Session Information
  $scope.id = id;

  PatientInfo.query({id: id}, function(data) {	
    $scope.patient = data;
    // need to handle failure
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
