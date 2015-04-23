package correctiontool.gui;

import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.eclipse.emf.ecore.EPackage;

import correctiontool.LoadData;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileFilter;

public class LoadModelGUI extends JPanel {
	private JTextField textField;
	private JButton btnbrowse;
	private JFileChooser fc;
	private MainFrameGUI parent;
	private File modelFile;
	private JLabel lblerrorLabel;
	/**
	 * Create the panel.
	 */
	public LoadModelGUI(MainFrameGUI parent) {
		setLayout(null);
		this.parent = parent;
		
		JLabel lblLoadEmfFile = new JLabel("Load Emf file from location");
		lblLoadEmfFile.setBounds(12, 12, 281, 30);
		add(lblLoadEmfFile);
		
		textField = new JTextField();
		textField.setBounds(12, 60, 610, 30);
		add(textField);
		textField.setColumns(10);
		
		btnbrowse = new JButton("..Browse");
		btnbrowse.setBounds(662, 60, 119, 29);
		btnbrowse.addActionListener(new actionlistner());
		add(btnbrowse);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Status", TitledBorder.LEADING, TitledBorder.TOP, null, Color.DARK_GRAY));
		panel.setBounds(13, 119, 768, 315);
		add(panel);
		panel.setLayout(null);
		
		lblerrorLabel = new JLabel();
		lblerrorLabel.setBounds(27, 51, 588, 43);
		panel.add(lblerrorLabel);
		
		fc = new JFileChooser();
		File workingDirectory = new File(System.getProperty("user.dir"));
		fc.setCurrentDirectory(workingDirectory);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("emf","emf");
		fc.setAcceptAllFileFilterUsed(false);
		fc.addChoosableFileFilter(filter);
		
	}	
	
	public EPackage getModel() throws Exception{
		EPackage model  = null;
		
		
		model = LoadData.getEpackage(modelFile);
		return model;
	}
	
	public void setErrorText(String error){
		lblerrorLabel.setText(error);
	}
	
	public class actionlistner implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent event) {
			JButton src = (JButton) event.getSource();
			
			if(src.equals(btnbrowse)){
				int returnVal = fc.showOpenDialog(LoadModelGUI.this);
				
				if (returnVal == JFileChooser.APPROVE_OPTION) {
		            modelFile = fc.getSelectedFile();
		            textField.setText(modelFile.getAbsolutePath());
		            //This is where a real application would open the file.
		           
		        } else {
		            
		        }
			}

		}
		
	}
}
