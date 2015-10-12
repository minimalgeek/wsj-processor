adminApp.service('wsjReceiverService', function($http) {
    this.baseURL = "/wsj-receiver/";
    this.moveArticles = function(func) {
        $http.get(this.baseURL + "moveArticles")
        .success(
            function(response) {
                func(response);
            }
        );
    };
    this.processAllArticle = function(func) {
        $http.get(this.baseURL + "processAllArticle")
            .success(
            function(response) {
                func(response);
            }
        );
    };
    this.getArticle = function(id, func) {
        $http.get(this.baseURL + "get-article?id=" + id)
            .success(
            function(response) {
                func(response);
            }
        );
    };
    this.collectContent = function(func) {
        $http.get(this.baseURL + "collectContent")
            .success(
            function(response) {
                func(response);
            }
        );
    };
    this.exportDatesToCSV = function(func) {
        $http.get(this.baseURL + "exportDatesToCSV")
            .success(
            function(response) {
                func(response);
            }
        );
    }
});