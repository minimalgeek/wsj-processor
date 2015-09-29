adminApp.
    controller("wsjReceiverController", function ($scope, wsjReceiverService) {
        $scope.data = {
            refreshedURLs: [],
            refreshAllReportDates: function () {
                wsjReceiverService.valami();
            }
        }
    })
;