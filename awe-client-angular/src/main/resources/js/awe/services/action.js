import { aweApplication } from "./../awe";

// Action object
aweApplication.factory('Action',
  ['AweUtilities', 'AweSettings', '$log',
    /**
     * Retrieve the action object
     * @param {Service} Utilities Awe Utilities
     * @param {Service} $settings Awe default $settings
     * @param {Service} $log Log service
     * @returns {Function} Action object
     */
    function (Utilities, $settings, $log) {
      function Action(parameters) {
        // Service variables;
        this.attributes = {
          deferred: Utilities.q.defer(),
          running: false,
          alive: true,
          timer: null
        };
        // Action methods
        this.onReject = null;
        this.onCancel = null;

        // Add parameters to default attributes
        _.merge(this.attributes, parameters);
      }

      Action.prototype = {
        /**
         * Retrieve if connection is active
         * @param {type} attribute
         * @param {type} value
         */
        attr: function (attribute, value) {
          if (value !== undefined) {
            this.attributes[attribute] = value;
          } else {
            return this.attributes[attribute];
          }
        },
        /**
         * Launches the action
         * @param {string} channel
         */
        launchAction: function (channel) {
          // Debug call
          //$log.debug("[Action] Call action: " + channel, this.attributes)

          // Store run time
          this.attr("startTime", new Date());

          // Launch call
          Utilities.publish(channel, this);
        },
        /**
         * Retrieve if connection is active
         */
        run: function () {
          var _self = this;
          this.attr("running", true);
          var channel = "/action/" + this.attr("type");

          // Launch action (with timeout as is deferred)
          Utilities.timeout.cancel(this.attr("timer"));
          this.attr("timer", Utilities.timeout(function () {
            _self.launchAction(channel);
          }, $settings.get("actionsStack")));

          // Return promise
          return this.attr("deferred").promise;
        },
        /**
         * Show action information
         */
        showInfo: function () {
          var _object = this.attr("target");
          _object = Utilities.isNull(_object) ? "" : " (" + _object + ")";

          // Show info
          $log.debug("[Action] " + this.attr("type") + _object, this.attributes);
        },
        /**
         * Get if action is alive
         * @returns {boolean} action is alive
         */
        isAlive: function () {
          return this.attr("alive");
        },
        /**
         * Accept action
         */
        accept: function () {
          // Log end time
          var dateDiff = (new Date() - this.attr("startTime")) / 1000;
          $log.debug("[Action] Action finished (" + this.attr("type") + ") in " + dateDiff + " seconds", this.attributes);
          this.attr("deferred").resolve();
        },
        /**
         * Reject action
         */
        reject: function () {
          if (typeof this.onReject === "function") {
            this.onReject();
          }
          this.destroy();
          this.attr("deferred").reject();
        },
        /**
         * Abort action
         */
        abort: function () {
          this.attr("alive", false);
          this.attr("deferred").resolve();
        },
        /**
         * Cancel action
         */
        cancel: function () {
          if (typeof this.onCancel === "function") {
            this.onCancel();
          }
          this.destroy();
        },
        /**
         * Remove action
         */
        destroy: function () {
          Utilities.timeout.cancel(this.attr("timer"));
          this.attr("alive", false);
        }
      };
      return Action;
    }
  ]);