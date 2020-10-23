package components;

public class Fog extends Trap {
  public int counter;

  public Fog(int turnNum, int bossesDefeated) {
    super("Fog", Random.nextInt(4,7));
    this.counter = Random.nextInt(5,8);
  }

  // Load from database
  public Fog(String data) {
    this(0, 0);
    DataInterface.apply(data, this);
  }

  public void tictok(GameBoard game, int thisX, int thisY) {
    this.counter--;
    if (this.counter == 0) {
      // Remove from game
      this.isDestroyed = true;
    }
  }

  public void interactWithHero(Hero hero, GameBoard game) {
    // Do Later...
  }
}
