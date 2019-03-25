import { aweApplication } from "./../awe";
import SockJS from "sockjs-client";
import Stomp from "@stomp/stompjs";

// Comet service
aweApplication.factory('Comet',
  ['AweSettings', 'AweUtilities', 'ActionController',
    /**
     * Retrieve the comet connection object
     * @param {Service} $settings AweSettings service
     * @param {Service} utils AweUtilities service
     * @param {Service} actionController Action controller
     * @returns {Object} Comet connection
     */
    function ($settings, utils, actionController) {

      // Service variables
      let connection = null;
      let connected = false;

      const $comet = {
        /**
         * Set connection
         * @param {object} connection
         * @private
         */
        setConnection: function(_connection) {
          connection = _connection;
        },
        /**
         * Retrieve connection
         * @private
         */
        getConnection: function() {
          return connection;
        },
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
          if ($comet.getConnection() === null) {
            $comet._connect();
          } else {
            $comet._reconnect();
          }
        },
        /**
         * Connection Management
         * @private
         */
        _connect: function () {
          // Set connection
          let connectionUrl = utils.getContextPath() + '/websocket';
          $comet.setConnection(Stomp.over(new SockJS(connectionUrl)));

          // Connect
          $comet.getConnection().connect(null, null, function (frame) {
            connected = true;
            // Subscribe to broadcast
            $comet.getConnection().subscribe("/topic/broadcast", $comet.manageBroadcast);
            // Subscribe to cometUID
            $comet.getConnection().subscribe("/topic/" + $settings.getToken(), $comet.manageBroadcast);
          }, () => $comet._disconnect());
        },
        _disconnect: function () {
          // Set connection
          $comet.getConnection().disconnect((frame) => {
            connected = false;
            $comet.setConnection(null);
          });
        },
        /**
         * Reconnect connection
         */
        _reconnect: function () {
          // Set connection
          if ($comet.getConnection() !== null) {
            $comet.getConnection().disconnect((frame) => {
              $comet._connect();
            });
          } else {
            $comet.init();
          }

        },
        /**
         * Receive broadcast message
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
          $comet._disconnect();
        }
      };
      return $comet;
    }
  ]);
