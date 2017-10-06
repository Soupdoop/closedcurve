import java.awt.*;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import java.awt.geom.Path2D;
import java.io.File;
import javax.imageio.ImageIO;

public class CC2O {

  public static final float MAX_PROB = 0.1f;
  public static final float BASE_WHITE = 0f;

  public CurvePoint[] points;

  private BufferedImage image;

  private double totalDarkness;

  public int height, width;

  public CC2O() {
    points = null;
    height = 0;
    width = 0;
    totalDarkness = 0;
    image = null;
  }

  public CC2O(BufferedImage bi) {
    image = bi;
    height = bi.getHeight();
    width = bi.getWidth();
    totalDarkness = 0;
    int tracked = 0;

    //Uniform points
    ArrayList<CurvePoint> expandablePoints = new ArrayList<CurvePoint>();
    float count = 0;
    for(int i = 0; i < width; i++) {
      for(int j = 0; j < height; j++) {
        count += MAX_PROB * dtransform(darkness(bi.getRGB(i,j)));
        if(count >= 1) {
          insertPoint(expandablePoints, new CurvePoint((float) i / width, (float) j / height));
          count -= 1;
        }
      }
    }
    points = new CurvePoint[expandablePoints.size() + 1];
    for(int i = 0; i < expandablePoints.size(); i++) {
      points[i] = expandablePoints.get(i);
    }
    System.out.println("\nPoints generated!");
    points[points.length - 1] = points[0];
    two_opt(0);
    two_opt(0);
    System.out.println("2-opt done!");
  }

  private void insertPoint(ArrayList<CurvePoint> ps, CurvePoint p) {
    int i = 0;
    while(i < ps.size() && p.sierpinski_pi() < ps.get(i).sierpinski_pi()) {
      i++;
    }
    ps.add(i,p);
  }

  private float darkness(int argb) {
    int blue = argb & 255;
    int green = (argb >> 8) & 255;
    int red = (argb >> 16) & 255;
    int alph = (argb >> 24) & 255;
    float greyscale = 1 - (float)(blue + green + red) / 3 / 255;
    return greyscale;
  }

  private float dtransform(float darkness) {
    return BASE_WHITE + (1 - BASE_WHITE) * darkness * darkness * darkness;
  }

  private void two_opt(int start) {
    boolean swapped = false;
    for(int j = start + 3; j < points.length; j++) {
      if(points[start].DistanceSquaredTo(points[start + 1]) + points[j - 1].DistanceSquaredTo(points[j]) >
         points[start].DistanceSquaredTo(points[j - 1]) + points[start + 1].DistanceSquaredTo(points[j])) {
        swap(start + 1, j - 1);
        swapped = true;
      }
    }
    if(swapped) {
      two_opt(start);
    } else if(points.length - start > 4) {
      two_opt(start + 1);
    }
  }

  private void swap(int start, int end) {
    for(int i = 0; i < (end - start + 1) / 2; i++) {
      CurvePoint temp = points[start + i];
      points[start + i] = points[end - i];
      points[end - i] = temp;
    }
  }

  public BufferedImage draw() {
    BufferedImage b = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    Graphics2D canvas = b.createGraphics();
    canvas.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    canvas.setColor(new Color(0x000000));
    canvas.setStroke(new BasicStroke(2));
    Path2D.Float curve = new Path2D.Float();
    curve.moveTo(points[0].getX() * width, points[0].getY() * height);
    for(int i = 1; i < points.length; i++) {
      curve.lineTo(points[i].getX() * width, points[i].getY() * height);
    }
    canvas.draw(curve);
    return b;
  }

  public BufferedImage drawSmooth() {
    BufferedImage b = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    Graphics2D canvas = b.createGraphics();
    canvas.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    canvas.setColor(new Color(0x000000));
    canvas.setStroke(new BasicStroke(2));
    Path2D.Float curve = new Path2D.Float();
    if(points.length > 2) {
      curve.moveTo(points[0].average(points[1]).getX() * width, points[0].average(points[1]).getY() * height);
      for(int i = 1; i < points.length; i++) {
        curve.quadTo(points[i].getX() * width, points[i].getY() * height, points[i].average(points[(i + 1) % (points.length - 1)]).getX() * width, points[i].average(points[(i + 1) % (points.length - 1)]).getY() * height);
      }
    }
    canvas.draw(curve);
    return b;
  }

  public BufferedImage fill() {
    BufferedImage b = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    Graphics2D canvas = b.createGraphics();
    canvas.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    canvas.setColor(new Color(0x000000));
    canvas.setStroke(new BasicStroke(2));
    Path2D.Float curve = new Path2D.Float();
    curve.moveTo(points[0].getX() * width, points[0].getY() * height);
    for(int i = 1; i < points.length; i++) {
      curve.lineTo(points[i].getX() * width, points[i].getY() * height);
    }
    canvas.fill(curve);
    return b;
  }

  public BufferedImage fillSmooth() {
    BufferedImage b = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    Graphics2D canvas = b.createGraphics();
    canvas.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    canvas.setColor(new Color(0x000000));
    canvas.setStroke(new BasicStroke(2));
    Path2D.Float curve = new Path2D.Float();
    if(points.length > 2) {
      curve.moveTo(points[0].average(points[1]).getX() * width, points[0].average(points[1]).getY() * height);
      for(int i = 1; i < points.length; i++) {
        curve.quadTo(points[i].getX() * width, points[i].getY() * height, points[i].average(points[(i + 1) % (points.length - 1)]).getX() * width, points[i].average(points[(i + 1) % (points.length - 1)]).getY() * height);
      }
    }
    canvas.fill(curve);
    return b;
  }
}