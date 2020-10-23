package components;

import javax.inject.Inject;
import play.mvc.*;
import play.libs.ws.*;
import play.Environment;
import java.util.concurrent.CompletionStage;

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

  public CompletionStage<String> getSave(String player, int gameId) {
    if (this.databaseURL == "") { return null; }
    String url = this.databaseURL + "/users/"
      + player +"/save/"+ gameId + ".json?orderBy=\"$key\"&limitToLast=1";
    WSRequest request = this.ws.url(url);
    return request.get().thenApply(r -> r.getBody());
  }

  public void pushSave(String player, int gameId, GameBoard board) {
    if (this.databaseURL == "") { return; }
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
}
