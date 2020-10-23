package components;
import java.util.*;
import java.lang.reflect.*;
import java.lang.Class;

public class DataInterface {
  private String raw;
  private int turnIndex;
  private int bossKills;
  private String data;
  private GameBoardComponent[][] board;

  public boolean isValid = false;

  public DataInterface(String rawData) {
    this.raw = rawData;
    this.isValid = _interface.Interface.isValid(rawData);
    if (this.isValid) {
      this.turnIndex = _interface.Interface.getIndex(rawData);
      this.bossKills = _interface.Interface.getKills(rawData);
      this.data = _interface.Interface.getData(rawData);
    }
  } 

  public GameBoardComponent[][] getBoard() {
    if (this.isValid != true) {
      return null;
    }
    if (this.board == null) {
      this.board = DataInterface.parse(data);
    }
    return this.board;
  }

  public int getIndex() {
    if (this.isValid != true) {
      return -999;
    }
    return this.turnIndex;
  }

  public int getKills() {
    if (this.isValid != true) {
      return -999;
    }
    return this.bossKills;
  }

  private static GameBoardComponent[][] parse(String data) {
    String[][][] nameData = _interface.Interface.parseBoard(data);
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
          obj = _class.getConstructor(String.class).newInstance(nameData[1][i][j]);
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
  protected static void apply(String data, GameBoardComponent obj) {
    HashMap<String, String> map = _interface.Interface.parseObject(data);

    // For each key-value pair parsed
    map.forEach((k, v)->{
      Class _class = obj.getClass();
      do {
        try {
          // Get the corresponding object field
          Field field = _class.getDeclaredField(k);

          // Assign value to object field
          DataInterface.applyField(obj, field, v);
          return;
        }
        //No such field found in current class...
        catch (NoSuchFieldException e) {} 
      }
      //..check each superclass until we reach class:Object
      while((_class=_class.getSuperclass())!=Object.class);
      System.out.println("Could not find field: "+k);
    });
  }

  private static void applyField(GameBoardComponent obj, Field field, String v) {
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
