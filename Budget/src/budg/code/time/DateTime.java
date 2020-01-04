package budg.code.time;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTime {
	private static SimpleDateFormat textFormat = new SimpleDateFormat("dow mon dd hh:mm:ss zzz yyyy");
	private static SimpleDateFormat format = new SimpleDateFormat("MM/dd/yy");
	
	public static String getCurrentDateAsString() {
		Date date = new Date();
		textFormat.format(date);
		
		return(date.toString());
	}
	
	public static Date getCurrentDate() {
		Date date = new Date();
		format.format(date);
		
		return date;
	}
}
