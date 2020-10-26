import {aweApplication} from "./../awe";
import {DefaultSettings} from "../data/options";

// $settings service
aweApplication.factory('AweSettings', ['Storage', '$translate', '$log', 'AweUtilities', '$http', '$location', '$rootScope', '$state', '$injector',
  function ($storage, $translate, $log, $utilities, $http, $location, $scope, $state, $injector) {
    let tokenKey = "token";
    let initialize = $utilities.q.defer();
    let $connection = null;
    const $settings = {
      /**
       * Initialize $settings from server
       */
      init: function () {
        // Define default application $settings
        let settings = DefaultSettings;
        $storage.init();
        $settings.update(settings);
        let url = settings.pathServer + "settings";
        $http.defaults.headers.common = {...$http.defaults.headers.common, ...$settings.getAuthenticationHeaders()};
        $http({
          method: 'POST',
          url: url
        }).then(function (response) {
          let newSettings = angular.fromJson(response.data);
          $settings.update(newSettings);
          $settings.settingsLoaded(newSettings);
        }).catch(function (error) {
          $log.error("FATAL ERROR: Application settings retrieval failure", error);
        });
        return initialize.promise;
      },
      /**
       * Initialize $settings from server
       * @param {object} $settings
       */
      settingsLoaded: function (settings) {
        // Retrieve $settings and set initial language
        let language = $settings.getLanguage();

        // Start (or reconnect) server connection
        $connection = $injector.get('Connection');

        $connection.init(settings.pathServer, settings.encodeTransmission, settings.cometUID)
          .then(() => {
            // Store token
            $settings.setToken(settings.cometUID);

            // Store updated and language
            $storage.putRoot("language", language);

            // Set language
            $settings.changeLanguage(language, true);

            // Store connection
            $storage.putRoot("connection", settings.connectionProtocol);

            // Load current state
            let initialState = $utilities.getState(settings.reloadCurrentScreen ? settings.initialURL : location.href);
            $settings.update({reloadCurrentScreen: false});

            // Go to initial state
            $state.go(initialState.to, initialState.parameters, {reload: false, inherit: true, notify: true, location: true});

            // Resolve initialization
            initialize.resolve();
          });
      },
      /**
       * Retrieve $settings
       * @returns {object} $settings
       */
      get: function (parameter) {
        return $storage.getRoot("settings")[parameter];
      },
      /**
       * Retrieve $settings
       * @returns {object} $settings
       */
      update: function (settings = {}) {
        $storage.putRoot("settings", {
          ...$storage.getRoot("settings") || {},
          ...settings
        });
      },
      /**
       * Store session token
       * @param {String} token Token to set
       * @param {boolean} restartConnection Restart comet connection
       */
      setToken: function (token, restartConnection = false) {
        $storage.putSession(tokenKey, token);
        $http.defaults.headers.common = {...$http.defaults.headers.common, ...$settings.getAuthenticationHeaders()};
        if (restartConnection) {
          $connection.restart(token);
        }
      },
      /**
       * Store session token
       * @return token
       */
      getToken: function () {
        if ($storage.hasSession(tokenKey)) {
          return $storage.getSession(tokenKey);
        }
        return $settings.get("cometUID");
      },
      /**
       * Clear session token
       * @return token
       */
      clearToken: function () {
        $storage.removeSession(tokenKey);
      },
      /**
       * Retrieve authentication headers
       * @returns {{Authorization: (*|token)}}
       */
      getAuthenticationHeaders: function() {
        return {
          'Authorization': $settings.getToken(),
          'Content-Type': 'application/json'
        };
      },
      /**
       * Retrieve session token
       * @returns {Object} Session token
       */
      getLanguage: function () {
        let language = $settings.get("language");
        return language === null ? null : language.toLowerCase();
      },
      /**
       * Change current language;
       * @param {String} language Language to set
       * @param {boolean} forceChange Force change language
       */
      changeLanguage: function (language, forceChange) {
        if (language !== null) {
          var newLanguage = language.toLowerCase();
          if (newLanguage != $settings.getLanguage() || forceChange) {
            // Change language $settings
            $settings.update({language: newLanguage});

            // Change locals
            $translate.use(newLanguage).then(function () {
              // Launch local functions
              $utilities.publish("launchLocalFunctions");

              // Report language changed
              $log.info("Language changed to " + newLanguage);

              // Apply changes
              $utilities.publish("languageChanged", newLanguage);
            });
          }
        }
      },
      /**
       * Preload angular templates
       */
      preloadTemplates: function () {
        // Preload templates
        var $serverData = $injector.get('ServerData');
        var templateList = [
          {path: 'grid/header'},
          {path: 'grid/cell'},
          {path: 'grid/footer'},
          {path: 'grid/row'},
          {path: 'grid/pagination'},
          {path: 'grid/viewport', name: 'ui-grid/uiGridViewport'},
          {path: 'grid/renderContainer', name: 'ui-grid/uiGridRenderContainer'},
          {path: 'grid/headerFilter'},
          {path: 'grid/cellRowNumber'},
          {path: 'grid/cellCheckbox'},
          {path: 'grid/cellTreeIcons', name: "ui-grid/treeBaseRowHeaderButtons"},
          {path: 'grid/headerTreeIcons', name: "ui-grid/treeBaseExpandAllButtons"},
          {path: 'grid/selectionHeaderCell', name: "ui-grid/selectionHeaderCell"},
          {path: 'grid/headerCell', name: "ui-grid/ui-grid-header"},
          {path: 'grid/headerCheckbox'}];
        _.each(templateList, function (template) {
          $serverData.preloadAngularTemplate(template);
        });
      }
    };
    return $settings;
  }]);
