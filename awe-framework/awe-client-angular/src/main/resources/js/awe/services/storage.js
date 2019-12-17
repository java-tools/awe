import { aweApplication } from "./../awe";

// Storage service
aweApplication.factory('Storage',
  ['WindowStorage', 'TabStorage',
    /**
     * General control methods
     * @param {Service} windowStorage
     * @param {Service} tabStorage
     */
    function (windowStorage, tabStorage) {
      let localStorage;
      let Storage = {
        /**
         * Initializes storage
         */
        init: function () {
          localStorage = {};
        },
        /**
         * Storage has key
         * @param {String} key
         */
        has: function (key) {
          return key in localStorage;
        },
        /**
         * Retrieve a JSON value
         * @param {String} key
         */
        get: function (key) {
          return localStorage[key];
        },
        /**
         * Store a JSON value
         * @param {String} key
         * @param {Object} value
         */
        put: function (key, value) {
          localStorage[key] = value;
        },
        /**
         * Remove a JSON value
         * @param {String} key
         */
        remove: function (key) {
          delete localStorage[key];
        },
        /**
         * Storage has key
         * @param {String} key
         */
        hasSession: function (key) {
          return tabStorage.has(key);
        },
        /**
         * Retrieve a JSON value
         * @param {String} key
         */
        getSession: function (key) {
          return tabStorage.get(key);
        },
        /**
         * Store a JSON value
         * @param {String} key
         * @param {Object} value
         */
        putSession: function (key, value) {
          tabStorage.put(key, value);
        },
        /**
         * Remove from session
         * @param {String} key
         */
        removeSession: function (key) {
          tabStorage.remove(key);
        },
        /**
         * Store a JSON value in root
         * @param {String} key
         * @param {Object} value
         */
        putRoot: function (key, value) {
          windowStorage.put(key, value);
        },
        /**
         * Retrieve json value from root
         * @param {String} key
         * @return {Object} value
         */
        getRoot: function (key) {
          return windowStorage.get(key);
        }
      };
      return Storage;
    }
  ]);