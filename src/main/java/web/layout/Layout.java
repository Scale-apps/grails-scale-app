package web.layout;

import static j2html.TagCreator.a;
import static j2html.TagCreator.body;
import static j2html.TagCreator.div;
import static j2html.TagCreator.each;
import static j2html.TagCreator.html;
import static j2html.TagCreator.link;
import static j2html.TagCreator.meta;
import static j2html.TagCreator.nav;
import static j2html.TagCreator.script;
import static j2html.TagCreator.strong;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import j2html.tags.DomContent;
import j2html.tags.specialized.FooterTag;
import j2html.tags.specialized.HtmlTag;
import java.util.Arrays;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import web.Routes;
import web.utils.UiRouterMapping;

public class Layout {

  /** CDN libs to be loaded into the header of our main layout */
  private static final List<String> ULR_LIBS =
      Arrays.asList(
          "https://cdn.jsdelivr.net/npm/@angular-wave/angular.ts@0.0.48/dist/angular-ts.umd.min.js");

  public static Context get(Context ctx) {
    return ctx.html(layout().render());
  }

  public static HtmlTag layout() {
    return html(
        head(),
        body()
            // Bootstrap our AngularTS app
            .attr("ng-app", "app")
            .with(header(), div().attr("ng-view"), footer()));
  }

  public static HtmlTag layout(@NotNull DomContent content) {
    return html(head(), body().attr("ng-app", "app").with(header(), content), footer());
  }

  public static DomContent head() {
    return j2html.TagCreator.head(
        meta().attr("charset", "utf-8"),
        meta()
            .attr("name", "viewport")
            .attr("content", "width=device-width, initial-scale=1, shrink-to-fit=no"),
        meta().attr("name", "google").attr("content", "notranslate"),
        link().attr("rel", "stylesheet").attr("href", "/public/web/app.css"),
        each(ULR_LIBS, s -> script().withSrc(s).isDefer()),
        // Define ng-router routes
        script("window.routes = " + convertToJsonArray(Routes.spaRoutes)),
        script("window.crudRoutes = " + convertToJsonArray(Routes.crudRoutes)));
  }

  public static DomContent header() {
    return j2html.TagCreator.header(nav(a(strong("JAVALIN APP")).withHref("/")));
  }

  public static FooterTag footer() {
    return j2html.TagCreator.footer(
        script()
            .withCondAsync(true)
            .withSrc("http://localhost:3000/browser-sync/browser-sync-client.js?v=2.27.10"),
        script().attr("type", "module").attr("src", "/public/web/app.js"));
  }

  private static String convertToJsonArray(List<UiRouterMapping> routeMappings) {
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      return objectMapper.writeValueAsString(routeMappings);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}
