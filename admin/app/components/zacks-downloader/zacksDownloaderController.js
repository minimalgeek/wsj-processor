adminApp.
    controller("zacksDownloaderController", function ($scope, zacksDownloaderService) {
        $scope.data = {
            showLoadingBar : false,
            refreshedURLs: [],
            refreshAllReportDates: function () {
                $scope.data.showLoadingBar = true;
                zacksDownloaderService.refreshAllReportDates(function(data) {
                    $scope.data.refreshedURLs = data;
                    $scope.data.showLoadingBar = false;
                });
            }
        }
    })
;