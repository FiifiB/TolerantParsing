package correctiontool.gui;

import graphtools.Graph;
import graphtools.Vertex;

import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.LinkedHashMap;

import javax.swing.JPanel;

import org.eclipse.emf.ecore.EPackage;
import org.jdom2.Document;

public class MainFrameGUI {

	private JFrame frame;
	private JButton next = new JButton("Next");
	private JButton previous = new JButton("Previous");
	private LoadModelGUI loadmodelpanel =  new LoadModelGUI(this);
	private LoadXMLGUI loadxmlpanel = new LoadXMLGUI(this);
	private GraphTransformGUI graphtransformpanel = new GraphTransformGUI(this);
	private ValidateGUI validatingpanel = new ValidateGUI(this);
	private CorrectionGUI correctionpanel = new CorrectionGUI(this);
	private OutputGUI outputpanel = new OutputGUI(this);
	private JPanel contentPanel ;
	private CardLayout cardlayout = new CardLayout();
	public int panel = 1;
	public EPackage model;
	public Document xmldocument;
	public Graph modelGraph;
	public Graph XMLGraph;
	public String validationMethod;
	public Graph validationResult;
	public String correctionMethod;
	public LinkedHashMap<Vertex, Double> correctionResult;
	public String outputMethod;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrameGUI window = new MainFrameGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainFrameGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 820, 522);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		contentPanel = new JPanel();
		contentPanel.setBounds(12, 0, 794, 448);
		frame.getContentPane().add(contentPanel);
		contentPanel.setLayout(cardlayout);
		
		actionlistner al = new actionlistner();
		
		next.setBounds(674, 460, 117, 25);
		next.addActionListener(al);
		frame.getContentPane().add(next);
		
		previous.setBounds(22, 456, 117, 25);
		previous.addActionListener(al);
		frame.getContentPane().add(previous);
		
		contentPanel.add(loadmodelpanel,"loadmodel");
		contentPanel.add(loadxmlpanel, "loadxml");
		contentPanel.add(graphtransformpanel, "graphtransform");
		contentPanel.add(validatingpanel, "validation");
		contentPanel.add(correctionpanel, "correction");
		contentPanel.add(outputpanel, "output");
		
		
		cardlayout.show(contentPanel, "loadmodel");
		panel = 1;
	}
	
	public class actionlistner implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent event) {
			JButton src = (JButton) event.getSource();
			
			if(src.equals(next)){
				if(panel == 1){
					try {
						System.out.println("executed 1");
						model = loadmodelpanel.getModel();
						cardlayout.show(contentPanel, "loadxml");
						panel ++;
					} catch (NullPointerException npe) {
						loadmodelpanel.setErrorText("No Input file selected");
						JOptionPane.showConfirmDialog(loadmodelpanel
								, "No meta-model file provided.\nPlease provide a suitable meta-model"
								,"Blank Input"
								,JOptionPane.OK_CANCEL_OPTION
								,JOptionPane.INFORMATION_MESSAGE);
					} catch (Exception e) {
						StringWriter sw = new StringWriter();
						e.printStackTrace(new PrintWriter(sw));
						String exceptionAsString[] = sw.toString().split("\n");
						int indexSubstring = exceptionAsString[0].indexOf("Exception");
						loadmodelpanel.setErrorText(exceptionAsString[0].substring(indexSubstring+10));
						JOptionPane.showConfirmDialog(loadmodelpanel
								, "An error occured with the meta-model.\nLook at panel for details"
								,"Meta-Model Error"
								,JOptionPane.OK_CANCEL_OPTION
								,JOptionPane.INFORMATION_MESSAGE);
						
					}
				}
				else if(panel == 2){
					try {
						System.out.println("executed 2");
						xmldocument = loadxmlpanel.getDocument();
						cardlayout.show(contentPanel, "graphtransform");
						panel ++;
					} catch (NullPointerException npe) {
						loadxmlpanel.setErrorText("No Input file selected");
						JOptionPane.showConfirmDialog(loadxmlpanel
								, "No XML file provided.\nPlease provide a suitable XML document"
								,"Blank Input"
								,JOptionPane.OK_CANCEL_OPTION
								,JOptionPane.INFORMATION_MESSAGE);
					} catch (Exception e) {
						StringWriter sw = new StringWriter();
						e.printStackTrace(new PrintWriter(sw));
						String exceptionAsString[] = sw.toString().split("\n");
						int indexSubstring = exceptionAsString[0].indexOf("Exception");
						loadxmlpanel.setErrorText(exceptionAsString[0].substring(indexSubstring+10));
						JOptionPane.showConfirmDialog(loadxmlpanel
								, "An error occured with the XML document.\nLook at panel for details"
								,"XML Error"
								,JOptionPane.OK_CANCEL_OPTION
								,JOptionPane.INFORMATION_MESSAGE);
					}
				}
				else if(panel == 3){
					graphtransformpanel.transformFiles();
					validationMethod = graphtransformpanel.getValidationMethodName();
					if (validationMethod == null || validationMethod == "")
						JOptionPane.showConfirmDialog(graphtransformpanel, "Please select valid validation", "Error", JOptionPane.OK_OPTION);
					else{
						panel++;
						cardlayout.show(contentPanel, "validation");
						validationResult = validatingpanel.startValidating(validationMethod);
					}					
				}
				else if(panel == 4){
					correctionMethod = validatingpanel.getCorrectionalMethod();
					if (correctionMethod == null || correctionMethod == "")
						JOptionPane.showConfirmDialog(validatingpanel, "Please select valid correction", "Error", JOptionPane.OK_OPTION);
					else{
						panel =  panel + 1;
						cardlayout.show(contentPanel, "correction");
						correctionResult = correctionpanel.startCorrecting(correctionMethod);
					}			
				}
				else if(panel == 5){
					outputMethod = correctionpanel.getOutputMethod();
					if (outputMethod == null || outputMethod == "")
						JOptionPane.showConfirmDialog(validatingpanel, "Please select valid Output method", "Error", JOptionPane.OK_OPTION);
					else{
						panel =  panel + 1;
						cardlayout.show(contentPanel, "output");
						outputpanel.startOutput(outputMethod);
					}			
				}
				
			}
			if(src.equals(previous)){
				if(panel > 1){
					panel = panel - 1;
					cardlayout.previous(contentPanel);
				}
			}
			
		}
		
	}
}
