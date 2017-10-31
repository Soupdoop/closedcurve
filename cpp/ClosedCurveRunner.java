import javax.imageio.ImageIO;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Scanner;

import java.io.*;

import javax.swing.*;
import java.awt.event.*;

public class ClosedCurveRunner extends JFrame {

	public JLabel inputPathLabel, outputPathLabel, lineLabel, whiteLabel, maxLabel, transformLabel;

	public JTextField inputPath, outputPath;

	public JSlider lineThickness, baseWhite, maxDensity;

	public JComboBox<String> transformType;

	public JButton runButton;

	public ClosedCurveRunner() {
		inputPathLabel = new JLabel("Path to original image:");
		inputPath = new JTextField();
		outputPathLabel = new JLabel("Path to output image:");
		outputPath = new JTextField();
		lineLabel = new JLabel("Line width:");
		lineThickness = new JSlider(1, 5, 1);
		whiteLabel = new JLabel("Base white density:");
		baseWhite = new JSlider(0, 100, 0);
		maxLabel = new JLabel("Maximum density:");
		maxDensity = new JSlider(0, 100, 20);
		transformLabel = new JLabel("Transform type:");
		transformType = new JComboBox<String>(new String[]{"Linear", "Cubic"});
		runButton = new JButton("Run");

		runButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				processImage();
				File points = new File(".points");
				points.delete();
				File spoints = new File(".spoints");
				spoints.delete();
			}
		});

		GroupLayout layout = new GroupLayout(this.getContentPane());
		this.getContentPane().setLayout(layout);

		this.setSize(300,300);

		layout.setAutoCreateGaps(true);

		layout.setHorizontalGroup(
			layout.createSequentialGroup().addGroup(
				layout.createParallelGroup()
					.addComponent(inputPathLabel)
					.addComponent(outputPathLabel)
					.addComponent(lineLabel)
					.addComponent(whiteLabel)
					.addComponent(maxLabel)
					.addComponent(transformLabel)
					.addComponent(runButton)
			).addGroup(
				layout.createParallelGroup()
					.addComponent(inputPath)
					.addComponent(outputPath)
					.addComponent(lineThickness)
					.addComponent(baseWhite)
					.addComponent(maxDensity)
					.addComponent(transformType)
			)
		);

		layout.setVerticalGroup(
			layout.createSequentialGroup().addGroup(
				layout.createParallelGroup(GroupLayout.Alignment.CENTER)
					.addComponent(inputPathLabel)
					.addComponent(inputPath)
			).addGroup(
				layout.createParallelGroup(GroupLayout.Alignment.CENTER)
					.addComponent(outputPathLabel)
					.addComponent(outputPath)
			).addGroup(
				layout.createParallelGroup(GroupLayout.Alignment.CENTER)
					.addComponent(lineLabel)
					.addComponent(lineThickness)
			).addGroup(
				layout.createParallelGroup(GroupLayout.Alignment.CENTER)
					.addComponent(whiteLabel)
					.addComponent(baseWhite)
			).addGroup(
				layout.createParallelGroup(GroupLayout.Alignment.CENTER)
					.addComponent(maxLabel)
					.addComponent(maxDensity)
			).addGroup(
				layout.createParallelGroup(GroupLayout.Alignment.CENTER)
					.addComponent(transformLabel)
					.addComponent(transformType)
			).addComponent(runButton)
		);
		this.setVisible(true);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}

	public void processImage() {
		String imagePath = inputPath.getText();
		File imgfile = new File(imagePath);
		BufferedImage img = null;
		try {
			img = ImageIO.read(imgfile);
		} catch(Exception e) {
			JOptionPane.showMessageDialog(null, "The image cannot be read!", "IO Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		CC2O cc2o = new CC2O(img);
		cc2o.generatePoints(maxDensity.getValue() / ((float)100), baseWhite.getValue() / ((float)100.0), transformType.getSelectedIndex() == 1);

		PrintWriter pointWriter = null;
		try {
			pointWriter = new PrintWriter(".points");
			cc2o.print(pointWriter);
		} catch(Exception e) {
			JOptionPane.showMessageDialog(null, "Error in the handoff to C++ program", "IO Error", JOptionPane.ERROR_MESSAGE);
			return;
		} finally {
			if(pointWriter != null)
				pointWriter.close();
		}

		int status = 0;
		try {
			Process two_opt = Runtime.getRuntime().exec("./sorter.exe .points .spoints");
			status = two_opt.waitFor();
		} catch(Exception e) {
			JOptionPane.showMessageDialog(null, "Sorting interrupted", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		if(status != 0) {
			JOptionPane.showMessageDialog(null, "Error in the sorting program", "C++ Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		CC2O goodPoints = null;
		try {
			goodPoints = new CC2O(".spoints");
		} catch(Exception e) {
			JOptionPane.showMessageDialog(null, "Error in the handoff to C++ program", "IO Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		BufferedImage finalImage = goodPoints.draw(lineThickness.getValue());
		try {
			ImageIO.write(finalImage, "png", new File(outputPath.getText()));
		} catch(Exception e) {
			JOptionPane.showMessageDialog(null, "Error in writing final image", "IO Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
	}

	public static void main(String[] args) {
		ClosedCurveRunner ccr = new ClosedCurveRunner();
		/*if(args.length < 2) {
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
		}*/
	}
}
