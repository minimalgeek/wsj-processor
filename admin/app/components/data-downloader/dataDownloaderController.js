adminApp.
    controller("dataDownloaderController", function ($scope, dataDownloaderService) {
        $scope.data = {
            showLoadingBar : false,
            tradingSymbol: "",
            tradingSymbolResponse: "",
            refreshedURLs: [],
            downloadedInsiders: [],
            downloadedEarningsCalls: [],
            refreshAllReportDates: function () {
                $scope.data.showLoadingBar = true;
                dataDownloaderService.refreshAllReportDates(function(data) {
                    $scope.data.refreshedURLs = data;
                    $scope.data.showLoadingBar = false;
                });
            },
            collectInsiderContent: function () {
                $scope.data.showLoadingBar = true;
                dataDownloaderService.collectInsiderContent(function() {
                    $scope.data.downloadedInsiders = data;
                    $scope.data.showLoadingBar = false;
                });
            },
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