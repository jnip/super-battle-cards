package components;
import javax.inject.Inject;

public class WebService {
  private Database db;
  public String latestError = null;

  @Inject
  public WebService(FirebaseDatabase db) {
    this.db = db;
  }

  public GameBoard getSave(String player, int gameId) {
    return this.getSave(player, gameId, -1);
  }

  public GameBoard getSave(String player, int gameId, int moveNumber) {
    db.updateSettings(player, gameId);
    GameBoard game = (moveNumber < 0)?db.fetchGame():db.fetchGame(moveNumber);
    if (null == game) {
      // Failed to fetch game save
      handleDatabaseError();
    }
    return game;
  }

  public void pushSave(boolean waitForCompletion) {
    if (waitForCompletion) {
      if (null == db.updateAndWait()) {
        // Failed to update
        handleDatabaseError();
      }
    }
    else {
      if (null == db.update()) {
        // Failed to update
        handleDatabaseError();
      }
    }
  }

  public int getGameCount(String player) {
    int gameCount = this.db.getGameCount(player);
    if (gameCount < 0) {
      // Failed to fetch game count
      handleDatabaseError();
    }
    return gameCount;
  }

  public int saveNewGame(String player, GameBoard game) {
    int gameId = this.db.saveNewGame(player, game);
    if (gameId < 0) {
      // Failed to save
      handleDatabaseError();
    }
    return gameId;
  }

  private void handleDatabaseError() {
    this.latestError = "Database Error: " + this.db.error;
  }
}
