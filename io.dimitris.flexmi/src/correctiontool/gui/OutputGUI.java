package correctiontool.gui;

import graphtools.Graph;
import graphtools.Vertex;

import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import org.jdom2.Document;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import correctiontool.ErrorCorrector;
import correctiontool.ErrorReporter;

public class OutputGUI extends JPanel {
	private MainFrameGUI parent;
	private JLabel lblLocationFile;
	private JLabel lblOutputStatus;

	/**
	 * Create the panel.
	 */
	public OutputGUI(MainFrameGUI parent) {
		setLayout(null);
		this.parent = parent;
		
		lblOutputStatus = new JLabel("...Output Success");
		lblOutputStatus.setBounds(12, 112, 281, 30);
		add(lblOutputStatus);
		
		lblLocationFile = new JLabel("Please find the newly corrected XML document in \n C:/Users/John/Documents/  as CorrectedLibrary.XML");
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
	
	public void startOutput(String outputMethod){
		lblOutputStatus.setText("...Processing Output...");
		if(outputMethod == "Automatically correct XML document"){
			try {
				correctFile("Test", parent.xmldocument, parent.correctionResult, parent.modelGraph);
				lblLocationFile.setText("Please find the newly corrected XML document in \n" + System.getProperty("user.dir") + "Test" + "_corrected.xml");
			} catch (IOException e) {
				JOptionPane.showConfirmDialog(this, "Already corrected the xml File/Some other error","Error Correcting",JOptionPane.OK_OPTION);
				e.printStackTrace();
			}
		}else if(outputMethod == "Print out corrections"){
			printCorrections(parent.xmldocument, parent.correctionResult);
		}
	
		
	}
	
	private void correctFile (String filename,Document doc, LinkedHashMap<Vertex, Double> fixpointResult,Graph modelGraph) throws IOException{
		Document result = new ErrorCorrector(doc, fixpointResult, modelGraph).start();
		
		XMLOutputter outputter =  new XMLOutputter(Format.getCompactFormat());
		FileWriter writer = new FileWriter(filename + "_corrected");
		outputter.output(result, writer);
		writer.flush();
		writer.close();
		
		
	}
	
	private void printCorrections(Document XMLDocument, LinkedHashMap<Vertex, Double> bestMatchings){
		ErrorReporter reporter = new ErrorReporter(XMLDocument, bestMatchings);
	}
}
