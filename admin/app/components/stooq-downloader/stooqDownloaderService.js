adminApp.service("stooqDownloaderService", function($http) {
    this.baseURL = "/data-downloader/";
    this.downloadAll = function(func) {
        $http.get(this.baseURL + "downloadAll")
            .success(
            function(response) {
                console.log("downloadAll success response");
                func(response);
            }
        );
    };
    this.downloadMissing = function(func) {
        $http.get(this.baseURL + "downloadMissing")
            .success(
            function(response) {
                console.log("downloadMissing success response");
                func(response);
            }
        );
    };
});