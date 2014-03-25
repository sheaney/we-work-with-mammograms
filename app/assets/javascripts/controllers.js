'use strict';

/* Controllers */

var staffApp = angular.module('staffApp', []);

staffApp.controller('StaffListCtrl', function($scope) {
  $scope.staff = [
    { 'name': 'Dr. Juan Francisco Martínez Garza' },
    { 'name': 'Juan Cordova Santa' },
    { 'name': 'Dr. Juan De La Garza Lujan' }
  ];

  $scope.orderName = 'name';
});

staffApp.controller('PatientListCtrl', function($scope) {
  $scope.navigateToPatient = function(url, id) {
    window.location.href = url.replace('-1', id)
  };

  $scope.setPatientId = function(id) {
    $scope.patientId = id;
  };

  $scope.cssClass = function(ownPatient) {
    return ownPatient ? 'info' : 'success';
  };

  $scope.numberOfStudiesText = function(nbrOfStudies) {
    return nbrOfStudies > 0 ? nbrOfStudies : 'No tiene estudios';
  }

  $scope.patients = [
    {
      'id': 0,
      'name': 'Juan Francisco Martínez Garza',
      'own': false,
      'studies': 3
    },
    {
      'id': 1,
      'name': 'Katia Cordova Santa',
      'own': true,
      'studies': 0
    },
    {
      'id': 2,
      'name': 'Aura De La Garza Lujan',
      'own': true,
      'studies': 1
    },
    {
      'id': 3,
      'name': 'Alejandra Pulido De La Llave',
      'own': false,
      'studies': 1
    },
    {
      'id': 4,
      'name': 'Enrique Sanchez Godoy',
      'own': false,
      'studies': 2
    },
    {
      'id': 5,
      'name': 'Carlos Garza Quintero',
      'own': true,
      'studies': 0
    },
    {
      'id': 6,
      'name': 'Kristina Montejano De La Garza',
      'own': false,
      'studies': 3
    },
    {
      'id': 7,
      'name': 'Erubiel Zambrano Moctezuma',
      'own': true,
      'studies': 2
    }
  ];

  $scope.orderName = 'name';
});
