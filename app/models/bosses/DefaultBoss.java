package components;

public class DefaultBoss extends Boss {
  public DefaultBoss(int turnNum, int bossesDefeated) {
     super("DefaultBoss", bossesDefeated+12);
  }

  // Load from database
  public DefaultBoss(String data) {
    this(0, 0);
    DataInterface.apply(data, this);
  }
}
