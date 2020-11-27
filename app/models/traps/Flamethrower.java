package components;

public class Flamethrower extends Trap {
  public int state; // orientation: 0 = horizontal
  public int counter;
  protected int maxCounter;
  protected boolean pendingActivation = false;

  public Flamethrower(int turnNum, int bossesDefeated) {
    super("Flamethrower", (10+bossesDefeated)/2);
    this.state = 0;
    this.maxCounter = Random.nextInt(2,4);
    this.maxCounter = (this.maxCounter == 4)?4:3;
    this.counter = this.maxCounter;
  }

  public void tictok(GameBoard game, int thisX, int thisY) {
    this.counter--;
    this.state = (this.state == 0)?1:0;
    if (this.counter == 0) {
      this.counter = this.maxCounter;
      this.pendingActivation = true;      
    }
  }

  public void afterTictok(GameBoard game, int thisX, int thisY) {
    if (this.pendingActivation) {
      this.pendingActivation = false;
      GameBoardComponent[] targets = this.findTargets(game, thisX, thisY);
      for (int i = 0; i < targets.length; i++) {
        targets[i].takeDamage(this.value);
      }
    }
  }

  public GameBoardComponent[] findTargets(GameBoard game, int thisX, int thisY) {
    GameBoardComponent[] result = new GameBoardComponent[2];
    int count = 0;
    if (this.state == 0) {
      if (thisX - 1 >= 0) {
        result[count++] = game.board[thisY][thisX-1];
      }
      if (thisX + 1 < game.board[0].length) {
        result[count++] = game.board[thisY][thisX+1];
      }
    }
    else {
      if (thisY - 1 >= 0) {
        result[count++] = game.board[thisY-1][thisX];
      }
      if (thisY + 1 < game.board.length) {
        result[count++] = game.board[thisY+1][thisX];
      }
    }
    return java.util.Arrays.copyOfRange(result, 0, count);
  }

  public void interactWithHero(Hero hero, GameBoard game) {
  }
}
