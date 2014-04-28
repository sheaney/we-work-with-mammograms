var patientInfoApp = angular.module('patientInfoApp', ['patientInfoServices', 'CookieCtrl', 'xeditable']);

patientInfoApp.controller('PatientInfoCtrl', function($scope, PatientInfo, id) {
	
	// Session Information
  $scope.id = id;

  PatientInfo.query({id: id}, function(data) {	
    $scope.patient = data;
    setPatientInfoAvailability($scope.patient);
    // need to handle failure
    $scope.fullName = data.personalInfo.name + " " + data.personalInfo.firstLastName + " " + data.personalInfo.secondLastName;
  });

  // Patient info availability
  var setPatientInfoAvailability = function(patient) {
    $scope.availablePersonalInfo = patient.personalInfo != undefined;
    $scope.availableMedicalInfo  = patient.medicalInfo != undefined;
    $scope.availableStudies      = patient.studies != undefined;
  };

  // orderBy property
  $scope.mostRecent = 'createdAt';

});

patientInfoApp.run(function(editableOptions) {
    editableOptions.theme = 'bs3';
});
