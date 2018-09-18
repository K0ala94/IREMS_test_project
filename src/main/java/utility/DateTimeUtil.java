package utility;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
	
	public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm";

	public static LocalDateTime convertStringtoLocalDateTime(String strToConvert){
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
		LocalDateTime dateTime = LocalDateTime.parse(strToConvert, formatter);
		return dateTime;
	}
	
	public static String convertLocalDateTimeToString(LocalDateTime dateTime){
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
		return dateTime.format(formatter);
	}
}
