adminApp.
    controller("stooqDownloaderController", function ($scope, stooqDownloaderService) {
        $scope.data = {
            showLoadingBar : false,
            pairs: [],
            downloadAll: function () {
                $scope.data.showLoadingBar = true;
                stooqDownloaderService.downloadAll(function (data) {
                    console.log("downloadAll callback");
                    $scope.data.pairs = data;
                    $scope.data.showLoadingBar = false;
                });
            },
            downloadMissing: function () {
                $scope.data.showLoadingBar = true;
                stooqDownloaderService.downloadMissing(function (data) {
                    console.log("downloadMissing callback");
                    $scope.data.refreshedPairs = data;
                    $scope.data.showLoadingBar = false;
                });
            }
        }
    })
;