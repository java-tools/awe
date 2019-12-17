import { aweApplication } from "./../awe";

/**
 * Service to launch server calls
 * @param {String} Service name
 * @param {Array} Injection call
 */
aweApplication.factory('Ajax',
  ['AweUtilities', '$log', '$http', 'ActionController', '$httpParamSerializerJQLike', 'LoadingBar',
    /**
     * Retrieve the comet connection object
     * @param {object} $utilities AweUtilities service
     * @param {object} $log Log service
     * @param {object} $http Http request service
     * @param {object} $actionController Action controller
     * @param {object} $httpParamSerializer Parameter serializer
     * @param {object} $loadingBar Loading bar
     * @returns {Object} Ajax connection
     */
    function ($utilities, $log, $http, $actionController, $httpParamSerializer, $loadingBar) {

      // Service variables;
      let connectionType = 'Ajax';
      let connected = true;
      let encodeTransmission = false;
      let serverPath = "";

      const $ajax = {
        /**
         * Retrieve if connection is active
         * @private
         */
        isConnected: function () {
          return connected;
        },
        /**
         * Init Connection
         */
        init: function (path, encode) {
          serverPath = path;
          encodeTransmission = encode;
        },
        /**
         * Send message
         * @param {String} message Message received
         */
        sendMessage: function (message) {

          var action = message.action;
          var target = message.target ? message.target : {};
          action.attr("callbackTarget", target);

          // Send message
          let promise = $ajax.send(message)
            .then(function (response) {
              // Manage message and hide loading bar
              $ajax.manageMessage(response, action);
            })
            .catch(function (response) {
              // Manage message and hide loading bar
              $ajax.manageError(response, action);
            })
            .finally(function () {
              $loadingBar.endTask();
            });


          // Show loading bar
          if (!action.attr("silent")) {
            $loadingBar.startTask();
          }

          return promise;
        },
        /**
         * Send message
         * @param {Object} message Send message parameters
         */
        send: function (message) {
          // Send data
          return $ajax.httpRequest({
            method: 'POST',
            url: $ajax.getActionUrl(message.values.serverAction, message.values.targetAction),
            data: message.values
          });
        },
        /**
         * Send get message
         * @param {Object} url Send message url
         * @param {Object} expectedContent Content type expected
         */
        get: function (url, expectedContent) {
          return $ajax.httpRequest({
            method: 'GET',
            url: url
          }, expectedContent);
        },
        /**
         * Send post message
         * @param {String} url Message url
         * @param {Object} data Post message data
         * @param {Object} expectedContent Content type expected
         */
        post: function (url, data, expectedContent) {
          return $ajax.httpRequest({
            method: 'POST',
            url: url,
            data: data
          }, expectedContent);
        },
        /**
         * Send request
         * @param {String} parameters Request parameters
         * @param {Object} expectedContent Content type expected
         */
        httpRequest: function (parameters, expectedContent) {
          // Set content-type if defined
          if (expectedContent) {
            parameters["headers"] = {
              'Accept': expectedContent
            };
          }

          // Send data
          return $http(parameters);
        },
        /**
         * Retrieve raw url
         * @return {String} context path + server path
         */
        getRawUrl: function () {
          return $utilities.getContextPath() + serverPath;
        },
        /**
         * Retrieve action's url
         * @param {String} actionId Action name
         * @param {String} targetId Action identifier
         * @return {String} Action url
         */
        getActionUrl: function (actionId, targetId) {
          return $ajax.getRawUrl() + "/action/" + actionId + (($utilities.isEmpty(targetId)) ? "" : "/" + targetId);
        },
        /**
         * Serialize the post parameters
         * @param {Object} message Send message parameters
         * @return {Object} Serialized parameters
         */
        serializeParameters: function (parameters) {
          return parameters;
        },
        /**
         * Receive message
         * @param {Object} message Message received
         * @param {Object} action Action launched
         */
        manageMessage: function (message, action) {
          // Decode data
          var data = $utilities.decodeData(message.data, encodeTransmission);

          // Only accept action if is alive
          if (action.isAlive()) {

            // Add actions attributes to retrieved data if not defined previously
            data.silent = data.silent || action.attr("silent");
            data.async = data.async || action.attr("async");

            // Finish call action
            $actionController.acceptAction(action);

            // Add action list
            if (angular.isArray(data)) {
              $actionController.addActionList(data, false, {address: action.attr("callbackTarget"), context: action.attr("context")});
            }
          }
        },
        /**
         * Receive error
         * @param {Object} error Error received
         * @param {Object} action Action launched
         */
        manageError: function (error, action) {
          var target = action.attr("callbackTarget");

          // Finish call action (cancel)
          $actionController.closeAllActions();

          // Generate message title
          let title = "Connection error";

          // Launch message
          let endLoad = {
            type: 'end-load',
            callbackTarget: target,
            parameters: {}
          };
          let actions = [endLoad];
          if (!$utilities.isEmpty(error.data)) {
            let message = {
              type: 'message',
              parameters: {
                type: "error",
                title: title,
                message: "[" + connectionType + "] " + error.data
              }};
            actions.push(message);
          }
          // Log error output
          $log.error("[" + connectionType + "] " + title, error);
          $actionController.addActionList(actions, false, {address: target, context: action.attr("context")});
        }
      };
      return $ajax;
    }
  ]);
