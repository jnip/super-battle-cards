package components;
import play.libs.ws.*;
import javax.inject.Inject;

public class FirebaseDatabase extends Database {
  private String urlPrefix = "https://super-battle-cards.firebaseio.com";

  @Inject
  public FirebaseDatabase(WSClient ws) {
    super(ws, new FirebaseInterface());
  }

  public int saveNewGame(String player, GameBoard game) {
    int numGames = this.incrementGameCount(player);
    if (numGames < 0) { return -1; }
    int gameId = numGames - 1;
    this.updateSettings(player, gameId);
    this.forceUpdate(game);
    return gameId;
  }

  public boolean isReady() {
    if (urlPrefix.trim().equals("") || urlPrefix == null || player == null || gameId < 0) {
      return false;
    }
    return true;
  }

  String urlPUT(GameBoard game) {
    return urlPUTPrefix() + tenCharFormat(game.turn) + "_" + game.bossesKilled + ".json";
  }
  String urlGETGameCount(String player) {
    return urlPrefix + "/users/" + player + "/gameCount.json";
  }
  String urlGETForTurn(int index) {
    return urlGETSavePrefix() + "&startAt=\"move_" + tenCharFormat(index) + "_\"&limitToFirst=1";
  }
  String urlGET() {
    return urlGETSavePrefix() + "&limitToLast=1";
  }

  private String urlGETSavePrefix() {
    return urlPrefix + "/users/" + player +"/save/"+ gameId + ".json?orderBy=\"$key\"";
  }
  private String urlPUTPrefix() {
    return urlPrefix + "/users/" + player +"/save/"+ gameId + "/move_";
  }
  private String tenCharFormat(int index) {
    String indexString = ""+index;
    while (indexString.length() < 10) {
      indexString = "0" + indexString;
    }
    return indexString;
  }
  private int incrementGameCount(String player) {
    int gameCount = getGameCount(player);
    if (gameCount >= 0) {
      int newGameCount = gameCount + 1;
      ws.url(urlGETGameCount(player)).put(""+newGameCount);
      return newGameCount;
    }
    else {
      return -1;
    }
  }
}
