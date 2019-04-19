import { aweApplication } from "./../awe";

// Tag list directive
aweApplication.directive('aweTagList',
  ['ServerData', 'Component', 'Storage', 'Connection', '$compile',
    function (serverData, Component, Storage, Connection, $compile) {
      return {
        restrict: 'A',
        scope: {
          'tagListId': '@aweTagList'
        },
        link: function (scope, elem) {
          // Init as component
          var component = new Component(scope, scope.tagListId);
          if (!component.asComponent()) {
            // If component initialization is wrong, cancel initialization
            return false;
          }

          /**
           * Filter the taglist (reload it)
           */
          component.reload = function () {
            var parameters = serverData.getFormValues();
            var option = Storage.get("screen")[component.address.view].option;
            var url = serverData.getTaglistUrl(option, component.id);
            Connection.post(url, parameters).then(function (response) {
              if (response.data && response.status === 200) {
                // assign it into the current DOM
                elem.html(response.data);

                // compile the new DOM and link it to the current scope.
                $compile(elem.contents())(scope);
              }
            });
          };

          // Launch a initial filter
          component.reload();
        }
      };
    }
  ]);
