package _interface;
import components.GameBoardComponent;
import java.util.*;

public class Interface {
  public static HashMap parseObject(String data) {
    return JSONInterface.parseObject(data);
  }

  public static String[][][] parseBoard(String data) {
    return JSONInterface.parseBoard(data);
  }

  public static boolean isValid(String raw) {
    return JSONInterface.isValid(raw);
  }

  public static int getIndex(String raw) {
    return JSONInterface.getIndex(raw);
  }

  public static int getKills(String raw) {
    return JSONInterface.getKills(raw);
  }

  public static String getData(String raw) {
    return JSONInterface.getData(raw);
  }
}
