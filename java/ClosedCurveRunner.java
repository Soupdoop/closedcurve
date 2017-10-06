/* Jack Herd
 *
 * Closed-Curve Image Generator
 *
 */

import javax.imageio.ImageIO;
import java.io.File;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class ClosedCurveRunner {
  public static void main(String[] args) {
    if(args.length == 0) {
      System.out.println("Please pass in a filename as a parameter.");
      return;
    }
    for(int i = 0; i < args.length; i++) {
      processImage(args[i]);
    }
  }

  public static void processImage(String filename) {
    try {
      File imgFile = new File(filename);
      BufferedImage img = ImageIO.read(imgFile);

      CC2O cc2o = new CC2O(img);
      ImageIO.write(cc2o.draw(), "png", new File("jagged-" + filename));
      System.out.println("Wrote first image!");
      ImageIO.write(cc2o.drawSmooth(), "png", new File("smooth-" + filename));
      System.out.println("Wrote second image!");
    } catch (Exception e) {
      System.err.println("Error while processing the file \"" + filename + "\"");
      e.printStackTrace();
    }
  }
}