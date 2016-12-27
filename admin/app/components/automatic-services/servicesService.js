adminApp.service("servicesService", function($http) {
    this.baseURL = "/data-downloader/";
    this.getErrors = function(func) {
        $http.get(this.baseURL + "getErrors")
            .success(
            function(response) {
                func(response);
            }
        );
    };
})