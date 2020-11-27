package components;

public class EmptyComponent extends GameBoardComponent {
  public EmptyComponent() {
    super(ComponentType.OBSTACLE, "Empty");
  }
  public void tictok(GameBoard game, int thisX, int thisY) {}
  public void interactWithHero(Hero hero, GameBoard game) {}
}
