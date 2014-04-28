var patientInfoApp = angular.module('patientInfoApp', ['patientInfoServices', 'xeditable']);

patientInfoApp.controller('PatientInfoCtrl', function($scope, PatientInfo) {
	
	// Patient id
  $scope.id = $('#patient').data('patient-id');

  PatientInfo.query({id: $scope.id}, function(data) {	
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
