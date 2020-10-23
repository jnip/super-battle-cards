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

    @Inject
    public HomeController(WebService ws) {
      this.ws = ws;
    }

    public Result index(Http.Request request, String name, int id) {
        // Send HTML
        Optional<String> userAgent = request.getHeaders().get("User-Agent");
        Boolean fromKindle = false;
        if (userAgent.isPresent()) {
          fromKindle = userAgent.get().contains("X11; U; Linux armv7l like Android;");
        }
        
        return (fromKindle)? 
          ok(views.html.kindle.render()):
          ok(views.html.index.render());
    }

    public Result nextMove(String player, int gameId, String playerMove) {
        // Load game from database and Execute move
        CompletionStage<String> promiseOfData = ws.getSave(player, gameId);
        if (promiseOfData == null) {
          return badRequest("Database not set up.");
        }
        CompletableFuture<GameBoard> promiseOfGame = (CompletableFuture<GameBoard>) promiseOfData.thenApply(body -> {
          DataInterface dataObj = new DataInterface(body);
          GameBoard board;
          // If missing or corrupted save: Create new game
          if (!dataObj.isValid) {
            board = new GameBoard();
          }
          // Otherwise: Load data and Execute move
          else {
            board = new GameBoard(dataObj.getBoard(), dataObj.getIndex(), dataObj.getKills());
            board.executeMove(playerMove);
          }
          return board;
        });
        CompletableFuture<Result> promiseOfResult = promiseOfGame.thenApply(
          board -> ok(board.toJSON()));

        try {
          // Save game
          GameBoard board = promiseOfGame.get();
          if (board.isDirty) {
            ws.pushSave(player, gameId, board);
          }

          // Send updated board back
          return promiseOfResult.get();
        }
        catch (Exception e){
          System.out.println(e);
          return ok(e.toString());
        }
    }
}
