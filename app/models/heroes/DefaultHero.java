package components;

public class DefaultHero extends Hero {
  public DefaultHero(int turnNum, int bossesDefeated) {
    super("DefaultHero", 10);
  }

  // Load from database
  public DefaultHero(String data) {
    this(0, 0);
    DataInterface.apply(data, this);
  }
}
