package budg.code.gfx;

import javax.swing.JFrame;
import javax.swing.JPanel;

import budg.code.screens.ScreenHandler;

public class FrameCreator {
	private static JFrame frame;
	private static JPanel panel;
	private static int width, height;
	
	// TODO make the frame DISPOSE_ON_CLOSE and make an updater to find that and turn runnable to false when that happens
	
	public static void createFrame(String title, int width, int height) {
		FrameCreator.width = width;
		FrameCreator.height = height;
		FrameCreator.frame = new JFrame(title);
		FrameCreator.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		FrameCreator.frame.setResizable(false);
		
		panel = ScreenHandler.getHomeScreen();
		panel.validate();
		
		frame.add(panel);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	public static void setPanel(JPanel secondPanel) {
		if (secondPanel == null)
			return;
		
		panel.setVisible(false);
		panel.setEnabled(false);
		frame.remove(panel);
		panel = secondPanel;
		panel.setVisible(true);
		panel.setEnabled(true);
		frame.add(panel);
		frame.validate();
	}
	
	public static int getWidth() {
		return FrameCreator.width;
	}
	
	public static int getHight() {
		return FrameCreator.height;
	}
}
