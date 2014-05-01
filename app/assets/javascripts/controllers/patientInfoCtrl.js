var patientInfoApp = angular.module('patientInfoApp', ['patientInfoServices', 'xeditable']);

patientInfoApp.controller('PatientInfoCtrl', function($scope, PatientInfo) {
	
	// Patient id
  $scope.id = $('#patient').data('patient-id');

  // Numbered options
  $scope.options = [
    {value: 0, text: '0'},
    {value: 1, text: '1'},
    {value: 2, text: '2'},
    {value: 3, text: '3'},
    {value: 4, text: '4'},
    {value: 5, text: '5'},
    {value: 6, text: '6'},
    {value: 7, text: '7'},
    {value: 8, text: '8'},
    {value: 9, text: '9'}
  ];

  // True or false
  $scope.booleans = [
    {value: true, text: 'Sí'},
    {value: false, text: 'No'}
  ];

  $scope.translate = function(value) {
    return value ? 'Sí' : 'No';
  };

  PatientInfo.query({id: $scope.id}, function(data) {	
    $scope.patient = data;
    setPatientInfoAvailability($scope.patient);
    // need to handle failure
  });

  // Patient info availability
  var setPatientInfoAvailability = function(patient) {
    $scope.availablePersonalInfo = patient.personalInfo != undefined;
    $scope.availableMedicalInfo  = patient.medicalInfo != undefined;
    $scope.availableStudies      = patient.studies != undefined;
  };

  // onaftersave handlers
  $scope.submitPersonalInfo = function() {
    var personalInfo = $scope.patient.personalInfo;
    console.log(personalInfo);
    console.log('Submitting personal info');
  };

  $scope.submitMedicalInfo = function() {
    var medicalInfo = $scope.patient.medicalInfo;
    console.log(medicalInfo);
    console.log('Submitting medical info');
  };

  // ng orderBy property
  $scope.mostRecent = 'createdAt';

});

patientInfoApp.run(function(editableOptions) {
    editableOptions.theme = 'bs3';
});
