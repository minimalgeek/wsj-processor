adminApp.service('wsjReceiverService', function($http) {
    this.baseURL = "http://localhost:8080/wsj-receiver/";
    this.valami = function() {
        $http.get(this.baseURL + "valami")
        .success(
            function(response) {

            }
        );
    }
});