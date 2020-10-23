package components;

public class Monster extends GameBoardComponent {
  public int health;
  public int poisonCount = 0;

  public Monster(String name, int initialHP) {
    super(ComponentType.MONSTER, name);
    this.health = initialHP;
  }

  // Reduces HP
  public void takeDamage(int damage) {
    if (damage > 0) {
      this.health -= (damage > this.health)?this.health:damage;
    }
    if (this.health <= 0) {
      this.isDestroyed = true;
    }
  }

  public void takePoison(int poison) {
    if (poison > this.poisonCount) {
      this.poisonCount = poison;
    }
  }

  // Damage from poison
  public void tictok(GameBoard game, int thisX, int thisY) {
    if (poisonCount > 0) {
      poisonCount--;
      this.takeDamage(1);
    }
  }

  // Will always die when attacked by hero
  public void interactWithHero(Hero hero, GameBoard game) {
    int damageToHero = this.health;
    this.health = 0;
    hero.takeDamage(damageToHero);
    this.isDestroyed = true;
  }

  // Deprecated - no longer used; instead use JSON.stringify
  public String toJSON() {
    String monsterJSON = JSON.stringify(this, Monster.class);
    return JSON.merge(monsterJSON, super.toJSON());
  }
}
