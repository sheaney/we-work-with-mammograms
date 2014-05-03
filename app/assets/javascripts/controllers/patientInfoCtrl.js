var patientInfoApp = angular.module('patientInfoApp', ['patientInfoServices', 'xeditable']);

patientInfoApp.controller('PatientInfoCtrl', function($scope, $http, PatientInfo) {
	
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

  // Obtains xeditable form from outer scope
  var getXeditableForm = function(formName) {
    var form = window[formName];
    return angular.element(form).scope()[formName];
  };

  // onaftersave handlers
  $scope.submitPersonalInfo = function() {
    var personalInfo = $scope.patient.personalInfo;
    console.log(personalInfo);
    var route = jsRoutes.controllers.API.updatePersonalInfo($scope.id);
    var xeditableForm = getXeditableForm('editablePersonalInfo');

    console.log('Submitting personal info');
    return $http.put(route.url, personalInfo).
      success(function(data, status, headers, config) {
        console.log('success');
      }).
      error(function(error, status, headers, config) {
        console.log('error');
        xeditableForm.$setError(error.field, error.msg);
      });
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
