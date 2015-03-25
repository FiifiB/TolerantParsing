package io.dimitris.flexmi.gui;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JList;

public class LoadXMLGUI extends JPanel {
	private JTextField textField;

	/**
	 * Create the panel.
	 */
	public LoadXMLGUI() {
		setLayout(null);
		
		JLabel lblLoadXMLFile = new JLabel("Load XML documents from location");
		lblLoadXMLFile.setBounds(12, 12, 281, 30);
		add(lblLoadXMLFile);
		
		textField = new JTextField();
		textField.setBounds(12, 60, 610, 30);
		add(textField);
		textField.setColumns(10);
		
		JButton btnbrowse = new JButton("..Browse");
		btnbrowse.setBounds(662, 60, 119, 29);
		add(btnbrowse);
		
		JList list = new JList();
		list.setBounds(13, 119, 768, 294);
		add(list);
		
		JLabel lblClickNextTo = new JLabel("Click Next to start tool");
		lblClickNextTo.setBounds(12, 425, 598, 15);
		add(lblClickNextTo);
		

	}
}
