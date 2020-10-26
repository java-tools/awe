import {aweApplication} from "../awe";
import {ClientActions} from "../data/actions";
import ngFileUpload from "ng-file-upload";

// Require file upload
aweApplication.requires.push(ngFileUpload);

// Uploader service
aweApplication.factory('Uploader',
  ['Criterion', 'AweSettings', 'Upload', 'ActionController', 'ServerData', 'AweUtilities', '$translate',
    /**
     * Uploader service methods
     * @param {object} Criterion
     * @param {object} $settings
     * @param {object} Upload
     * @param {object} $actionController
     * @param {object} ServerData
     * @param {object} $utilities
     * @param {object} $translate
     */
    function (Criterion, $settings, Upload, $actionController, ServerData, $utilities, $translate) {
      /**
       * Uploader constructor
       * @param {Scope} scope Numeric scope
       * @param {String} id Numeric id
       * @param {String} element Numeric element
       */
      function Uploader(scope, id, element) {
        this.scope = scope;
        this.id = id;
        this.element = element;
        this.component = new Criterion(this.scope, this.id, this.element);
        var uploader = this;
        this.component.asUploader = function () {
          return uploader.init();
        };
        return this.component;
      }
      Uploader.prototype = {
        /**
         * Initialize date criteria
         */
        init: function () {
          var component = this.component;
          // Define type as text
          component.uploaderUID = 0;
          var updateTimer, destination;

          if (!component.asCriterion()) {
            // If criterion is wrong, cancel initialization
            return false;
          }

          // Store destination
          destination = component.controller.destination || "";

          // Add classes
          component.specialClass = "pixel-file-input-" + component.scope.size;
          component.updateClasses();

          // Set uploader max size
          // Upload.setDefaults({ngfMaxSize:$settings.get("uploadMaxSize")})

          /**
           * Validate the file
           */
          component.scope.validate = function(file) {
            if (file.size > ($settings.get("uploadMaxSize") * 1024 * 1024)) {
              // Send error message
              $actionController.sendMessage(component.scope, 'error', 'ERROR_TITLE_FILE_UPLOAD', $translate.instant('ERROR_MESSAGE_SIZE_LIMIT', { elementSize: $utilities.getSizeString(file.size), maxSize: $utilities.getSizeString($settings.get("uploadMaxSize"))}));

              // Return not valid
              return false;
            }

            // Return ok
            return true;
          };

          /**
           * Click to choose a file
           * @param {type} files
           */
          component.scope.chooseFile = function (files) {
            if (files && files.length > 0) {
              // Call upload
              component.uploaderUID++;
              component.uploaderIdentifier = "uploader-" + component.id + "-" + component.uploaderUID;

              // Send the file
              let file = files[0];
              let parameters = {
                [$settings.get("uploadIdentifier")]: component.uploaderIdentifier,
                [$settings.get("addressIdentifier")]: $utilities.stringifyJSON(component.address),
                destination: destination
              };

              var uploader = Upload.upload({
                  url: ServerData.getFileUrl("upload"),
                  headers: $settings.getAuthenticationHeaders(),
                  fields: parameters, // additional data to send
                  file: file
                });

              // On start upload, manage success, error and progress
              uploader.then(
                // On success
                function() {
                },
                // On error
                function() {
                  component.onReset();
                },
                // On progress
                function(evt) {
                    var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
                    component.onFileStatus(progressPercentage);
                });

              // Change uploader status
              component.uploading = true;
              component.uploadProgress = 0;
            }
          };

          /**
           * Clear a uploaded file
           * @param {Event} event
           */
          component.scope.clearFile = function (event) {
            $utilities.stopPropagation(event, true);
            // Launch clear file action
            $utilities.timeout(function () {
              component.deleting = true;
              var deleteValues = {type: "delete-file"};
              deleteValues["filename"] = component.model.selected;
              deleteValues["destination"] = destination;

              var serverAction = ServerData.getServerAction(component.address, deleteValues, true, true);
              $actionController.addActionList([serverAction], false, {address: component.address, context: component.context});
            });
          };

          /**
           * Download the file
           * @param {Event} event
           */
          component.scope.downloadFile = function (event) {
            $utilities.stopPropagation(event, true);
            // Launch clear file action
            $utilities.timeout(function () {
              // Generate download parameters
              var parameters = {
                filename: component.model.selected,
                destination: destination
              }

              // Generate url parameter
              var fileData = ServerData.getFileData("download", parameters);

              // Download file
              $utilities.downloadFile(fileData);
            });
          };

          /**
           * On start upload event
           * @param {number} percent Percentage
           */
          component.onFileStatus = function (percent) {
            if (!updateTimer) {
              updateTimer = $utilities.timeout(function () {
                updateTimer = null;
                if (!component.model.selected) {
                  // Change uploading percent
                  component.uploading = true;
                  component.uploadProgress = Math.floor(percent) + "%";
                }
              }, 100);
            }
          };

          /**
           * On start upload event
           * @param {object} parameters parameters sent
           */
          component.onFileUploaded = function (parameters) {
            // Fill progress bar
            $utilities.timeout.cancel(updateTimer);
            component.uploadProgress = "100%";

            // Finish file upload
            updateTimer = $utilities.timeout(function () {
              // Change model and finish uploading
              component.uploading = false;
              component.model.selected = parameters.path;
              component.model.name = parameters.name;
              component.model.size = parameters.size;
              updateTimer = null;

              // Update model
              component.model.values = [{value:parameters.path, label: parameters.name, size: parameters.size}];

              // Update the model
              component.modelChange();
            }, 100);
          };

          /**
           * Retrieve visible value
           */
          component.getVisibleValue = function () {
            var visibleValue = "";
            if (component.model.name && component.model.size) {
              visibleValue = component.model.name + " (" + $utilities.getSizeString(component.model.size) + ")";
            }
            return visibleValue;
          };

          /**
           * Reset criterion
           */
          component.onReset = function () {
            // Reset model
            component.clearUploader();

            // Update the model
            component.modelChange();
          };

          /**
           * Clear uploader
           */
          component.clearUploader = function () {
            // Cancel status timeout
            $utilities.timeout.cancel(updateTimer);

            // Reset status
            component.deleting = false;
            component.uploading = false;
            component.model.name = null;
            component.model.size = null;
            component.model.selected = null;
            component.model.values = [];
          };

          /**
           * On model changed
           */
          component.onModelChanged = function () {
            if (component.model.selected && !component.model.name) {
              var fileInfo = {type: "file-info"};
              fileInfo["filename"] = component.model.selected;
              var serverAction = ServerData.getServerAction(component.address, fileInfo, true, true);
              $actionController.addActionList([serverAction], false, {address: component.address, context: component.context});
            } else {
              if (!component.model.selected) {
                component.clearUploader();
              }
            }

            // Update visible value
            component.updateVisibleValue();
          };

          // Initialize criterion if model has value
          component.onModelChanged();

          /**
           * Event listeners
           */
          component.listeners = component.listeners || {};

          // Action listener definition
          $actionController.defineActionListeners(component.listeners, ClientActions.uploader, component.scope, component);

          // Action listener definition
          $utilities.defineModelChangeListeners(component.listeners, {scope: component.scope, check: ["selected"], service: component, method: "onModelChanged"});

          // Return initialized
          return true;
        }
      };
      return Uploader;
    }
  ]);