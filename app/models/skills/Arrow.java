package components;

public class Arrow extends Skill {
  public Arrow(int turnNum, int bossesDefeated) {
    super("Arrow", (10+bossesDefeated)/3+Random.nextInt(0,2));
  }

  public void interactWithHero(Hero hero, GameBoard game) {
    for (var i = 0; i < game.board.length; i++) {
      for (var j = 0; j < game.board[0].length; j++) {
        if (game.board[i][j] instanceof Monster) {
          game.board[i][j].takeDamage(this.value);
        }
      }
    }
  }
}
