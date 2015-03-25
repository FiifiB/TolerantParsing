package io.dimitris.flexmi.gui;

import java.awt.Container;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Start {

	public Start() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setTitle("My Empty Frame");
		frame.setSize(300,200); // default size is 0,0
		frame.setLocation(10,200); // default is 0,0 (top left corner)
		Container contentPane = frame.getContentPane();
		JPanel panel1 = new JPanel();
		JPanel panel2 = new JPanel();
		
		JTextField textfield =  new JTextField();
		textfield.setSize(100,20);
		JButton button = new JButton("..Browse");
		panel1.add(textfield);
		panel1.add(button);
		contentPane.add(panel1);
		
		frame.setVisible(true);

	}

}
