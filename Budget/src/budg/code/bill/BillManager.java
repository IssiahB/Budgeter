package budg.code.bill;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import budg.code.bill.Bill.BillType;
import budg.code.file.XmlManager;

public class BillManager {
	private volatile static List<Bill> accountBills = new ArrayList<Bill>();
	private volatile static int totalBillAmount, totalExpense, totalIncome;
	private volatile static int numOfExpenses, numOfIncomes, numOfBills;
	private volatile static int incomeMean, expenseMean;

	/**
	 * Replaces all the bills in the list of bills stored in the BillManager
	 * 
	 * @param replace - The list to replace with
	 */
	public static void replaceAccount(List<Bill> replace) {
		BillManager.accountBills = replace;
		updateStatistics();
	}

	private static int calculateTotalExpense() {
		int expense = 0;
		Iterator<Bill> iter = accountBills.stream().filter(i -> i.getType() == BillType.expense).iterator();

		while (iter.hasNext()) {
			Bill bill = (Bill) iter.next();
			expense += bill.getAmount();
		}

		return expense;
	}

	private static int calculateTotalIncome() {
		int income = 0;
		Iterator<Bill> iter = accountBills.stream().filter(i -> i.getType() == BillType.income).iterator();

		while (iter.hasNext()) {
			Bill bill = (Bill) iter.next();
			income += bill.getAmount();
		}

		return income;
	}

	private static int getAmountOfExpenses() {
		int amount = 0;
		amount = (int) accountBills.stream().filter(c -> c.getType() == BillType.expense).count();
		return amount;
	}

	private static int getAmountOfIncomes() {
		int amount = 0;
		amount = (int) accountBills.stream().filter(c -> c.getType() == BillType.income).count();
		return amount;
	}

	private static int getIncomeAverage() {
		if (numOfIncomes == 0)
			return 0;
		if (totalIncome == 0)
			return 0;

		int average = 0;
		average = totalIncome / numOfIncomes;

		return average;
	}

	private static int getExpenseAverage() {
		if (numOfExpenses == 0)
			return 0;
		if (totalExpense == 0)
			return 0;

		int average = 0;
		average = totalExpense / numOfExpenses;

		return average;
	}

	private static void updateStatistics() {
		numOfBills = accountBills.size();
		totalExpense = calculateTotalExpense();
		totalIncome = calculateTotalIncome();
		numOfExpenses = getAmountOfExpenses();
		numOfIncomes = getAmountOfIncomes();
		incomeMean = getIncomeAverage();
		expenseMean = getExpenseAverage();
		totalBillAmount = totalIncome - totalExpense;
	}

	/**
	 * Adds a new bill to the list of bills then updates the statistics for that new
	 * bill
	 * 
	 * @param bill - The new bill being added to the list
	 */
	public static void addBill(Bill bill) {
		accountBills.add(bill);
		updateStatistics();
	}

	/**
	 * Removes a bill from the list of bills then updates the statistics for that
	 * new bill
	 * 
	 * @param bill - the bill being removed from the list
	 */
	public static void removeBill(Bill bill) {
		accountBills.remove(bill);
		updateStatistics();
	}
	
	/**
	 * Replaces an old bill in the list with a new bill
	 * 
	 * @param oldBill - the old bill that exists in the list
	 * 
	 * @param newBill - the new bill to replace the old bill
	 */
	public static void replaceBill(Bill oldBill, Bill newBill) {
		if (!accountBills.contains(oldBill))
			return;
		// replace bill
		accountBills.remove(oldBill);
		addBill(newBill);
	}

	/**
	 * Get the bill from the list at the specific index
	 * 
	 * @param index - the index of where to get the bill
	 * 
	 * @return The bill at that index
	 */
	public static Bill get(int index) {
		return (accountBills.get(index));
	}

	/**
	 * Gets all the bills in the accountBills list and returns it as an array of
	 * Bills
	 * 
	 * @return The accountBills as an array of Bills
	 */
	public static Bill[] getBills() {
		Bill[] bills = new Bill[accountBills.size()];
		bills = accountBills.toArray(bills);

		return bills;
	}

	/**
	 * Saves all the bill data into a file
	 * 
	 * @throws FileNotFoundException If the file cannot be found
	 */
	public static void saveAccount() throws FileNotFoundException {
		XmlManager.replaceAccount(accountBills);
	}

	public static int getTotalBillAmount() {
		return totalBillAmount;
	}

	public static int getNumOfExpenses() {
		return numOfExpenses;
	}

	public static int getNumOfIncomes() {
		return numOfIncomes;
	}

	public static int getNumOfBills() {
		return numOfBills;
	}

	public static int getIncomeMean() {
		return incomeMean;
	}

	public static int getExpenseMean() {
		return expenseMean;
	}

	public static int getTotalExpense() {
		return totalExpense;
	}

	public static int getTotalIncome() {
		return totalIncome;
	}
}
