import { aweApplication } from "./../awe";

// Session storage service
aweApplication.factory('SessionStorage',
  ['$cookies',
    /**
     * Session storage
     * @param {Service} $cookies
     */
    function ($cookies) {
      var SessionStorage = {
        /**
         * Storage has key
         * @param {String} key
         */
        has: function (key) {
          // return key in $cookies - Before angular 1.4
          return !!$cookies.get(key);
        },
        /**
         * Retrieve a JSON value
         * @param {String} key
         */
        get: function (key) {
          // return $cookies[key] - Before angular 1.4
          return $cookies.get(key);
        },
        /**
         * Store a JSON value
         * @param {String} key
         * @param {Object} value
         */
        put: function (key, value) {
          // $cookies[key] = value - Before angular 1.4
          $cookies.put(key, value);
        },
        /**
         * Remove a key
         * @param {String} key
         */
        remove: function (key) {
          // delete $cookies[key] - Before angular 1.4
          $cookies.remove(key);
        }
      };
      return SessionStorage;
    }
  ]);