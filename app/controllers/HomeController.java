package controllers;

import javax.inject.Inject;
import java.util.Optional;
import play.mvc.*;
import components.*;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {
    public WebService ws;

    @Inject
    public HomeController(WebService ws) {
      this.ws = ws;
    }

    public Result showMenu(Http.Request request) {
      String css = "/assets/stylesheets/";
      css += (this.isKindleRequest(request))?"menuKindle.css":"menu.css";
      return ok(views.html.menu.render(css));
    }

    public Result newGame(String player) {
      GameBoard board = new GameBoard();
      int newGameId = ws.saveNewGame(player, board);
      return redirect(controllers.routes.HomeController.showGame(player, newGameId));
    }

    public Result showLatestGame(String player) {
      int gameId = this.ws.getGameCount(player) - 1;
      if (gameId < 0) { return redirect(controllers.routes.HomeController.newGame(player)); }
      return redirect(controllers.routes.HomeController.showGame(player, gameId));
    }

    public Result showReplay(Http.Request request, String name, int id) {
      return (this.isKindleRequest(request))? 
        ok(views.html.replayKindle.render()):
        ok(views.html.replay.render());
    }

    public Result getGameCount(String name) {
      return ok(""+ws.getGameCount(name));
    }

    public Result getMoveCount(String name, int gameId) {
      try {
        GameBoard board = ws.getSave(name, gameId);
        return (board == null)?badRequest("No such game."):ok(""+board.turn);
      }
      catch (Exception e) {
        System.out.print("getMoveCount: ");
        System.out.println(e);
        return badRequest(e.toString());
      }
    }

    public Result getBoard(String name, int gameId, int moveNum) {
      try {
        GameBoard board = ws.getSave(name, gameId, moveNum);
        return (board == null)?badRequest("No such game."):ok(board.toJSON());
      }
      catch (Exception e) {
        System.out.print("getBoard: ");
        System.out.println(e);
        return badRequest(e.toString());
      }
    }

    public Result showGame(Http.Request request, String name, int id) {
      return (this.isKindleRequest(request))? 
        ok(views.html.kindle.render()):
        ok(views.html.index.render());
    }

    public Result registerAction(String player, int gameId, String playerMove) {
        // Load game from database
        GameBoard game = ws.getSave(player, gameId);
        if (game == null) {
          return badRequest("No such game.");
        }
        // Execute move. Save to database and return result
        game.executeMove(playerMove);
        if (game.isDirty) {
          ws.pushSave(false);
        }
        return ok(game.toJSON());
    }

    private boolean isKindleRequest(Http.Request request) {
      Optional<String> userAgent = request.getHeaders().get("User-Agent");
      Boolean fromKindle = false;
      if (userAgent.isPresent()) {
        fromKindle = userAgent.get().contains("X11; U; Linux armv7l like Android;");
      }
      return fromKindle;
    }
}
