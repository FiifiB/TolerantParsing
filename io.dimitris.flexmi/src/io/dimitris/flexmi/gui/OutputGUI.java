package io.dimitris.flexmi.gui;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class OutputGUI extends JPanel {

	/**
	 * Create the panel.
	 */
	public OutputGUI() {
		setLayout(null);
		
		JLabel lblOutputStatus = new JLabel("...Output Success");
		lblOutputStatus.setBounds(12, 112, 281, 30);
		add(lblOutputStatus);
		
		JLabel lblLocationFile = new JLabel("Please find the newly corrected XML document in \n C:/Users/John/Documents/  as CorrectedLibrary.XML");
		lblLocationFile.setBounds(12, 162, 598, 168);
		add(lblLocationFile);
		
		JLabel lblThankYou = new JLabel("Thank You for using the tool :)");
		lblThankYou.setBounds(12, 425, 598, 15);
		add(lblThankYou);
		
//		JProgressBar progressBar = new JProgressBar();
//		progressBar.setBounds(24, 69, 329, 48);
//		progressBar.setValue(50);
//		progressBar.setString("...Outputing");
//		add(progressBar);

	}
}
