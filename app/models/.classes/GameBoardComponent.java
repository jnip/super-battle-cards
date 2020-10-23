package components;
import java.lang.reflect.*;
import java.lang.Class;

public class GameBoardComponent {
  public ComponentType type;
  public String name;
  public boolean isDestroyed = false;
  public boolean preventNextTictok = false;

  public static enum ComponentType {
    BOSS, MONSTER, TRAP,
    HERO, ITEM, SKILL,
    OBSTACLE, MOVEMENT
  }

  // Empty Component
  public GameBoardComponent() {
    this.type = ComponentType.OBSTACLE;
    this.name = "Empty";
  }

  public GameBoardComponent(ComponentType type, String name) {
    this.type = type;
    this.name = name;
  }

  // what happens after play takes a turn
  public void tictok(GameBoard game, int thisX, int thisY) {
  }

  // what happens after hero steps on this tile
  public void interactWithHero(Hero hero, GameBoard game) {
  }

  // Deprecated - no longer used; instead use JSON.stringify
  public String toJSON() {
    return JSON.stringify(this, GameBoardComponent.class);
  }

  // [allow travel, increment time]
  public boolean[] attemptTravelHere() {
    return new boolean[] {true, true};
  }

  // If subclass does not handle taking damage then it will be marked as destroyed when hurt
  public void takeDamage(int damage) {
    this.isDestroyed = true;
  }
}

/*
  Classes
    Living
      Heroes
      Bosses
      Monsters

    Non-living
      Traps
        - HP: reduce HP
        - !Indestructable: remove card
        
        removes skill instantly
        removes item instantly
        removes money instantly
        removes barrel

        hurts living things

        hurts/kill trap
        pusher not hurt
        movement/gate/corridor
      Items
      Skills
      Neutral
        Indestructable
*/
