package components;

public class Movement extends GameBoardComponent {
  public Movement(String tileName) {
    super(ComponentType.MOVEMENT, tileName);
  }

  // Nothing happens here, movement is controlled by GameBoard
  public void interactWithHero(Hero hero, GameBoard game) {
  }

  // [allow travel, increment time]
  public boolean[] attemptTravelHere() {
    return new boolean[] {true, true};
  }

  // Cannot be hurt or destroyed from damage
  public void takeDamage(int damage) {
  }
}
