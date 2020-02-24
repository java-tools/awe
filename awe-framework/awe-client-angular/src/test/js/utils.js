/**
 * Launch a screen action
 * @param {String} actionName Action name
 * @param {String} actionMethod Action method
 * @param {Object} parameters Parameters
 * @param {Function} done Launch when done
 */
export function launchScreenAction($injector, actionName, actionMethod, parameters, done) {
  let $screen = $injector.get('Screen');
  let $actionController = $injector.get('ActionController');

  // Launch action
  $actionController.closeAllActions();
  let action = $actionController.generateAction({type: actionName, ...parameters}, {address: {view: "base"}}, true, true);

  // Spy screen action
  spyOn(action, "accept").and.callFake(done);

  // Call action
  $screen[actionMethod].call(this, action);
}