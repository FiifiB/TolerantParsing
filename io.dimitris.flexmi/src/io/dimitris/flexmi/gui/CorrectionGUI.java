package io.dimitris.flexmi.gui;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class CorrectionGUI extends JPanel {

	/**
	 * Create the panel.
	 */
	public CorrectionGUI() {
		setLayout(null);
		
		JLabel lblCorrectionStatus = new JLabel("...Validating");
		lblCorrectionStatus.setBounds(12, 112, 281, 30);
		add(lblCorrectionStatus);
		
		JLabel lblChooseOutputMethod = new JLabel("Please Choose how you want the corrections to be applied");
		lblChooseOutputMethod.setBounds(12, 162, 458, 30);
		add(lblChooseOutputMethod);
		
		JRadioButton rdbtnPrintOutCorrections = new JRadioButton("Print out corrections");
		rdbtnPrintOutCorrections.setBounds(12, 239, 215, 23);
		add(rdbtnPrintOutCorrections);
		
		JRadioButton rdbtnAutomaticallyCorrectXml = new JRadioButton("Automatically correct XML document");
		rdbtnAutomaticallyCorrectXml.setBounds(293, 239, 305, 23);
		add(rdbtnAutomaticallyCorrectXml);
		
		//Group the radio buttons.
	    ButtonGroup group = new ButtonGroup();
	    group.add(rdbtnPrintOutCorrections);
	    group.add(rdbtnAutomaticallyCorrectXml);
		
		JLabel lblClickNextTo = new JLabel("Click Next to continue");
		lblClickNextTo.setBounds(12, 425, 598, 15);
		add(lblClickNextTo);

	}
}
