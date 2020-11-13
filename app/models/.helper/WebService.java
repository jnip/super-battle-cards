package components;

import javax.inject.Inject;
import database.*;

public class WebService {
  private Database db;

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
    }
    return game;
  }

  public void pushSave(boolean waitForCompletion) {
    if (waitForCompletion) {
      if (null == db.updateAndWait()) {
        // Failed to update
      }
    }
    else {
      if (null == db.update()) {
        // Failed to update
      }
    }
  }

  public int getGameCount(String player) {
    int gameCount = this.db.getGameCount(player);
    if (gameCount < 0) {
      // Failed to fetch game count
    }
    return gameCount;
  }

  public int saveNewGame(String player, GameBoard game) {
    int gameId = this.db.saveNewGame(player, game);
    if (gameId < 0) {
      // Failed to save
    }
    return gameId;
  }
}
