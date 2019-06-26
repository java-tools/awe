import { aweApplication } from "./../awe";

// Locals service
aweApplication.factory('AweLocals',
  ['AweSettings', 'ActionController', '$rootScope', 'AweUtilities', '$translate', 'Connection',
    function ($settings, $actionController, $rootScope, Utilities, $translate, Connection) {

      // Locals repository

      var locals = {};
      var deferred = null;

      /**
       * Launch local functions for jquery widgets
       * @returns {undefined}
       */
      var launchLocalFunctions = function () {
        // Retrieve language
        var language = $settings.getLanguage();

        // Create script tag and add it to HTML
        var url = Connection.getRawUrl() + "/js/locals-" + language + ".js";
        var script = $("<script type='text/javascript' id='localeFunctions' src='" + url + "'></script>");

        // Append localeFunctions (and replace old)
        var oldLocaleFunctions = $("#localeFunctions");
        if (oldLocaleFunctions.length > 0) {
          oldLocaleFunctions.replaceWith(script);
        } else {
          $("body").append(script);
        }
        /*Connection.get(url, "text/javascript").then(function (response) {
          if (response.data && response.status === 200) {
            // assign it into the current DOM
            var script = $("<script type='text/javascript' id='localeFunctions'>");
            script.text(response.data);

            // Append localeFunctions (and replace old)
            var oldLocaleFunctions = $("#localeFunctions");
            if (oldLocaleFunctions.length > 0) {
              oldLocaleFunctions.replaceWith(script);
            } else {
              $("body").append(script);
            }
          }
        });*/
      };

      // Store locals retrieved
      $rootScope.$on('/action/locals-retrieved', function (event, action) {
        // Retrieve action parameters
        var parameters = action.attr("parameters");
        var language = parameters.language;

        if (language !== null) {
          // Set language to lower case
          language = language.toLowerCase();

          // Finish locals action
          $actionController.acceptAction(action);

          // Store data
          locals[language] = parameters;

          // Set language
          deferred.resolve(locals[language].translations);
        }
      });

      // Store locals retrieved
      $rootScope.$on('/action/reload-language', function (event, action) {
        // Get language
        var language = $settings.getLanguage();

        // If language in locals, retrieve it
        if (language !== null) {
          if (language in locals) {
            delete locals[language];
            $translate.refresh();
          }
        }
        $actionController.acceptAction(action);
      });

      // Launch local functions
      $rootScope.$on('launchLocalFunctions', launchLocalFunctions);
      return function () {
        // Generate deferred
        deferred = Utilities.q.defer();

        // Get language
        var language = $settings.getLanguage();

        // If language in locals, retrieve it
        if (language !== null) {
          if (language in locals) {
            deferred.resolve(locals[language].translations);
          } else {
            var parameters = {language: language};
            parameters[$settings.get("serverActionKey")] = "get-locals";
            var getLocals = {type: 'server', parameters: parameters};
            $actionController.addActionList([getLocals], false, {address: {}, context: ""});
          }
        }

        return deferred.promise;
      };
    }
  ]);