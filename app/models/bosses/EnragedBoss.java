package components;

public class EnragedBoss extends Boss {
  public EnragedBoss(int turnNum, int bossesDefeated) {
     super("EnragedBoss", (bossesDefeated+12)*2);
  }
}
