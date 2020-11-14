package components;

public class Money extends Item {
  public Money(int turnNum, int bossesDefeated) {
    super("Money", Random.nextInt(1,10));
    this.state = 0; // Money does not appear in barrels
  }

  public void interactWithHero(Hero hero, GameBoard game) {
    hero.money += this.value;
  }
}
