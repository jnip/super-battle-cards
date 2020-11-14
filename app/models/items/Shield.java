package components;

public class Shield extends Item {
  public Shield(int turnNum, int bossesDefeated) {
    super("Shield", (bossesDefeated/2+Random.nextInt(3,7))/2+1);
  }

  public void interactWithHero(Hero hero, GameBoard game) {
    int shield = this.value;
    this.value = 0;
    hero.takeShield(shield);
  }
}
