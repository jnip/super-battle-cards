package components;

public class Ghost extends Monster {
  public int counter;
  protected int maxCounter;
  protected boolean pendingAttack = false;

  public Ghost(int turnNum, int bossesDefeated) {
    super("Ghost", Random.nextInt(2,4) + bossesDefeated/5);
    this.maxCounter = (bossesDefeated < 10)?Random.nextInt(3,5):Random.nextInt(3,4);
    this.counter = this.maxCounter;
  }

  @Override
  public void tictok(GameBoard game, int thisX, int thisY) {
    super.tictok(game, thisX, thisY);
    this.counter--;
    if (this.counter == 0) {
      this.counter = this.maxCounter;
      this.pendingAttack = true;
    }
  }

  @Override
  public void afterTictok(GameBoard game, int thisX, int thisY) {
    if (this.isDestroyed) { return; }
    if (this.pendingAttack) {
      this.pendingAttack = false;
      game.hero.takeDamage(1);
    }
  }
}
