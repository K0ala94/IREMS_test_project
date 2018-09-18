package utility;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
	
	public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm";
	public static final String DATE_FORMAT_SECONDS = "yyyy-MM-dd HH:mm:ss";
	public static final long MILIS_IN_MIN = 1000*60;

	public static LocalDateTime convertStringtoLocalDateTime(String strToConvert){
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
		LocalDateTime dateTime = LocalDateTime.parse(strToConvert, formatter);
		return dateTime;
	}
	
	public static String convertLocalDateTimeToString(LocalDateTime dateTime){
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
		return dateTime.format(formatter);
	}
	
	public static long millisecondsBetween(LocalDateTime latter, LocalDateTime former){
		return latter.toInstant(ZoneOffset.UTC).toEpochMilli() - former.toInstant(ZoneOffset.UTC).toEpochMilli();
	}
}
