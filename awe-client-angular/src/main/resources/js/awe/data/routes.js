// Route method definition
export const routeMethods = {
  "base": () => "",
  "public": ["$stateParams", (p) => "screen/public/" + p.screenId],
  "private": ["$stateParams", (p) => "screen/private/" + p.screenId],
  "screenData": ["$stateParams", "ServerData", (p, $serverData) => {
      let view = routeMethods.view(p);
      return $serverData.getScreenData(routeMethods.screen(p), view).then(() => view);
    }],
  "template": (p) => angular.element(document).injector().get('ServerData').getTemplateUrl(routeMethods.screen(p), routeMethods.view(p)),
  "view": (p) => "subScreenId" in p ? "report" : "base",
  "screen": (p) => "subScreenId" in p ? p.subScreenId : "screenId" in p ? p.screenId : null
};

// Routing data for view controller
const viewControllerData = {controller: "ViewController", templateUrl: routeMethods.template, resolve: {screenData: routeMethods.screenData, context: routeMethods.base}};

// Set up states
export const states = [
  {name: 'index', url: "/", views: {"base": {...viewControllerData}}},
  {name: 'global', url: "/screen/:screenId", views: {"base": {...viewControllerData}}},
  {name: 'public', url: "/screen/public/:screenId", views: {"base": {...viewControllerData, abstract: true}}},
  {name: 'public.screen', url: "/:subScreenId", views: {"report": {...viewControllerData, resolve: {...viewControllerData.resolve, context: routeMethods.public}}}},
  {name: 'private', url: "/screen/private/:screenId", views: {"base": {...viewControllerData, abstract: true}}},
  {name: 'private.screen', url: "/:subScreenId", views: {"report": {...viewControllerData, resolve: {...viewControllerData.resolve, context: routeMethods.private}}}}
];
