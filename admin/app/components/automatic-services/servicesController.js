adminApp.controller("servicesController", function($scope, servicesService) {
	$scope.data = {
		errors : [],

		getErrors : function() {
			servicesService.getErrors(function(data) {
				$scope.data.errors = data;
			});
		}
	}
});