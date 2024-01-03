package web.product;

import static j2html.TagCreator.*;
import static web.utils.ViewHelpers.render;

import app.db.Db;
import app.models.Product;
import io.javalin.http.Context;
import j2html.tags.DomContent;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
import web.utils.StateService;

public class ProductController {

  public static Context newForm(@NotNull Context ctx) {

    return render(
        ctx,
        main(
            form()
                .attr("data-action", "/products")
                .attr("data-success", StateService.created("products"))
                .with(
                    each(
                        filter(getFieldNames(Product.class), f -> !"id".equals(f)),
                        f -> label(f).with(input().withName(f))),
                    button("Submit").withType("submit"))));
  }

  public static Context updateForm(@NotNull Context ctx) throws SQLException {
    String id = ctx.pathParam("id");
    List<String> fields = getFieldNames(Product.class);
    Optional<Product> product =
        Db.queryVal(Product.class, "SELECT * FROM products WHERE id = ?", new BigInteger(id));
    if (product.isEmpty()) {
      return render(ctx, div("Not found"));
    } else {
      return render(
          ctx,
          main(
              form()
                  .attr("data-action", "/products/update/" + id)
                  .attr("data-success", StateService.get("products", id))
                  .with(
                      each(
                          filter(fields, f -> !"id".equals(f)),
                          f ->
                              label(f)
                                  .with(
                                      input()
                                          .withName(f)
                                          .withValue(getFieldValue(product.get(), f).toString()))),
                      button("Submit").withType("submit"))));
    }
  }

  private static class IdReq {
    public String id;
  }

  public static void delete(@NotNull Context ctx) {
    String id = ctx.bodyAsClass(IdReq.class).id;
    try {
      Db.execute("DELETE FROM products WHERE id = ?", new BigInteger(id));
      ctx.status(204);
    } catch (SQLException e) {
      ctx.status(404);
    }
  }

  public static Context getAll(@NotNull Context ctx) {
    Class clazz = Product.class;
    List<Product> items = Db.queryList(clazz, "SELECT * FROM products");
    List<String> fields = getFieldNames(clazz);

    return render(
        ctx,
        main(
            createProductTable(fields, items),
            menu(button("Create").attr("onclick", StateService.create("products")))));
  }

  public static Context getOne(@NotNull Context ctx) throws SQLException {
    String id = ctx.pathParam("id");
    Optional<Product> product =
        Db.queryVal(Product.class, "SELECT * FROM products WHERE id = ?", new BigInteger(id));
    if (product.isEmpty()) {
      return render(ctx, div("Not found"));
    } else {
      return render(
          ctx,
          main(
              table(
                  each(
                      getFieldNames(Product.class),
                      f -> tr(th(f), td(getFieldValue(product.get(), f).toString()))),
                  menu(
                      a("Edit")
                          .attr(
                              "onclick",
                              StateService.edit("products", product.get().id.toString())),
                      form()
                          .attr("data-action", "/products/delete")
                          .attr("data-success", StateService.list("products"))
                          .with(
                              input()
                                  .isHidden()
                                  .withName("id")
                                  .withValue(product.get().id.toString()),
                              button("Delete").withClass("secondary"))))));
    }
  }

  public static Context create(@NotNull Context ctx) throws SQLException, IllegalAccessException {
    Product product = ctx.bodyAsClass(Product.class);
    String res = Db.create("products", product);
    IdReq resBody = new IdReq();
    resBody.id = res;
    return ctx.json(resBody).status(201);
  }

  public static Context update(@NotNull Context ctx) throws SQLException, IllegalAccessException {
    String id = ctx.pathParam("id");
    Product product = ctx.bodyAsClass(Product.class);
    Db.update("products", id, product);
    return ctx.json("").status(204);
  }

  private static List<String> getFieldNames(Class<?> clazz) {
    return Arrays.stream(clazz.getDeclaredFields())
        .map(field -> field.getName())
        .collect(Collectors.toList());
  }

  private static DomContent createProductTable(List<String> fields, List<Product> items) {
    return table(createTableHeader(fields), createTableBody(fields, items));
  }

  private static DomContent createTableHeader(List<String> fields) {
    return thead(each(fields, f -> th(f)), th()); // button column
  }

  private static DomContent createTableBody(List<String> fields, List<Product> items) {
    return tbody(each(items, item -> createTableRow(fields, item)));
  }

  private static DomContent createTableRow(List<String> fields, Product item) {
    return tr(
        each(fields, f -> td(String.valueOf(getFieldValue(item, f)))),
        td(a("View").attr("onclick", StateService.get("products", item.id.toString()))));
  }

  private static Object getFieldValue(Product item, String fieldName) {
    try {
      return Product.class.getDeclaredField(fieldName).get(item);
    } catch (IllegalAccessException | NoSuchFieldException e) {
      return "";
    }
  }
}
