package web;

import io.javalin.Javalin;
import io.javalin.http.Handler;
import static io.javalin.http.HandlerType.*;

import web.demo.DemoController;
import web.docs.DocsController;
import web.home.HomeController;
import web.layout.Layout;

import java.util.ArrayList;
import java.util.List;


public class Routes {
    public static ArrayList<RouteMapping> routes = new ArrayList<>();
    public static ArrayList<UiRouterRouteConfig> spaRoutes = new ArrayList<>();
    static RouteMapping[] mappings = {
            new RouteMapping(GET, "", Layout::get), // root component
            new RouteMapping(GET, HomeController.HOME, HomeController::get),
            new RouteMapping(GET, HomeController.SUBVIEW, HomeController::subview),
            new RouteMapping(GET, DemoController.GET, DemoController::get),
            new RouteMapping(GET, DocsController.GET, DocsController::get)
    };

    static {
        List.of(mappings).forEach(route -> routes.add(route));
    }

    public static Javalin init(Javalin javalin) {
        routes.forEach(routeMapping ->
            javalin.addHandler(
                    routeMapping.method,
                    routeMapping.url,
                    routeMapping.handler)
        );
        return javalin;
    }

}
