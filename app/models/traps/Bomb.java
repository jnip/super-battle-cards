package components;

public class Bomb extends Trap {
  public int counter;
  private boolean pendingActivation = false;

  public Bomb(int turnNum, int bossesDefeated) {
    super("Bomb", (10+bossesDefeated)/2);
    this.counter = Random.nextInt(5,7);
  }

  public void tictok(GameBoard game, int thisX, int thisY) {
    this.counter--;
    if (this.counter == 0) {
      this.pendingActivation = true;
    }
  }

  public void afterTictok(GameBoard game, int thisX, int thisY) {
    if (pendingActivation) {
      // Get components to do damage to
      GameBoardComponent[] toDamage = this.getComponents(game, thisX, thisY);
      // Do damage
      for (int i = 0; i < toDamage.length; i++) {
        toDamage[i].takeDamage(this.value);
      }
      // Remove self from game
      this.isDestroyed = true;
    }
  }

  public GameBoardComponent[] getComponents(GameBoard game, int thisX, int thisY) {
    GameBoardComponent[] result = new GameBoardComponent[game.board.length + game.board[0].length - 2];
    int count = 0;
    for (int y = 0; y < game.board.length; y++) {
      if (thisY != y) {
        result[count++] = game.board[y][thisX];
      }
    }
    for (int x = 0; x < game.board[0].length; x++) {
      if (thisX != x) {
        result[count++] = game.board[thisY][x];
      }
    }
    return result;
  }

  public void interactWithHero(Hero hero, GameBoard game) {
  }
}
