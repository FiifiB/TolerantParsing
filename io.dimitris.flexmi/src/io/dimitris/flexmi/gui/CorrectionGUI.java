package io.dimitris.flexmi.gui;

import graphtools.Graph;
import graphtools.Vertex;
import io.dimitris.flexmi.ErrorCorrector;
import io.dimitris.flexmi.FloodingCorrection;

import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class CorrectionGUI extends JPanel {
	private MainFrameGUI parent;
	private ButtonGroup group;

	/**
	 * Create the panel.
	 */
	public CorrectionGUI(MainFrameGUI parent) {
		setLayout(null);
		this.parent = parent;
		
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
	    group = new ButtonGroup();
	    group.add(rdbtnPrintOutCorrections);
	    group.add(rdbtnAutomaticallyCorrectXml);
		
		JLabel lblClickNextTo = new JLabel("Click Next to continue");
		lblClickNextTo.setBounds(12, 425, 598, 15);
		add(lblClickNextTo);

	}
	
	public String getOutputMethod(){
		JRadioButton radiobutton = (JRadioButton)group.getSelection();
		return radiobutton.getText();
	}
	
	public String startCorrecting(String correctionMethod){
		if (correctionMethod == "Simularity Flooding"){
			FloodingCorrection flooding = new FloodingCorrection(parent.xmldocument);
			Graph propagatedGraph = flooding.propagateGraph(parent.validationResult);
			LinkedHashMap<Vertex, Double> fixpointResult = flooding.runFixpointComputation(propagatedGraph);
			try {
				correctedFile("Test", parent.xmldocument, fixpointResult, parent.modelGraph);
			} catch (IOException e) {
				JOptionPane.showConfirmDialog(this, "Already corrected the xml File/Some other error","Error Correcting",JOptionPane.OK_OPTION);
				e.printStackTrace();
				return "Correction made";
			}
			return System.getProperty("user.dir") + "Test" + "_corrected";
		}
		return "Corrections have been made";
	}
	
	private void correctedFile (String filename,Document doc, LinkedHashMap<Vertex, Double> fixpointResult,Graph modelGraph) throws IOException{
		Document result = new ErrorCorrector(doc, fixpointResult, modelGraph).start();
		
		XMLOutputter outputter =  new XMLOutputter(Format.getCompactFormat());
		FileWriter writer = new FileWriter(filename + "_corrected");
		outputter.output(result, writer);
		writer.flush();
		writer.close();
		
	}
}
