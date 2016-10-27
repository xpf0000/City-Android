package citycircle.com.OA.uitls;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateCompare {

	public boolean date(String date1, String date2) throws ParseException {
		String DateStr1 = date1;
		String DateStr2 = date2;
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date dateTime1 = dateFormat.parse(DateStr1);
		Date dateTime2 = dateFormat.parse(DateStr2);
		int i = dateTime1.compareTo(dateTime2);
		if (i < 0) {
			return false;
		} else {
			return true;
		}

	}
	public String  datetume(String time) throws ParseException {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		if(time.equals("")||time==null){
			return "";
		}else{
			String dstr=time;
			Date date=sdf.parse(dstr); 
			String str=sdf.format(date);
			return str;
		}
		
		
	}
	public boolean date2(String date1, String date2) throws ParseException {
		String DateStr1 = date1;
		String DateStr2 = date2;
		DateFormat dateFormat = new SimpleDateFormat("yyyy-M-d");
		Date dateTime1 = dateFormat.parse(DateStr1);
		Date dateTime2 = dateFormat.parse(DateStr2);
		int i = dateTime1.compareTo(dateTime2);
		if (i < 0) {
			return false;
		} else {
			return true;
		}

	}
}
