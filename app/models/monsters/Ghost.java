package components;

public class Ghost extends Monster {
  public int counter;
  protected int maxCounter;

  public Ghost(int turnNum, int bossesDefeated) {
    super("Ghost", Random.nextInt(2,4) + bossesDefeated/5);
    this.maxCounter = (bossesDefeated < 10)?Random.nextInt(3,5):Random.nextInt(3,4);
    this.counter = this.maxCounter;
  }

  // Load from database
  public Ghost(String data) {
    this(0, 0);
    DataInterface.apply(data, this);
  }

  public void tictok(GameBoard game, int thisX, int thisY) {
    // If killed by poison, don't hurt Hero
    super.tictok(game, thisX, thisY);
    if (this.isDestroyed) { return; }

    this.counter--;
    if (this.counter == 0) {
      this.counter = this.maxCounter;
      game.hero.takeDamage(1);
    }
  }
}
