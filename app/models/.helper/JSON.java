package components;
import java.lang.reflect.*;
import java.lang.Class;

public class JSON {
  public static String stringify(GameBoardComponent obj) {
    return JSON.stringify(obj, false);
  }
  public static String stringify(GameBoardComponent obj, boolean includeProtected) {
    Class<?> _class = obj.getClass();
    String result = JSON.stringify(obj, _class, includeProtected);
    while (_class != GameBoardComponent.class) {
      _class = _class.getSuperclass();
      result = JSON.merge(result, JSON.stringify(obj, _class, includeProtected));
    }
    return result;
  }
  public static String stringify(Object obj) {
    return JSON.stringify(obj, null);
  }
  public static String stringify(Object obj, Class _class) {
    return JSON.stringify(obj, _class, false);
  }
  public static String stringify(Object obj, Class _class, boolean includeProtected) {
    String result = "{";
    _class = (_class == null)?obj.getClass():_class;

    // For each public field of the object (current class/layer only)
    Field[] fields = _class.getDeclaredFields();
    for (int i = 0; i < fields.length; i++) {
      if (fields[i].getModifiers() == Modifier.PUBLIC ||
          (includeProtected && fields[i].getModifiers() == Modifier.PROTECTED)) {
        Object valueObj;
        // Try to access the field
        try {
          valueObj = fields[i].get(obj);
        } catch (Exception e) { continue; }
        // Save key-value pair
        String name = fields[i].getName();
        String value;
        try {
          value = valueObj.toString();
        } catch (Exception e) {
          value = "null";
        }
        result += ('"' + name + "\":");
        result += (valueObj instanceof Number)?value:('"'+value+'"');
        result += ", ";
      }
    }
    if (result.length() > 1) {
      result = result.substring(0, result.length()-2);
    }
    result += "}";
    return result;
  }
  // Deprecated
  public static String merge(String JSON_a, String JSON_b) {
    if (JSON_a.length() <= 2) { return JSON_b; }
    if (JSON_b.length() <= 2) { return JSON_a; }
    String result = JSON_a.substring(0, JSON_a.length()-1);
    result += ", ";
    result += JSON_b.substring(1, JSON_b.length());
    return result;
  }
}
