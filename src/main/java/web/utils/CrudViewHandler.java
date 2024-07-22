package web.utils;

import static j2html.TagCreator.a;
import static j2html.TagCreator.button;
import static j2html.TagCreator.div;
import static j2html.TagCreator.each;
import static j2html.TagCreator.filter;
import static j2html.TagCreator.form;
import static j2html.TagCreator.input;
import static j2html.TagCreator.label;
import static j2html.TagCreator.main;
import static j2html.TagCreator.menu;
import static j2html.TagCreator.table;
import static j2html.TagCreator.tbody;
import static j2html.TagCreator.td;
import static j2html.TagCreator.th;
import static j2html.TagCreator.thead;
import static j2html.TagCreator.tr;
import static web.utils.ViewHelpers.getFieldNames;
import static web.utils.ViewHelpers.getFieldValue;

import app.db.Db;
import io.javalin.apibuilder.CrudHandler;
import io.javalin.http.Context;
import j2html.tags.DomContent;
import java.math.BigInteger;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import org.eclipse.jetty.http.HttpMethod;
import web.Routes;
import web.layout.Layout;

public abstract class CrudViewHandler<T extends Model> implements CrudHandler {

  public CrudViewHandler() {
    Routes.crudRoutes.add(
        new UiRouterMapping(this.getName(), this.getPath(), this.getServerPath()));
  }

  @Override
  public void create(Context ctx) {
    String res = Db.create(getCreateValidator(ctx.body()).validate());
    IdReq resBody = new IdReq();
    resBody.id = res;
    ctx.json(resBody).status(201);
  }

  @Override
  public void delete(Context ctx, String id) {
    Optional<?> item = Db.findById(getModelClass(), new BigInteger(id));
    if (item.isEmpty()) {
      ctx.status(404);
    } else {
      Db.delete(Db.getTableName(getModelClass()), id);
      ctx.status(204);
    }
  }

  @Override
  public void getAll(Context ctx) {
    List<? extends Model> items = Db.queryList(getModelClass());
    List<String> fields = getFieldNames(getModelClass());

    view(
        ctx,
        main(
            table(
                thead(each(fields, f -> th(f)), th()),
                tbody(
                    each(
                        items,
                        item ->
                            tr(
                                each(fields, f -> td(String.valueOf(getFieldValue(item, f)))),
                                td(
                                    a("View")
                                        .attr(
                                            "onclick",
                                            StateService.get(getName(), item.getId()))))))),
            menu(button("Create").attr("onclick", StateService.create(getName())))));
  }

  @Override
  public void getOne(Context ctx, String id) {

    Optional<? extends Model> item = Db.findById(getModelClass(), new BigInteger(id));
    if (item.isEmpty()) {
      view(ctx, div("Not found"));
    } else {
      view(
          ctx,
          main(
              table(
                  each(
                      getFieldNames(getModelClass()),
                      f -> tr(th(f), td(getFieldValue(item.get(), f).toString())))),
              menu(
                  a("Edit").attr("onclick", StateService.edit(getName(), item.get().getId())),
                  form()
                      .attr("ng-delete", "/" + getName() + "/" + item.get().getId())
                      .attr("data-method", HttpMethod.DELETE)
                      .attr("data-success", StateService.list(getName()))
                      .with(button("Delete").withClass("secondary")))));
    }
  }

  @Override
  public void update(Context ctx, String id) {
    Db.update(id, getUpdateValidator(ctx.body()).validate());
    ctx.json("").status(204);
  }

  public void newForm(Context ctx) {
    view(
        ctx,
        main(
            createForm(
                Optional.empty(),
                HttpMethod.POST,
                "/" + getName(),
                StateService.created(getName()))));
  }

  public void updateForm(Context ctx, String id) {
    Optional<? extends Model> item = Db.findById(getModelClass(), new BigInteger(id));
    if (item.isEmpty()) {
      view(ctx, div("Not found"));
    } else {
      view(
          ctx,
          main(
              createForm(
                  item,
                  HttpMethod.PUT,
                  "/" + getName() + "/" + id,
                  StateService.get(getName(), id))));
    }
  }

  private DomContent createForm(
      Optional<? extends Model> item, HttpMethod method, String actionUrl, String dataSuccess) {
    List<String> fields = getFieldNames(getModelClass());
    return form()
        .attr("ng-" + method.asString().toLowerCase(Locale.ROOT), actionUrl)
        .attr("data-method", method.asString())
        .attr("data-success", dataSuccess)
        .with(
            each(
                filter(fields, f -> !"id".equals(f)),
                f ->
                    label(f)
                        .with(
                            item.map(
                                    p ->
                                        input()
                                            .withName(f)
                                            .withValue(getFieldValue(p, f).toString()))
                                .orElse(input().withName(f)))),
            button("Submit").withType("submit"));
  }

  public void get(Context ctx) {
    Layout.get(ctx);
  }

  public abstract Class<? extends Model> getModelClass();

  public abstract ValidationHelper<T> getCreateValidator(String body);

  public ValidationHelper<T> getUpdateValidator(String body) {
    return getCreateValidator(body);
  }

  public final String getPath() {
    return "/" + getName();
  }

  public final String getServerPath() {
    return "/_" + getName();
  }

  public final String getName() {
    return Db.getTableName(getModelClass());
  }

  public static void view(Context ctx, DomContent dc) {
    ctx.html(dc.render());
  }
}
