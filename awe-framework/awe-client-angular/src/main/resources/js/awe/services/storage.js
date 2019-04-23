import { aweApplication } from "./../awe";

// Storage service
aweApplication.factory('Storage',
  ['WindowStorage', 'TabStorage', 'SessionStorage',
    /**
     * General control methods
     * @param {Service} windowStorage
     * @param {Service} tabStorage
     * @param {Service} sessionStorage
     */
    function (windowStorage, tabStorage, sessionStorage) {
      var localStorage;
      var sharedStorage = tabStorage;
      var Storage = {
        /**
         * Initializes storage
         */
        init: function () {
          localStorage = {};
          Storage.setSharedSession(Storage.getSharedSession());
        },
        /**
         * Sets shared session
         * @param {boolean} shareSession Share session between tabs
         */
        setSharedSession: function (shareSession) {
          sharedStorage = shareSession ? sessionStorage : tabStorage;
          tabStorage.put("ShareSession", sharedStorage);
        },
        /**
         * Sets shared session
         * @param {boolean} shareSession Share session between tabs
         */
        getSharedSession: function () {
          return tabStorage.get("ShareSession");
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
          return sessionStorage.has(key) || tabStorage.has(key);
        },
        /**
         * Retrieve a JSON value
         * @param {String} key
         */
        getSession: function (key) {
          return sessionStorage.has(key) ? sessionStorage.get(key) : tabStorage.get(key);
        },
        /**
         * Store a JSON value
         * @param {String} key
         * @param {Object} value
         */
        putSession: function (key, value) {
          sharedStorage.put(key, value);
        },
        /**
         * Remove from session
         * @param {String} key
         */
        removeSession: function (key) {
          tabStorage.remove(key);
          sessionStorage.remove(key);
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