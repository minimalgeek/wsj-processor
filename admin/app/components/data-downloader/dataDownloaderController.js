adminApp.
    controller("dataDownloaderController", function ($scope, dataDownloaderService) {
        $scope.data = {
            showLoadingBar : false,
            tradingSymbol: "",
            tradingSymbolResponse: "",

            refreshedURLs: [],

            downloadedInsiders: [],
            downloadedEarningsCalls: [],

            insiderTradingSymbol: "",
            insiderResponse: "",

            refreshAllReportDates: function () {
                $scope.data.showLoadingBar = true;
                dataDownloaderService.refreshAllReportDates(function(data) {
                    $scope.data.refreshedURLs = data;
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
                dataDownloaderService.collectEarningsCalls(function() {
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
            }
        }
    })
;