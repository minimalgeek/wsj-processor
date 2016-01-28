adminApp.service("dataDownloaderService", function($http) {
    this.baseURL = "/data-downloader/";
    this.refreshAllReportDates = function(func) {
        $http.get(this.baseURL + "refreshAllReportDates")
            .success(
            function(response) {
                func(response);
            }
        );
    };
    this.collectInsiderContent = function(func) {
        $http.get(this.baseURL + "collectContent")
            .success(
            function(response) {
                func(response);
            }
        );
    };
    this.collectEarningsCalls = function(func) {
        $http.get(this.baseURL + "collectEarningsCalls")
            .success(
            function(response) {
                func(response);
            }
        );
    }
});