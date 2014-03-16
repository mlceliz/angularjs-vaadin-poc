'use strict';

/* Controllers */

angular.module('suaApp.controllers', [])
.controller('LoginCtrl', [ '$rootScope', '$scope', '$location', '$window', 'Auth', function($rootScope, $scope, $location, $window, Auth) {
	$scope.rememberme = true;
	$scope.login = function() {
		Auth.login({
			username : $scope.username,
			password : $scope.password,
			rememberme : $scope.rememberme
		}, function(res) {
			$location.path('/');
		}, function(err) {
			$rootScope.error = "Failed to login";
		});
	};

	$scope.loginOauth = function(provider) {
		$window.location.href = '/auth/' + provider;
	};
}])
.controller('NavCtrl', ['$scope', '$location', 'Auth', function($scope, $location, Auth) {
    $scope.user = Auth.user;
    $scope.userRoles = Auth.userRoles;
    $scope.accessLevels = Auth.accessLevels;

    $scope.logout = function() {
        Auth.logout(function() {
            $location.path('.#/login');
        }, function() {
            $rootScope.error = "Failed to logout";
        });
    };
}])
.controller('HomeCtrl', [ '$rootScope', 
function($rootScope) {

} ])
.controller('ServicioCtrl', function($scope, $http, Restangular) {
	$scope.showGrid = true;
	$scope.showForm = false;
	$scope.servicio = [];
	$scope.alerts = []
	$scope.updateServices = function(s) {
		Restangular.one('admin').getList("servicios", {})
		.then(function(data) {
			$scope.servicios = data;
		}, function(data, status, headers, config) {
			$scope.alerts.push({
				msg : "Status: " + status + ", error: " + data
			});
		});		
	};

	$scope.updateServices();

	$scope.serviciosGrid = {
		data : 'servicios',
		selectedItems : $scope.servicio,
		multiSelect : false,
		columnDefs : [ {
			field : 'servicio',
			displayName : 'Servicio'
		}, {
			field : 'descripcion',
			displayName : 'Descripcion',
			resizable : true
		}, {
			field : 'url',
			displayName : 'URL',
			resizable : true
		}, ],
		afterSelectionChange : function(rowItem, event) {
			$scope.showGrid = false;
			$scope.showForm = true;
		}
	};

	$scope.newService = function() {
		$scope.servicio = [ {
			id : "",
			servicio : "",
			descripcion : "",
			url : "",
			certificado : ""
		} ];
		$scope.showGrid = false;
		$scope.showForm = true;
	}

	$scope.saveService = function(s) {
		var transform = function(data) {
			return $.param(data);
		}
		$http.post('admin/servicio/', {
			servicio : s
		}, {
			headers : {
				'Content-Type' : 'application/x-www-form-urlencoded; charset=UTF-8'
			},
			transformRequest : transform
		}).success(function(data) {
			$scope.resultado = data;
			$scope.updateServices();
			$scope.showGrid = true;
			$scope.showForm = false;
		}).error(function(data, status, headers, config) {
			$scope.alerts.push({
				msg : "Status: " + status + ", error: " + data
			});
		});
	}
	// $scope.myData = [{name:
	// "Moroniqjehrqjkwqhrejlwrhjqqqqjerhhrjhjhjhjhjhjhjhjhjhjhjhjhjhjhjhjhjhjehrerererererejhjhjherere",
	// age: 50},
	// {name: "Tiancum", age: 43},
	// {name: "Jacob", age: 27},
	// {name: "Nephi", age: 29},
	// {name: "Enos", age: 34}];
	// $scope.gridOptions = { data: 'myData', enableColumnResize: true,
	// columnDefs: [
	// {field: 'name', displayName: 'Name', resizable: true},
	// {field:'age', displayName:'Age'},
	// {displayName :'Algo', cellTemplate: '<div class="ngCellText"><a
	// ng-click="exportCert(row.getProperty(\'age\'))">Certificado</a></div>'}
	// ] };

	$scope.exportCert = function(s) {
		console.log($location.url("/api/certificate/" + s));
		var dataUrl = 'data:text/json;utf-8,' + encodeURI("/api/certificate/" + s);
		var link = document.createElement('a');
		angular.element(link)
		/*
		 * .attr('href', dataUrl) .attr('href', "/sua/api/certificate/"+s)
		 */
		.attr('href', $location.url("/api/certificate/" + s)).attr('download', "certicate-" + s) // Pretty
																									// much
																									// only
		// works in
		// chrome
		link.click();
	};
})
.controller('ConfianzaCtrl', ['$scope', '$http',
function($scope, $http) {
	$http.get('admin/confianzas').success(function(data) {
		$scope.confianzas = data;
	}).error(function(data, status, headers, config) {
		$scope.alerts.push({
			msg : "Status: " + status + ", error: " + data
		});
	});

	$scope.confianzasGrid = {
		data : 'confianzas'
	};

} ]);
