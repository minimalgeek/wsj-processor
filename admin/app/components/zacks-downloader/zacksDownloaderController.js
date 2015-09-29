adminApp.
    controller("zacksDownloaderController", function ($scope, zacksDownloaderService) {
        $scope.data = {
            refreshedURLs: [],
            refreshAllReportDates: function () {
                zacksDownloaderService.refreshAllReportDates($scope.data.refreshedURLs);
            }
        }
    })
;