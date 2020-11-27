package components;

public class Skeleton extends Monster {
  public Skeleton(int turnNum, int bossesDefeated) {
    super("Skeleton", 
      (10+bossesDefeated)/2
      + Random.nextInt(-1*(10+bossesDefeated)/4, 0)
      + turnNum/75);
  }
}
