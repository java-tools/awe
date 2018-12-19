import { aweApplication } from "./../../awe";
import marked from "marked";

// Markdown editor directive
aweApplication.directive('aweInputMarkdownEditor',
  ['ServerData', 'Criterion', 'AweUtilities', 'AweSettings',
    function (serverData, Criterion, Utilities, $settings) {
      return {
        restrict: 'E',
        replace: true,
        templateUrl: function () {
          return serverData.getAngularTemplateUrl('input/markdown-editor');
        },
        scope: {
          'criterionId': '@inputMarkdownEditorId'
        },
        compile: function () {
          return {
            /**
             * Pregeneration function
             * @param {Object} scope
             * @param {Object} elem
             */
            pre: function (scope, elem) {
              // Initialize criterion
              scope.component = new Criterion(scope, scope.criterionId, elem);
              scope.initialized = scope.component.asCriterion();
            },
            post: function (scope, elem) {
              // Generate markdown component
              elem.find(".form-control").first().markdown({
                iconlibrary: "fa",
                language: $settings.getLanguage(),
                parser: marked,
                height: scope.component.controller.style,
                onChange: function (e) {
                  Utilities.timeout(function () {
                    scope.component.model.selected = e.getContent();
                  });
                },
                onBlur: function () {
                  Utilities.timeout(function () {
                    scope.component.modelChange();
                  });
                }
              });
            }
          };
        }
      };
    }
  ]);