'use strict';

/* Controllers */

var staffApp = angular.module('staffApp', []);

staffApp.controller('StaffListCtrl', function($scope) {
  $scope.selectedMember = null;

  $scope.displayMember = function(member) {
    $scope.selectedMember = member;
  };

  $scope.fullName = function(member) {
    return member.name + " " + member.firstLastName + " " + member.secondLastName;
  };

  $scope.staff = [
    {
      'id': 0,
      'name': 'Dr. Juan Francisco',
      'firstLastName': 'Martínez ',
      'secondLastName': 'Garza'
    },
    {
      'id': 1,
      'name': 'Juan',
      'firstLastName': 'Cordova ',
      'secondLastName': 'Santa'
    },
    {
      'id': 2,
      'name': 'Dr. Juan',
      'firstLastName': 'De La Garza',
      'secondLastName': 'Lujan'
    }
  ];

  $scope.orderName = 'name';
});

staffApp.controller('PatientListCtrl', ['$scope', '$http', function($scope, $http, $location) {
  $scope.show= function(patient) {
    window.location = jsRoutes.controllers.Staffs.showPatient(patient.id).url;
  };

  $scope.setPatientId = function(id) {
    $scope.patientId = id;
  };

  $scope.cssClass = function(ownPatient) {
    return ownPatient ? 'info' : 'success';
  };

  $scope.numberOfStudiesText = function(patient) {
    var numberOfStudies = patient.studies.length;
    return numberOfStudies > 0 ? numberOfStudies : 'No tiene estudios';
  };

  $scope.fullName = function(patient) {
    return patient.name + " " + patient.firstLastName + " " + patient.secondLastName;
  };

  $scope.patients = [
    {
      'id': 0,
      'name': 'Juan Francisco',
      'firstLastName': 'Martínez ',
      'secondLastName': 'Garza',
      'own': false,
      'studies': [1,2,3]
    },
    {
      'id': 1,
      'name': 'Katia',
      'firstLastName': 'Cordova ',
      'secondLastName': 'Santa',
      'own': true,
      'studies': []
    },
    {
      'id': 2,
      'name': 'Aura',
      'firstLastName': 'De La Garza ',
      'secondLastName': 'Lujan',
      'own': true,
      'studies': [1]
    },
    {
      'id': 3,
      'name': 'Alejandra',
      'firstLastName': 'Pulido ',
      'secondLastName': 'De La Llave',
      'own': false,
      'studies': [1]
    },
    {
      'id': 4,
      'name': 'Enrique',
      'firstLastName': 'Sanchez ',
      'secondLastName': 'Godoy',
      'own': false,
      'studies': [1,2]
    },
    {
      'id': 5,
      'name': 'Carlos',
      'firstLastName': 'Garza ',
      'secondLastName': 'Quintero',
      'own': true,
      'studies': []
    },
    {
      'id': 6,
      'name': 'Kristina',
      'firstLastName': 'Montejano ',
      'secondLastName': 'De La Garza',
      'own': false,
      'studies': [1,2,3]
    },
    {
      'id': 7,
      'name': 'Erubiel',
      'firstLastName': 'Zambrano ',
      'secondLastName': 'Moctezuma',
      'own': true,
      'studies': [1,2]
    }
  ];

  $scope.orderName = 'name';
}]);

var form_app = angular.module("form_app", ["xeditable"]);

form_app.run(function(editableOptions) {
  editableOptions.theme = 'bs3';
});

form_app.controller('FormCtrl', function($scope) {
  $scope.patient = {
    patientName: 'Juan Pérez',
    patientAdress: 'Eugenio Garza Sada #123',
    email: 'juan@hotmail.com',
    phone: '123-3431',
    bd: '28/07/1982',

    sexAct: '22',
    pregnancy: 0,
    caesarea: 0,
    labour: 0,
    abortion: 0,
    menopause: '50',
    period: '14'

  }; 

   $scope.staff = {
    staffName: 'Juan Pérez',
    staffAdress: 'Eugenio Garza Sada #123',
    email: 'juan@hotmail.com',
    phone: '123-3431',
    bd: '28/07/1982',
    role: 'Doctor',
    rfc: 'AF43L0P343',
    cedula: '1233456'
  };  

 

 $scope.options = [
    {value: 0, text: '0'},    
    {value: 1, text: '1'},
    {value: 2, text: '2'},
    {value: 3, text: '3'},
    {value: 4, text: '4'},
    {value: "5 o más", text: '5 o más'}
  ]; 

   $scope.showPregnancy = function() {
    var selected = $filter('filter')($scope.options, {value: $scope.patient.pregnancy});
    return ($scope.patient.pregnancy && selected.length) ? selected[0].text : null;
  };


 $scope.roles = [
    {value: "Doctor", text: 'Doctor'},    
    {value: "Radiólogo", text: 'Radiólogo'},
    {value: "Enfermero", text: 'Enfermero'}
  ]; 

    $scope.showRoles = function() {
    var selected = $filter('filter')($scope.roles, {value: $scope.staff.role});
    return ($scope.staff.role && selected.length) ? selected[0].text : null;
  };


});
