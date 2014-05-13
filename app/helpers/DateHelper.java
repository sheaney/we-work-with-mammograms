package helpers;

import play.data.format.Formats;

import java.util.Date;
import java.util.Locale;

/**
 * Created by sheaney on 5/12/14.
 */
public class DateHelper {
    public static String getDateFormat(Date date) {
        Formats.DateFormatter dateFormatter = new Formats.DateFormatter("dd-MM-yyyy HH:mm");
        Locale locale = new Locale("es", "MX");
        return dateFormatter.print(date, locale);
    }
}
