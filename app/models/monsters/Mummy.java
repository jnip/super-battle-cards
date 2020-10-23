package components;

public class Mummy extends Monster {
  public int counter;
  public int state;

  protected int maxCounter;
  protected int maxHealth;

  public Mummy(int turnNum, int bossesDefeated) {
    super("Mummy", 
      Random.nextInt(5,7)
      + bossesDefeated/Random.nextInt(1,3)
      + Math.max(turnNum/50,3));
    this.state = 1;
    this.maxHealth = this.health;
    this.maxCounter = 4;
    this.counter = this.maxCounter;
  }

  // Load from database
  public Mummy(String data) {
    this(0, 0);
    DataInterface.apply(data, this);
  }

  public void tictok(GameBoard game, int thisX, int thisY) {
    this.counter--;

    // Toggle state, health update
    if (this.counter == 0) {
      this.counter = this.maxCounter;
      this.state = (this.state == 0)?1:0;
      boolean growStronger = this.state == 1;
      if (growStronger) {
        this.health = this.health*3;
      }
      else {
        this.health = (int)Math.ceil(this.health/3.0);
      }
      if (this.health > this.maxHealth) {
        this.health = this.maxHealth;
      }
    }

    // Take poison damage after changing state
    super.tictok(game, thisX, thisY);
  }
}
