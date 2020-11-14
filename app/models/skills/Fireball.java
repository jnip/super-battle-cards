package components;
import java.util.Arrays;

public class Fireball extends Skill {
  public Fireball(int turnNum, int bossesDefeated) {
    super("Fireball", (10+bossesDefeated)/2);
  }

  public void interactWithHero(Hero hero, GameBoard game) {
    // Get hero location
    int[] heroLocation = findHero(hero, game);
    if (heroLocation == null) { return; }
    // Get surrounding tiles
    GameBoardComponent[] damageComponents = getSurroundingComponents(heroLocation, game);
    // Damage them all
    for (int i = 0; i < damageComponents.length; i++) {
      damageComponents[i].takeDamage(this.value);
    }
  }

  private int[] findHero(Hero hero, GameBoard game) {
    for (int x = 0; x < game.board[0].length; x++) {
      for (int y = 0; y < game.board.length; y++) {
        if (game.board[y][x] == hero) {
          return new int[]{x, y};
        }
      }
    }
    return null;
  }

  private GameBoardComponent[] getSurroundingComponents(int[] heroLocation, GameBoard game) {
    GameBoardComponent[] result = new GameBoardComponent[8];
    int x = heroLocation[0]; int y = heroLocation[1];
    int resultCount = 0;

    String[] locations = new String[]{ x+"_"+(y+1), x+"_"+(y-1),
      (x-1)+"_"+y, (x-1)+"_"+(y+1), (x-1)+"_"+(y-1),
      (x+1)+"_"+y, (x+1)+"_"+(y+1), (x+1)+"_"+(y-1) };

    for (int i = 0; i < locations.length; i++) {
      try {
        String[] xy = locations[i].split("_");
        result[resultCount] = game.board[Integer.parseInt(xy[1])][Integer.parseInt(xy[0])];
        resultCount++;
      }
      catch (ArrayIndexOutOfBoundsException e) {}
    }
    return Arrays.copyOfRange(result, 0, resultCount);
  }
}
