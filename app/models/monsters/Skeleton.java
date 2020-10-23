package components;

public class Skeleton extends Monster {
  public Skeleton(int turnNum, int bossesDefeated) {
    super("Skeleton", 
      (10+bossesDefeated)/2
      + Random.nextInt(-1*(10+bossesDefeated)/4, 0)
      + turnNum/75);
  }

  // Load from database
  public Skeleton(String data) {
    this(0, 0);
    DataInterface.apply(data, this);
  }

  // Deprecated - no longer used; instead use JSON.stringify
  public String toJSON() {
    String skeletonJSON = JSON.stringify(this, Skeleton.class);
    return JSON.merge(skeletonJSON, super.toJSON());
  }
}
