import { aweApplication } from "./../awe";

// Loader directive
aweApplication.directive('aweLoader',
  ['ServerData', '$compile', '$templateCache', 'AweUtilities',
    /**
     * Help directive
     * @param {object} ServerData Server call service
     * @param {object} $compile compilation service
     * @param {object} $templateCache Template cache
     * @param {object} Utilities Awe utilities
     */
    function (ServerData, $compile, $templateCache, Utilities) {
      // Action Controller methods
      let TEMPLATE_LOADING = "--LOADING--";
      return {
        restrict: 'E',
        compile: function () {
          return function (scope, elem, attrs) {
            /**
             * Compile the template
             * @param {type} template
             * @returns {undefined}
             */
            function compileTemplate(template) {
              // Compile the received data
              let newElement = $compile(template)(scope);
              // Which we can then append to our DOM element.
              elem.append(newElement);
            }

            /**
             * Compile the template
             * @param {type} template
             * @returns {undefined}
             */
            let endWatchTemplateLoaded;
            function onTemplateLoaded(event, template) {
              endWatchTemplateLoaded();
              compileTemplate(template);
            }

            // Observe select2 attributes
            let initWatch = attrs.$observe('iconLoader', initLoader);

            /**
             * Loader initialization
             * @param {String} iconLoader
             */
            function initLoader(iconLoader) {
              // Add action
              let templateName = "loader/" + (iconLoader || "spinner");

              // Check parameters
              let template = $templateCache.get(templateName);
              if (!template) {
                $templateCache.put(templateName, TEMPLATE_LOADING);
                ServerData.preloadAngularTemplate({path: templateName}, function (data) {
                  Utilities.publishDelayed('template-' + templateName, data);
                  compileTemplate(data);
                });
              } else if (template === TEMPLATE_LOADING) {
                endWatchTemplateLoaded = scope.$on('template-' + templateName, onTemplateLoaded);
              } else {
                compileTemplate(template);
              }
              // Remove watch
              initWatch();
            }
          };
        }
      };
    }
  ]);
