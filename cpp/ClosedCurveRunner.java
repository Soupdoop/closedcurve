import javax.imageio.ImageIO;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Scanner;

import java.io.*;

import javax.swing.*;
import java.awt.event.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;


public class ClosedCurveRunner extends JFrame {

	public JLabel inputPathLabel, outputPathLabel, lineLabel, whiteLabel, maxLabel, transformLabel, programStatus;

	public JTextField inputPath, outputPath;

	public JSlider lineThickness, baseWhite, maxDensity;

	public JComboBox<String> transformType;

	public JButton runButton;

	public JSeparator topDivider, bottomDivider;

	public final String[] states = new String[]{"Waiting for input ...", "Reading image ...", "Generating points ...", "Handing off to sorting program ...", "Running the two-opt algorithm", "Handing back off to java ...", "Rendering image ...", "Writing image to file..."};

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

		topDivider = new JSeparator();
		bottomDivider = new JSeparator();

		programStatus = new JLabel("Waiting for input");

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

		this.setSize(300,400);

		layout.setAutoCreateGaps(true);

		layout.setHorizontalGroup(
			layout.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addComponent(topDivider)
				.addComponent(runButton)
				.addComponent(bottomDivider)
				.addComponent(programStatus)
				.addGroup(
					layout.createSequentialGroup()
					.addGap(16)
					.addGroup(
						layout.createParallelGroup()
							.addComponent(inputPathLabel)
							.addComponent(outputPathLabel)
							.addComponent(lineLabel)
							.addComponent(whiteLabel)
							.addComponent(maxLabel)
							.addComponent(transformLabel)
					).addGroup(
						layout.createParallelGroup()
							.addComponent(inputPath)
							.addComponent(outputPath)
							.addComponent(lineThickness)
							.addComponent(baseWhite)
							.addComponent(maxDensity)
							.addComponent(transformType)
					).addGap(16)
				)
		);

		layout.setVerticalGroup(
			layout.createSequentialGroup()
			.addGap(16)
			.addGroup(
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
			).addComponent(topDivider)
			.addComponent(runButton)
			.addComponent(bottomDivider)
			.addComponent(programStatus)
			.addGap(16)
		);
		this.setVisible(true);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}

	public void processImage() {
		ImageProcessor proc = new ImageProcessor(inputPath.getText(), outputPath.getText(), maxDensity.getValue() / ((float)100), baseWhite.getValue() / ((float)100), transformType.getSelectedIndex() == 1, lineThickness.getValue());
		proc.addPropertyChangeListener(
			new PropertyChangeListener() {
				public void propertyChange(PropertyChangeEvent evt) {
             if ("progress".equals(evt.getPropertyName())) {
                 programStatus.setText(states[(Integer)evt.getNewValue()]);
             }
         }
			}
		);
		proc.execute();
	}

	public static void main(String[] args) {
		try {
			ClosedCurveRunner ccr = new ClosedCurveRunner();
		} catch(Exception e) {
			JOptionPane.showMessageDialog(null, "Error in launching!", "IO Error", JOptionPane.ERROR_MESSAGE);
		}
	}
}
