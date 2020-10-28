package controllers;
import play.mvc.*;

public class Application extends Controller {
  public Result customRedirect(String name, int gameId) {
    if (name.equals("") || gameId < 0) {
      return redirect(controllers.routes.HomeController.showMenu());
    }
    return redirect(controllers.routes.HomeController.showGame(name, gameId));
  }

  public Result replayRedirect(String name, int gameId) {
    return redirect(controllers.routes.HomeController.showReplay(name, gameId));
  }
}
