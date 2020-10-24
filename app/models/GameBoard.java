package components;
import java.lang.Class;
import java.util.Optional;

public class GameBoard {
  public GameBoardComponent nextTile;
  public GameBoardComponent[][] board;
  public int turn = 0;
  public int bossesKilled = 0;
  private enum Move {UP, DOWN, RIGHT, LEFT}

  public Hero hero;
  public boolean isDirty = true;
  private int bossCounter;
  private int width = 5;
  private int height = 5; 

  // Load game
  public GameBoard(GameBoardComponent[][] board, int turn, int kills) {
    this.board = board;
    this.turn = turn;
    this.bossesKilled = kills;
    this.isDirty = false;
    this.height = board.length;
    this.width = board[0].length;
    int[] heroLocation = this.findHero();
    this.hero = (Hero)this.board[heroLocation[1]][heroLocation[0]];
    this.bossCounter = 10-this.hero.timeSinceBossKill;
  }

  // New game
  public GameBoard() {
    int size = 5;
    board = new GameBoardComponent[size][size];
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        this.board[i][j] = SpawnManager.getNext(turn, bossesKilled);
      }
    }
    this.hero = new DefaultHero(turn, bossesKilled);
    this.board[size/2][size/2] = this.hero;
    this.bossCounter = 10;
  }

  public String toJSON() {
    return this.toJSON(false);
  }

  public String toJSON(boolean includeProtected) {
    String result = "[";
    for (var i = 0; i < this.board.length; i++) {
      result += "[";
      for (var j = 0; j < this.board.length; j++) {
        result += JSON.stringify(this.board[i][j], includeProtected);
        result += ", ";
      }
      result = result.substring(0, result.length()-2);
      result += "], ";
    }
    result = result.substring(0, result.length()-2);
    result += "]";
    return result;
  }

  public boolean executeMove(String move) {
    return this.executeMove(move, null);
  }

  public boolean executeMove(String move, int[] teleportLocation) {
    // Move hero and associated tiles
    GameBoardComponent removedComponent = this.travel(move, teleportLocation);
    if (removedComponent == null) { return false; }

    // Fill empty tile left by removedComponent
    int[] empty = this.findEmpty();
    if (empty != null) {
      if (bossCounter <= 1 && this.findBoss() == null && !(removedComponent instanceof Boss)) {
        this.board[empty[1]][empty[0]] = SpawnManager.getBoss(turn, bossesKilled);
      }
      else {
        this.board[empty[1]][empty[0]] = SpawnManager.getNext(turn, bossesKilled);
      }
      this.board[empty[1]][empty[0]].preventNextTictok = true; // Don't increment time on newly created Component
    }

    // Touched the Pusher: Travel again if possible
    if (removedComponent instanceof Pusher) {
      String nextMove = removedComponent.toString();
      boolean travelledAgain = executeMove(nextMove);
      // Travel was a succuess: done!
      if (travelledAgain) { 
        return true;
      }
      // Continue execution if failure
    }

    // Touched the Portal: Travel again via Teleportion
    if (removedComponent instanceof Portal) {
      // Find suitable location
      int x, y, xTravel, yTravel;
      int[] heroPosition = this.findHero();

      int numTries = 0;
      do {
        // Find a valid teleportation location
        do {
          x = Random.nextInt(0,width-1);
          y = Random.nextInt(0,height-1);
          xTravel = Math.abs(heroPosition[0] - x);
          yTravel = Math.abs(heroPosition[1] - y);
          removedComponent = this.board[y][x];
        } while (xTravel == yTravel || removedComponent instanceof Boss || removedComponent instanceof Hero);
        
        // Try to make teleportation advantageous
        boolean advantageous = removedComponent instanceof Monster;
        advantageous = ((removedComponent instanceof Item) && !(removedComponent instanceof Money)) || advantageous;
        advantageous = (removedComponent instanceof Skill) || advantageous;
        if (advantageous) { break; }
      } while (numTries++ < 10);

      // Teleport Hero
      String nextMove = (xTravel > yTravel)?((x-heroPosition[0] > 0)?"right":"left"):((y-heroPosition[1] > 0)?"down":"up");
      int[] nextTeleportLocation = {x, y};
      return executeMove(nextMove, nextTeleportLocation);
    }

    // Interact with removedComponent (the one the hero walked into)
    if (!(removedComponent instanceof Poison)) {
      if (teleportLocation != null) {
        // Hero teleportion cannot be harmful
        this.hero.invulnerableInteract(removedComponent, this);
      } else {
        // Hero interaction with removedComponent
        this.hero.interactWith(removedComponent, this);
      }
    }

    // Add new components to replace the destroyed ones (due to Hero skill)
    for (int x = 0; x < this.width; x++) {
      for (int y = 0; y < this.height; y++) {
        if (this.board[y][x].isDestroyed) {
          this.registerKill(this.board[y][x]);
          this.board[y][x] = SpawnManager.getReward(turn, bossesKilled, this.board[y][x]);
        }
      }
    }

    // Move time forward
    this.tictok();

    // Touched Poison Skill: Interaction is delayed till after tictok
    if (removedComponent instanceof Poison) {
      this.hero.interactWith(removedComponent, this);
    }

    // Register hero kill - also counting kills by traps or skills (see below)
    this.registerKill(removedComponent);

    // Add new components to replace the destroyed ones (due to time moving forward)
    for (int x = 0; x < this.width; x++) {
      for (int y = 0; y < this.height; y++) {
        if (this.board[y][x].isDestroyed) {
          this.registerKill(this.board[y][x]);
          this.board[y][x] = SpawnManager.getReward(turn, bossesKilled, this.board[y][x]);
        }
      }
    }
  
    // Successful execution, mark for database update
    this.isDirty = true;
    return true;
  }

  private void registerKill(GameBoardComponent component) {
    if (component instanceof Boss) {
      this.hero.killedBoss();
      this.bossesKilled++;
    }
    else if (component instanceof Monster) {
      this.hero.killedMonster();
    }
  }

  private void tictok() {
    this.bossCounter--;
    for (int i = 0; i < this.height; i++) {
      for (int j = 0; j < this.width; j++) {
        if (this.board[i][j].preventNextTictok) {
          this.board[i][j].preventNextTictok = false;
        }
        else {
          this.board[i][j].tictok(this, j, i);
        }
      }
    }
    this.turn++;
  }



  // returns the gameComponent that the hero moved into
  private GameBoardComponent travel(String move) {
    return this.travel(move, null);
  }

  private GameBoardComponent travel(String move, int[] teleportLocation) {
    // Get Hero location
    int[] heroLocation = findHero();
    if (heroLocation == null) { return null; }
    int x, y, newX, newY;
    x = newX = heroLocation[0];
    y = newY = heroLocation[1];

    // Get direction of move and new location coordinates
    Move moveDirection;
    switch (move.toLowerCase()) {
      case "up":
        moveDirection = Move.UP;
        newY = y-1;
        break;
      case "down":
        moveDirection = Move.DOWN;
        newY = y+1;
        break;
      case "left":
        moveDirection = Move.LEFT;
        newX = x-1;
        break;
      case "right":
        moveDirection = Move.RIGHT;
        newX = x+1;
        break;
      default:
        return null;
    }

    // Override destination for teleportation
    if (teleportLocation != null) {
      newX = teleportLocation[0];
      newY = teleportLocation[1];
    }
    else {
      // Make sure we are not moving out of bounds
      if (newY < 0 || newX < 0) { return null; }
      if (newY >= this.board.length || newX >= this.board[0].length) { return null; }

      // Manage special cases that prevent walking to new location
      // e.g. barrels, gates and barricades
      boolean[] travelPermission;
      GameBoardComponent component = this.board[newY][newX];
      if (component == null) { travelPermission = new boolean[] {true, true}; }
      else { travelPermission = component.attemptTravelHere(); }
      boolean canTravel = travelPermission[0];
      boolean incrementTime = travelPermission[1];
      // To prevent component from being consumed, we pass back an empty GameBoardComponent
      if (!canTravel) { return (incrementTime)?new GameBoardComponent():null; }
    }

    // Move Hero and get the existing object at the location coordinates
    GameBoardComponent removedComponent = this.board[newY][newX];
    this.board[newY][newX] = this.board[y][x];

    // Shift other tiles to fill Hero's previous location
    if (this.isEdge(x, y)) {
      this.shiftTilesToEdge(x, y, moveDirection);
    }
    else {
      this.shiftTilesTo(x, y, moveDirection);
    }

    return removedComponent;
  }

  // For tile movement
  private boolean isEdge(int x, int y) {
    if (x == 0 || y == 0) return true;
    if (x == this.board[0].length-1 || y == this.board.length-1) return true;
    return false;
  }

  // For tile movement
  private void shiftTilesToEdge(int x, int y, Move direction) {
    // Top-left corner
    if (x == 0 && y == 0) {
      if (direction == Move.RIGHT) { this.shiftTilesUp(x, y); }
      else { this.shiftTilesLeft(x, y); }
    }
    // Top-right corner
    else if (x == this.board[0].length-1 && y == 0) {
      if (direction == Move.LEFT) { this.shiftTilesUp(x, y); }
      else { this.shiftTilesRight(x, y); }
    }
    // Bottom-left corner
    else if (x == 0 && y == this.board.length-1) {
      if (direction == Move.RIGHT) { this.shiftTilesDown(x, y); }
      else { this.shiftTilesLeft(x, y); }
    }
    // Bottom-right corner
    else if (x == this.board[0].length-1 && y == this.board.length-1) {
      if (direction == Move.LEFT) { this.shiftTilesDown(x, y); }
      else { this.shiftTilesRight(x, y); }
    }
    
    // If Hero/component is still on the edge, special move
    else {
      if (x == 0 && direction == Move.RIGHT) { this.shiftTilesUp(x, y); }
      else if (y == 0 && direction == Move.DOWN) { this.shiftTilesRight(x, y); }
      else if (x == this.board[0].length-1 && direction == Move.LEFT) { this.shiftTilesDown(x, y); }
      else if (y == this.board.length-1 && direction == Move.UP) { this.shiftTilesLeft(x, y); }
      
      // If Hero/component did not move away form the edge, do normal tile move
      else { this.shiftTilesTo(x, y, direction); }
    }
  }
  private void shiftTilesTo(int x, int y, Move direction) {
    switch (direction) {
      case UP:
        this.shiftTilesUp(x, y);
        break;
      case DOWN:
        this.shiftTilesDown(x, y);
        break;
      case LEFT:
        this.shiftTilesLeft(x, y);
        break;
      case RIGHT:
        this.shiftTilesRight(x, y);
        break;
      default:
    }
  }
  private void shiftTilesUp(int x, int y) {
    while (y < this.board.length-1) {
      this.board[y][x] = this.board[++y][x];
    }
    this.board[y][x] = null;
  }
  private void shiftTilesDown(int x, int y) {
    while (y > 0) {
      this.board[y][x] = this.board[--y][x];
    }
    this.board[y][x] = null;
  }
  private void shiftTilesRight(int x, int y) {
    while (x > 0) {
      this.board[y][x] = this.board[y][--x];
    }
    this.board[y][x] = null;
  }
  private void shiftTilesLeft(int x, int y) {
    while (x < this.board[0].length-1) {
      this.board[y][x] = this.board[y][++x];
    }
    this.board[y][x] = null;
  }
  // returns [x, y]
  private int[] findHero() {
    for (int i = 0; i < this.board.length; i++) {
      for (int j = 0; j < this.board[i].length; j++) {
        if (this.board[i][j] instanceof Hero) {
          return new int[] {j, i};
        }
      }
    }
    return null;
  }
  private int[] findBoss() {
    for (int i = 0; i < this.board.length; i++) {
      for (int j = 0; j < this.board[i].length; j++) {
        if (this.board[i][j] instanceof Boss) {
          return new int[] {j, i};
        }
      }
    }
    return null;
  }
  private int[] findEmpty() {
    for (int i = 0; i < this.board.length; i++) {
      for (int j = 0; j < this.board[i].length; j++) {
        if (this.board[i][j] == null) {
          return new int[] {j, i};
        }
      }
    }
    return null;
  }
}
