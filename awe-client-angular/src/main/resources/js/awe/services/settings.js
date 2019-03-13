import { aweApplication } from "./../awe";
import { DefaultSettings } from "../data/options";

// $settings service
aweApplication.factory('AweSettings', ['Storage', '$translate', '$log', 'AweUtilities', '$http', '$location', '$rootScope', '$state', '$httpParamSerializerJQLike',
  function ($storage, $translate, $log, $utilities, $http, $location, $scope, $state, $httpParamSerializer ) {
    var tokenKey = "token";
    var initialize = $utilities.q.defer();
    var AweSettings = {
      /**
       * Initialize $settings from server
       */
      init: function () {
        // Define default application $settings
        let settings = DefaultSettings;
        $storage.init();
        AweSettings.update(settings);
        var data = this.getTokenObject();
        var url = settings.pathServer + "settings";
        $http({
          method: 'POST',
          url: url,
          data: $httpParamSerializer(data),
          headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
          }
        }).then(function (response) {
          let newSettings = angular.fromJson(response.data);
          AweSettings.update(newSettings);
          AweSettings.settingsLoaded(newSettings);
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
        let language = AweSettings.getLanguage();

        $storage.setSharedSession(settings.shareSessionInTabs);
        AweSettings.setToken(AweSettings.getToken());

        // Store updated and language
        $storage.putRoot("language", language);

        // Start server connection
        $storage.putRoot("connection", angular.element(document).injector().get('Connection').init());

        // Set language
        AweSettings.changeLanguage(language, true);

        // Load current state
        let initialState = $utilities.getState(settings.reloadCurrentScreen ? settings.initialURL : location.href);

        // Go to initial state
        $state.go(initialState.to, initialState.parameters, {reload: false, inherit: true, notify: true, location: true});

        // Activate cache when redirected
        let removeEvent = $scope.$on('$viewContentLoaded', function () {
          angular.element(document).injector().get('ServerData').toggleCache(true);
          removeEvent();
        });

        // Resolve initialization
        initialize.resolve();
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
       */
      setToken: function (token) {
        $storage.putSession(tokenKey, token);
      },
      /**
       * Store session token
       * @return token
       */
      getToken: function () {
        if ($storage.hasSession(tokenKey)) {
          return $storage.getSession(tokenKey);
        }
        return AweSettings.get("cometUID");
      },
      /**
       * Clear session token
       * @return token
       */
      clearToken: function () {
        $storage.removeSession(tokenKey);
      },
      /**
       * Retrieve session token as object
       * @returns {Object} Session token
       */
      getTokenObject: function () {
        var tokenObject = {};
        var token = AweSettings.getToken();
        tokenObject[AweSettings.get("connectionId")] = token;
        return tokenObject;
      },
      /**
       * Retrieve session token as stringº
       * @returns {Object} Session token as string
       */
      getTokenString: function () {
        return AweSettings.get("connectionId") + "=" + AweSettings.getToken();
      },
      /**
       * Retrieve session token
       * @returns {Object} Session token
       */
      getLanguage: function () {
        let language = AweSettings.get("language");
        return language === null ? null : language.toLowerCase();
      },
      /**
       * Change current language;
       * @param {String} language Language to set
       * @param {String} forceChange Force change language
       */
      changeLanguage: function (language, forceChange) {
        if (language !== null) {
          var newLanguage = language.toLowerCase();
          if (newLanguage !== AweSettings.getLanguage() || forceChange) {
            // Change language $settings
            AweSettings.update({language: newLanguage});

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
        var $serverData = angular.element(document).injector().get('ServerData');
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
    return AweSettings;
  }]);
