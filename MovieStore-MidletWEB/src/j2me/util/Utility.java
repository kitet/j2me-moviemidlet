package j2me.util;

import java.util.Calendar;
import java.util.Date;

public class Utility {

	public static Date parseDate(String dateString)throws Exception {
		Date date = new Date(0);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		String yearStr = dateString.substring(0, 4);
		String monthStr = dateString.substring(5, 7);
		String dayStr = dateString.substring(8, 10);
		
		int year = 0;
		int day = 0;
		int month = 0;

		try {
			year = Integer.parseInt(yearStr);
		} catch (Exception e) {
			throw new Exception("Could not parse '" + yearStr
					+ "' as a valid year");
		}
		try {
			day = Integer.parseInt(dayStr);
		} catch (Exception e) {
			throw new Exception("Could not parse '" + dayStr
					+ "' as a valid day");
		}
		try {
			month = Integer.parseInt(monthStr) - 1; // Zero Based Months
		} catch (Exception e) {
			throw new Exception("Could not parse '" + monthStr
					+ "' as a valid month");
		}

		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.DATE, day);
		cal.set(Calendar.YEAR, year);

		date = cal.getTime();

		return date;
	}
}
