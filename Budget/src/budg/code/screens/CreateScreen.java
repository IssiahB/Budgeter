package budg.code.screens;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import budg.code.Main;
import budg.code.bill.Bill;
import budg.code.bill.Bill.BillImportance;
import budg.code.bill.Bill.BillType;
import budg.code.bill.BillManager;
import budg.code.gfx.FrameCreator;

public class CreateScreen extends ScreenMaster {

	private static Bill bill = new Bill();
	private static Bill oldBill = new Bill();
	private static JTextField overview;
	private JTextArea status;

	private static volatile boolean inEditMode = false;

	// Refresh Components
	private static JTextField name;
	private static JSpinner amount;
	private static JRadioButton income;
	private static JRadioButton expense;
	private static JRadioButton lowImp;
	private static JRadioButton medImp;
	private static JRadioButton highImp;
	private static JSpinner month;
	private static JSpinner day;
	private static JSpinner year;

	public CreateScreen(int width, int height) {
		super(width, height);
	}

	@Override
	protected void addComponents() {
		addButtons();
		addBasicBillInfo();
		addLabels();
		addBillType();
		addDueDate();
		addImportance();
		addStatus();
		addOverview();

		Thread statusThread = new Thread(new Runnable() {
			private int totalIncome;
			private int totalExpense;
			private int leftAmount;
			
			private void updateVariables() {
				if (!inEditMode) {
					if (bill.getType() == BillType.income) {
						// update totalIncome with current bill income
						totalIncome = ((BillManager.getTotalIncome()) + bill.getAmount());
						totalExpense = (BillManager.getTotalExpense());
					}
					// Check if current bill is expense
					if (bill.getType() == BillType.expense) {
						// update totalExpense with current bill expense
						totalExpense = ((BillManager.getTotalExpense()) + bill.getAmount());
						totalIncome = (BillManager.getTotalIncome());
					}
				} else {
					/* If the bill that you selected to edit <oldBill> was of the type income, then when this edit bill's
					 * income type is selected take away the old bill's amount that you selected to edited and Vies Versa.
					 * This allows the user to view the current total amount without the oldBill getting in the way.
					 */
					if (oldBill.getType() == BillType.income) {
						if (bill.getType() == BillType.income) {
							totalIncome = (BillManager.getTotalIncome() + (bill.getAmount() - oldBill.getAmount()));
							totalExpense = BillManager.getTotalExpense();
						} else {
							totalExpense = (BillManager.getTotalExpense() + bill.getAmount());
							totalIncome = (BillManager.getTotalIncome() - oldBill.getAmount());
						}
					} else {
						if (bill.getType() == BillType.income) {
							totalIncome = (BillManager.getTotalIncome() + bill.getAmount());
							totalExpense = (BillManager.getTotalExpense() - oldBill.getAmount());
						} else {
							totalExpense = (BillManager.getTotalExpense() + (bill.getAmount() - oldBill.getAmount()));
							totalIncome = BillManager.getTotalIncome();
						}
					}
				}
				// Calculate Left Over Amount
				leftAmount = (totalIncome - totalExpense);
			}

			private void updateStatusText() {
				StringBuilder build = new StringBuilder();
				build.append("  Total Income: $" + totalIncome + "\n");
				build.append("  Total Expense: $" + totalExpense + "\n");
				build.append("  Left Over: $" + leftAmount);

				status.setText(build.toString());
			}

			@Override
			public void run() {
				while (Main.running) {
					// wait about 1 second
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
						updateVariables();
						updateStatusText();
				}
			}
		});
		statusThread.start();
	}

	private void refreshBillComponents() {
		name.setText("");
		amount.setValue(0);
		income.setSelected(true);
		expense.setSelected(false);
		lowImp.setSelected(true);
		medImp.setSelected(false);
		highImp.setSelected(false);
		month.setValue(0);
		day.setValue(0);
		year.setValue(0);
	}

	private void addButtons() {
		int buttonX = 40;
		int buttonY = 375;

		JButton cancel = new JButton("Cancel");
		cancel.setBounds(buttonX, buttonY, buttonWidth, buttonHeight);
		cancel.setBackground(secoundaryColor);
		cancel.setFont(primLabel);
		cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (inEditMode) {
					bill = new Bill();
					oldBill = new Bill();
					overview.setText(bill.getSum());
					refreshBillComponents();
					inEditMode = false;
				}
				FrameCreator.setPanel(ScreenHandler.getHomeScreen());
			}
		});

		JButton create = new JButton("Done");
		create.setBounds(buttonX + 520, buttonY, buttonWidth, buttonHeight);
		create.setBackground(secoundaryColor);
		create.setFont(primLabel);
		create.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int[] due = bill.getDueDate();
				if (bill.getName().equals(""))
					return;
				if (bill.getAmount() == 0)
					return;
				if (due[0] == 0 || due[1] == 0 || due[2] == 0)
					return;
				// create bill
				if (!inEditMode)
					BillManager.addBill(bill);
				// edited bill
				if (inEditMode) {
					BillManager.replaceBill(oldBill, bill);
					inEditMode = false;
				}

				bill = new Bill();
				oldBill = new Bill(); 
				overview.setText(bill.getSum());
				refreshBillComponents();

				FrameCreator.setPanel(ScreenHandler.getHomeScreen());
			}
		});

		panel.add(cancel);
		panel.add(create);
	}

	private void addBasicBillInfo() {
		name = new JTextField();
		name.setToolTipText("Name");
		name.setFont(secLabel);
		name.setBackground(secoundaryColor);
		name.setBounds(40, 100, 150, 30);
		name.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				bill.setName(name.getText());
				overview.setText(bill.getSum());
			}
		});

		amount = new JSpinner();
		amount.setToolTipText("Amount");
		amount.setFont(secLabel);
		amount.setBackground(secoundaryColor);
		amount.setBounds(40, 160, 150, 30);
		amount.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				int billAmount = (int) amount.getValue();

				if (billAmount > 1000000)
					amount.setValue(1000000);
				if (billAmount < 0)
					amount.setValue(0);

				bill.setAmount(billAmount);
				overview.setText(bill.getSum());
			}
		});

		panel.add(name);
		panel.add(amount);
	}

	private void addLabels() {
		JLabel billName = new JLabel("Bill Name:");
		billName.setBackground(secoundaryColor);
		billName.setFont(secLabel);
		billName.setBounds(40, 75, 100, 30);

		JLabel billAmount = new JLabel("Bill Amount:");
		billAmount.setBackground(secoundaryColor);
		billAmount.setFont(secLabel);
		billAmount.setBounds(40, 135, 150, 30);

		JLabel billType = new JLabel("Bill Type:");
		billType.setBackground(secoundaryColor);
		billType.setFont(secLabel);
		billType.setBounds(40, 220, 100, 30);

		JLabel billDue = new JLabel("Due Date:");
		billDue.setBackground(secoundaryColor);
		billDue.setFont(secLabel);
		billDue.setBounds(300, 75, 100, 30);

		JLabel billImp = new JLabel("Importance:");
		billImp.setBackground(secoundaryColor);
		billImp.setFont(secLabel);
		billImp.setBounds(300, 155, 100, 30);

		JLabel status = new JLabel("Status:");
		status.setBackground(secoundaryColor);
		status.setFont(secLabel);
		status.setBounds(300, 220, 100, 30);

		panel.add(billName);
		panel.add(billAmount);
		panel.add(billType);
		panel.add(billDue);
		panel.add(billImp);
		panel.add(status);
	}

	private void addBillType() {
		income = new JRadioButton("Income");
		expense = new JRadioButton("Expence");

		income.setSelected(true);

		// Income Type
		income.setBounds(50, 250, 100, 30);
		income.setFont(secLabel);
		income.setBackground(primaryColor);
		income.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (income.isSelected()) {
					expense.setSelected(false);
					bill.setType(BillType.income);
					overview.setText(bill.getSum());
				}
				income.setSelected(true);
			}
		});

		// Expense Type
		expense.setBounds(50, 280, 100, 30);
		expense.setFont(secLabel);
		expense.setBackground(primaryColor);
		expense.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (expense.isSelected()) {
					income.setSelected(false);
					bill.setType(BillType.expense);
					overview.setText(bill.getSum());
				}
				expense.setSelected(true);
			}
		});

		panel.add(income);
		panel.add(expense);
	}

	private void addDueDate() {
		month = new JSpinner();
		day = new JSpinner();
		year = new JSpinner();

		month.setToolTipText("Month");
		month.setFont(secLabel);
		month.setBackground(secoundaryColor);
		month.setBounds(300, 100, 100, 30);
		month.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				int monthValue = (int) month.getValue();

				if (monthValue > 12)
					month.setValue(12);
				if (monthValue < 0)
					month.setValue(0);

				bill.setMonth(monthValue);
				overview.setText(bill.getSum());
			}
		});

		day.setToolTipText("Day");
		day.setFont(secLabel);
		day.setBackground(secoundaryColor);
		day.setBounds(420, 100, 100, 30);
		day.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				int dayValue = (int) day.getValue();

				if (dayValue > 31)
					day.setValue(31);
				if (dayValue < 0)
					day.setValue(0);

				bill.setDay(dayValue);
				overview.setText(bill.getSum());
			}
		});

		year.setToolTipText("Year");
		year.setFont(secLabel);
		year.setBackground(secoundaryColor);
		year.setBounds(540, 100, 100, 30);
		year.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				int yearValue = (int) year.getValue();

				if (yearValue > 30)
					year.setValue(30);
				if (yearValue < 0)
					year.setValue(0);

				bill.setYear(yearValue);
				overview.setText(bill.getSum());
			}
		});

		panel.add(month);
		panel.add(day);
		panel.add(year);
	}

	private void addImportance() {
		lowImp = new JRadioButton("Low");
		medImp = new JRadioButton("Med");
		highImp = new JRadioButton("High");

		lowImp.setSelected(true);

		lowImp.setBounds(320, 180, 100, 30);
		lowImp.setFont(secLabel);
		lowImp.setBackground(primaryColor);
		lowImp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (medImp.isSelected() || highImp.isSelected()) {
					medImp.setSelected(false);
					highImp.setSelected(false);
					bill.setImportance(BillImportance.low);
					overview.setText(bill.getSum());
				}

				lowImp.setSelected(true);
			}
		});

		medImp.setBounds(440, 180, 100, 30);
		medImp.setFont(secLabel);
		medImp.setBackground(primaryColor);
		medImp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (lowImp.isSelected() || highImp.isSelected()) {
					lowImp.setSelected(false);
					highImp.setSelected(false);
					bill.setImportance(BillImportance.med);
					overview.setText(bill.getSum());
				}

				medImp.setSelected(true);
			}
		});

		highImp.setBounds(560, 180, 100, 30);
		highImp.setFont(secLabel);
		highImp.setBackground(primaryColor);
		highImp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (medImp.isSelected() || lowImp.isSelected()) {
					medImp.setSelected(false);
					lowImp.setSelected(false);
					bill.setImportance(BillImportance.high);
					overview.setText(bill.getSum());
				}

				highImp.setSelected(true);
			}
		});

		panel.add(lowImp);
		panel.add(medImp);
		panel.add(highImp);
	}

	private void addStatus() {
		status = new JTextArea();
		Font font = new Font("Mono", 1, 20);

		status.setEditable(false);
		status.setLineWrap(false);
		status.setWrapStyleWord(false);
		status.setBackground(secoundaryColor);
		status.setFont(font);
		status.setAutoscrolls(false);
		status.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		status.setBounds(300, 250, 340, 100);

		ScrollPane scroll = new ScrollPane();
		scroll.add(status);
		scroll.setBounds(status.getX(), status.getY(), status.getWidth(), status.getHeight());
		scroll.setBackground(secoundaryColor);

		panel.add(scroll);
	}

	private void addOverview() {
		overview = new JTextField();
		overview.setText(bill.getSum());

		overview.setEditable(false);
		overview.setBackground(secoundaryColor);
		overview.setFont(secLabel);
		overview.setBounds(50, 25, 550, 30);

		panel.add(overview);
	}

	public static void editBill(Bill bill) {
		Bill.copyBill(bill, CreateScreen.bill);
		oldBill = bill;
		// set GUI
		overview.setText(bill.getSum());
		name.setText(bill.getName());
		amount.setValue(bill.getAmount());
		month.setValue(bill.getDueDate()[0]);
		day.setValue(bill.getDueDate()[1]);
		year.setValue(bill.getDueDate()[2]);

		if (bill.getType() == BillType.income) {
			income.setSelected(true);
			expense.setSelected(false);
		} else {
			expense.setSelected(true);
			income.setSelected(false);
		}

		if (bill.getImportance() == BillImportance.low) {
			lowImp.setSelected(true);
			medImp.setSelected(false);
			highImp.setSelected(false);
		} else if (bill.getImportance() == BillImportance.med) {
			lowImp.setSelected(false);
			medImp.setSelected(true);
			highImp.setSelected(false);
		} else {
			lowImp.setSelected(false);
			medImp.setSelected(false);
			highImp.setSelected(true);
		}
	}

	public static void setInEditMode(boolean inEditMode) {
		CreateScreen.inEditMode = inEditMode;
	}

	public static boolean isInEditMode() {
		return inEditMode;
	}

}
