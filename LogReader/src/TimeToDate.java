import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class TimeToDate {
    public static void main(String[] args) {
        convertTimeToDate();
    }

    public static long convertTimeToDate() {
        String target = "1904/01/01 12:00 AM";  // Your given date string
        long nanoseconds = 13551723208300l;   // nanoseconds since target time that you want to convert to java.util.Date

        long millis = TimeUnit.MILLISECONDS.convert(nanoseconds, TimeUnit.NANOSECONDS);

        DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd hh:mm aaa");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        try {
            date = formatter.parse(target);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long newTimeInmillis = date.getTime() + millis;

        Date date2 = new Date(newTimeInmillis);

        System.out.println(date2);
        return 0;
    }
}
