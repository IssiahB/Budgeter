package budg.code.screens;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JPanel;

public abstract class ScreenMaster {
	protected JPanel panel;
	protected Dimension size;
	
	public static Color primaryColor = new Color(112, 146, 191);
	public static Color secoundaryColor = new Color(199, 191, 230);
	
	protected static Font primLabel = new Font("Mono", 1, 20);
	protected static Font secLabel = new Font("Roboto", 1, 15);
	
	protected static int buttonWidth = 100;
	protected static int buttonHeight = 40;
	
	public ScreenMaster(int width, int height) {
		size = new Dimension(width, height);
		panel = new JPanel(null);
		panel.setBackground(primaryColor);
		panel.setMaximumSize(size);
		panel.setMinimumSize(size);
		panel.setPreferredSize(size);
		
		addComponents();
	}
	
	protected abstract void addComponents();

	public JPanel getPanel() {
		return panel;
	}

	public Dimension getSize() {
		return size;
	}
}
