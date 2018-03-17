var app = angular.module('statelessApp', ['ngCookies']).factory('TokenStorage', function () {
    var storageKey = 'auth_token';
    return {
        store: function (token) {
            return localStorage.setItem(storageKey, token);
        },
        retrieve: function () {
            return localStorage.getItem(storageKey);
        },
        clear: function () {
            return localStorage.removeItem(storageKey);
        }
    };
}).factory('TokenAuthInterceptor', function ($q, $rootScope, TokenStorage) {
    return {
        request: function (config) {
            var authToken = TokenStorage.retrieve();
            if (authToken) {
                config.headers['X-AUTH-TOKEN'] = authToken;
            }
            return config;
        },
        responseError: function (error) {
            if (error.status === 401 || error.status === 403) {
                TokenStorage.clear();
                $rootScope.authenticated = false;
            }
            return $q.reject(error);
        }
    };
}).config(function ($httpProvider) {
    $httpProvider.interceptors.push('TokenAuthInterceptor');
});

app.controller('AuthCtrl', function ($scope, $rootScope, $http, $cookies, TokenStorage) {
    $rootScope.authenticated = false;

    $scope.init = function () {
        var authCookie = $cookies['AUTH-TOKEN'];
        if (authCookie) {
            TokenStorage.store(authCookie);
            delete $cookies['AUTH-TOKEN'];
        }
        $http.get('api/user/current').success(function (user) {
            if (user.username) {
                $rootScope.authenticated = true;
                $scope.username = user.username;
            }
        });
    };

    $scope.logout = function () {
        // Just clear the local storage
        TokenStorage.clear();
        $rootScope.authenticated = false;
        $scope.socialDetails = '';
    };

    $scope.getUserCurrent = function () {
        $http.get('api/user/current').success(function (userCurrent) {
            $scope.userCurrent = userCurrent;
        });
    };

    $scope.getSocialDetails = function () {
        $http.get('api/facebook/details').success(function (socialDetails) {
            $scope.socialDetails = socialDetails;
        });
    };

    $scope.getSomeResources = function () {
        $http.get('api/someResource?page=1&pageSize=10').success(function (someResources) {
            $scope.someResources = someResources;
        });
    };

    $scope.getSomeResource = function () {
        $http.get('api/someResource/1').success(function (someResource) {
            $scope.someResource = someResource;
        });
    };

    $scope.getContinents = function () {
        $http.get('api/continent').success(function (continents) {
            $scope.continents = continents;
        });
    };

    $scope.getRegions = function () {
        $http.get('api/continent/c1/region').success(function (regions) {
            $scope.regions = regions;
        });
    };

    $scope.getPlaces = function () {
        $http.get('api/place?latitude=10&longitude=-10&radius=5').success(function (places) {
            $scope.places = places;
        });
    };

    $scope.generateQRCodeForPlace = function () {
        $http.post('api/place/1/qrcode', {"generate": "true"}).success(function (qrcode) {
            $scope.qrcode = qrcode;
        });
    };

    $scope.visitPlace = function () {
        $http.post('api/place/1/visit', {"latitude": 10, "longitude": -10}).success(function (visitPlaceAnswer) {
            $scope.visitPlaceAnswer = visitPlaceAnswer;
        });
    };

    $scope.removeUser = function () {
        $http.post('api/user/2/remove', {"remmove": "true"}).success(function (removeUserAnswer) {
            $scope.removeUserAnswer = removeUserAnswer;
        });
    };

});