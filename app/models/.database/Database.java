package components;
import play.libs.ws.*;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.CompletableFuture;

public abstract class Database {
  abstract public int saveNewGame(String player, GameBoard game); //returns the id of the new game
  abstract String urlPUT(GameBoard game);
  abstract String urlGETGameCount(String player);
  abstract String urlGETForTurn(int index);
  abstract String urlGET();
  
  public String error = null;
  final WSClient ws;
  final DatabaseInterface dbI;
  GameBoard fetchedGame;
  String player;
  int gameId;
  
  public Database(WSClient ws, DatabaseInterface dbI) {
    this.ws = ws;
    this.dbI = dbI;
    this.fetchedGame = null;
    this.player = null;
    this.gameId = -1;
  }

  public void updateSettings(String player, int gameId) {
    this.player = player;
    this.gameId = gameId;
  }

  public CompletableFuture<WSResponse> update() {
    return this.update(false, this.fetchedGame);
  }
  public CompletableFuture<WSResponse> update(GameBoard game) {
    return this.update(false, game);
  }
  public CompletableFuture<WSResponse> forceUpdate(GameBoard game) {
    this.fetchedGame = game;
    return this.update(true, game);
  }

  public WSResponse updateAndWait() {
    return this.get(this.update());
  }
  public WSResponse updateAndWait(GameBoard game) {
    return this.get(this.update(game));
  }
  public WSResponse forceUpdateAndWait(GameBoard game) {
    return this.get(this.forceUpdate(game));
  }

  public int getGameCount(String player) {
    String dataString = this.fetch(this.urlGETGameCount(player));
    if (dataString == null) { return -1; }
    if (dataString.equals("null")) { return 0; }
    return Integer.parseInt(dataString);
  }

  public GameBoard fetchGame() {
    return this.fetchGame(-1);
  }
  public GameBoard fetchGame(int turnIndex) {
    // Network request
    String url = (turnIndex < 0)?this.urlGET():this.urlGETForTurn(turnIndex);
    String dataString = this.fetch(url);

    // Parse data string
    this.dbI.loadData(dataString);
    this.fetchedGame = (this.dbI.isDataValid)? 
      new GameBoard(this.dbI.getBoard(), this.dbI.getIndex(), this.dbI.getKills())
      : null;
    return this.fetchedGame;
  }

  /* ------------------------- */
  /* ---- Private Methods ---- */
  /* ------------------------- */
  private CompletableFuture<WSResponse> update(boolean forceUpdate, GameBoard game) {
    boolean shouldUpdate = game != null && ( forceUpdate || (game == this.fetchedGame && game.isDirty) );
    if (!shouldUpdate) {
      return null;
    }
    String data = this.dbI.gameToString(game);
    WSRequest request = this.ws.url(this.urlPUT(game));
    return (CompletableFuture<WSResponse>) request.put(data);
  }

  private WSResponse get(CompletableFuture<WSResponse> promise) {
    if (promise == null) {
      return null;
    }
    try {
      return promise.get();
    }
    catch (Exception e) {
      return null;
    }
  }

  private String fetch(String url) {
    try {
      WSRequest request = this.ws.url(url);
      CompletionStage<WSResponse> future = request.get();
      WSResponse response = ((CompletableFuture<WSResponse>) future).get();

      boolean isSuccess = (response.getStatus() >= 100 && response.getStatus() <= 299);
      if (isSuccess) {
        return response.getBody();
      }
      else {
        this.error = response.getBody();
        return null;
      }
    }
    catch (Exception e) {
      this.error = e.toString();
      return null;
    }
  }
}
