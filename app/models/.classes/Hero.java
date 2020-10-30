package components;

public class Hero extends GameBoardComponent {
  public int maxHealth;
  public int health;
  public int armour = 0;
  public int money = 0;
  public int stepsWalked = 0;
  public int bossesKilled = 0;
  public int monstersKilled = 0;
  public int timeSinceBossKill = 0;
  public int fogCounter = 0;
  private boolean invulnerable = false;

  public Hero(String name, int startingHP) {
    super(ComponentType.HERO, name);
    this.maxHealth = startingHP;
    this.health = this.maxHealth;
  }

  public void killedBoss() {
    this.bossesKilled++;
    this.maxHealth++;
    this.timeSinceBossKill = 0;
  }

  public void killedEnragedBoss() {
    int recovery = this.maxHealth/2;
    int damageSustained = this.maxHealth - this.health;
    int shieldReward = recovery - damageSustained;

    this.bossesKilled += 5;
    this.maxHealth += 5;
    this.timeSinceBossKill = 0;

    this.health = this.maxHealth;
    if (shieldReward > 0) { this.armour += shieldReward; }
  }

  public void killedMonster() {
    this.monstersKilled++;
  }

  public void interactWith(GameBoardComponent component, GameBoard game) {
    component.interactWithHero(this, game);
  }

  public void invulnerableInteract(GameBoardComponent component, GameBoard game) {
    this.invulnerable = true;
    this.interactWith(component, game);
    this.invulnerable = false;
  }

  public void takeDamage(int damage) {
    if (this.invulnerable) {
      return;
    }
    this.armour -= damage;
    if (this.armour < 0) {
      this.health += this.armour;
      this.armour = 0;
    }
    if (this.health < 0) {
      // GAME OVER!!!
    }
  }

  public void takeHealing(int heal) {
    this.health += heal;
    if (this.health > this.maxHealth) {
      this.health = this.maxHealth;
    }
  }

  public void takeShield(int shield) {
    if (shield > this.armour) {
      this.armour = shield;
    } 
    else {
      this.armour++;
    }
  }

  // [allow travel, increment time]
  public boolean[] attemptTravelHere() {
    return new boolean[] {false, false};
  }

  public void tictok(GameBoard game, int thisX, int thisY) {
    this.stepsWalked++;
    this.timeSinceBossKill++;
    if (this.fogCounter > 0) {
      this.fogCounter--;
    }
  }
}
