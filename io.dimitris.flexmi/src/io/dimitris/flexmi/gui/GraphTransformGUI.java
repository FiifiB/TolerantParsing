package io.dimitris.flexmi.gui;

import io.dimitris.flexmi.GraphTransform;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JComboBox;

public class GraphTransformGUI extends JPanel {
	private JComboBox comboBox ;

	/**
	 * Create the panel.
	 */
	public GraphTransformGUI(MainFrameGUI parent) {
		setLayout(null);
		
		JLabel lblChooseValidation = new JLabel("Please Choose Validation Process");
		lblChooseValidation.setBounds(12, 12, 281, 30);
		add(lblChooseValidation);
		
		String[] validationMethods = { "Pairwise Validation" };
		comboBox = new JComboBox(validationMethods);
		comboBox.setBounds(12, 54, 497, 30);
		add(comboBox);
		
		JLabel lblClickNextTo = new JLabel("Click Next to validate Document");
		lblClickNextTo.setBounds(12, 425, 598, 15);
		add(lblClickNextTo);
		
		parent.modelGraph = GraphTransform.convertModelToGraph(parent.model);
		parent.XMLGraph = GraphTransform.convertDocToGraph(parent.xmldocument);

	}
	
	public String getValidationMethodName(){
		return (String)comboBox.getSelectedItem();
	}
}
