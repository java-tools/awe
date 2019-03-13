import { aweApplication } from "./../awe";
import { getUID } from "../data/options";
import _ from 'lodash';

// Utilities service
aweApplication.factory('AweUtilities',
  ['$log', '$window', '$compile', '$timeout', '$interval', '$rootScope', 'Storage', '$q', '$location',
    function ($log, $window, $compile, $timeout, $interval, $rootScope, Storage, $q, $location) {
      let Utilities = {
        /**
         * Indicates if parameter is a string
         * @public
         * @param  {Object} objeto Object to check
         * @return {Boolean} Object is a string
         * @example
         * alert(AweUtilities.isString(miVariable));
         */
        isString: function (objeto) {
          return angular.isString(objeto);
        },
        /**
         * Indicates if parameter is a string
         * @public
         * @return {Boolean} Object is a string
         * @example
         * alert(AweUtilities.isString(miVariable));
         */
        isMobile: function () {
          var check = false;
          (function (a) {
            if (/(android|bb\d+|meego).+mobile|avantgo|bada\/|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|iris|kindle|lge |maemo|midp|mmp|mobile.+firefox|netfront|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\/|plucker|pocket|psp|series(4|6)0|symbian|treo|up\.(browser|link)|vodafone|wap|windows (ce|phone)|xda|xiino/i.test(a) || /1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\-(n|u)|c55\/|capi|ccwa|cdm\-|cell|chtm|cldc|cmd\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\-s|devi|dica|dmob|do(c|p)o|ds(12|\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\-|_)|g1 u|g560|gene|gf\-5|g\-mo|go(\.w|od)|gr(ad|un)|haie|hcit|hd\-(m|p|t)|hei\-|hi(pt|ta)|hp( i|ip)|hs\-c|ht(c(\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\-(20|go|ma)|i230|iac( |\-|\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\/)|klon|kpt |kwc\-|kyo(c|k)|le(no|xi)|lg( g|\/(k|l|u)|50|54|\-[a-w])|libw|lynx|m1\-w|m3ga|m50\/|ma(te|ui|xo)|mc(01|21|ca)|m\-cr|me(rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\-2|po(ck|rt|se)|prox|psio|pt\-g|qa\-a|qc(07|12|21|32|60|\-[2-7]|i\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\-|oo|p\-)|sdk\/|se(c(\-|0|1)|47|mc|nd|ri)|sgh\-|shar|sie(\-|m)|sk\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\-|v\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\-|tdg\-|tel(i|m)|tim\-|t\-mo|to(pl|sh)|ts(70|m\-|m3|m5)|tx\-9|up(\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|yas\-|your|zeto|zte\-/i.test(a.substr(0, 4))) {
              check = true;
            }
          })(navigator.userAgent || navigator.vendor || window.opera);
          return check;
        },
        /**
         * Retrieve the browser
         */
        getBrowser: function () {
          var userAgent = $window.navigator.userAgent;
          var browsers = {chrome: /chrome/i, safari: /safari/i, firefox: /firefox/i, ie: /msie|trident/i};
          for (var key in browsers) {
            if (browsers[key].test(userAgent)) {
              return key;
            }
          }
          return 'unknown';
        },
        /**
         * Return a date adding it days, months or years
         * @param  {String}  date  Date
         * @param  {Integer} diff  Diff
         * @param  {String}  type  Aggregation type ("D","M","A");
         * @param  {String}  separator Date separator
         * @return {String}  Date plus diff
         */
        dateAdd: function (date, diff, type, separator) {
          var _dateOut = "";
          if (date.length < 10) {
            return date;
          }
          var _dateArr = date.split(separator);
          var _date = new Date(parseInt(_dateArr[2], 10), parseInt(_dateArr[1], 10) - 1, parseInt(_dateArr[0], 10));
          switch (type) {
            case "D":
              _date.setDate(parseInt(_date.getDate(), 10) + diff);
              break;
            case "M":
              _date.setMonth(parseInt(_date.getMonth(), 10) + diff);
              break;
            case "A":
              _date.setYear(parseInt(_date.getFullYear(), 10) + diff);
              break;
            default:
              break;
          }
          _dateOut = (_date.getDate() < 10 ? "0" : "") + _date.getDate() + separator +
            (_date.getMonth() < 9 ? "0" : "") + (_date.getMonth() + 1) + separator +
            _date.getFullYear();
          return _dateOut;
        },
        /**
         * Return current time
         * @return {String}  Current time
         */
        getCurrentTime: function () {
          // Get current date
          var now = new Date();

          // Retrieve data
          var hh = now.getHours();
          var mm = now.getMinutes();
          var ss = now.getSeconds();
          var ms = now.getMilliseconds();

          // Format data
          var fhh = hh <= 9 ? "0" + hh : hh;
          var fmm = mm <= 9 ? "0" + mm : mm;
          var fss = ss <= 9 ? "0" + ss : ss;
          var fms = ms <= 9 ? "00" + ms : ms <= 99 ? "0" + ms : ms;

          // Merge data
          var formattedTime = fhh + ":" + fmm + ":" + fss + "." + fms;

          // Return formatted time
          return formattedTime;
        },
        /**
         * Converts a json into a string
         * @public
         * @param {Object} value
         * @return {String} Json String
         */
        stringifyJSON: function (value) {
          return JSON.stringify(value);
        },
        /**
         * Converts a string into a json object
         * @public
         * @param {string} json
         * @return {Object} JSON object
         */
        parseJSON: function (json) {
          var output;
          switch (typeof json) {
            case "object":
              output = json;
              break;
            default:
              try {
                output = JSON.parse(json);
              } catch (exc) {
                $log.error("[JSON] Error parsing json string: '" + json + "'", exc);
                output = Utilities.evalJSON(json);
              }
              break;
          }
          return output;
        },
        /**
         * Returns a random number to avoid cache
         * @public
         * @return {Integer} Cache number
         */
        cache: function () {
          return Math.floor(Math.random() * 1000000000);
        },
        /**
         * Unique id generation
         * @public
         * @param {Integer} char_numbers Number of characters of the new UID
         * @return {String} UID generated
         */
        getUID: getUID,
        /**
         * Capitalizes a string
         * @public
         * @param {String} data String to capitalize
         * @return {String} Capitalized string
         */
        capitalize: function (data) {
          var low = data.toLowerCase();
          var words = low.split(" ");
          var wordsCap = [];
          for (var i = 0, t = words.length; i < t; i++) {
            if (words[i].length > 0) {
              wordsCap.push(words[i].charAt(0).toUpperCase() + words[i].substr(1, words[i].length - 1));
            }
          }
          return wordsCap.join(" ");
        },
        /**
         * Returns the number of occurrences of a substring inside a string
         * @public
         * @param {String} text Text to search occurrences
         * @param {String} sub  Substring to search for occurrences
         * @return {Integer} Number of occurrences of sub inside text
         */
        count: function (text, sub) {
          return text.split(sub).length - 1;
        },
        /**
         * Encodes a char to URL format
         * @param {String} c Char to encode
         * @return Char encoded
         */
        urlEncodeCharacter: function (c) {
          return '%' + c.charCodeAt(0).toString(16);
        },
        /**
         * Decodes a char from URL format
         * @param {String} c Char to decode
         * @return Char decoded
         */
        urlDecodeCharacter: function (c) {
          return String.fromCharCode(parseInt(c, 16));
        },
        /**
         * Encodes a string into URL format
         * @param {String} s String to encode
         * @return String encoded
         */
        urlEncode: function (s) {
          return encodeURIComponent(s).replace(/\%20/g, '+').replace(/[!'()*~]/g, function (e) {
            return Utilities.urlEncodeCharacter(e);
          });
        },
        /**
         * Decodes a string from URL format
         * @param {String} s String to decode
         * @return String decoded
         */
        urlDecode: function (s) {
          return decodeURIComponent(s.replace(/\+/g, '%20')).replace(/\%([0-9a-f]{2})/g, function (e) {
            return Utilities.urlDecodeCharacter(e);
          });
        },
        /**
         * Encodes a string
         * @param {String} s String to encode
         * @return String encoded
         */
        encodeSymetric: function (s) {
          var out = Base64.encode(s, false);
          return Utilities.urlEncode(out);
        },
        /**
         * Decodes a string
         * @param {String} s String to decode
         * @return String decoded
         */
        decodeSymetric: function (s) {
          var out = Utilities.urlDecode(s);
          return Base64.decode(out, false);
        },
        /**
         * Removes new lines
         * @param {String} s String to remove new lines
         * @return String without new lines
         */
        removeNewlines: function (s) {
          var _out = s;
          if (Utilities.isString(s)) {
            _out = s.replace(/\\\\n/ig, '').replace(/\\\n/ig, '').replace(/\\n/ig, '').replace(/\n/ig, '');
          }
          return _out;
        },
        /**
         * Fixes a value to send it
         * @param {String} s String to encode
         * @return String encoded
         */
        fixValue: function (s) {
          var _out = s;
          if (Utilities.isString(s)) {
            _out = s === "&#160;" ? "" : s;
          }
          return _out;
        },
        /**
         * Cleans a object converting it into a <[String,String> object
         * @param {Object} o Object to clean
         * @return Object clean
         */
        cleanObject: function (o) {
          for (var key in o) {
            if (Utilities.isNull(o[key])) {
              o[key] = "";
            } else {
              o[key] = String(o[key]);
            }
          }

          return o;
        },
        /**
         * Returns true if a string is a number
         * @param {String} n String to test
         * @return {boolean} String is numeric
         */
        isNumber: function (n) {
          switch (typeof n) {
            case "number":
              return true;
            case "string":
              var numberPattern = /(\-\d+)?\d*\.?\d*/i;
              return numberPattern.test(n);
            default:
              return false;
          }
        },
        /**
         * Returns true if a variable is null
         * @param {String} n Variable to test
         * @return {boolean} String is null or undefined
         */
        isNull: function (n) {
          return n === null || n === undefined;
        },
        /**
         * Returns true if a variable is null or empty
         * @param {String} n Variable to test
         * @return {boolean} String is null or undefined
         */
        isEmpty: function (n) {
          return Utilities.isNull(n) || $.trim(n) === "";
        },
        /**
         * Removes blanks from string header and footer
         * @param {String} value String to trim
         * @return {String} String trimmed
         */
        trim: function (value) {
          if (!Utilities.isString(value)) {
            return value;
          }
          if (!String.prototype.trim) {
            return value.replace(/^\s*/, '').replace(/\s*$/, '');
          }
          return String.prototype.trim.apply(value);
        },
        /**
         * Evaluates a json string without key quotes
         * @param {string} jsonString JSON string
         * @returns {Object} json evaluated
         */
        evalJSON: function (jsonString) {
          return Utilities.eval("(" + jsonString + ")");
        },
        /**
         * Evaluates a expression
         * @param {string} expression string
         * @returns {Object} expression evaluated
         */
        eval: function (expression) {
          let c = "constructor";
          return $window[c][c]("return " + expression + ";")();
        },
        /**
         * Evaluates a formule and retrieves the result
         * @param {string} formule Formule string
         * @param {Object} values
         * @returns {Object} expression evaluated
         */
        formule: function (formule, values) {
          /* Get formule */
          var value = null;

          /* Replace formule parameters */
          _.each(values, function (value, valueId) {
            formule = formule.replace(new RegExp("\\[" + valueId + "\\]", "ig"), value);
          });
          formule = formule.replace(new RegExp("#", "ig"), "\"");

          /* Eval formule */
          try {
            value = Utilities.eval(formule);
          } catch (exc) {
            $log.error("[FORMULE] Formule: " + formule, {params: values, exception: exc});
          }

          return value;
        },
        /**
         * Evaluates a json string without key quotes
         * @param {string} booleanString Boolean string
         * @returns {boolean} boolean evaluated
         */
        parseBoolean: function (booleanString) {
          var booleanValue = false;
          var boolean = Utilities.trim(String(booleanString)).toLowerCase();
          if (boolean === "true" || boolean === "1") {
            booleanValue = true;
          }
          return booleanValue;
        },
        /**
         * Encode parameters
         * @param {Object} parameters Parameter map
         * @param {boolean} encode Encode parameters
         * @returns {String} Parameter list
         */
        encodeParameters: function (parameters, encode) {
          var parameterString = Utilities.stringifyJSON(parameters);
          return encode ? Utilities.encodeSymetric(parameterString) : parameterString;
        },
        /**
         * Decode data
         * @param {Object} data Data received
         * @param {boolean} decode Decode data
         * @returns {String} Parameter list
         */
        decodeData: function (data, decode) {
          return decode === "1" ? Utilities.parseJSON(Utilities.decodeSymetric(data)) : Utilities.parseJSON(data);
        },
        /**
         * Sanitize $settings
         * @param {object} unsanitized $settings
         * @param {object} definition $settings definition
         * @returns {object} $settings sanitized
         */
        sanitize: function (unsanitized, definition) {
          var sanitized = {};
          _.each(unsanitized, function (value, setting) {
            if (typeof definition[setting] === 'string') {
              sanitized[setting] = Utilities.sanitizeSetting(value, definition[setting]);
            } else {
              // Define setting object
              if (!(setting in sanitized)) {
                sanitized[setting] = {};
              }
              // Store subsettings in setting
              _.each(definition[setting], function (sType, sSetting) {
                sanitized[setting][sSetting] = Utilities.sanitizeSetting(unsanitized[setting][sSetting], sType);
              });
            }
          });
          return sanitized;
        },
        /**
         * Sanitize the value
         * @param {mixed} value
         * @param {string} type
         * @returns {mixed} value sanitized
         */
        sanitizeSetting: function (value, type) {
          switch (type) {
            case 'int':
              return parseInt(value, 10);
              break;
            case 'boolean':
              return Utilities.parseBoolean(value);
              break;
            default:
              return value;
          }
        },
        /**
         * Check how many watches are there in scope
         * @param {type} scope
         * @param {type} deferred
         */
        watchersContainedIn: function (scope, deferred) {
          // Check the watches contained in scope
          var checkWatches = function () {
            var watchers = (scope.$$watchers) ? scope.$$watchers.length : 0;
            var child = scope.$$childHead;
            while (child) {
              watchers += (child.$$watchers) ? child.$$watchers.length : 0;
              child = child.$$nextSibling;
            }
            $log.debug("[WATCH COUNTER] There are " + watchers + " watches in component " + scope.component.address.component, scope.component.address);
          };
          // Launch deferred to retrieve the real number of watches
          if (deferred) {
            setTimeout(checkWatches);
          } else {
            checkWatches();
          }
        },
        /**
         * Compile contents with angular
         * @param {type} container
         * @param {type} scope
         * @param {type} detach
         */
        compileContents: function (container, scope, detach) {
          // 0: Safe compile
          if (container && container.length > 0) {
            _.each(container, function (node) {
              // Retrieve node to compile
              var $node = $(node);
              // 1: Get Contents
              var contents = $node.contents();
              // 2: Detach contents (if detach)
              if (detach) {
                contents.detach();
              }
              // 3: Compile contents
              $compile(contents)(scope);
              // 4: Append contents
              if (detach) {
                $node.append(contents);
              }
            });
          }
        },
        /**
         * Check if model has changed or not for the scope
         * @param {Object} component
         * @param {Object} launchers
         * @return {Mixed} which elements have changed | false
         */
        modelChanged: function (component, launchers) {
          var changes = false;
          var launcherId = Utilities.getAddressId(component.address);

          // Check if launcher is in launchers
          if (launcherId in launchers) {
            changes = launchers[launcherId];
          }

          // Retrieve changes
          return changes;
        },
        /**
         * Define a list of action listeners
         * @param {type} listeners
         * @param {type} actions
         * @param {type} scope
         * @param {type} executor
         */
        defineActionListeners: function (listeners, actions, scope, executor) {
          _.each(actions, function (actionOptions, actionId) {
            listeners[actionId] = scope.$on("/action/" + actionId, function (event, action) {
              actionOptions["service"] = executor;
              actionOptions["scope"] = scope;
              Utilities.resolveAction(action, actionOptions);
            });
          });
        },
        /**
         * Define a list of action listeners
         * @param {type} listeners
         * @param {type} parameters
         */
        defineModelChangeListeners: function (listeners, parameters) {
          var scope = parameters.scope;
          var service = parameters.service;
          listeners[parameters.method] = scope.$on("modelChanged", function (event, launchers) {
            // Set as editing or not
            var changes = Utilities.modelChanged(scope, launchers);
            if (Utilities.checkAttributes(changes, parameters.check)) {
              service[parameters.method](changes);
            }
          });
        },
        /**
         * Clear all the listeners
         * @param {type} listeners
         */
        clearListeners: function (listeners) {
          // Destroy event listeners
          _.each(listeners, function (listener) {
            listener();
          });
          listeners = null;
        },
        /**
         * Check if a
         * @param {type} element
         * @param {array} attributes Atrributes to check in element
         * @returns {undefined}
         */
        checkAttributes: function (element, attributes) {
          var check = true;
          // Phase 1: check
          if (element) {
            if (attributes) {
              _.each(attributes, function (attribute) {
                check = check && attribute in element;
              });
            }
          } else {
            check = false;
          }
          return check;
        },
        /**
         * Check if a
         * @param {type} address1
         * @param {type} address2
         * @param {array} check Elements to check in address (default is all)
         * @returns {undefined}
         */
        checkAddress: function (address1, address2, check) {
          var launch = true;
          // Phase 1: check
          if (check) {
            _.each(check, function (index) {
              launch = launch && (address1[index] === address2[index]);
            });
          } else {
            launch = _.isEqual(address1, address2);
          }
          return launch;
        },
        /**
         * Resolve the action if matches
         * @param {type} action
         * @param {object} parameters
         */
        resolveAction: function (action, parameters) {
          var scope = parameters.scope || {};
          var check = parameters.check || false;

          // Launch action
          if (Utilities.checkAddress(action.attr("callbackTarget"), scope.component.address || {}, check)) {
            // Launch method
            parameters.service[parameters.method](action.attr("parameters"), scope, action.attr("callbackTarget"));

            // Finish action
            action.accept();
          }
        },
        /**
         * Animate a
         * @param {type} node
         * @param {type} animation
         * @param {type} time
         * @param {type} endFunction
         * @returns {undefined}
         */
        animateCSS: function (node, animation, time, endFunction) {
          // Css animation
          node.css({
            "-webkit-transition-duration": time + "ms !important",
            "-moz-transition-duration": time + "ms !important",
            "-ms-transition-duration": time + "ms !important",
            "-o-transition-duration": time + "ms !important",
            "transitionDuration": time + "ms !important"
          });
          node.addClass('animateEaseTransition');
          node.css(animation);
          if (endFunction) {
            $timeout(endFunction, time);
          }
        },
        /**
         * Animate a
         * @param {type} node
         * @param {type} animation
         * @param {type} time
         * @param {type} endFunction
         * @returns {undefined}
         */
        animateJavascript: function (node, animation, time, endFunction) {
          // Javascript animation
          if (endFunction) {
            node.animate(animation, time);
          } else {
            node.animate(animation, time, endFunction);
          }
        },
        /**
         * Get size string
         * @param {Integer} size File size
         * @private
         */
        getSizeString: function (size) {
          var sizeString = "";
          // Bytes
          if (size < 1024) {
            sizeString = Math.floor(size) + "B";
            // KBytes
          } else if (size < Math.pow(1024, 2)) {
            sizeString = (Math.floor(size * 10 / 1024) / 10) + "KB";
            // MBytes
          } else if (size < Math.pow(1024, 3)) {
            sizeString = (Math.floor(size * 10 / Math.pow(1024, 2)) / 10) + "MB";
            // GBytes
          } else if (size < Math.pow(1024, 4)) {
            sizeString = (Math.floor(size * 10 / Math.pow(1024, 3)) / 10) + "GB";
            // TBytes
          } else if (size < Math.pow(1024, 5)) {
            sizeString = (Math.floor(size * 10 / Math.pow(1024, 4)) / 10) + "TB";
            // PBytes
          } else {
            sizeString = (Math.floor(size / Math.pow(1024, 5)) / 10) + "PB";
          }
          return sizeString;
        },
        /**
         * Download a file
         * @param {type} file
         */
        downloadFile: function (file) {
          Utilities.publish("download-file", file);
        },
        /**
         * Retrieve component model attribute
         * @param {String} address Element address
         * @param {String} attribute Attribute to check
         * @returns {Object} Attribute value
         */
        getAttribute: function (address, attribute) {
          // Value definition
          var value;
          var model = Storage.get("model");
          var api = Storage.get("api");
          var modelView = model[address.view];
          var apiView = api[address.view];
          // Call controller-specified getAttribute function (if exists)
          if (address.component in apiView && apiView[address.component].getAttribute) {
            value = apiView[address.component].getAttribute(attribute, address.column, address.row);
            // Else, check in model
          } else {
            value = modelView[address.component][attribute];
          }
          return value;
        },
        /**
         * Remove children (fast)
         * @param {type} node
         * @returns {undefined}
         */
        removeChildren: function (node) {
          var last;
          while (last = node.lastChild) {
            node.removeChild(last);
          }
        },
        /**
         * Manage parameter as array
         * @param {Mixed} value
         * @return {Array} value as array
         */
        asArray: function (value) {
          var outputArray = [];
          if (!Utilities.isEmpty(value)) {
            if (angular.isArray(value)) {
              outputArray = value;
            } else {
              outputArray.push(value);
            }
          }
          return outputArray;
        },
        /**
         * Publish into a channel
         * @param {String} channel
         * @param {Object} parameters
         */
        publish: function (channel, parameters) {
          Utilities.publishFromScope(channel, parameters, $rootScope);
        },
        /**
         * Publish into a channel from a specific scope
         * @param {String} channel
         * @param {Object} parameters
         * @param {Object} scope
         */
        publishFromScope: function (channel, parameters, scope) {
          scope.$broadcast(channel, parameters);
        },
        /**
         * Publish into a channel
         * @param {String} channel
         * @param {Object} parameters
         */
        publishDelayed: function (channel, parameters) {
          $timeout(function () {
            Utilities.publish(channel, parameters);
          });
        },
        /**
         * $timeout wrapper
         */
        timeout: $timeout,
        /**
         * $interval wrapper
         */
        interval: $interval,
        /**
         * $q wrapper
         */
        q: $q,
        /**
         * Publish into a channel from a specific scope
         * @param {String} channel
         * @param {Object} parameters
         * @param {Object} scope
         */
        publishDelayedFromScope: function (channel, parameters, scope) {
          $timeout(function () {
            Utilities.publishFromScope(channel, parameters, scope);
          });
        },
        /**
         * Emit into a channel from a specific scope
         * @param {String} channel
         * @param {Object} parameters
         * @param {Object} scope
         */
        emitDelayedFromScope: function (channel, parameters, scope) {
          $timeout(function () {
            Utilities.emitFromScope(channel, parameters, scope);
          });
        },
        /**
         * Emit into a channel from a specific scope
         * @param {String} channel
         * @param {Object} parameters
         * @param {Object} scope
         */
        emitFromScope: function (channel, parameters, scope) {
          scope.$emit(channel, parameters);
        },
        /**
         * Get object length
         * @param {Object} obj
         * @return Object length
         */
        objectLength: function (obj) {
          return Object.keys(obj).length;
        },
        /**
         * Check if node is visible
         * @param {Object} obj
         * @return Node is visible
         */
        isVisible: function (node) {
          return !!node.offsetParent;
        },
        /**
         * Compare if two values are equal
         * @param {type} value1
         * @param {type} value2
         * @returns {Boolean}
         */
        compareEqualValues: function (value1, value2) {
          var equals;
          if (typeof value1 === typeof value2) {
            equals = value1 === value2;
          } else {
            equals = (Utilities.isEmpty(value1) && Utilities.isEmpty(value2)) ||
              String(value1) === String(value2);
          }
          return equals;
        },
        /**
         * Stop event propagation and default action
         * @param {Object} event
         * @return Object length
         */
        stopPropagation: function (event, preventDefault) {
          // Cancel event propagation
          if (event.stopPropagation) {
            event.stopPropagation();
          }
          // Cancel default action
          if (event.preventDefault && preventDefault) {
            event.preventDefault();
          }
          event.cancelBubble = true;
          event.returnValue = false;
        },
        /**
         *
         * @returns {undefined}No operation
         */
        noop: function () {
        },
        /**
         * Add initializer to api list
         * @param {type} list
         * @param {type} initializer
         */
        initializeApi: function (list, initializer) {
          if (list.indexOf(initializer) < 0) {
            list.push(initializer);
          }
        },
        /**
         * Retrieve cell id
         * @param {Object} address
         * @return {String} cellId
         */
        getCellId: function (address) {
          return address.component + "-" + address.column + "-" + address.row;
        },
        /**
         * Retrieve address id
         * @param {Object} address
         * @return {String} addressId
         */
        getAddressId: function (address) {
          var addressId = address.component;
          if ("column" in address && address.column && "row" in address && address.row) {
            addressId = Utilities.getCellId(address);
          }
          return addressId;
        },
        /**
         * Retrieve application context path
         * @return Context path
         */
        getContextPath: function() {
          return $location.absUrl().substr(0, $location.absUrl().lastIndexOf($location.path()));
        },
        /**
         * Retrieve application context path
         * @return Context path
         */
        getState: function(location) {
          let state = {};
          let locationFixed = location.replace(Utilities.getContextPath(), "");
          let locationData = locationFixed.split('/');
          if (locationFixed.indexOf('/public') != -1) {
            // Public state
            state.to = 'public.screen';
            state.parameters = {
              screenId: locationData[3],
              subScreenId: locationData[4]
            };
          } else if (locationFixed.indexOf('/private') != -1) {
            // Private state
            state.to = 'private.screen';
            state.parameters = {
              screenId: locationData[3],
              subScreenId: locationData[4]
            };
          } else if (locationFixed.indexOf('/screen') != -1) {
            // Global state
            state.to = 'global';
            state.parameters = {
              screenId: locationData[2]
            };
          } else {
            // Index
            state.to = 'index';
            state.parameters = {};
          }
          return state;
        },
        /**
         * Debounce a function call
         * @param {function} func Function to launch
         * @param {number} wait Time to wait
         */
        debounce: function(func, wait) {
          return _.debounce(() => $timeout(func), wait);
        }
      };
      return Utilities;
    }
  ]);
