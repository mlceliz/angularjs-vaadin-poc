'use strict';

var app = angular.module('suaApp', [ 'suaApp.controllers', 'suaApp.sercvices', 'suaApp.directives', 'ngRoute', 'ui.bootstrap', 'ngGrid', 'ngProgress', 'restangular' ]);

app.config(function($routeProvider) {
	$routeProvider.when('/', {
		templateUrl : 'views/home.html',
		controller : 'HomeCtrl',
	});
	$routeProvider.when('/login', {
		templateUrl : 'views/login.html',
		controller : 'LoginCtrl',
	});
	$routeProvider.when('/servicios', {
		templateUrl : 'views/servicios.html',
		controller : 'ServicioCtrl',
	});
	$routeProvider.when('/confianzas', {
		templateUrl : 'views/confianzas.html',
		controller : 'ConfianzaCtrl',
	});
	$routeProvider.when('/404', {
		templateUrl : '/404.html',
	});
	$routeProvider.otherwise({
		redirectTo : '/404'
	});
}).run(function(Restangular, ngProgress) {
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