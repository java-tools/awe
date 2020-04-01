import {routeMethods} from "../../../main/resources/js/awe/data/routes";

describe('awe-framework/awe-client-angular/src/test/js/data/routes.js', function() {

  it('should get the public screen url', function() {
    expect(routeMethods.public[1]({screenId:"tutu"})).toBe("screen/public/tutu");
  });

  it('should get the private screen url', function() {
    expect(routeMethods.private[1]({screenId:"tutu"})).toBe("screen/private/tutu");
  });

  it('should retrieve the view on a full page', function() {
    expect(routeMethods.view({screenId:"tutu?lalasdsad=12313"})).toBe("base");
  });

  it('should retrieve the view on a report page', function() {
    expect(routeMethods.view({subScreenId: "lala", screenId:"tutu"})).toBe("report");
  });

  it('should retrieve the screen on a full page', function() {
    expect(routeMethods.screen({screenId:"tutu?lalasdsad=12313"})).toBe("tutu");
  });

  it('should retrieve the screen on a report page', function() {
    expect(routeMethods.screen({subScreenId: "lala", screenId:"tutu"})).toBe("lala");
  });

  it('should retrieve screen data on index', function() {
    let serverData = {getScreenData: () => {}};
    spyOn(serverData, "getScreenData").and.returnValue({then: () => null});
    routeMethods.screenData[2]({}, serverData);
    expect(serverData.getScreenData).toHaveBeenCalled();
  });

  it('should retrieve screen data on a full page', function() {
    let serverData = {getScreenData: () => {}};
    spyOn(serverData, "getScreenData").and.returnValue({then: () => null});
    routeMethods.screenData[2]({screenId:"tutu?lalasdsad=12313"}, serverData);
    expect(serverData.getScreenData).toHaveBeenCalled();
  });

  it('should retrieve screen data on a report page', function() {
    let serverData = {getScreenData: () => {}};
    spyOn(serverData, "getScreenData").and.returnValue({then: () => null});
    routeMethods.screenData[2]({subScreenId: "lala", screenId:"tutu"}, serverData);
    expect(serverData.getScreenData).toHaveBeenCalled();
  });
});