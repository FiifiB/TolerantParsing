package io.dimitris.flexmi.gui;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.border.TitledBorder;
import java.awt.Color;

public class LoadModelGUI extends JPanel {
	private JTextField textField;

	/**
	 * Create the panel.
	 */
	public LoadModelGUI() {
		setLayout(null);
		
		JLabel lblLoadEmfFile = new JLabel("Load Emf file from location");
		lblLoadEmfFile.setBounds(12, 12, 281, 30);
		add(lblLoadEmfFile);
		
		textField = new JTextField();
		textField.setBounds(12, 60, 610, 30);
		add(textField);
		textField.setColumns(10);
		
		JButton btnbrowse = new JButton("..Browse");
		btnbrowse.setBounds(662, 60, 119, 29);
		add(btnbrowse);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Status", TitledBorder.LEADING, TitledBorder.TOP, null, Color.DARK_GRAY));
		panel.setBounds(13, 119, 768, 315);
		add(panel);
		
	}
}
