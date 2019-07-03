angular.module('FileManagerApp').config([ 'fileManagerConfigProvider', '$httpProvider', '$provide', function(config, $httpProvider, $provide) {
  config.set({
    appName : 'angular-filemanager',
    breadcrumbName : 'Root path',
    listUrl : contextPath + "/fm/listUrl",
    uploadUrl : contextPath + "/fm/uploadUrl",
    renameUrl : contextPath + "/fm/renameUrl",
    copyUrl : contextPath + "/fm/copyUrl",
    moveUrl : contextPath + "/fm/moveUrl",
    removeUrl : contextPath + "/fm/removeUrl",
    editUrl : contextPath + "/fm/editUrl",
    getContentUrl : contextPath + "/fm/getContentUrl",
    createFolderUrl : contextPath + "/fm/createFolderUrl",
    downloadFileUrl : contextPath + "/fm/downloadFileUrl",
    downloadMultipleUrl : contextPath + "/fm/downloadMultipleUrl",
    compressUrl : contextPath + "/fm/compressUrl",
    extractUrl : contextPath + "/fm/extractUrl",
    permissionsUrl : contextPath + "/fm/permissionsUrl"
  });

  // We override the upload method so all files are sent in an array and not as independent arguments with name file-xxx
  $provide.decorator('apiHandler', [ '$delegate', '$q', 'Upload', function apiHandlerDecorator($delegate, $q, Upload) {

    function newUpload(apiUrl, destination, files) {
      var self = this;
      var deferred = $q.defer();
      self.prototype.inprocess = true;
      self.progress = 0;
      self.error = '';

      var data = {
          destination: destination,
          files: files
      };

      if (files && files.length) {
          Upload.upload({
              url: apiUrl,
              data: data,
              arrayKey: ''
          }).then(function (response) {
              self.prototype.deferredHandler(response.data, deferred, data.status);
          }, function (response) {
              self.prototype.deferredHandler(response.data, deferred, data.status, 'Unknown error uploading files');
          }, function (evt) {
              self.progress = Math.min(100, parseInt(100.0 * evt.loaded / evt.total)) - 1;
          })['finally'](function() {
              self.inprocess = false;
              self.progress = 0;
          });
      }

      return deferred.promise;
    }

    $delegate.prototype.upload = newUpload.bind($delegate);
    return $delegate;
  } ]);
} ]);
