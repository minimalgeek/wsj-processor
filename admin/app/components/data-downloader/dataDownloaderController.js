adminApp.
    controller("dataDownloaderController", function ($scope, dataDownloaderService) {
        $scope.data = {
            showLoadingBar : false,
            tradingSymbol: "",
            tradingSymbolResponse: "",
            nrOfTranscripts: 4,

            refreshedURLs: [],
            downloadedURLs: [],

            downloadedInsiders: [],
            downloadedEarningsCalls: [],
            downloadedShortInterestData: [],
            downloadedZECDs: [],

            insiderTradingSymbol: "",
            insiderResponse: "",
            ipoActivity: "",
            oilReportsResponse: "",

            refreshAllReportDates: function () {
                $scope.data.showLoadingBar = true;
                dataDownloaderService.refreshAllReportDates(function(data) {
                    $scope.data.refreshedURLs = data;
                    $scope.data.showLoadingBar = false;
                });
            },

            downloadAllZECD: function () {
                $scope.data.showLoadingBar = true;
                dataDownloaderService.downloadAllZECD(function(data) {
                    $scope.data.downloadedZECDs = data;
                    $scope.data.showLoadingBar = false;
                });
            },

            // INSIDER TRADING
            collectInsiderContent: function () {
                $scope.data.showLoadingBar = true;
                dataDownloaderService.collectInsiderContent(function(data) {
                    $scope.data.downloadedInsiders = data;
                    $scope.data.showLoadingBar = false;
                });
            },
            collectInsiderContentFor: function () {
                $scope.data.showLoadingBar = true;
                dataDownloaderService.collectInsiderContentFor($scope.data.insiderTradingSymbol, function(data) {
                    $scope.data.insiderResponse = data;
                    $scope.data.showLoadingBar = false;
                });
            },

            // EARNINGS CALL
            collectEarningsCalls: function () {
                $scope.data.showLoadingBar = true;
                dataDownloaderService.collectEarningsCalls(function(data) {
                    $scope.data.downloadedEarningsCalls = data;
                    $scope.data.showLoadingBar = false;
                });
            },
            collectEarningsCallsForSymbol : function() {
                $scope.data.showLoadingBar = true;
                dataDownloaderService.collectEarningsCallsFor($scope.data.tradingSymbol, function(data) {
                    $scope.data.tradingSymbolResponse = data;
                    $scope.data.showLoadingBar = false;
                });
            },
            collectLastNTranscripts : function() {
                $scope.data.showLoadingBar = true;
                dataDownloaderService.collectLastNTranscripts($scope.data.nrOfTranscripts, function(data) {
                    $scope.data.downloadedURLs = data;
                    $scope.data.showLoadingBar = false;
                });
            },

            // SHORT INTEREST
            downloadShortInterestData: function () {
                $scope.data.showLoadingBar = true;
                dataDownloaderService.downloadShortInterestData(function(data) {
                    $scope.data.downloadedShortInterestData = data;
                    $scope.data.showLoadingBar = false;
                });
            },

            downloadHistoricalShortInterest: function() {
                $scope.data.showLoadingBar = true;
                dataDownloaderService.downloadHistoricalShortInterest(function() {
                    $scope.data.showLoadingBar = false;
                });
            },

            // IPO
            downloadAllIPOActivity: function () {
                $scope.data.showLoadingBar = true;
                dataDownloaderService.downloadAllIPOActivity(function(data) {
                    $scope.data.ipoActivity = data;
                    $scope.data.showLoadingBar = false;
                });
            },

            // OIL REPORTS
            downloadOilReports: function () {
                $scope.data.showLoadingBar = true;
                dataDownloaderService.downloadOilReports(function() {
                    $scope.data.showLoadingBar = false;
                });
            },

            // MACRO MAN
            downloadMacroMans: function () {
                $scope.data.showLoadingBar = true;
                dataDownloaderService.downloadMacroMans(function() {
                    $scope.data.showLoadingBar = false;
                });
            }
        }
    })
;