adminApp.config(['$routeProvider', function($routeProvider) {
    $routeProvider.

        when('/dataDownloader', {
            templateUrl: 'app/components/data-downloader/dataDownloader.html',
            controller: 'dataDownloaderController'
        }).

        when('/stooqDownloader', {
            templateUrl: 'app/components/stooq-downloader/stooqDownloader.html',
            controller: 'stooqDownloaderController'
        }).

        when('/wsjReceiver', {
            templateUrl: 'app/components/wsj-receiver/wsjReceiver.html',
            controller: 'wsjReceiverController'
        }).

        when('/automaticServices', {
            templateUrl: 'app/components/automatic-services/services.html',
            controller: 'servicesController'
        }).

        otherwise({
            redirectTo: '/dataDownloader'
        });
}]);