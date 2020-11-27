package components;

public class Gate extends Movement {
  public int state;
  public int counter;

  public Gate(int turnNum, int bossesDefeated) {
    super("Gate");
    this.state = 1;
    this.counter = Random.nextInt(2,5);
  }

  @Override
  public void tictok(GameBoard game, int thisX, int thisY) {
    if (this.state == 1) {
      this.counter--;
      if (this.counter == 0) {
        this.state = 0;
      }
    }
  }

  // [allow travel, increment time]
  @Override
  public boolean[] attemptTravelHere() {
    boolean canTravel = (this.state == 0);
    return new boolean[] {canTravel, canTravel};
  }
}
