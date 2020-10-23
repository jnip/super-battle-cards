package components;

public class Path extends Movement {
  public int state;
  public int counter;

  public Path(int turnNum, int bossesDefeated) {
    super("Path");
    this.state = (Random.nextInt(1,5)<=2)?1:0; // 1 in 10
    this.counter = (this.state==0)?0:Random.nextInt(1,3);
  }

  // Load from database
  public Path(String data) {
    this(0, 0);
    DataInterface.apply(data, this);
  }

  // [allow travel, increment time]
  public boolean[] attemptTravelHere() {
    boolean canTravel = (this.state == 0);
    if (!canTravel) {
      this.counter--;
      if (this.counter <= 0) { this.state = 0; }
    }
    return new boolean[] {canTravel, true};
  }
}
