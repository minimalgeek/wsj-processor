adminApp.service("zacksDownloaderService", function($http) {
    this.baseURL = "http://localhost:8080/data-downloader/";
    this.refreshAllReportDates = function(func) {
        $http.get(this.baseURL + "refreshAllReportDates")
            .success(
            function(response) {
                func(response);
            }
        );
    }
});