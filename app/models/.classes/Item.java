package components;

public class Item extends GameBoardComponent {
  public int value;
  public int state; //Hidden in barrel

  public Item(String name, int value) {
    super(ComponentType.ITEM, name);
    this.value = value;
    this.state = (Random.nextInt(1,6)==1)?1:0;
  }

  // [allow travel, increment time]
  public boolean[] attemptTravelHere() {
    boolean canTravel = (this.state == 0);
    if (!canTravel) {
      this.state = 0;
    }
    return new boolean[] {canTravel, true};
  }
}
