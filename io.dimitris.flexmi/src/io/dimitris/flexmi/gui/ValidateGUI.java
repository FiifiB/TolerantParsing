package io.dimitris.flexmi.gui;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class ValidateGUI extends JPanel {

	/**
	 * Create the panel.
	 */
	public ValidateGUI() {
		setLayout(null);
		
		JLabel lblValidatingStatus = new JLabel("...Validating");
		lblValidatingStatus.setBounds(12, 112, 281, 30);
		add(lblValidatingStatus);

	}

}
