import { aweApplication } from "./../awe";

// Tab storage service
aweApplication.factory('TabStorage',
  [
    /**
     * Tab storage
     */
    function () {
      var store = window.sessionStorage;
      var TabStorage = {
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
          store.removeItem(key);
        }
      };
      return TabStorage;
    }
  ]);