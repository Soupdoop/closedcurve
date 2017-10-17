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

public class ClosedCurveRunner {
  public static void main(String[] args) {
    Scanner gets = new Scanner(System.in);
    String cont = "";
    do {
      System.out.print("Enter the filename of the image => ");
      String filename = gets.nextLine();
      processImage(filename);
      System.out.print("Enter yes to process another image (anything else will quit) => ");
      cont = gets.nextLine();
    } while(cont.equalsIgnoreCase("y") || cont.equalsIgnoreCase("yes"));
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
