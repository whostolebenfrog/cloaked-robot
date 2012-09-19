'use strict';

/* Services */

// Demonstrate how to register services
// In this case it is a simple value service.
angular.module('robot.services', []).
  value('version', '0.1');

angular.module('robot.services', []).factory('store', function($http) {
    return {
        write : function(moves) {
                    $http.put('/users/test/moves', moves).success(function(data) {
                    }).error(function(data) {
                    });
                },
        read : function(scope) {
                   $http.get('/users/test/moves/last').success(function(data) {
                       scope.moves = data[0].moves;
                       scope.f1 = data[0].f1;
                       scope.f2 = data[0].f2;
                   }).error(function(data) {
                   });
               }
    }
});
