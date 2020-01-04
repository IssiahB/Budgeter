package budg.code.screens;

import javax.swing.JPanel;

public class ScreenHandler {
	private static HomeScreen home;
	private static CreateScreen create;
	private static EditScreen edit;
	private static DeleteScreen delete;

	public static void setup(int width, int height) {
		home = new HomeScreen(width, height);
		create = new CreateScreen(width, height);
		edit = new EditScreen(width, height);
		delete = new DeleteScreen(width, height);
	}

	public static JPanel getHomeScreen() {
		return home.panel;
	}

	public static JPanel getCreateScreen() {
		return create.panel;
	}

	public static JPanel getEditScreen() {
		return edit.panel;
	}

	public static JPanel getDeleteScreen() {
		return delete.panel;
	}
}
