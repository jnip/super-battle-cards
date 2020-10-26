package components;

public class EnragedBoss extends Boss {
  public EnragedBoss(int turnNum, int bossesDefeated) {
     super("EnragedBoss", (bossesDefeated+12)*2);
  }

  // Load from database
  public EnragedBoss(String data) {
    this(0, 0);
    DataInterface.apply(data, this);
  }
}
