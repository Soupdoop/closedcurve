import java.awt.*;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import java.awt.geom.Path2D;
import java.io.File;
import javax.imageio.ImageIO;
import java.util.Scanner;
import java.io.IOException;


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
    points[points.length - 1] = points[0];
  }

  public CC2O(String pointsFileName) throws IOException {
    Scanner file = new Scanner(new File(pointsFileName));
    int numPoints = Integer.parseInt(file.nextLine());
    width = Integer.parseInt(file.nextLine());
    height = Integer.parseInt(file.nextLine());
    points = new CurvePoint[numPoints];
    for(int i = 0; i < numPoints; i++) {
      float a = Float.parseFloat(file.nextLine());
      float b = Float.parseFloat(file.nextLine());
      points[i] = new CurvePoint(a,b);
    }
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

  public void print() {
    System.out.println(points.length);
    System.out.println(width);
    System.out.println(height);
    for(int i = 0; i < points.length; i++) {
      System.out.println(points[i].getX());
      System.out.println(points[i].getY());
    }
  }
}
