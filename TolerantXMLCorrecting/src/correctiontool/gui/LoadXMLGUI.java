package correctiontool.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.jdom2.Document;
import org.jdom2.JDOMException;

import correctiontool.LoadData;

public class LoadXMLGUI extends JPanel {
	private JTextField textField;
	private JButton btnbrowse;
	private JFileChooser fc;
	private MainFrameGUI parent;
	private File documentFile;
	private JLabel lblerrorLabel;

	/**
	 * Create the panel.
	 */
	public LoadXMLGUI(MainFrameGUI parent) {
		setLayout(null);
		this.parent = parent;
		
		JLabel lblLoadXMLFile = new JLabel("Load XML documents from location");
		lblLoadXMLFile.setBounds(12, 12, 281, 30);
		add(lblLoadXMLFile);
		
		textField = new JTextField();
		textField.setBounds(12, 60, 610, 30);
		add(textField);
		textField.setColumns(10);
		
		btnbrowse = new JButton("..Browse");
		btnbrowse.setBounds(662, 60, 119, 29);
		btnbrowse.addActionListener(new actionlistner());
		add(btnbrowse);
		
//		JTextArea list = new JTextArea();
//		list.setBounds(13, 119, 768, 294);
//		add(list);
//		list.setText("This is a test to see where the text is");
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Status", TitledBorder.LEADING, TitledBorder.TOP, null, Color.DARK_GRAY));
		panel.setBounds(13, 119, 768, 294);
		add(panel);
		panel.setLayout(null);
		
		lblerrorLabel = new JLabel();
		lblerrorLabel.setBounds(27, 51, 588, 43);
		panel.add(lblerrorLabel);
		
		JLabel lblClickNextTo = new JLabel("Click Next to start tool");
		lblClickNextTo.setBounds(12, 425, 598, 15);
		add(lblClickNextTo);
		
		fc = new JFileChooser();
		File workingDirectory = new File(System.getProperty("user.dir"));
		fc.setCurrentDirectory(workingDirectory);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("XML format","xml");
		fc.setAcceptAllFileFilterUsed(false);
		fc.addChoosableFileFilter(filter);
		

	}
	
	public void setErrorText (String error){
		lblerrorLabel.setText(error);
	}
	
	public Document getDocument() throws JDOMException, IOException{
		Document document = null ;
		document = LoadData.getDocument(documentFile);
		return document;
	}
	
	public class actionlistner implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent event) {
			JButton src = (JButton) event.getSource();
			
			if(src.equals(btnbrowse)){
				int returnVal = fc.showOpenDialog(LoadXMLGUI.this);
				
				if (returnVal == JFileChooser.APPROVE_OPTION) {
		            documentFile = fc.getSelectedFile();
		            textField.setText(documentFile.getAbsolutePath());
		            //This is where a real application would open the file.
		           
		        } else {
		            
		        }
			}

		}
		
	}
}
