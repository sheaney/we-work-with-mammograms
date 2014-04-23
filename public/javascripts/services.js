    var staffServices = angular.module('staffServices', ['ngResource']);
    
    staffServices.factory('Staff', function($resource){
    return $resource('/api/staff/:id', {}, {
     query: {method:'GET', params: {id:'id'}, isArray:false}
    });
    });
    
    