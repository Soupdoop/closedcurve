import java.awt.*;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import java.awt.geom.Path2D;
import java.io.File;
import javax.imageio.ImageIO;
import java.util.Scanner;
import java.io.IOException;
import java.io.PrintWriter;


public class CC2O {

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
    file.close();
  }

  public void generatePoints(float maxDensity, float baseWhite, boolean isCubic) {
    //Uniform points
    ArrayList<CurvePoint> expandablePoints = new ArrayList<CurvePoint>();
    float count = 0;
    for(int i = 0; i < width; i++) {
      for(int j = 0; j < height; j++) {
        float currdarkness = darkness(image.getRGB(i,j));
        count += maxDensity * (baseWhite + (1 - baseWhite) * (isCubic ? currdarkness * currdarkness * currdarkness : currdarkness));
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

  private void insertPoint(ArrayList<CurvePoint> ps, CurvePoint p) {
    ps.add(p);
  }

  private float darkness(int argb) {
    int blue = argb & 255;
    int green = (argb >> 8) & 255;
    int red = (argb >> 16) & 255;
    int alph = (argb >> 24) & 255;
    float greyscale = 1 - (float)(blue + green + red) / 3 / 255;
    return greyscale;
  }

  public BufferedImage draw(int thickness) {
    BufferedImage b = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    Graphics2D canvas = b.createGraphics();
    canvas.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    canvas.setColor(new Color(0x000000));
    canvas.setStroke(new BasicStroke(thickness));
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

  public void print() {
    System.out.println(points.length);
    System.out.println(width);
    System.out.println(height);
    for(int i = 0; i < points.length; i++) {
      System.out.println(points[i].getX());
      System.out.println(points[i].getY());
    }
  }

  public void print(PrintWriter p) {
    p.println(points.length);
    p.println(width);
    p.println(height);
    for(int i = 0; i < points.length; i++) {
      p.println(points[i].getX());
      p.println(points[i].getY());
    }
  }
}
