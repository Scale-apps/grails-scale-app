package web.home;

import static j2html.TagCreator.a;
import static j2html.TagCreator.aside;
import static j2html.TagCreator.div;
import static j2html.TagCreator.each;
import static j2html.TagCreator.filter;
import static j2html.TagCreator.h1;
import static j2html.TagCreator.h6;
import static j2html.TagCreator.li;
import static j2html.TagCreator.main;
import static j2html.TagCreator.section;
import static j2html.TagCreator.ul;
import static web.utils.UiRouterMapping.uiRoute;
import static web.utils.ViewHelpers.render;

import io.javalin.http.Context;
import io.javalin.http.HandlerType;
import web.Routes;
import web.demo.DemoController;
import web.docs.DocsController;
import web.utils.UiRouterMapping;

public class HomeController {
  public static final UiRouterMapping HOME = uiRoute("home", "/", "/_home");
  public static final UiRouterMapping SUBVIEW = uiRoute("home.subview", "/", "/_subview");

  public static Context home(Context ctx) {

    return render(
        ctx,
        div()
            .attr("id", "home")
            .with(
                main(
                    h1("Javalin Boilerplate"),
                    div("A starter template"),
                    div("test")
                        .attr(
                            "ng-include",
                            "'" + SUBVIEW.serverPath + "'"), // ng-include wants expressions :(
                    section(
                        a("Demo").withHref(DemoController.URL),
                        a("Docs").withHref(DocsController.URL))),
                aside(
                    h6("Available controllers:"),
                    ul().with(
                            each(
                                filter(
                                    Routes.routes,
                                    routeMapping ->
                                        !"".equals(routeMapping.url)
                                            && !routeMapping.isUiRoute()
                                            && routeMapping.method == HandlerType.GET),
                                routeMapping ->
                                    li(a(routeMapping.defaultName()).withHref(routeMapping.url))),
                            each(
                                Routes.crudRoutes,
                                routeMapping ->
                                    li(
                                        a(routeMapping.getControllerName())
                                            .withHref(routeMapping.url)))))));
  }

  public static Context subview(Context ctx) {
    return render(ctx, div("for scalable development"));
  }
}
