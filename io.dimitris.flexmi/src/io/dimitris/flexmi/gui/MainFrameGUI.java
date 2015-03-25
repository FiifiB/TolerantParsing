package io.dimitris.flexmi.gui;

import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JFrame;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

public class MainFrameGUI {

	private JFrame frame;
	private JButton next = new JButton("Next");
	private JButton previous = new JButton("Previous");
	private LoadModelGUI loadmodelpanel =  new LoadModelGUI();
	private LoadXMLGUI loadxmlpanel = new LoadXMLGUI();
	private GraphTransformGUI graphtransformpanel = new GraphTransformGUI();
	private ValidateGUI validatingpanel = new ValidateGUI();
	private JPanel contentPanel ;
	private CardLayout cardlayout = new CardLayout();

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
		
		cardlayout.show(contentPanel, "loadmodel");
	}
	
	public class actionlistner implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent event) {
			JButton src = (JButton) event.getSource();
			
			if(src.equals(next)){
				cardlayout.next(contentPanel);;
			}
			if(src.equals(previous)){
				cardlayout.previous(contentPanel);
			}
			
		}
		
	}
}
