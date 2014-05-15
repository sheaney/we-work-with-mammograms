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

  $scope.comments = function(comment) {
    return comment.content;
  };

  $scope.formatDate = function(epoch) {
    var date = new Date(epoch);
    return moment(date).format('LLLL');
  };

  $scope.showMammogram = function(study, mammogram) {
    var mammogramUrl = jsRoutes.controllers.Patients.showMammogram(study.id, mammogram.id).url;
    window.location.href = mammogramUrl;
  };

  // ng orderBy property
  $scope.mostRecent = '-createdAt';

});
