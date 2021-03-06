package components;

public class Fog extends Trap {
  public int counter;

  public Fog(int turnNum, int bossesDefeated) {
    super("Fog", Random.nextInt(4,7));
    this.counter = Random.nextInt(5,8);
  }

  public void tictok(GameBoard game, int thisX, int thisY) {
    this.counter--;
    if (this.counter == 0) {
      // Remove from game
      this.isDestroyed = true;
    }
  }

  public void interactWithHero(Hero hero, GameBoard game) {
    if (this.value >= hero.fogCounter) {
      // interaction happens before incrementing time, so value + 1
      hero.fogCounter = this.value + 1;
    }
  }
}
