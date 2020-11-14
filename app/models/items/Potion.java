package components;

public class Potion extends Item {
  public Potion(int turnNum, int bossesDefeated) {
    super("Potion", bossesDefeated/2+Random.nextInt(3,7));
  }
  
  public void interactWithHero(Hero hero, GameBoard game) {
    int healing = this.value;
    this.value = 0;
    hero.takeHealing(healing);
  }
}
