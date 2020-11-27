package components;
import java.lang.reflect.*;
import java.lang.Class;

public abstract class GameBoardComponent {
  public ComponentType type;
  public String name;
  public boolean isDestroyed = false;
  public boolean preventNextTictok = false;

  public static enum ComponentType {
    BOSS, MONSTER, TRAP,
    HERO, ITEM, SKILL,
    OBSTACLE, MOVEMENT
  }

  public GameBoardComponent(ComponentType type, String name) {
    this.type = type;
    this.name = name;
  }

  // tictok is called on all components after each hero action/movement
  abstract public void tictok(GameBoard game, int thisX, int thisY);
  public void beforeTictok(GameBoard game, int thisX, int thisY){};
  public void afterTictok(GameBoard game, int thisX, int thisY){};

  // interaction happens when the hero successfully moves into this component
  abstract public void interactWithHero(Hero hero, GameBoard game);

  // called when hero attempts to move into this component
  public boolean[] attemptTravelHere() {
    boolean allowTravel = true;
    boolean incrementTime = true; // call tictok on all components
    return new boolean[] {allowTravel, incrementTime};
  }

  public void takeDamage(int damage) {
    this.isDestroyed = true;
  }
}
