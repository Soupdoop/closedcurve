/* Jack Herd
 *
 * Closed-Curve Image Generator
 *
 */

import javax.imageio.ImageIO;
import java.io.File;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Scanner;

public class ImageProcessor {

  public static void main(String[] args) {
    if(args.length < 1) {
      System.err.println("Not enough arguments!");
    } else {
      processImage(args[0]);
    }
  }

  public static void processImage(String filename) {
    try {
      File imgFile = new File(filename);
      BufferedImage img = ImageIO.read(imgFile);

      CC2O cc2o = new CC2O(img);
      cc2o.print();
    } catch (Exception e) {
      System.err.println("Error while processing the file \"" + filename + "\"");
      e.printStackTrace();
    }
  }
}
