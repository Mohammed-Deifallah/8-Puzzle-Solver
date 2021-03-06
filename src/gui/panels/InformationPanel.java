package gui.panels;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import file.FileManagement;
import file.FileManagementInf;
import gui.OutputDependentComponent;
import solver.PuzzleSolver;

public class InformationPanel extends JPanel implements OutputDependentComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JLabel infoString;
	
	private JButton writeToFile;
	
	private PuzzleSolver solver;
	
	public InformationPanel(Font font, Color bg, PuzzleSolver solv) {
		this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		this.setBackground(bg);
		infoString = new JLabel();
		solver = solv;
		initializeLabel(infoString,
				buildInfoString("NaN", "NaN", "NaN", "NaN"),
				font);
		
		this.add(Box.createHorizontalGlue());
		writeToFile = new JButton();
		initializeButton(writeToFile, "save2.png", "Save To File");
		writeToFile.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				saveAction();
			}
		});
		writeToFile.setEnabled(false);
		this.add(Box.createHorizontalStrut(10));
	}
	
	@Override
	public void informOutputReady() {
		String cost = String.valueOf(solver.getSearchResult().goal_cost);
		String nodes = String.valueOf(solver.getSearchResult().expanded_list.size());
		String depth = String.valueOf(solver.getSearchResult().search_depth);
		String time = String.valueOf(solver.getSearchResult().search_time / 1e6);
		infoString.setText(buildInfoString(cost, nodes, depth, time));
		writeToFile.setEnabled(true);
	}

	@Override
	public void informOutputUnready() {
		infoString.setText(buildInfoString("NaN", "NaN", "NaN", "NaN"));
		writeToFile.setEnabled(false);
	}
	
	private String buildInfoString(String cost, String nodes, String depth, String time) {
		StringBuilder builder = new StringBuilder();
		builder.append("Goal Cost : ");
		builder.append(cost);
		builder.append(" | Expanded Nodes : ");
		builder.append(nodes);
		builder.append(" | Max Depth : ");
		builder.append(depth);
		builder.append(" | Time : ");
		builder.append(time);
		builder.append(" ms");
		return builder.toString();
	}
	
	private void initializeLabel(JLabel label, String text, Font font) {
		label.setText(text);
		label.setFont(font);
		label.setForeground(Color.LIGHT_GRAY);
		this.add(label);
	}
	
	private void initializeButton(JButton button, String path, String text) {
		Cursor cursor = new Cursor(Cursor.HAND_CURSOR);
		Dimension buttonDimensions = new Dimension(100, 100);
		button.setPreferredSize(buttonDimensions);
		button.setSize(buttonDimensions);
		ImageIcon icon = new ImageIcon(path);
		Image img = icon.getImage();
		Image newimg = img.getScaledInstance((int)buttonDimensions.getWidth(),
				(int)buttonDimensions.getHeight(), java.awt.Image.SCALE_SMOOTH);
		icon = new ImageIcon(newimg);
		button.setIcon(icon);
		button.setToolTipText(text);
		button.setCursor(cursor);
		this.add(button);
	}
	
	/**
	 * Performs the saving action.
	 */
	private void saveAction() {
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter txtfilter
		= new FileNameExtensionFilter(
				"text files (*.txt)", "txt");
		chooser.addChoosableFileFilter(txtfilter);
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.setFileFilter(txtfilter);
		int choice = chooser.showSaveDialog(null);
		if (choice != JFileChooser.APPROVE_OPTION) {
			return;
		}
		File chosenFile = chooser.getSelectedFile();
		FileManagementInf writer = new FileManagement();
		writer.writeToFile(correctName(chosenFile, "txt"), solver);
		
		
	}
	/**
     * Corrects a file given name so that it has the right extension.
     * @param chosenFile
     * The file name.
     * @param extension
     * The required extension.
     * @return
     * The file name with the required extension.
     */
	private File correctName(final File chosenFile,
			final String extension) {
		String[] name = chosenFile.getName().split(".");
		if (name.length != 0
			&& name[name.length - 1].equals(extension)) {
			return chosenFile;
		} else {
			File file = new File(
				chosenFile.toString() + "." + extension);
			return file;
		}
	}
}
