// Route method definition
export const routeMethods = {
  "base": () => "",
  "public": ["$stateParams", (p) => "screen/public/" + p.screenId],
  "private": ["$stateParams", (p) => "screen/private/" + p.screenId],
  "screenData": ["$stateParams", "ServerData", (p, $serverData) => {
      let view = routeMethods.view(p);
      return $serverData.getScreenData(routeMethods.screen(p), view).then(() => view);
    }],
  "template": (p) => angular.element(document).injector().get('ServerData').getTemplateUrl(routeMethods.screen(p), routeMethods.view(p), "r" in p),
  "view": (p) => "subScreenId" in p ? "report" : "base",
  "screen": (p) => "subScreenId" in p ? p.subScreenId : "screenId" in p ? p.screenId : null
};

// Set up states
export const states = [
  {name: 'index', url: "/", views: {"base": {controller: "ViewController", templateUrl: routeMethods.template, resolve: {screenData: routeMethods.screenData, context: routeMethods.base}}}},
  {name: 'global', url: "/screen/:screenId", views: {"base": {controller: "ViewController", templateUrl: routeMethods.template, resolve: {screenData: routeMethods.screenData, context: routeMethods.base}}}},
  {name: 'public', url: "/screen/public/:screenId", views: {"base": {controller: "ViewController", abstract: true, templateUrl: routeMethods.template, resolve: {screenData: routeMethods.screenData, context: routeMethods.base}}}},
  {name: 'public.screen', url: "/:subScreenId?:r", views: {"report": {controller: "ViewController", templateUrl: routeMethods.template, resolve: {screenData: routeMethods.screenData, context: routeMethods.public}}}},
  {name: 'private', url: "/screen/private/:screenId", views: {"base": {controller: "ViewController", abstract: true, templateUrl: routeMethods.template, resolve: {screenData: routeMethods.screenData, context: routeMethods.base}}}},
  {name: 'private.screen', url: "/:subScreenId?:r", views: {"report": {controller: "ViewController", templateUrl: routeMethods.template, resolve: {screenData: routeMethods.screenData, context: routeMethods.private}}}}
];
