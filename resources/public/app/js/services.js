'use strict';

/* Services */

// Demonstrate how to register services
// In this case it is a simple value service.
angular.module('robot.services', []).
  value('version', '0.1');

angular.module('robot.services', []).factory('store', function($http) {
    return {
        write : function(moves) {
                    console.log(moves);
                    $http.put('/users/test/moves', moves).success(function(data) {
                    }).error(function(data) {
                    });
                }
    }
});
