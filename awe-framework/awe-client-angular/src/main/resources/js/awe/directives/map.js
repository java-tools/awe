import { aweApplication } from "./../awe";

// Map directive
aweApplication.directive('aweMap',
  ['ServerData', 'Component', 'AweUtilities', 'ActionController',
    function (serverData, Component, Utilities, $actionController) {
      return {
        restrict: 'E',
        replace: true,
        templateUrl: function () {
          return serverData.getAngularTemplateUrl('map');
        },
        scope: {
          'mapId': '@'
        },
        link: function ($scope, elem, attr) {
          // Init as component
          var component = new Component($scope, $scope.mapId);
          if (!component.asComponent()) {
            // If component initialization is wrong, cancel initialization
            return false;
          }

          $scope.showRoute = false;

          // Initial map $settings
          _.merge($scope, {
            map: {
              control: {},
              center: {
                latitude: 43.8667,
                longitude: 1.1111
              },
              options: {
                streetViewControl: false,
                panControl: false,
                maxZoom: 20,
                minZoom: 3
              },
              zoom: 4,
              dragging: false,
              bounds: {},
              markers: []
            }
          });

          // Print positions in the map
          var drawPoints = function (scope) {
            var positions = [];
            for (var val in scope.model.values) {
              var loc = null;
              loc = {
                id: val,
                idMov: scope.model.values[val].IdMov,
                latitude: scope.model.values[val].Lat,
                longitude: scope.model.values[val].Lon,
                lugar: scope.model.values[val].Lug,
                velocidad: scope.model.values[val].Vel,
                km: scope.model.values[val].Kil,
                dir: scope.model.values[val].Dir,
                pais: scope.model.values[val].Pai
              };
              positions.push(loc);
            }
            scope.map.markers = [];
            // BAD PRACTICE, CHANGE IN THE FUTURE
            Utilities.timeout(function () {
              scope.map.markers = positions;
            }, 500);
          };

          drawPoints($scope);

          // Draw truck routes
          var drawPolyline = function (routePoints,
            startLat, startLon) {
            var points = [];
            $scope.showRoute = true;
            for (var num in routePoints) {
              if (routePoints[num].Lat !== null && routePoints[num].Lon !== null) {
                var loc = {
                  latitude: routePoints[num].Lat,
                  longitude: routePoints[num].Lon
                };
                points.push(loc);
              }
            }
            _.merge($scope.map, {
              center: {
                latitude: startLat,
                longitude: startLon
              },
              zoom: 8
            });
            $scope.polylines = [{
                id: 1,
                path: points,
                stroke: {
                  color: '#6060FB',
                  weight: 4
                },
                editable: true,
                draggable: true,
                geodesic: true,
                visible: true
              }];
          };

          /**
           * Update model with action values
           * @param {Action} action Action received
           */
          var polyline = function (action) {
            // Retrieve parameters
            var parameters = _.cloneDeep(action.attr("parameters"));
            var routePoints = parameters.rows;
            if (routePoints.length !== 0) {
              var startLat = routePoints[0].Lat;
              var startLon = routePoints[0].Lon;
              if (routePoints.length === 0
                || startLat === undefined) {
                console.error("NO POINTS FOUND");
              } else {
                drawPolyline(routePoints,
                  startLat, startLon);
              }
            }
            // Finish action
            $actionController.acceptAction(action);
          };

          // Capture polyline action
          $scope.$on('/action/polyline', function (event, action) {
            return polyline(action);
          });

          // Marker on click event
          $scope.onMarkerClicked = function (marker) {
            marker.showWindow = true;
            $scope.$apply();
          };
        }
      };
    }
  ]);
