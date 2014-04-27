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

var patientInfoServices = angular.module('patientInfoServices', ['ngResource']);

patientInfoServices.factory('PatientInfo', function($resource){
  return $resource('/api/patient/:id/info', {}, {
    query: {method: 'GET', params: {id: 'id'}, isArray: false}
  });
});

var CookieCtrl = angular.module('CookieCtrl', ['ngCookies']);

//Cookie Session
CookieCtrl.factory('id', ['$cookies', function($cookies) {
    // read Play session cookie
    var rawCookie = $cookies['PLAY_SESSION'];
    var rawData = rawCookie.substring(rawCookie.indexOf('-') + 1, rawCookie.length -1);
    var session = {};
    var id;
    rawData.split("&").forEach(function(rawPair){
        var pair = rawPair.split('=');
        if(pair[0] == "id")
          id = pair[1];
        session[pair[0]] = pair[1];
    });
    return id;
}])
