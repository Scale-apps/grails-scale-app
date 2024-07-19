package web.product;

import app.models.Product;
import web.utils.CrudViewHandler;
import web.utils.ValidationHelper;

public class ProductController extends CrudViewHandler<Product> {

  @Override
  public Class<Product> getModelClass() {
    return Product.class;
  }

  @Override
  public ValidationHelper<Product> getCreateValidator(String body) {
    return new ProductValidator(body);
  }
}
