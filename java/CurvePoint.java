/* Jack Herd
 *
 * Closed-Curve Image Generator
 *
 */

public class CurvePoint {

  public static final int SPI_DEPTH = 10;

  private float x,y;

  private long cachedCode;
  private boolean isCodeCached;

  public CurvePoint() {
    x = 0;
    y = 0;
    cachedCode = 0;
    isCodeCached = false;
  }

  public CurvePoint(float x, float y) {
    this.x = x;
    this.y = y;
    cachedCode = 0;
    isCodeCached = false;
  }

  public float DistanceTo(CurvePoint other) {
    return (float)Math.sqrt(DistanceSquaredTo(other));
  }

  public float DistanceTo(float a, float b) {
    return DistanceTo(new CurvePoint(a,b));
  }

  public float DistanceSquaredTo(CurvePoint other) {
    return (float)(Math.pow(this.x - other.x, 2) + Math.pow(this.y - other.y, 2));
  }

  public float DistanceSquaredTo(float a, float b) {
    return DistanceSquaredTo(new CurvePoint(a,b));
  }

  public CurvePoint average(CurvePoint other) {
    return new CurvePoint((this.x + other.x) / 2, (this.y + other.y) / 2);
  }

  public void setX(float x) {
    this.x = x;
  }

  public void setY(float y) {
    this.y = y;
  }

  public float getX() {
    return x;
  }

  public float getY() {
    return y;
  }

  public long sierpinski_pi() {
    if(isCodeCached) {
      return cachedCode;
    }
    if(DistanceSquaredTo(0,0) < DistanceSquaredTo(1,1)) {
      cachedCode = sierpinski_pi(new CurvePoint(0,1), new CurvePoint(0,0), new CurvePoint(1,0), SPI_DEPTH, 0);
    } else {
      cachedCode = sierpinski_pi(new CurvePoint(0,1), new CurvePoint(1,1), new CurvePoint(1,0), SPI_DEPTH, 1);
    }
    isCodeCached = true;
    return cachedCode;
  }

  public long sierpinski_pi(CurvePoint a, CurvePoint b, CurvePoint c, int level, long code) {
    if(level <= 0) {
      return code;
    }
    if(DistanceSquaredTo(a) < DistanceSquaredTo(c)) {
      return sierpinski_pi(a, a.average(c), b, level - 1, code * 2);
    } else {
      return sierpinski_pi(b, a.average(c), c, level - 1, code * 2 + 1);
    }
  }

  public String toString() {
    return "(" + x + "," + y + ")";
  }
}
