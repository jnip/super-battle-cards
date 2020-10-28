package controllers;

import play.mvc.*;
import java.util.Optional;
import components.*;
import javax.inject.Inject;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.CompletableFuture;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {
    public WebService ws;
    public Application ac;

    @Inject
    public HomeController(WebService ws, Application ac) {
      this.ws = ws;
      this.ac = ac;
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
        GameBoard board = this.getBoardFromFuture(ws.getSave(name, gameId));
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
        GameBoard board = this.getBoardFromFuture(ws.getSave(name, gameId, moveNum));
        return (board == null)?badRequest("No such game."):ok(board.toJSON());
      }
      catch (Exception e) {
        System.out.print("getBoard: ");
        System.out.println(e);
        return badRequest(e.toString());
      }
    }

    private GameBoard getBoardFromFuture(CompletableFuture<String> future) throws Exception {
      String data = future.get();
      DataInterface dataObj = new DataInterface(data);
      GameBoard board = null;
      if (dataObj.isValid) {
        board = new GameBoard(dataObj.getBoard(), dataObj.getIndex(), dataObj.getKills());
      }
      return board;
    }

    public Result showGame(Http.Request request, String name, int id) {
      return (this.isKindleRequest(request))? 
        ok(views.html.kindle.render()):
        ok(views.html.index.render());
    }

    public Result registerAction(String player, int gameId, String playerMove) {
        // Load game from database and Execute move
        CompletableFuture<String> promiseOfData = ws.getSave(player, gameId);
        if (promiseOfData == null) {
          return badRequest("Database not set up.");
        }
        CompletableFuture<GameBoard> promiseOfGame = promiseOfData.thenApply(body -> {
          DataInterface dataObj = new DataInterface(body);
          GameBoard board;
          // If missing or corrupted save
          if (!dataObj.isValid) {
            board = null;
          }
          // Otherwise: Load data and Execute move
          else {
            board = new GameBoard(dataObj.getBoard(), dataObj.getIndex(), dataObj.getKills());
            board.executeMove(playerMove);
          }
          return board;
        });
        
        try {
          GameBoard board = promiseOfGame.get();
          if (board == null) {
            return badRequest("No such game.");
          }
          // Save game
          if (board.isDirty) {
            ws.pushSave(player, gameId, board);
          }
          // Send updated board back
          return ok(board.toJSON());
        }
        catch (Exception e){
          System.out.println(e);
          return ok(e.toString());
        }
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
