package components;

public class Spike extends Trap {
  public int state;

  public Spike(int turnNum, int bossesDefeated) {
    super("Spike", Random.nextInt(1, (bossesDefeated+10)/2));
    this.state = 0;
  }

  // Load from database
  public Spike(String data) {
    this(0, 0);
    DataInterface.apply(data, this);
  }

  public void tictok(GameBoard game, int thisX, int thisY) {
    this.state = (this.state == 0)?1:0;
  }

  public void interactWithHero(Hero hero, GameBoard game) {
    if (this.state == 1) {
      hero.takeDamage(this.value);
    }
  }
}
