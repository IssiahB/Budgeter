package budg.code.bill;

public class Bill {
	
	private String billName = "";
	private int billAmount = 0;
	private int[] due = {0, 0, 0};
	
	private String billSum = "";
	private String billStatus = "";
	
	private BillImportance importance = BillImportance.low;
	private BillType type = BillType.income;
	
	public static final int MAX_BILL_COST = 1000000;
	public static final int MIN_BILL_COST = 0;
	
	public static enum BillImportance {
		low, med, high;
	}
	public static enum BillType {
		income, expense;
	}
	
	public static BillImportance getImportanceFromString(String imp) {
		BillImportance importance = null;
		
		switch(imp) {
		case "low":
			importance = BillImportance.low;
			break;
		case "med":
			importance = BillImportance.med;
			break;
		case "high":
			importance = BillImportance.high;
			break;
		default:
			importance = BillImportance.low;
		}
		
		return importance;
	}
	
	public static BillType getTypeFromString(String tpe) {
		BillType type = null;
		
		if (tpe.equals("income"))
			type = BillType.income;
		if (tpe.equals("expense"))
			type = BillType.expense;
		
		return type;
	}
	
	public static void copyBill(Bill from, Bill to) {
		to.setName(from.getName());
		to.setAmount(from.getAmount());
		// due date
		int month, day, year;
		month = from.getDueDate()[0];
		day = from.getDueDate()[1];
		year = from.getDueDate()[2];
		to.setDueDate(month, day, year);
		
		to.setType(from.getType());
		to.setImportance(from.getImportance());
	}
	
	public void setName(String name) {
		if (name == null)
			return;
		this.billName = name;
	}
	
	public void setAmount(int cost) {
		if (cost > MAX_BILL_COST || cost < MIN_BILL_COST)
			return;
		billAmount = cost;
	}
	
	public void setDueDate(int month, int day, int year) {
		if (month <= 0 || day <= 0 || year <= 0)
			return;
		if (month > 12 || day > 31 || year > 30)
			return;
		
		this.due[0] = month;
		this.due[1] = day;
		this.due[2] = year;
	}
	
	public void setMonth(int month) {
		if (month <= 0) 
			return;
		if (month > 12)
			return;
		this.due[0] = month;
	}
	
	public void setDay(int day) {
		if (day <= 0) 
			return;
		if (day > 31)
			return;
		this.due[1] = day;
	}
	
	public void setYear(int year) {
		if (year <= 0) 
			return;
		if (year > 30)
			return;
		this.due[2] = year;
	}
	
	public String getSum() {
		String dueDate = due[0]+"/"+due[1]+"/"+due[2];
		billSum = "   "+billName+" is an "+type.toString()+", with and amount of "+billAmount+"; Due: "+dueDate+"; Importance: "+importance.toString();
		return billSum;
	}

	public void setImportance(BillImportance importance) {
		this.importance = importance;
	}

	public void setType(BillType type) {
		this.type = type;
	}
	
	public String getDueDateAsString() {
		String date = "";
		date = due[0]+"/"+due[1]+"/"+due[2];
		
		return date;
	}
	
	public String getName() {
		return billName;
	}
	public int getAmount() {
		return billAmount;
	}
	public int[] getDueDate() {
		return due;
	}
	public String getStatus() {
		return billStatus;
	}
	public BillImportance getImportance() {
		return importance;
	}
	public BillType getType() {
		return type;
	}
}
