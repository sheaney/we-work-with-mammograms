'use strict';

var showMammogramApp = angular.module('showMammogramApp', ['patientInfoServices', 'CookieCtrl']);
showMammogramApp.controller('showMammogramCtrl', function($scope, PatientInfo, id) {
	
//PatientInfo.query({id: 1}, function(data) {	

	// Shows Annotations
	$scope.showAnnotations = //function(study, mammogram){
		//data.studies[0].mammograms[0];
		// Hardcoded annotation
		"1. Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum. <br> 2. Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";	//}
	//});
	
	
});
  	

$(document).ready(function(){
    $(".annotation").popover({
        placement : 'bottom'
    });
});
