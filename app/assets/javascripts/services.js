    var staffServices = angular.module('staffServices', ['ngResource']);
    
    staffServices.factory('Staff', function($resource){
    return $resource('/api/staff/:id', {}, {
     query: {method:'GET', params: {id:'id'}, isArray:false}
    });
    });
    
    var patientServices = angular.module('patientServices', ['ngResource']);

    patientServices.factory('Patient', function($resource){
        return $resource('/api/patient/:id', {}, {
         query: {method:'GET', params: {id:'id'}, isArray:false}
        });
        });
    
    