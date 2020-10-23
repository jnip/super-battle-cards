package _interface;
import components.GameBoardComponent;
import java.util.*;

public class JSONInterface {
  private static int indexLength = 10;

  public static boolean isValid(String raw) {
    // {"move_INDEX_KILLS":[[{...}]]}
    return raw.matches("\\{\"move_[0-9]+_[0-9]+\":\\[\\[\\{\".*\\}\\]\\]\\}");
  }

  public static int getIndex(String raw) {
    // INDEX spans 10 characters
    int charIndexOfIndex = 7;
    int end = raw.indexOf('_', charIndexOfIndex);
    String index = raw.substring(charIndexOfIndex, end);
    return Integer.parseInt(index);
  }

  public static int getKills(String raw) {
    // {"move_INDEX_KILLS"...
    int charIndexOfKills = 8 + indexLength;
    int end = raw.indexOf('"', charIndexOfKills);
    String kills = raw.substring(charIndexOfKills, end);
    return Integer.parseInt(kills);
  }

  public static String getData(String raw) {
    int killsLength = (""+JSONInterface.getKills(raw)).length();
    return raw.substring(10+indexLength+killsLength, raw.length()-1);
  }

  public static HashMap parseObject(String data) {
    HashMap<String, String> map = new HashMap<String, String>();
    data = data.substring(1,data.length()-1);
    String[] fields = data.split("\\s*,\\s*", 0);
    
    for (int i = 0; i < fields.length; i++) {
      String[] field = fields[i].split("\\s*:\\s*");
      if (field.length == 2) {
        if (field[0].matches("\".*\"")) {
          map.put(field[0].substring(1,field[0].length()-1), field[1]);
          continue;
        }
      }
      System.out.println("DataInterface failed to process: "+Arrays.toString(field));
    }
    return map;
  }

/* Example raw data
  [[{"type":"MOVEMENT", "name":"Portal"}, {"counter":8, "value":5, "type":"TRAP", "name":"Fog"}, {"value":0, "state":0, "type":"ITEM", "name":"Money"}, {"value":3, "state":0, "type":"ITEM", "name":"Potion"}, {"counter":8, "value":5, "type":"TRAP", "name":"Fog"}], [{"type":"MOVEMENT", "name":"Portal"}, {"state":0, "counter":0, "type":"MOVEMENT", "name":"Path"}, {"counter":3, "state":1, "health":8, "type":"MONSTER", "name":"Mummy"}, {"value":5, "state":0, "type":"SKILL", "name":"Fireball"}, {"health":12, "type":"MONSTER", "name":"DefaultBoss"}], [{"health":3, "type":"MONSTER", "name":"Skeleton"}, {"state":0, "counter":4, "value":5, "type":"TRAP", "name":"Flamethrower"}, {"maxHealth":10, "health":10, "armour":0, "stepsWalked":0, "bossesKilled":0, "monstersKilled":0, "type":"HERO", "name":"DefaultHero"}, {"state":0, "value":3, "type":"TRAP", "name":"Spike"}, {"counter":5, "value":6, "type":"TRAP", "name":"Fog"}], [{"value":3, "state":0, "type":"SKILL", "name":"Poison"}, {"state":1, "counter":3, "type":"MOVEMENT", "name":"Gate"}, {"value":0, "state":1, "type":"ITEM", "name":"Money"}, {"health":3, "type":"MONSTER", "name":"Skeleton"}, {"value":5, "state":0, "type":"SKILL", "name":"Fireball"}], [{"state":0, "counter":0, "type":"MOVEMENT", "name":"Path"}, {"value":0, "state":0, "type":"ITEM", "name":"Money"}, {"counter":3, "state":1, "health":10, "type":"MONSTER", "name":"Mummy"}, {"state":0, "type":"MOVEMENT", "name":"Pusher"}, {"health":4, "type":"MONSTER", "name":"Skeleton"}]]
*/
  public static String[][][] parseBoard(String data) {
    if (data.matches("\\[\\[.*\\]\\]")) {
      data = data.substring(2,data.length()-2); //remove starting "[[" and ending "]]"
      String[] arrays = data.split("\\],\\s*\\[", 0); //split by "], ["
      for (int i = 0; i < arrays.length; i++) {
          if (!arrays[i].matches("\\{.*}")) {
            return null;
          }
      }
      // arrays[i] = {a:b, c:d}, {e:f}, {g:h}
      String[][][] nameData = new String[2][arrays.length][arrays.length];
      for (int i = 0; i < arrays.length; i++) {
        arrays[i] = arrays[i].substring(1,arrays[i].length()-1); //remove starting "{" and ending "}"
        String[] objects = arrays[i].split("\\},\\s*\\{", 0); //split by "}, {"
        // objects = a:b, c:d
        for (int j = 0; j < objects.length; j++) {
          objects[j] = '{'+objects[j]+'}'; //wrap it so it looks like an object
          nameData[0][i][j] = JSONInterface.getObjectName(objects[j]);
          nameData[1][i][j] = objects[j];
        }
      }
      return nameData;
    }
    return null;
  }

  private static String getObjectName(String data) {
    String toMatch = "\"name\":";
    int index = data.indexOf(toMatch);
    if (index == -1) {
      return "_";
    }
    index += toMatch.length();
    int numQuotes = 0;
    int endIndex = 0;
    for (int i = index; i < data.length(); i++) {
      if (data.charAt(i) == '"') {
        numQuotes++;
        if (numQuotes == 1) {
          index = i;
        }
        if (numQuotes == 2) {
          endIndex = i;
          break;
        }
      }
    }
    if (endIndex == 0) {
      return "_";
    }
    return data.substring(index+1, endIndex);
  }
}
