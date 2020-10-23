package components;

public class Pusher extends Movement {
  public int state;

  public Pusher(int turnNum, int bossesDefeated) {
    super("Pusher");
    this.state=0;
  }

  // Load from database
  public Pusher(String data) {
    this(0, 0);
    DataInterface.apply(data, this);
  }

  public void tictok(GameBoard game, int thisX, int thisY) {
    this.state++;
    if (this.state==4) { this.state=0; }
  }

  public String toString() {
    switch (state) {
      case 0:
        return "right";
      case 1:
        return "down";
      case 2:
        return "left";
      case 3:
        return "up";
      default:
        return "?";
    }
  }
}
