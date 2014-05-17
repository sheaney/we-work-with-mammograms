'use strict';

var showMammogramApp = angular.module('showMammogramApp', [
		'mammogramServices', 'CookieCtrl', 'xeditable' ]);

showMammogramApp.controller('showMammogramCtrl',
		function($scope, $http, Mammogram, id) {

			$scope.init = function(mammogramId) {
				$scope.mammogramId = mammogramId;

				Mammogram.query({id : mammogramId}, function(data) {
					$scope.mammogram = data;
          $scope.annotations = $scope.mammogram.annotations;
          $scope.prettyAnnotations = getHtmlAnnotations($scope.annotations);
				});
			};

      var getHtmlAnnotations = function(annotations) {
        var arr = annotations;
        var htmlAnnotations = "";

        for (var d = 0, len = arr.length; d < len; d += 1) {
          htmlAnnotations += htmlizeAnnotation(arr[d]);
        }

        return htmlAnnotations;
      };

      var htmlizeAnnotation = function(annotation) {
        return "<b>■</b> " + annotation.content + " - " + 
          "<b>" + getAnnotator(annotation) + "</b>" + "<br>";
      };

      var getAnnotator = function(annotation) {
        var annotator = annotation['annotatorStaff'];
        return annotator != undefined ? annotator : annotation['serviceAnnotator'];
      };

      // Create a new annotation associated to mammogram
      $scope.submitAnnotation = function(annotation) {
        var route = jsRoutes.controllers.API.createAnnotation($scope.mammogramId);
        var url = route.url;
        var method = route.method.toLowerCase();
        var annotationObj = {content: annotation};

        return $http[method](url, annotationObj).
          success(function(data, status, headers, config) {
            $scope.prettyAnnotations += htmlizeAnnotation(data);
            console.log(data); // success
          }).
          error(function(errors, status, headers, config) {
            console.log(errors); // error
          });
      };

		});

$(document).ready(function() {
	$(".annotation").popover({
		placement : 'bottom'
	});
});
