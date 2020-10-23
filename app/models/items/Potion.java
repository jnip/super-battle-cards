package components;

public class Potion extends Item {
  public Potion(int turnNum, int bossesDefeated) {
    super("Potion", bossesDefeated/2+Random.nextInt(3,7));
  }

  // Load from database
  public Potion(String data) {
    this(0, 0);
    DataInterface.apply(data, this);
  }

  public void interactWithHero(Hero hero, GameBoard game) {
    int healing = this.value;
    this.value = 0;
    hero.takeHealing(healing);
  }
}
