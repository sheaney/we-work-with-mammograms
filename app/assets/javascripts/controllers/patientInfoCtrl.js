var patientInfoApp = angular.module('patientInfoApp', ['patientInfoServices', 'xeditable']);

patientInfoApp.controller('PatientInfoCtrl', function($scope, $http, $filter, PatientInfo) {
	
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
    var patient = data;
    $scope.patient = patient;
    setPatientInfoAvailability(patient);
    setUpdateableInfo(patient);
    setExtraScopeAttributes(patient);
    // need to handle failure
  });

  // Patient info availability
  var setPatientInfoAvailability = function(patient) {
    $scope.availablePersonalInfo = patient.personalInfo != undefined;
    $scope.availableMedicalInfo  = patient.medicalInfo != undefined;
    $scope.availableStudies      = patient.studies != undefined;
  };

  var setUpdateableInfo = function(patient) {
    $scope.updateablePersonalInfo = patient.updateablePersonalInfo;
    $scope.updateableMedicalInfo  = patient.updateableMedicalInfo;
    $scope.updateableStudies      = patient.updateableStudies;
  };

  var setExtraScopeAttributes = function(patient) {
    // Render correct date format for birthdate
    if ($scope.availablePersonalInfo) {
      $scope.patient.personalInfo.birthdate= $filter('date')($scope.patient.personalInfo.birthdate, 'dd/MM/yyyy');
    }
    $scope.studyUrl = jsRoutes.controllers.Staffs.study($scope.id, $scope.patient.id).url;
  };

  // Patient full name gets computed if personalInfo is available
  $scope.patientFullName = function() {
    if ($scope.availablePersonalInfo) {
      return $scope.patient.personalInfo.name + " " + $scope.patient.personalInfo.firstLastName + " " + $scope.patient.personalInfo.secondLastName;
    } else {
      return 'Paciente prestado';
    }
  };

  // Obtains xeditable form from outer scope
  var getXeditableForm = function(formName) {
    var form = window[formName];
    return angular.element(form).scope()[formName];
  };

  $scope.submitPersonalInfo = function() {
    var route = jsRoutes.controllers.API.updatePersonalInfo($scope.id);
    var personalInfo = $scope.patient.personalInfo;
    var xeditableForm = getXeditableForm('editablePersonalInfo');

    return updateRequest(route.url, personalInfo, xeditableForm);
  };

  $scope.submitMedicalInfo = function() {
    var route = jsRoutes.controllers.API.updateMedicalInfo($scope.id);
    var medicalInfo = $scope.patient.medicalInfo;
    var xeditableForm = getXeditableForm('editableMedicalInfo');

    return updateRequest(route.url, medicalInfo, xeditableForm);
  };

  // Updates patient info on server
  var updateRequest = function(url, info, xeditableForm) {
    return $http.put(url, info).
      success(function(data, status, headers, config) {
        console.log(data); // Success
      }).
      error(function(errors, status, headers, config) {
        for (var i = 0; i < errors.length; i++) {
          var error = errors[i];
          xeditableForm.$setError(error.field, error.msg);
        }
      });
  };

  // ng orderBy property
  $scope.mostRecent = 'createdAt';

});

patientInfoApp.run(function(editableOptions) {
  editableOptions.theme = 'bs3';
});
