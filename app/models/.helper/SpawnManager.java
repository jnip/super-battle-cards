package components;
import java.lang.Class;
import java.lang.reflect.*;
import java.util.*;

public class SpawnManager {
  private enum Category {MOVEMENT, MONSTER, SKILL, ITEM, TRAP}
  private static int[] categoryOdds = {20, 37, 8, 20, 15}; //{28, 28, 5, 28, 11}, {20, 45, 5, 20, 10}
  private static int[] rewardCategoryOdds = {50, 0, 0, 50, 0}; //{70, 0, 0, 30, 0}
  private static Map<String, ?> map = Map.of(
    // Movement spawn rate
    "movementClasses", new Class[]{Gate.class, Path.class, Portal.class, Pusher.class},
    "movementOdds", new int[]{20, 50, 10, 20}, //{8, 80, 5, 7}
    // Monster spawn rate
    "monsterClasses", new Class[]{Skeleton.class, Mummy.class, Ghost.class},
    "monsterOdds", new int[]{55, 30, 15}, //{70, 20, 10}
    // Skill spawn rate
    "skillClasses", new Class[] {Arrow.class, Fireball.class, Poison.class},
    "skillOdds", new int[]{25, 50, 25}, //{33, 34, 33}
    // Item spawn rate
    "itemClasses", new Class[]{Money.class, Potion.class, Shield.class},
    "itemOdds", new int[]{40, 30, 30}, //{60, 20, 20}
    // Trap spawn rate
    "trapClasses", new Class[]{Bomb.class, Flamethrower.class, Fog.class, Spike.class},
    "trapOdds", new int[]{15, 15, 10, 60} //{20, 25, 10, 45}
  );
  private static Map<String, ?> rewardMap = Map.of(
    // Movement spawn rate
    "movementClasses", new Class[]{OpenPath.class},
    "movementOdds", new int[]{100},
    // Item spawn rate
    "itemClasses", new Class[]{Money.class, Potion.class, Shield.class},
    "itemOdds", new int[]{0, 50, 50} //{0, 50, 50}
  );

  public static Boss getBoss(int turn, int bossesDefeated) {
    if (Random.nextInt(1,10) <= 2) { return new EnragedBoss(turn, bossesDefeated); }
    return new DefaultBoss(turn, bossesDefeated);
  }

  public static GameBoardComponent getReward(int turn, int bossesDefeated, GameBoardComponent destroyedComponent) {
    // Only give reward to monster kills
    if (destroyedComponent instanceof Monster) {
      return getNext(turn, bossesDefeated, true);
    }
    // Otherwise return neutral tile
    return new OpenPath();
  }

  public static GameBoardComponent getNext(int turn, int bossesDefeated) {
    return getNext(turn, bossesDefeated, false);
  }

  private static GameBoardComponent getNext(int turn, int bossesDefeated, boolean isReward) {
    Class<? extends GameBoardComponent> _class;

    // get random Type of Component
    Category spawnType;
    do {
        spawnType = getRandom(Category.values(), (isReward)?rewardCategoryOdds:categoryOdds);
    } while(turn == 0 && (spawnType == Category.SKILL || spawnType == Category.TRAP)); // do not spawn skill nor traps tiles on board creation
    String type = spawnType.toString().toLowerCase();

    // get Class and Odds List for the selected type
    Map<String, ?> currMap = (isReward)? rewardMap : map;
    Class<? extends GameBoardComponent>[] classes = (Class<? extends GameBoardComponent>[]) currMap.get(type+"Classes");
    int[] odds = (int[]) currMap.get(type+"Odds");

    // get random Class from list
    _class = getRandom(classes, odds);

    // create and return Component
    try {
      return _class.getConstructor(int.class, int.class).newInstance(turn, bossesDefeated);
    }
    catch (Exception e) {
      System.out.println("Error at: SpawnManager.getNext");
      System.out.println(e);
      return null;
    }
  }

  // Assumes that sum of odds = 100
  private static <T> T getRandom(T[] key, int[] odds) {
    if (key == null || key.length == 0) { return null; }
    int sum = 0;
    int length = Math.min(key.length, odds.length);

    for (int i = 0; i < length; i++) {
      if ((100.0*odds[i])/(100-sum) >= Random.nextInt(1,100)) {
        return key[i];
      }
      sum += odds[i];
    }
    // Should not happen
    System.out.println("Error at: SpawnManager.getRandom");
    return key[0];
  }
}
