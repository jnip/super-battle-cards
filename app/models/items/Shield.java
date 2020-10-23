package components;

public class Shield extends Item {
  public Shield(int turnNum, int bossesDefeated) {
    super("Shield", (bossesDefeated/2+Random.nextInt(3,7))/2+1);
  }

  // Load from database
  public Shield(String data) {
    this(0, 0);
    DataInterface.apply(data, this);
  }

  public void interactWithHero(Hero hero, GameBoard game) {
    int shield = this.value;
    this.value = 0;
    hero.takeShield(shield);
  }
}
