adminApp.config(['$routeProvider', function($routeProvider) {
    $routeProvider.

        when('/zacksDownloader', {
            templateUrl: 'app/components/zacks-downloader/zacksDownloader.html',
            controller: 'zacksDownloaderController'
        }).

        when('/stooqDownloader', {
            templateUrl: 'app/components/stooq-downloader/stooqDownloader.html',
            controller: 'stooqDownloaderController'
        }).

        when('/wsjReceiver', {
            templateUrl: 'app/components/wsj-receiver/wsjReceiver.html',
            controller: 'wsjReceiverController'
        }).

        otherwise({
            redirectTo: '/zacksDownloader'
        });
}]);