adminApp.
    controller("wsjReceiverController", function ($scope, wsjReceiverService) {
        $scope.data = {
            showLoadingBar : false,
            movedArticles : 0,
            moveArticles : function() {
                $scope.data.showLoadingBar = true;
                wsjReceiverService.moveArticles(function (data){
                    $scope.data.movedArticles = data;
                    $scope.data.showLoadingBar = false;
                });
            },
            processedArticles : 0,
            processAllArticle :function() {
                $scope.data.showLoadingBar = true;
                wsjReceiverService.processAllArticle(function (data){
                    $scope.data.processedArticles = data;
                    $scope.data.showLoadingBar = false;
                });
            },
            articleId : 0,
            article : [],
            getArticle : function() {
                $scope.data.showLoadingBar = true;
                wsjReceiverService.getArticle($scope.data.articleId, function (data){
                    $scope.data.article = data;
                    $scope.data.showLoadingBar = false;
                });
            }
        }
    })
;