package controllers;
import play.mvc.*;

public class Application extends Controller {
  public Result customRedirect(String name, int gameId) {
    //return redirect("https://www.playframework.com/");
    return redirect(controllers.routes.HomeController.index(name, gameId));
  }
}
