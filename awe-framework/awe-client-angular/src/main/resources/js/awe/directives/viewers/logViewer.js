import { aweApplication } from "./../../awe";

// Log viewer
aweApplication.directive('aweLogViewer',
  ['ServerData', 'AweUtilities', 'AweSettings', 'Component', 'ActionController',
    function (serverData, $utilities, $settings, Component, $actionController) {

      return {
        restrict: 'E',
        replace: true,
        templateUrl: function () {
          return serverData.getAngularTemplateUrl('logViewer');
        },
        scope: {
          'widgetId': '@logViewerId'
        },
        /**
         * Link function
         * @param {Object} $scope Directive scope
         * @param {Object} $element Directive node
         */
        link: function ($scope, $element) {
          // Init as component
          let component = new Component($scope, $scope.widgetId);
          if (!component.asComponent()) {
            // If component initialization is wrong, cancel initialization
            return false;
          }

          // Retrieve widget values
          let stickBottom = false;
          let allContent = [];
          let initial = true;
          let lineHeight = $element.find(".test-line").height();
          let contentLayer = $element.find(".content");
          let positionLayer = $element.find(".visible-text");
          let lastScrollCheck = -1;
          if (component.controller.parameters) {
            stickBottom = "stickBottom" in component.controller.parameters ? component.controller.parameters["stickBottom"] : stickBottom;
          }

          /**
           * Reload data from element
           */
          component.onReset = function () {
            // Reset values
            allContent = [];
            lastScrollCheck = -1;

            // Print text
            printTextAtScrollPosition();
          };

          /**
           * Reload data from element
           */
          component.reload = function (parameters = {silent: this.reloadSilent, async: this.reloadAsync}) {
            // Retrieve silent and async if arguments eq 2
            let isSilent = parameters.silent;
            let isAsync = parameters.async;

            // Add action to actions stack
            let values = {};
            values.type = this.controller[$settings.get("serverActionKey")];
            values[$settings.get("targetActionKey")] = this.controller[$settings.get("targetActionKey")];
            values.r = Math.random();
            values.offset = allContent.length;

            // Generate server action
            let serverAction = serverData.getServerAction(this.address, values, isAsync, isSilent);

            // Send action list
            $actionController.addActionList([serverAction], false, {address: this.address, context: this.context});
          };


          // On action reset
          $scope.$on('/action/reset', function (event, action) {
            $actionController.resolveAction(action, {scope: $scope, service: component, method: "onReset"});
          });

          // On log delta, print log and scroll down
          $scope.$on('/action/log-delta', function (event, action) {
            let parameters = action.attr("parameters");
            let currentContent = parameters ? parameters.log || [] : [];
            lineHeight = $element.find(".test-line").height();
            if (currentContent.length > 0) {
              // Add content to log
              allContent = allContent.concat(currentContent);

              // Check whether to move scroll
              let moveScroll = $element[0].scrollHeight <= $element.scrollTop() + $element.height() + 20;

              // Increase height
              let height = allContent.length * lineHeight;
              contentLayer.height(height);

              // Force repaint
              lastScrollCheck = -1;

              // Stick scroll to bottom
              stickToBottom(moveScroll, height);
            }

            // Accept action
            $actionController.acceptAction(action);
          });

          /**
           * Stick scroll to bottom
           * @param {boolean} moveScroll Move scroll or not
           * @param {integer} top Position to move
           */
          function stickToBottom(moveScroll, top) {
            if (stickBottom && (moveScroll || initial)) {
              $utilities.timeout(() => {
                $element.animate({scrollTop: top}, {complete: () => {
                  initial = false;
                  $element.trigger("scroll");
                }});
              });
            }
          }

          /**
           * Print text at scroll position
           */
          function printTextAtScrollPosition() {
            // If scroll has moved
            let scrollPosition = $element.scrollTop();
            if (lastScrollCheck !== scrollPosition) {

              // Get positions
              lastScrollCheck = scrollPosition;
              let scrollTotal = $element[0].scrollHeight || 1;
              let containerHeight = $element.height();
              let scrollPercent = scrollPosition / scrollTotal;
              let linesToPrint = Math.ceil(containerHeight / lineHeight) + 60;
              let totalLines = allContent.length;
              let initialTextPosition = Math.max(0, Math.floor(totalLines * scrollPercent) - 30);
              let finalTextPosition = Math.min(totalLines, initialTextPosition + linesToPrint);

              // Get text layer position
              let textLayerPosition = initialTextPosition * lineHeight;

              // Move text container to visible part
              positionLayer[0].style.top = textLayerPosition + "px";

              // Fulfill with text
              positionLayer[0].textContent = allContent.slice(initialTextPosition, finalTextPosition).join("\n");
            }
          }

          // On scroll or resize, render scroll data
          let renderScroll = _.debounce(printTextAtScrollPosition, 10);

          // Bind scroll event
          $element.on("scroll", renderScroll);

          // Bind resize event
          $scope.$on("resize", renderScroll);
        }
      };
    }]);
