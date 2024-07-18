package web.utils;

import static web.utils.ViewHelpers.getFieldValue;

import io.javalin.json.JavalinJackson;
import io.javalin.validation.BodyValidator;
import kotlin.jvm.functions.Function1;

public abstract class ValidationHelper<T> extends BodyValidator {

  public ValidationHelper(String body, Class<T> clazz) {
    super(body, clazz, new JavalinJackson());
  }

  public static final String NULL_NOT_EMPTY_MESSAGE = "Cannot be empty";

  public static Function1<Object, Boolean> notNullOrEmpty(String field) {
    return it -> getFieldValue(it, field) != null && getFieldValue(it, field) != "";
  }

  public abstract T validate();
}
