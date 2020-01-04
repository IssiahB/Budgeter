package budg.code.screens;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextArea;

import budg.code.Main;
import budg.code.bill.Bill;
import budg.code.bill.BillManager;
import budg.code.gfx.FrameCreator;
import budg.code.time.DateTime;

public class HomeScreen extends ScreenMaster implements ActionListener {

	private JTextArea infoBoard;
	private volatile String infoText = "";

	public HomeScreen(int width, int height) {
		super(width, height);
	}

	@Override
	protected void addComponents() {
		addButtons();
		addLabels();
		addTextField();

		Thread textUpdate = new Thread(new Runnable() {
			private String formatString() {
				StringBuilder build = new StringBuilder();
				// Statistics
				build.append("    \tStatistics\n");
				build.append("    Key\t\tValue\n\n");
				build.append("    Total Bills:\t              " + BillManager.getNumOfBills() + "\n\n");
				build.append("    Left Over:\t               " + BillManager.getTotalBillAmount() + "\n\n");
				build.append("    Income Total:\t         " + BillManager.getTotalIncome() + "\n\n");
				build.append("    Expense Total:\t       " + BillManager.getTotalExpense() + "\n\n");
				build.append("    Average Expense:\t  " + BillManager.getExpenseMean() + "\n\n");
				build.append("    Average Income:\t    " + BillManager.getIncomeMean() + "\n\n\n\n\n");

				// Bills
				build.append("    \tBills\n");
				build.append("    Key\t\tValue\n\n");
				for (int i = BillManager.getNumOfBills() - 1; i >= 0; i--) {
					Bill bill = BillManager.get(i);
					build.append(bill.getName() + "\n");
					build.append("    Amount:\t     " + bill.getAmount() + "\n");
					build.append("    Due Date:\t   " + bill.getDueDateAsString() + "\n");
					build.append("    Type:          \t" + bill.getType().toString() + "\n\n");
				}
				return build.toString();
			}

			@Override
			public void run() {
				String text = infoText;
				// Keeps updating the text displayed
				while (Main.running) {
					// wait about 1 second
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					// change the text
					infoText = formatString();
					// update text on screen
					if (!text.equals(infoText)) {
						infoBoard.setText(infoText);
						text = infoText;
					}
				}
			}
		});
		textUpdate.start();
	}

	private void addTextField() {
		Font font = new Font("Mono", 1, 20);

		infoBoard = new JTextArea();
		infoBoard.setEditable(false);
		infoBoard.setLineWrap(true);
		infoBoard.setWrapStyleWord(true);
		infoBoard.setBackground(secoundaryColor);
		infoBoard.setFont(font);
		infoBoard.setAutoscrolls(false);
		infoBoard.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
		infoBoard.setBounds(200, 150, 450, 320);

		ScrollPane scrollable = new ScrollPane();
		scrollable.add(infoBoard);
		scrollable.setBounds(infoBoard.getX(), infoBoard.getY(), infoBoard.getWidth(), infoBoard.getHeight());
		scrollable.setBackground(secoundaryColor);

		panel.add(scrollable);
	}

	private void addLabels() {
		Font titleFont = new Font("Roboto", 1, 50);

		JLabel title = new JLabel("Budgeter");
		title.setBounds(250, 20, 400, 100);
		title.setFont(titleFont);

		JLabel desc = new JLabel("Info:");
		desc.setBounds(210, 120, 50, 30);
		desc.setFont(primLabel);

		panel.add(title);
		panel.add(desc);
	}

	private void addButtons() {
		int buttonX = 40;
		int buttonY = 150;

		JButton create = new JButton("Create");
		create.setBackground(secoundaryColor);
		create.setBounds(buttonX, buttonY, buttonWidth, buttonHeight);
		create.setFont(primLabel);
		create.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FrameCreator.setPanel(ScreenHandler.getCreateScreen());
			}
		});

		JButton edit = new JButton("Edit");
		edit.setBackground(secoundaryColor);
		edit.setBounds(buttonX, buttonY + 75, buttonWidth, buttonHeight);
		edit.setFont(primLabel);
		edit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				EditScreen.createBillButtons();
				FrameCreator.setPanel(ScreenHandler.getEditScreen());
			}
		});

		JButton save = new JButton("Delete");
		save.setBackground(secoundaryColor);
		save.setBounds(buttonX, buttonY + 150, buttonWidth, buttonHeight);
		save.setFont(primLabel);
		save.addActionListener(ActionListener -> {
			DeleteScreen.createBillButtons();
			FrameCreator.setPanel(ScreenHandler.getDeleteScreen());
		});

		JButton exit = new JButton("Exit");
		exit.setBackground(secoundaryColor);
		exit.setBounds(buttonX, buttonY + 225, buttonWidth, buttonHeight);
		exit.setFont(primLabel);
		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					BillManager.saveAccount();
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
					System.err.println(DateTime.getCurrentDateAsString() + "\n\tCould not find XML file to save to");
				}
				Main.running = false;
				System.exit(0);
			}
		});

		panel.add(create);
		panel.add(edit);
		panel.add(save);
		panel.add(exit);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

	}
}
