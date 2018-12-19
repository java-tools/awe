import { aweApplication } from "./../awe";

// Require file upload
aweApplication.requires.push("ngFileUpload");

// Uploader service
aweApplication.factory('Uploader',
  ['Criterion', 'AweSettings', 'Upload', 'ActionController', 'ServerData', 'AweUtilities', 'Actions', '$translate',
    /**
     * Uploader service methods
     * @param {type} Criterion
     * @param {type} $settings
     * @param {type} Upload
     * @param {type} ActionController
     * @param {type} ServerData
     * @param {type} Utilities
     * @param {type} Control
     * @param {type} Actions
     * @param {type} $translate
     */
    function (Criterion, $settings, Upload, ActionController, ServerData, Utilities, Actions, $translate) {
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
          var updatingStatus, updateTimer, destination;

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
            if (file.size > $settings.get("uploadMaxSize")) {
              // Send error message
              ActionController.sendMessage(component.scope, 'error', 'ERROR_TITLE_FILE_UPLOAD', $translate.instant('ERROR_MESSAGE_SIZE_LIMIT', { elementSize: Utilities.getSizeString(file.size), maxSize: Utilities.getSizeString($settings.get("uploadMaxSize"))}));

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
              var parameters = $settings.getTokenObject();
              var file = files[0];
              parameters[$settings.get("uploadIdentifier")] = component.uploaderIdentifier;
              parameters[$settings.get("addressIdentifier")] = component.address;
              parameters["destination"] = destination;

              var uploader = Upload.upload({
                  url: ServerData.getFileUrl("upload"),
                  fields: ServerData.getEncodedParameters(parameters), // additional data to send
                  file: file
                });

              // On start upload, manage success, error and progress
              uploader.then(
                // On success
                function() {
                },
                // On error
                function(error) {
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
            Utilities.stopPropagation(event, true);
            // Launch clear file action
            Utilities.timeout(function () {
              component.deleting = true;
              var deleteValues = {type: "delete-file"};
              deleteValues["filename"] = component.model.selected;
              deleteValues["destination"] = destination;

              var serverAction = ServerData.getServerAction(component.address, deleteValues, true, true);
              ActionController.addActionList([serverAction], false, {address: component.address, context: component.context});
            });
          };

          /**
           * Download the file
           * @param {Event} event
           */
          component.scope.downloadFile = function (event) {
            Utilities.stopPropagation(event, true);
            // Launch clear file action
            Utilities.timeout(function () {
              // Generate download parameters
              var parameters = {
                ...$settings.getTokenObject(),
                filename: component.model.selected,
                destination: destination
              }

              // Generate url parameter
              var fileData = ServerData.getFileData("download", parameters);

              // Download file
              Utilities.downloadFile(fileData);
            });
          };

          /**
           * On start upload event
           * @param {object} parameters parameters sent
           */
          component.onFileStatus = function (percent) {
            if (!updateTimer) {
              updateTimer = Utilities.timeout(function () {
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
            Utilities.timeout.cancel(updateTimer);
            updatingStatus = true;
            component.uploadProgress = "100%";

            // Finish file upload
            updateTimer = Utilities.timeout(function () {
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
              visibleValue = component.model.name + " (" + Utilities.getSizeString(component.model.size) + ")";
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
            Utilities.timeout.cancel(updateTimer);

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
              ActionController.addActionList([serverAction], false, {address: component.address, context: component.context});
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
          Utilities.defineActionListeners(component.listeners, Actions.uploader, component.scope, component);

          // Action listener definition
          Utilities.defineModelChangeListeners(component.listeners, {scope: component.scope, check: ["selected"], service: component, method: "onModelChanged"});
        }
      };
      return Uploader;
    }
  ]);