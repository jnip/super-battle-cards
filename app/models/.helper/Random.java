package components;

public class Random {
  private static java.util.Random rand = new java.util.Random();

  public static int nextInt(int x) {
    return rand.nextInt(x);
  }

  public static int nextInt(int start, int end) {
    if (start == end) return start;
    if (start > end) {var t=start;start=end;end=t;}
    return rand.nextInt(end-start+1)+start;
  }
}
