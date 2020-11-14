package components;
import java.util.*;
import java.lang.reflect.*;
import java.lang.Class;

public abstract class DatabaseInterface {
  abstract public HashMap parseObject(String data);
  abstract public String[][][] parseBoard(String data);
  abstract public boolean isValid(String raw);
  abstract public int getIndex(String raw);
  abstract public int getKills(String raw);
  abstract public String getData(String raw);

  private String raw;
  private int turnIndex;
  private int bossKills;
  private String data;
  private GameBoardComponent[][] board;

  private boolean isDataProcessed = false;
  public boolean isDataValid = false;

  public void loadData(String rawData) {
    this.isDataProcessed = false;
    this.raw = rawData;
    this.isDataValid = this.isValid(rawData);
    if (this.isDataValid) {
      this.turnIndex = this.getIndex(rawData);
      this.bossKills = this.getKills(rawData);
      this.data = this.getData(rawData);
    }
  }

  public GameBoardComponent[][] getBoard() {
    if (this.isDataValid) {
      if (!this.isDataProcessed) {
        this.isDataProcessed = true;
        this.board = this.parse(this.data);
      }
      return this.board;
    }
    return null;
  }

  public int getIndex() {
    if (this.isDataValid != true) {
      return -999;
    }
    return this.turnIndex;
  }

  public int getKills() {
    if (this.isDataValid != true) {
      return -999;
    }
    return this.bossKills;
  }

  private GameBoardComponent[][] parse(String data) {
    String[][][] nameData = this.parseBoard(data);
    if (nameData == null) {return null;}

    int width = nameData[0].length;
    int height = nameData[0][0].length;
    GameBoardComponent[][] board = new GameBoardComponent[width][height];
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        // Create GameBoardComponent object
        GameBoardComponent obj;
        try {
          Class<GameBoardComponent> _class = (Class<GameBoardComponent>)Class.forName("components."+nameData[0][i][j]);
          obj = _class.getConstructor(int.class, int.class).newInstance(0,0);
          this.apply(nameData[1][i][j], obj);
          board[i][j] = obj;
        }
        catch (ClassNotFoundException e) {
          System.out.println(e);
          return null;
        }
        // getConstructor exception
        catch (NoSuchMethodException e) {
          System.out.println(e);
          return null;
        }
        // newInstance exceptions
        catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
          System.out.println(e);
          return null;
        }
      }
    }
    if (width == 0 || height == 0) {
      return null;
    }
    return board;
  }

  // Used by object contructor to load data
  protected void apply(String data, GameBoardComponent obj) {
    HashMap<String, String> map = this.parseObject(data);

    // For each key-value pair parsed
    map.forEach((k, v)->{
      Class _class = obj.getClass();
      do {
        try {
          // Get the corresponding object field
          Field field = _class.getDeclaredField(k);

          // Assign value to object field
          this.applyField(obj, field, v);
          return;
        }
        //No such field found in current class...
        catch (NoSuchFieldException e) {} 
      }
      //..check each superclass until we reach class:Object
      while((_class=_class.getSuperclass())!=Object.class);
      System.out.println("DatabaseInterface: Could not find field: "+k);
    });
  }

  private void applyField(GameBoardComponent obj, Field field, String v) {
    try {
      try {
        int value = Integer.parseInt(v);
        field.setInt(obj, value);
      }
      catch (NumberFormatException e) {
        // name and type do not need to be set
      }
    }
    catch (IllegalAccessException e) {
      System.out.println(e);
    }
  }
}
