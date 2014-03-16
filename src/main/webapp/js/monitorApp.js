'use strict';

var monitorApp = angular.module('monitorApp', [ 'restangular',  'ngProgress' ]);

monitorApp.config(function(RestangularProvider) {
	RestangularProvider.setBaseUrl('monitor');
});

monitorApp.run(function(Restangular, ngProgress) {
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

monitorApp.controller('MonitorCtrl', function ($scope, Restangular) {
	
	$scope.limit = 0.9;
	$scope.alerts = [];
	Restangular.one('system').get().then(
		function(data) {
			$scope.system = data;
		}, 
		function(data, status, headers, config) {
			$scope.alerts.push({msg : "Status: " + status + ", error: " + data});
		}
	);
/*	
	Restangular.one('memory').get().then(
			function(data) {
				$scope.memory = data;
			}, 
			function(data, status, headers, config) {
				$scope.alerts.push({msg : "Status: " + status + ", error: " + data});
			}
		);
	
	Restangular.one('databases').getList().then(
			function(data) {
				$scope.databases = data;
			}, 
			function(data, status, headers, config) {
				$scope.alerts.push({msg : "Status: " + status + ", error: " + data});
			}
		);
	
	Restangular.one("webapps").getList().then(
			function(data) {
				$scope.webapps = data;
			}, 
			function(data, status, headers, config) {
				$scope.alerts.push({msg : "Status: " + status + ", error: " + data});
			}
		);*/
});