package budg.code;

import java.io.IOException;

import javax.swing.SwingUtilities;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import budg.code.file.XmlManager;
import budg.code.gfx.FrameCreator;
import budg.code.screens.ScreenHandler;
import budg.code.time.DateTime;

public class Main {
	
	public static int width = 700, height = 500;
	public static volatile boolean running = false;
	
	public static void main(String[] args) {
		running = true;
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// find and open account
				try {
					DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
					try {
						new XmlManager(builder);
					} catch (SAXException e) {
						e.printStackTrace();
						System.err.println(DateTime.getCurrentDateAsString()+"\n\tProgram was not able to create the XML parser\n");
						return;
					} catch (IOException e) {
						e.printStackTrace();
						System.err.println(DateTime.getCurrentDateAsString()+"\n\tXML Document could not be created and/or could not be found\n");
						return;
					}
				} catch (ParserConfigurationException e) {
					e.printStackTrace();
					System.err.println(DateTime.getCurrentDateAsString()+"\n\tXML parser was not able to be configured, fored to stop build");
					return;
				}
				// setup screen and load home screen
				ScreenHandler.setup(width, height);
				FrameCreator.createFrame("Budgeter", width, height);
			}
		});
	}
}
