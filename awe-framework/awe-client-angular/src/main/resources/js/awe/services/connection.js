import { aweApplication } from "./../awe";

// Connection service
aweApplication.factory('Connection',
  ['Comet', 'Ajax',
    /**
     *
     * @param {object} $settings Application $settings
     * @param {object} comet Comet based connection
     * @param {object} ajax Ajax based connection
     */
    function (comet, ajax) {
      // Service variables;

      return {
        /**
         * Init Connection
         */
        init: function (serverPath, encodeTransmission, token) {
          // Initialize connection and backup
          ajax.init(serverPath, encodeTransmission);

          // Return comet initialization
          return comet.init(encodeTransmission, token);
        },
        /**
         * Restart Connection
         */
        restart: function (token) {
          // Restart connection with another token
          return comet.restart(token);
        },
        /**
         * Disconnect comet connection
         */
        disconnect: function () {
          // Disconnect
          return comet.disconnect();
        },
        /**
         * Send message via websocket (does not wait response)
         * @param {String} message Message to send
         */
        sendMessage: function (message) {
          ajax.sendMessage(message);
        },
        /**
         * Send message with a promise
         * @param {String} message Message to send
         */
        send: function (message) {
          return ajax.send(message);
        },
        /**
         * Send get message with a promise
         * @param {String} url Message to send
         * @param {String} expectedContent Content type expected
         */
        get: function (url, expectedContent) {
          return ajax.get(url, expectedContent);
        },
        /**
         * Send post message with a promise
         * @param {String} url Message url
         * @param {Object} data Message data
         * @param {String} expectedContent Content type expected
         */
        post: function (url, data, expectedContent) {
          return ajax.post(url, data, expectedContent);
        },
        /**
         * Subscribe to a channel
         * @param token
         */
        subscribe: function(token) {
          comet.subscribe(token);
        },
        /**
         * Retrieve message URL
         * @return {String} Raw URL
         */
        getRawUrl: function () {
          return ajax.getRawUrl();
        },
        /**
         * Serialize the post parameters
         * @param {Object} parameters Send message parameters
         * @return {Object} Serialized parameters
         */
        serializeParameters: function (parameters) {
          return ajax.serializeParameters(parameters);
        }
      };
    }
  ]);
