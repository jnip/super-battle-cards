package components;

public class Portal extends Movement {
  public Portal(int turnNum, int bossesDefeated) {
    super("Portal");
  }

  // Load from database
  public Portal(String data) {
    this(0, 0);
    DataInterface.apply(data, this);
  }
}
