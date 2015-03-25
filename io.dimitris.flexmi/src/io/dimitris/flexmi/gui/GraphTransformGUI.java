package io.dimitris.flexmi.gui;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JComboBox;

public class GraphTransformGUI extends JPanel {

	/**
	 * Create the panel.
	 */
	public GraphTransformGUI() {
		setLayout(null);
		
		JLabel lblChooseValidation = new JLabel("Please Choose Validation Process");
		lblChooseValidation.setBounds(12, 12, 281, 30);
		add(lblChooseValidation);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setBounds(12, 54, 497, 30);
		add(comboBox);
		
		JLabel lblClickNextTo = new JLabel("Click Next to validate Document");
		lblClickNextTo.setBounds(12, 425, 598, 15);
		add(lblClickNextTo);

	}
}
