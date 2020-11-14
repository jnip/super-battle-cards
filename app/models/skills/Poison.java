package components;

public class Poison extends Skill {
  public Poison(int turnNum, int bossesDefeated) {
    super("Poison", (10+bossesDefeated)/3);
  }

  public void interactWithHero(Hero hero, GameBoard game) {
    for (int i = 0; i < game.board.length; i++) {
      for (int j = 0; j < game.board[0].length; j++) {
        if (game.board[i][j] instanceof Monster) {
          ((Monster)(game.board[i][j])).takePoison(this.value);
        }
      }
    }
  }
}
