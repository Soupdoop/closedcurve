import javax.imageio.ImageIO;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Scanner;

import java.io.*;

import javax.swing.*;
import java.awt.event.*;

public class ImageProcessor extends SwingWorker<BufferedImage, String> {

	String inputPathName;
	String outputPathName;
	float maxDensity;
 	float baseWhite;
	boolean isCubic;
	int lineThickness;

	public ImageProcessor(String ipn, String opn, float md, float bw, boolean ic, int lt) {
		inputPathName = ipn;
		outputPathName = opn;
		maxDensity = md;
		baseWhite = bw;
		isCubic = ic;
		lineThickness = lt;
	}

	public BufferedImage doInBackground() {
		File points = new File(".points");
		File spoints = new File(".spoints");
		points.delete();
		spoints.delete();
		File imgfile = new File(inputPathName);
		BufferedImage img = null;
		setProgress(1);
		try {
			img = ImageIO.read(imgfile);
		} catch(Exception e) {
			JOptionPane.showMessageDialog(null, "The image cannot be read!", "IO Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}

		setProgress(2);
		CC2O cc2o = new CC2O(img);
		cc2o.generatePoints(maxDensity, baseWhite, isCubic);

		setProgress(3);
		PrintWriter pointWriter = null;
		try {
			pointWriter = new PrintWriter(".points");
			cc2o.print(pointWriter);
		} catch(Exception e) {
			JOptionPane.showMessageDialog(null, "Error in the handoff to C++ program", "IO Error", JOptionPane.ERROR_MESSAGE);
			return null;
		} finally {
			if(pointWriter != null)
				pointWriter.close();
		}

		int status = 0;
		setProgress(4);
		try {
			Process two_opt = Runtime.getRuntime().exec("./sorter.exe .points .spoints");
			status = two_opt.waitFor();
		} catch(Exception e) {
			JOptionPane.showMessageDialog(null, "Two-opt interrupted", "Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		if(status != 0) {
			JOptionPane.showMessageDialog(null, "Error in the two-opt program", "C++ Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}

		setProgress(5);
		CC2O goodPoints = null;
		try {
			goodPoints = new CC2O(".spoints");
		} catch(Exception e) {
			JOptionPane.showMessageDialog(null, "Error in the handoff to C++ program", "IO Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}

		setProgress(6);
		BufferedImage finalImage = goodPoints.draw(lineThickness);

		setProgress(7);
		try {
			ImageIO.write(finalImage, "png", new File(outputPathName));
		} catch(Exception e) {
			JOptionPane.showMessageDialog(null, "Error in writing final image", "IO Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}

		setProgress(0);
		return finalImage;
	}

}
