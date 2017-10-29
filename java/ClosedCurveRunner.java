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

import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.JLabel;

public class ClosedCurveRunner extends JFrame {

  public JProgressBar progressbar;
  public JLabel currentJob;

  public ClosedCurveRunner() {
    JProgressBar jpb = new JProgressBar(0,1);
    jpb.setIndeterminate(true);
    jpb.setVisible(true);
    getContentPane().add(jpb);
    JLabel curjob = new JLabel("Initializing...");
    //curjob.setVisible(true);
    //getContentPane().add(curjob);
    setVisible(true);
    setSize(300,100);
    setTitle("Initializing ...");
    progressbar = jpb;
    currentJob = curjob;
  }

  public static void main(String[] args) {
    ClosedCurveRunner ccr = new ClosedCurveRunner();
    Scanner gets = new Scanner(System.in);
    String cont = "";
    do {
      System.out.print("Enter the filename of the image => ");
      String filename = gets.nextLine();
      ccr.processImage(filename);
      System.out.print("Enter yes to process another image (anything else will quit) => ");
      cont = gets.nextLine();
    } while(cont.equalsIgnoreCase("y") || cont.equalsIgnoreCase("yes"));
    System.exit(0);
  }

  public void processImage(String filename) {
    try {
      File imgFile = new File(filename);
      BufferedImage img = ImageIO.read(imgFile);

      CC2O cc2o = new CC2O(img, this);
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
