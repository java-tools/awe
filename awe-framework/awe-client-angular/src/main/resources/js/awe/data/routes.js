// Route method definition
const fixParameters = (p) =>  {if ("subScreenId" in p) {p.subScreenId = p.subScreenId.split("?")[0];} else if ("screenId" in p) {p.screenId = p.screenId.split("?")[0];}};
export const routeMethods = {
  "base": () => "",
  "public": ["$stateParams", (p) => "screen/public/" + p.screenId],
  "private": ["$stateParams", (p) => "screen/private/" + p.screenId],
  "screenData": ["$stateParams", "ServerData", (p, $serverData) => {
      fixParameters(p);
      let view = routeMethods.view(p);
      return $serverData.getScreenData(routeMethods.screen(p), view).then(() => view);
    }],
  "template": (p) => angular.element(document).injector().get('ServerData').getTemplateUrl(routeMethods.screen(p), routeMethods.view(p)),
  "view": (p) => "subScreenId" in p ? "report" : "base",
  "screen": (p) => "subScreenId" in p ? p.subScreenId.split("?")[0] : "screenId" in p ? p.screenId.split("?")[0] : null
};

// Routing data for view controller
const viewControllerData = {"controller": "ViewController", "templateUrl": routeMethods.template, "resolve": {"screenData": routeMethods.screenData, "context": routeMethods.base}};

// Set up states
export const states = [
  {"name": 'index', "url": "/", "views": {"base": {...viewControllerData}}},
  {"name": 'global', "url": "/screen/:screenId", "views": {"base": {...viewControllerData}}},
  {"name": 'public', "url": "/screen/public/:screenId", "views": {"base": {...viewControllerData, "abstract": true}}},
  {"name": 'public.screen', "url": "/:subScreenId", "views": {"report": {...viewControllerData, "resolve": {...viewControllerData.resolve, "context": routeMethods.public}}}},
  {"name": 'private', "url": "/screen/private/:screenId", "views": {"base": {...viewControllerData, "abstract": true}}},
  {"name": 'private.screen', "url": "/:subScreenId", "views": {"report": {...viewControllerData, "resolve": {...viewControllerData.resolve, "context": routeMethods.private}}}}
];
