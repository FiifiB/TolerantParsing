package io.dimitris.flexmi.gui;

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
	}
	
	public class actionlistner implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent event) {
			JButton src = (JButton) event.getSource();
			
			if(src.equals(next)){
				if(panel == 1){
					try {
						model = loadmodelpanel.getModel();
						cardlayout.show(contentPanel, "loadxml");
						panel =  panel + 1;
					} catch (Exception e) {
						StringWriter sw = new StringWriter();
						e.printStackTrace(new PrintWriter(sw));
						String exceptionAsString = sw.toString();
						loadmodelpanel.lblerrorLabel.setText(exceptionAsString);
						
					}
				}
				if(panel == 2){
					try {
						xmldocument = loadxmlpanel.getDocument();
						cardlayout.show(contentPanel, "graphtransform");
						panel =  panel + 1;
					} catch (Exception e) {
						StringWriter sw = new StringWriter();
						e.printStackTrace(new PrintWriter(sw));
						String exceptionAsString = sw.toString();
						JOptionPane.showConfirmDialog(loadxmlpanel, exceptionAsString);						
					}
				}
				if(panel == 3){
					validationMethod = graphtransformpanel.getValidationMethodName();
					if (validationMethod == null || validationMethod == "")
						JOptionPane.showConfirmDialog(graphtransformpanel, "Please select valid validation", "Error", JOptionPane.OK_OPTION);
					else{
						panel =  panel + 1;
						cardlayout.show(contentPanel, "validation");
						validationResult = validatingpanel.startValidating(validationMethod);
					}					
				}
				if(panel == 4){
					correctionMethod = validatingpanel.getCorrectionalMethod();
					if (correctionMethod == null || correctionMethod == "")
						JOptionPane.showConfirmDialog(validatingpanel, "Please select valid correction", "Error", JOptionPane.OK_OPTION);
					else{
						panel =  panel + 1;
						cardlayout.show(contentPanel, "correction");
//						correctionResult = 
					}			
				}
				if(panel == 5){
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
				if(panel != 1){
					panel = panel -1;
				}
				cardlayout.previous(contentPanel);
			}
			
		}
		
	}
}
