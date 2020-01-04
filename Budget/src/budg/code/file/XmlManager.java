package budg.code.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

//import com.sun.org.apache.xml.internal.serialize.OutputFormat;
//import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

import budg.code.bill.Bill;
import budg.code.bill.BillManager;

public class XmlManager {
	
	private static Document doc;
	private static List<Bill> accountBills = new ArrayList<Bill>(); // Bills that are just read from file, or are being written to file
	private static final File xmlFile = new File("res\\account.xml");
	
	/**
	 * Sets up XML Document and XML Parser. Reads any data from document and 
	 * creates in-memory data from XML document, then updates the BillManager
	 * 
	 * @param builder - The builder for parsing the XML Document
	 * 
	 * @throws SAXException
	 * If Cannot parse XML File
	 * 
	 * @throws IOException
	 * If Problem creating or writing to XML File
	 */
	public XmlManager(DocumentBuilder builder) throws SAXException, IOException {
		boolean wasCreated = verifyDocument(builder);
		if (!wasCreated)
			restoreAccount();
		updateBillManager();
	}
	
	/**
	 * Write to and configure the basic file structure for an XML file.
	 * 
	 * @param file - The XML file that the basic XML
	 * file structure will be written to.
	 * 
	 * @throws FileNotFoundException
	 * If the file does not exist
	 */
	private void structureFile(File file) throws FileNotFoundException {
		PrintWriter out = new PrintWriter(file);
		// basic file structure
		out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		out.println("<configuration>");
		out.println("</configuration>");
		out.close();
	}
	
	/**
	 * Makes sure file exists, if it does not exists create file and 
	 * file directory ("res/account.xml"). After creation, write to 
	 * the account.xml file and create the basic XML file structure.
	 * Then finally use a XML parser to build an in-memory document
	 * for further use in application.
	 * 
	 * @param build - The XML parser used to make the in-memory document.
	 * 
	 * @return True if XML file had to be created. Meaning there is no
	 * data in the file. Returns false if file already existed, meaning
	 * there could be data in file.
	 * 
	 * @throws SAXException
	 * If not able to parse the file and create the in-memory document
	 * 
	 * @throws IOException
	 * If XML file cannot be created
	 */
	private boolean verifyDocument(DocumentBuilder build) throws SAXException, IOException {
		boolean isNewDocument = false;
		File fileDir = new File("res");
		
		// create file if not exists
		if (!xmlFile.exists()) {
			isNewDocument = true;
			fileDir.mkdir();
			xmlFile.createNewFile();
			structureFile(xmlFile);
		}
		// create in-memory document
		XmlManager.doc = build.parse(xmlFile);
		return isNewDocument;
	}
	
	/**
	 * Creates a new Bill object and then uses a Node in the XML 
	 * document to find the data in that file. Then places that data
	 * into the newly created Bill object.
	 * 
	 * @param child - The Node used to find the data needed
	 * 
	 * @return a Bill object filled out with the data found 
	 * in the XML document.
	 */
	private Bill collectBillData(Node child) {
		Bill bill = new Bill();
		NodeList dataS = child.getChildNodes();
		
		for (int i = 0; i < dataS.getLength(); i++) {
			Node data = dataS.item(i);
			
			if (data instanceof Element) {
				switch(data.getNodeName()) {
				case "name": 
					bill.setName(data.getTextContent());
					break;
				case "amount":
					bill.setAmount(Integer.parseInt(data.getTextContent()));
					break;
				case "due_date": // Skips over white spaces
					// month
					bill.setMonth(Integer.parseInt(data.getChildNodes().item(1).getTextContent()));
					// day
					bill.setDay(Integer.parseInt(data.getChildNodes().item(3).getTextContent()));
					// year
					bill.setYear(Integer.parseInt(data.getChildNodes().item(5).getTextContent()));
					break;
				case "importance":
					bill.setImportance(Bill.getImportanceFromString(data.getTextContent()));
					break;
				case "type":
					bill.setType(Bill.getTypeFromString(data.getTextContent()));
					break;
				}
			}
		}
		
		return bill;
	}
	
	/**
	 * Gets all the data from the account.xml file and
	 * allows the program to access it from a list
	 */
	private void restoreAccount() {
		// get bills
		Element root = doc.getDocumentElement();
		NodeList childeren = root.getChildNodes();
		// loop through bills
		for (int i = 0; i < childeren.getLength(); i++) {
			Node child = childeren.item(i);
			// add bill to list with bill data
			if (child.getNodeName().equals("bill")) {
				Bill bill = collectBillData(child);
				accountBills.add(bill);
			}
		}
	}
	
	private void updateBillManager() {
		BillManager.replaceAccount(accountBills);
	}
	
	/**
	 * (CAUTION: Do Not Use Unless Saving to account.xml)
	 * Replaces the known account bills with a new set of bills
	 * 
	 * @param replace - The list of bills to replace with
	 * @throws FileNotFoundException 
	 */
	public static void replaceAccount(List<Bill> replace) throws FileNotFoundException {
		XmlManager.accountBills = replace;
		saveAccount();
	}
	
	/**
	 * Prints a bill and it's data to an output
	 * 
	 * @param out - The output outlet to print the bill data to 
	 * @param bill - The bill to get the data from
	 */
	private static void writeBill(PrintWriter out, Bill bill) {
		out.println("\t<bill>");
		out.println("\t\t<name>"+bill.getName()+"</name>");
		out.println("\t\t<amount>"+bill.getAmount()+"</amount>");
		out.println("\t\t<due_date>");
		out.println("\t\t\t<month>"+bill.getDueDate()[0]+"</month>");
		out.println("\t\t\t<day>"+bill.getDueDate()[1]+"</day>");
		out.println("\t\t\t<year>"+bill.getDueDate()[2]+"</year>");
		out.println("\t\t</due_date>");
		out.println("\t\t<importance>"+bill.getImportance().toString()+"</importance>");
		out.println("\t\t<type>"+bill.getType().toString()+"</type>");
		out.println("\t</bill>");
	}
	
	/**
	 * Prints all the account data to the XML Document
	 * 
	 * @throws FileNotFoundException
	 * If the xmlFile cannot be found
	 */
	private static void saveAccount() throws FileNotFoundException {
		PrintWriter out = new PrintWriter(xmlFile);
		out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		out.println("<configuration>");
		
		// Print the data
		for (int i = 0; i < accountBills.size(); i++) {
			Bill bill = accountBills.get(i);
			
			writeBill(out, bill);
		}
		
		out.println("</configuration>");
		out.close();
	}
}
