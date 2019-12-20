import { aweApplication } from "./../awe";
import SockJS from "sockjs-client";
import Stomp from "@stomp/stompjs";

// Comet service
aweApplication.factory('Comet',
  ['AweUtilities', 'ActionController',
    /**
     * Retrieve the comet connection object
     * @param {Service} $utilities AweUtilities service
     * @param {Service} actionController Action controller
     * @returns {Object} Comet connection
     */
    function ($utilities, actionController) {

      // Service variables
      let connection = null;
      let connected = false;
      let encodeTransmission = false;
      let connectionToken = null;
      let events = {};

      const $comet = {
        /**
         * Set connection
         * @param {object} _connection
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
         * @param encode
         */
        init: function (encode, token) {
          encodeTransmission = encode;
          connectionToken = token;
          return $comet._connect();
        },
        /**
         * Restart WebSocket Connection
         * @param token
         */
        restart: function (token) {
          connectionToken = token;
          $comet._reconnect();
        },
        /**
         * Disconnect comet connection
         */
        disconnect: function () {
          if ($comet._isConnected()) {
            return $comet._disconnect();
          }
        },
        /**
         * Retrieve if connection is still alive
         * @returns {boolean}
         * @private
         */
        _isConnected() {
          return $comet.getConnection() !== null && connected && $comet.getConnection().connected;
        },
        /**
         * Connection Management
         * @private
         */
        _connect: function () {
          // Set defer
          events["connect"] = $utilities.q.defer();

          // Set connection
          $comet.setConnection(Stomp.over(new SockJS($utilities.getContextPath() + '/websocket')));
          //connection.reconnect_delay = 500;

          // Connect
          $comet.getConnection().connect({'Authorization': connectionToken}, () => {
            connected = true;
            // Subscribe to broadcast
            $comet.subscribe("broadcast");

            // Subscribe to token
            $comet.subscribe(connectionToken);

            // Resolve initialization
            events["connect"].resolve();
          }, () => {
            $comet._disconnect();

            // Reject initialization
            events["connect"].reject();
          }, () => {
            connected = false;
          });

          return events["connect"].promise;
        },
        _disconnect: function () {
          // Disable connection
          connected = false;

          events["disconnect"] = $utilities.q.defer();
          // Set connection
          try {
            $comet.getConnection().disconnect(() => {
              $comet.setConnection(null);

              // Resolve disconnection
              events["disconnect"].resolve();
            }, () => {
              events["disconnect"].reject();
            });
          } catch (e) {
            $utilities.timeout(() => events["disconnect"].resolve());
          }

          return events["disconnect"].promise;
        },
        /**
         * Reconnect connection
         */
        _reconnect: function () {
          // Set connection
          if ($comet._isConnected()) {
            return $comet._disconnect().then($comet._connect);
          } else {
            return $comet._connect();
          }
        },
        /**
         * Subscribe
         * @param token
         */
        subscribe: function (token) {
          // Subscribe to token
          $comet.getConnection().subscribe(`/topic/${token}`, $comet.manageBroadcast);
        },
        /**
         * Receive broadcast message
         * @param {Object} message Message received
         */
        manageBroadcast: function (message) {
          // Decode data
          let data = $utilities.decodeData(message.body, encodeTransmission);

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
