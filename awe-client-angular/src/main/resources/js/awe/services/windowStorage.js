import { aweApplication } from "./../awe";

// Window storage service
aweApplication.factory('WindowStorage',
  ['$rootScope',
    /**
     * Window storage
     * @param {Scope} $rootScope
     */
    function ($rootScope) {
      var store = $rootScope;
      var WindowStorage = {
        /**
         * Storage has key
         * @param {String} key
         */
        has: function (key) {
          return key in store;
        },
        /**
         * Retrieve a JSON value
         * @param {String} key
         */
        get: function (key) {
          return store[key];
        },
        /**
         * Store a JSON value
         * @param {String} key
         * @param {Object} value
         */
        put: function (key, value) {
          store[key] = value;
        },
        /**
         * Remove a key from the store
         * @param {String} key
         */
        remove: function (key) {
          delete store[key];
        }
      };
      return WindowStorage;
    }
  ]);