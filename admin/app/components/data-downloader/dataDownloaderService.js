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
    this.downloadAllZECD = function(func) {
        $http.get(this.baseURL + "downloadAllZECD")
            .success(
            function(response) {
                func(response);
            }
        );
    };
    this.collectInsiderContent = function(func) {
        $http.get(this.baseURL + "collectGroupContent")
            .success(
            function(response) {
                func(response);
            }
        );
    };
    this.collectInsiderContentFor = function(id, func) {
        $http.get(this.baseURL + "collectGroupContentFor/" + id)
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
    };
    this.collectEarningsCallsFor = function(id, func) {
        $http.get(this.baseURL + "collectEarningsCallsFor/" + id)
            .success(
            function(response) {
                func(response);
            }
        );
    };
    this.downloadShortInterestData = function(func) {
        $http.get(this.baseURL + "downloadShortInterestData")
            .success(
            function(response) {
                func(response);
            }
        );
    };
    this.downloadAllIPOActivity = function(func) {
        $http.get(this.baseURL + "downloadAllIPOActivity")
            .success(
            function(response) {
                func(response);
            }
        );
    };
    this.downloadOilReports = function(func) {
        $http.get(this.baseURL + "downloadOilReports")
            .success(
            function() {
                func();
            }
        );
    };
    this.downloadHistoricalShortInterest = function(func) {
        $http.get(this.baseURL + "downloadHistoricalShortInterest")
            .success(
            function() {
                func();
            }
        );
    };
});