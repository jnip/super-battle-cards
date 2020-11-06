package components;

public class Money extends Item {
  public Money(int turnNum, int bossesDefeated) {
    super("Money", Random.nextInt(1,10));
    this.state = 0; // Money does not appear in barrels
  }

  // Load from database
  public Money(String data) {
    this(0, 0);
    DataInterface.apply(data, this);
  }

  public void interactWithHero(Hero hero, GameBoard game) {
    hero.money += this.value;
  }
}
