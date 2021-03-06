package components;

public abstract class Trap extends GameBoardComponent {
  public int value;

  public Trap(String trapName, int trapValue) {
    super(ComponentType.TRAP, trapName);
    this.value = trapValue;
  }

  // Weaken traps when damage is dealt
  @Override
  public void takeDamage(int damage) {
    this.value -= damage;
    if (this.value <= 0) {
      this.isDestroyed = true;
    }
  }
}
