package components;

public abstract class Movement extends GameBoardComponent {
  public Movement(String tileName) {
    super(ComponentType.MOVEMENT, tileName);
  }

  // Nothing happens here, movement is controlled by GameBoard
  public void interactWithHero(Hero hero, GameBoard game) {
  }

  // Cannot be hurt or destroyed from damage
  @Override
  public void takeDamage(int damage) {
  }

  // Expected override; nothing happens on default
  public void tictok(GameBoard game, int thisX, int thisY){
  }
}
