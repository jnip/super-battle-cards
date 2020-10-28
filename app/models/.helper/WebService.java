package components;

import javax.inject.Inject;
import play.mvc.*;
import play.libs.ws.*;
import play.Environment;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.CompletableFuture;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class WebService implements WSBodyReadables, WSBodyWritables {
  private WSClient ws;
  private Environment environment;
  private String databaseURL = "";

  @Inject
  public WebService(WSClient ws, Environment environment) {
    this.ws = ws;
    this.environment = environment;

    // Read url of database from private folder
    try {
      File urlFile = environment.getFile("conf/private/databaseURL");
      Scanner scanner = new Scanner(urlFile);
      if (scanner.hasNextLine()) {
        this.databaseURL = scanner.nextLine().replaceAll("\\s+", "");
      }
      else {
        System.out.println("Error in app/models/.helper/WebService: Please set up url to database.");
      }
      scanner.close();
    }
    catch (FileNotFoundException e) {
      System.out.println("Error in app/models/.helper/WebService: Please set up url to database.");
    }
  }

  public CompletableFuture<String> getSave(String player, int gameId) {
    return this.getSave(player, gameId, -1);
  }

  public CompletableFuture<String> getSave(String player, int gameId, int moveNumber) {
    if (this.databaseURL.equals("")) { return null; }

    String urlStart = this.databaseURL + "/users/"
      + player +"/save/"+ gameId + ".json?orderBy=\"$key\"";
    String urlEnd = "&limitToLast=1";
    if (moveNumber >= 0) {
      String move = ""+moveNumber;
      while (move.length() < 10) {
        move = "0"+move;
      }
      urlEnd = "&startAt=\"move_"+move+"_\"&limitToFirst=1";
    }
    WSRequest request = this.ws.url(urlStart+urlEnd);
    return (CompletableFuture<String>)(request.get().thenApply(r -> r.getBody()));
  }

  public void pushSave(String player, int gameId, GameBoard board) {
    if (this.databaseURL.equals("")) { return; }
    int index = board.turn;
    int kills = board.bossesKilled;
    String formattedIndex = "" + index;
    while (formattedIndex.length() < 10) {
      formattedIndex = "0" + formattedIndex;
    }
    String url = this.databaseURL + "/users/"
      + player +"/save/"+ gameId +"/move_"+ formattedIndex +"_"+kills+".json";
    String data = board.toJSON(true);
    WSRequest request = this.ws.url(url);
    request.put(data);
  }

  public int getGameCount(String player) {
    if (this.databaseURL.equals("")) { return -1; }
    String url = this.databaseURL + "/users/" + player + "/gameCount.json";
    WSRequest request = this.ws.url(url);
    CompletionStage<String> responseBody = request.get().thenApply(r -> r.getBody());
    try {
      String value = ((CompletableFuture<String>)responseBody).get();
      if (value.equals("null")) { return 0; }
      return Integer.parseInt(value);
    }
    catch (Exception e) {
      return -1;
    }
  }

  private int incrementGameCount(String player) {
    if (this.databaseURL.equals("")) { return -1; }
    String url = this.databaseURL + "/users/" + player + "/gameCount.json";
    int gameCount = this.getGameCount(player);
    int newGameCount = (gameCount < 0)?1:gameCount+1;
    this.ws.url(url).put(""+newGameCount);
    return newGameCount;
  }

  public int saveNewGame(String player, GameBoard game) {
    int numGames = incrementGameCount(player);
    int gameId = numGames - 1;
    this.pushSave(player, gameId, game);
    return gameId;
  }
}
