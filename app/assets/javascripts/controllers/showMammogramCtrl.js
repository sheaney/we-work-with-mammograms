'use strict';

var showMammogramApp = angular.module('showMammogramApp', [
		'mammogramServices', 'CookieCtrl' ]);
showMammogramApp.controller('showMammogramCtrl',
		function($scope, Mammogram, id) {

			$scope.init = function(mammogramId) {
				$scope.mammogramId = mammogramId;
				console.log(mammogramId);

				Mammogram.query({
					id : mammogramId
				}, function(data) {

					var mammogram = data;
					
					// Shows Annotations
					$scope.showAnnotations = function() {
						var arr = mammogram.annotations;
						var annotations = "<font size='3'>";
					
					// Iterates between annotations array
							for (var d = 0, len = arr.length; d < len; d += 1) {
								annotations = annotations + "<b>■</b> " + arr[d].content + "<br>";
						}

						annotations = annotations + "</font>";
						return annotations;
					};

				});

			};

		});

$(document).ready(function() {
	$(".annotation").popover({
		placement : 'bottom'
	});
});
