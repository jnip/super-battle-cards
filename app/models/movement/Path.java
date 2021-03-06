package components;

public class Path extends Movement {
  public int state;
  public int counter;

  public Path(int turnNum, int bossesDefeated) {
    super("Path");
    this.state = (Random.nextInt(1,5)<=2)?1:0; // 1 in 10
    this.counter = (this.state==0)?0:Random.nextInt(1,3);
  }

  // [allow travel, increment time]
  @Override
  public boolean[] attemptTravelHere() {
    boolean canTravel = (this.state == 0);
    if (!canTravel) {
      this.counter--;
      if (this.counter <= 0) { this.state = 0; }
    }
    return new boolean[] {canTravel, true};
  }
}
