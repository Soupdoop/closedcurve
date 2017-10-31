import javax.imageio.ImageIO;
import java.io.File;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Scanner;

public class ImageRenderer {
	public static void main(String[] args) {
		if(args.length < 2) {
      System.err.println("Not enough arguments!");
    } else {
			try {
				CC2O curve = new CC2O(args[0]);
				ImageIO.write(curve.drawSmooth(), "png", new File("smooth-" + args[1]));
			} catch(Exception e) {
				e.printStackTrace();
			}
    }
	}
}
