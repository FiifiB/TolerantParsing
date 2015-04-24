package correctiontool.gui;

import graphtools.Graph;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.commons.collections4.map.CaseInsensitiveMap;

import correctiontool.PairwiseValidation;
import correctiontool.Validation;

public class ValidateGUI extends JPanel {
	private MainFrameGUI parent;
	private JLabel lblValidatingStatus;
	private JComboBox comboBox;

	/**
	 * Create the panel.
	 */
	public ValidateGUI(MainFrameGUI parent) {
		setLayout(null);
		this.parent = parent;
		
		lblValidatingStatus = new JLabel("...Validating");
		lblValidatingStatus.setBounds(12, 112, 281, 30);
		add(lblValidatingStatus);
		
		JLabel lblChooseCorrectionalMethod = new JLabel("Please Choose Correctional method");
		lblChooseCorrectionalMethod.setBounds(12, 162, 281, 30);
		add(lblChooseCorrectionalMethod);
		
		String CorrectionalMethod[] = {"Simularity Flooding"};
		comboBox = new JComboBox(CorrectionalMethod);
		comboBox.setBounds(12, 194, 497, 30);
		add(comboBox);
		
		JLabel lblClickNextTo = new JLabel("Click Next to start Correction process");
		lblClickNextTo.setBounds(12, 425, 598, 15);
		add(lblClickNextTo);

	}
	public Graph startValidating(String ValidationName){
		if(ValidationName == "Pairwise Validation"){
			Validation pvalidation = new PairwiseValidation(parent.XMLGraph, parent.modelGraph, parent.xmldocument, parent.model);
			Object validationResult = pvalidation.getValidationResult();
			
			if (validationResult == null || validationResult == ""){
				lblValidatingStatus.setText("Validating Succes. No errors found");
				return null;
			}
			
			return (Graph)validationResult;			
		}
		return null;
	}
	
	public String getCorrectionalMethod(){
		return (String)comboBox.getSelectedItem();
	}

}
