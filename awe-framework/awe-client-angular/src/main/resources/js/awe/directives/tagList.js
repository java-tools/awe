import { aweApplication } from "./../awe";
import _ from "lodash";
import {ClientActions} from "../data/actions";

// Tag list directive
aweApplication.directive('aweTagList',
  ['ServerData', 'Component', 'Storage', 'Connection', '$compile', 'AweSettings', 'ActionController',
    function (serverData, Component, Storage, Connection, $compile, $settings, $actionController) {
      return {
        restrict: 'A',
        scope: {
          'tagListId': '@aweTagList'
        },
        link: function (scope, elem) {
          // Init as component
          let component = new Component(scope, scope.tagListId);
          if (!component.asComponent()) {
            // If component initialization is wrong, cancel initialization
            return false;
          }

          // Retrieve model, controller, API and screen
          let controller = Storage.get("controller");
          let api = Storage.get("api");
          let screen = Storage.get("screen");
          let model = Storage.get("model");
          let view = component.address.view;

          /**
           * Reload data from element
           */
          component.reload = function (parameters = {silent: this.reloadSilent, async: this.reloadAsync}) {
            // Retrieve silent and async if arguments eq 2
            let isSilent = parameters.silent;
            let isAsync = parameters.async;

            // Add action to actions stack
            let values = {};
            values.type = "data";
            values.taglist = component.id;
            values.option = Storage.get("screen")[component.address.view].option || null;
            values[$settings.get("targetActionKey")] = "getTaglistData";
            values.r = Math.random();

            // Generate server action
            let serverAction = serverData.getServerAction(component.address, values, isAsync, isSilent);

            // Send action list
            $actionController.addActionList([serverAction], false, {address: component.address, context: component.context});
          };

          /**
           * On data retrieved
           */
          component.onData = function (parameters) {
            // Store taglist json data
            _.each(parameters.components, function (currentComponent) {
              serverData.storeComponent(currentComponent, model[view], controller[view], api[view], screen[view].option)
            });

            // assign it into the current DOM
            elem.html(parameters.html);

            // compile the new DOM and link it to the current scope.
            $compile(elem.contents())(scope);
          };

          // Action listener definition
          $actionController.defineActionListeners(component.listeners, ClientActions.taglist, component.scope, component);
        }
      };
    }
  ]);
