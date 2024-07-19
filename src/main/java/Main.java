import app.db.Db;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import java.sql.SQLException;
import web.Routes;

@SuppressWarnings("DefaultPackage")
public class Main {

  public static void main(String[] args) throws SQLException {
    Db.init();
    Javalin javalin =
        Javalin.create(
            config -> {
              config.staticFiles.add(
                  staticFiles -> {
                    staticFiles.hostedPath =
                        "/public"; // change to host files on a subpath, like '/assets'
                    staticFiles.directory =
                        "./src/main/java"; // this exposes ALL files in the web folder so a
                    // different location would be in order
                    staticFiles.location = Location.EXTERNAL;
                  });
            });

    Routes.init(javalin).start(4000);
  }
}
