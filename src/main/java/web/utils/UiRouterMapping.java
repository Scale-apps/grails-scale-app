package web.utils;

import static web.utils.ViewHelpers.capitalize;

/** name: "home", url: "/", serverPath: "/_home", */
public class UiRouterMapping {
  public final String name;
  public final String url;
  public final String serverPath;

  public UiRouterMapping(String name, String url, String serverPath) {
    this.name = name;
    this.url = url;
    this.serverPath = serverPath;
  }

  public static UiRouterMapping uiRoute(
       String name, String url, String serverPath) {
    return new UiRouterMapping(name, url, serverPath);
  }

  public String getControllerName() {
    return capitalize(this.name) + "Controller";
  }
}
