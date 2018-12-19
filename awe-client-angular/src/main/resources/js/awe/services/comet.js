import { aweApplication } from "./../awe";
import SockJS from "sockjs-client";
import Stomp from "@stomp/stompjs";

// Comet service
aweApplication.factory('Comet',
  ['AweSettings', 'AweUtilities', 'ActionController', '$location',
    /**
     * Retrieve the comet connection object
     * @param {Service} $settings AweSettings service
     * @param {Service} utils AweUtilities service
     * @param {Service} actionController Action controller
     * @param {Service} $location Location service
     * @returns {Object} Comet connection
     */
    function ($settings, utils, actionController, $location) {

      // Service variables

      let connection = null;
      let connected = false;

      let Comet = {
        /**
         * Retrieve if connection is active
         * @private
         */
        isConnected: function () {
          return connected;
        },
        /**
         * Init WebSocket Connection
         */
        init: function () {
          // Set current connection
          var websocketPath = utils.getContextPath();
          var connectionUrl = websocketPath + '/websocket';
          var socket = new SockJS(connectionUrl);
          connection = Stomp.over(socket);

          // Request subscription
          Comet._connect();
        },
        /**
         * Connection Management
         * @private
         */
        _connect: function () {
          // Set connection
          connection.connect(null, null, function (frame) {
            connected = true;
            /**
             * Manage broadcast
             */
            var manageBroadcast = function(outputMessage) {
              Comet.manageBroadcast(outputMessage);
            };

            // Subscribe to broadcast
            connection.subscribe("/topic/broadcast", manageBroadcast);
            // Subscribe to cometUID
            connection.subscribe("/topic/" + $settings.getToken(), manageBroadcast);
          }, function (frame) {
            connected = false;
          });
        },
        _disconnect: function () {
          // Set connection
          connection.disconnect(function (frame) {
            connected = false;
          });
        },
        /**
         * Receive broadcasted message
         * @param {Object} message Message received
         */
        manageBroadcast: function (message) {
          // Decode data
          var data = utils.decodeData(message.body, $settings.get("encodeTransmission"));

          if (angular.isArray(data)) {
            // Launch broadcasted actions
            actionController.addActionList(data, true, {address: data.target, context: ""});
          }
        },
        /**
         * Close WebSockect Connection
         * @private
         */
        _close: function () {
          Comet._disconnect();
        }
      };
      return Comet;
    }
  ]);
