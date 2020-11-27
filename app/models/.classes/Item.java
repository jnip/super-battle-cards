package components;

public abstract class Item extends GameBoardComponent {
  public int value;
  public int state; //Hidden in barrel

  public Item(String name, int value) {
    super(ComponentType.ITEM, name);
    this.value = value;
    this.state = (Random.nextInt(1,6)==1)?1:0;
  }

  // [allow travel, increment time]
  @Override
  public boolean[] attemptTravelHere() {
    boolean canTravel = (this.state == 0);
    if (!canTravel) {
      this.state = 0;
    }
    return new boolean[] {canTravel, true};
  }

  public void tictok(GameBoard game, int thisX, int thisY) {};
}
