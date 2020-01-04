package budg.code.screens;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;

import budg.code.bill.Bill;
import budg.code.bill.BillManager;
import budg.code.gfx.FrameCreator;

public class DeleteScreen extends ScreenMaster {

	private static JPanel buttonHolder;
	private static Bill selectedBill = null;

	public DeleteScreen(int width, int height) {
		super(width, height);
	}

	@Override
	protected void addComponents() {
		addButtons();
		addButtonHolder();
		createBillButtons();
	}

	public static void createBillButtons() {
		buttonHolder.removeAll();
		Bill[] bills = BillManager.getBills();
		List<JButton> buttons = new ArrayList<JButton>();

		// create new button for each bill in list
		for (Bill b : bills) {
			JButton button = new JButton(b.getName());
			button.setFont(primLabel);
			button.setBackground(secoundaryColor);
			// when button clicked
			button.addActionListener(ActionListener -> {
				// if button already selected
				if (selectedBill == b) {
					// unselected button and change back color
					selectedBill = null;
					button.setBackground(secoundaryColor);
				} else {
					// select current button
					selectedBill = b;
					// change back color of all buttons
					Iterator<JButton> iter = buttons.iterator();
					while (iter.hasNext()) {
						JButton iterButton = iter.next();

						iterButton.setBackground(secoundaryColor);
					}
					// change current button's color
					button.setBackground(primaryColor);
				}
			});

			buttons.add(button);
			buttonHolder.add(button);
		}
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
				// Switch to home screen
				FrameCreator.setPanel(ScreenHandler.getHomeScreen());
				selectedBill = null;
			}
		});

		JButton select = new JButton("Delete");
		select.setBounds(buttonX + 520, buttonY, buttonWidth, buttonHeight);
		select.setBackground(secoundaryColor);
		select.setFont(primLabel);
		select.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (selectedBill == null)
					return;
				// remove current bill from list
				BillManager.removeBill(selectedBill);
				// switch to home screen
				FrameCreator.setPanel(ScreenHandler.getHomeScreen());
				selectedBill = null;
			}
		});

		panel.add(cancel);
		panel.add(select);
	}

	private void addButtonHolder() {
		buttonHolder = new JPanel(new GridLayout(5, 10));

		buttonHolder.setBackground(secoundaryColor);
		buttonHolder.setBounds(40, 40, 620, 285);

		panel.add(buttonHolder);
	}

}
