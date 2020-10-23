package components;

public class Skill extends GameBoardComponent {
  public int value;
  public int state; //Hidden in barrel

  public Skill(String name, int value) {
    super(ComponentType.SKILL, name);
    this.value = value;
    this.state = (Random.nextInt(1,6)==1)?1:0;
  }

  public void interactWithHero(Hero hero, GameBoard game) {
    // Nothing, GameBoard takes care of all Skill procs
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
