'use strict';

var app = angular.module('vaadinApp', [ 'ngRoute', 'ui.bootstrap', 'ngProgress', 'restangular' ]);

app.config(function($routeProvider) {
	$routeProvider.when('/', {
		templateUrl : 'views/home.html',
		controller : 'homeCtrl',
	});
	$routeProvider.when('/vaadin', {
		templateUrl : 'views/vaadin.html',
		controller : 'vaadinCtrl',
	});
	$routeProvider.when('/404', {
		templateUrl : '/404.html',
	});
	$routeProvider.otherwise({
		redirectTo : '/404'
	});
}).run(
		function(Restangular, ngProgress) {
			Restangular.setBaseUrl('/api');

			// Use Request interceptor
			Restangular.setRequestInterceptor(function(element, operation, route, url) {
				ngProgress.reset();
				ngProgress.start();
				return element;
			});

			// Use Response interceptor
			Restangular.setResponseInterceptor(function(data, operation, what, url, response, deferred) {
				ngProgress.complete();
				return data;
			});
		});

app.controller('vaadinCtrl', function($scope) {
	var formulario = Math.floor((Math.random()*1000)+1); 
	vaadin.vaadinConfigurations.vapp.windowName = "a&formulario="+ formulario +"&version=100&operacion=CREATE"
});

app.controller('homeCtrl', function($scope) {
	
});
