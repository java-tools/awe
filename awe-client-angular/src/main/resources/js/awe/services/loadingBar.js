import { aweApplication } from "./../awe";

// Loading bar service
aweApplication.factory('LoadingBar',
  ['cfpLoadingBar',
    function (loadingBarPlugin) {
      // Call number
      var calls = 0;
      var showing = false;
      var $window = $(window);

      var LoadingBar = {
        /**
         * Starts a server task
         */
        startTask: function () {
          // If not showing, start loading bar
          if (!showing) {
            $window.scrollTop(0);
            loadingBarPlugin.start();
            showing = true;
            calls = 1;
            // Else add a call
          } else {
            calls++;
            loadingBarPlugin.inc();
          }
        },
        /**
         * Starts some server tasks
         * @param {integer} tasks
         */
        startTasks: function (tasks) {
          // If not showing, start loading bar
          if (!showing) {
            $window.scrollTop(0);
            loadingBarPlugin.start();
            showing = true;
            calls = tasks;
            // Else add a call
          } else {
            calls += tasks;
          }
        },
        /**
         * Finishes a server task
         */
        endTask: function () {
          // Remove a call
          calls--;
          loadingBarPlugin.inc();

          // If calls are finished, complete the loading bar
          if (showing && calls === 0) {
            loadingBarPlugin.complete();
            showing = false;
          }
        },
        /**
         * Finishes all server tasks
         */
        end: function () {
          // Remove all calls
          calls = 1;

          // If calls are finished, complete the loading bar
          LoadingBar.endTask();
        }
      };
      return LoadingBar;
    }
  ]);