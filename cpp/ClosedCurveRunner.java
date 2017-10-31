import javax.imageio.ImageIO;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Scanner;

import java.io.*;

public class ClosedCurveRunner {
	public static void main(String[] args) {
		if(args.length < 2) {
			System.err.println("Invalid input –– should have both an input image name and an output image name");
			return;
		}
		try {
			PrintWriter pointWriter = new PrintWriter(".points");
			File imgFile = new File(args[0]);
      BufferedImage img = ImageIO.read(imgFile);
      CC2O cc2o = new CC2O(img);
			cc2o.print(pointWriter);
			pointWriter.close();
			Process two_opt = Runtime.getRuntime().exec("./sorter.exe .points .spoints");
			two_opt.waitFor();
			CC2O goodPoints = new CC2O(".spoints");
			ImageIO.write(goodPoints.draw(), "png", new File(args[1]));
			File p = new File(".points");
			p.delete();
			File sp = new File(".spoints");
			sp.delete();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
