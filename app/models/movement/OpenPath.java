package components;

public class OpenPath extends Path {
  public OpenPath() {
    this(0,0);
  }

  public OpenPath(int turnNum, int bossesDefeated) {
    super(turnNum, bossesDefeated);
    this.state = 0;
    this.counter = 0;
  }
}
