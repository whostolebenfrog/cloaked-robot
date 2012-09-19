'use strict';

// Declare app level module which depends on filters, and services
angular.module('robot', ['robot.filters', 'robot.services', 'robot.directives']).
  config(['$routeProvider', function($routeProvider) {
    $routeProvider.when('/robot', {template: 'partials/robot.html', controller: RobotCtrl});
    $routeProvider.when('/robot/:godmode', {template: 'partials/robot.html', controller: RobotCtrl});
    $routeProvider.otherwise({redirectTo: '/robot'});
  }]);
