package gui.panels;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import gui.OutputDependentComponent;
import gui.workers.TreeWorker;
import solver.PuzzleSolver;

public class SearchTreePanel extends JPanel implements OutputDependentComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton start, reset;
	/**
	 * scroll pane for the graph.
	 */
	private JScrollPane pane;
	private TreePanel tree_panel;
	private TreeWorker worker;
	private PuzzleSolver solver;

	public SearchTreePanel(PuzzleSolver solv, JFrame parent_frame) {
		solver = solv;
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
		start = new JButton();
		initializeButton(start, "start.png", "Simulate", buttonPanel);
		buttonPanel.add(Box.createHorizontalGlue());
		start.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				worker.cancel(true);
				worker = createNewTreeWorker();
				worker.execute();
				reset.setEnabled(true);
				start.setEnabled(false);
			}
		});

		reset = new JButton();
		initializeButton(reset, "reset.png", "Reset", buttonPanel);
		reset.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				worker.cancel(true);
				worker = createNewTreeWorker();
				reset.setEnabled(false);
				start.setEnabled(true);
				tree_panel.build_model();
			}
		});
		start.setEnabled(false);
		reset.setEnabled(false);
		worker = createNewTreeWorker();

		this.setLayout(new BorderLayout());

		tree_panel = new TreePanel(solver);
		pane = new JScrollPane(tree_panel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		tree_panel.pane = pane;
		this.add(buttonPanel, BorderLayout.NORTH);
		this.add(pane, BorderLayout.CENTER);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	}

	@Override
	public void informOutputReady() {
		worker.cancel(true);
		worker = createNewTreeWorker();
		start.setEnabled(true);
		reset.setEnabled(false);
		tree_panel.build_model();
	}

	@Override
	public void informOutputUnready() {
		worker.cancel(true);
		worker = createNewTreeWorker();
		start.setEnabled(false);
		reset.setEnabled(false);
		tree_panel.reset_model();
	}

	private void initializeButton(JButton button, String path, String text, JPanel panel) {
		Cursor cursor = new Cursor(Cursor.HAND_CURSOR);
		Dimension buttonDimensions = new Dimension(60, 60);
		button.setPreferredSize(buttonDimensions);
		button.setSize(buttonDimensions);
		ImageIcon icon = new ImageIcon(path);
		Image img = icon.getImage();
		Image newimg = img.getScaledInstance((int) buttonDimensions.getWidth(), (int) buttonDimensions.getHeight(),
				java.awt.Image.SCALE_SMOOTH);
		icon = new ImageIcon(newimg);
		button.setIcon(icon);
		button.setToolTipText(text);
		button.setCursor(cursor);
		panel.add(button);
	}

	private TreeWorker createNewTreeWorker() {
		return new TreeWorker(tree_panel, solver);
	}
}
