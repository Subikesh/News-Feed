package utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

/**
 * Class contains the static methods to format/parse dates
 */
public class DateUtilities {
    /**
     * Checks if the date entered as String is a valid date or not in yyyy/MM/dd format
     * @param date date as string
     * @return true if the date is valid
     */
    public static boolean isValidDate(String date) {
        if (date.isEmpty()) {
            return false;
        }
        try {
            // Date format parse
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            format.setLenient(false);
            Date dateObj = format.parse(date);
        } catch (ParseException e) {
            try {
                // Date and time ISO format
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                format.setLenient(false);
                Date dateTime = format.parse(date);
            } catch (ParseException ex) {
                // Invalid date format
                System.out.println("'" + date+"' is Invalid Date format");
                return false;
            }
            return false;
        }
        /* Return true if date format is valid */
        return true;
    }

    /**
     * Checks if the date entered is within the last 30 days.
     * @param date the date to check in string
     * @return true if the date is within last month from today
     */
    public static boolean isInLastMonth(String date) {
        if (!isValidDate(date)) {
            return false;
        }
        LocalDate prevDate = LocalDate.now().minusDays(30);
        LocalDate thisDate = LocalDate.parse(date);
        if (thisDate.isAfter(prevDate) && thisDate.isBefore(LocalDate.now()))
            return true;
        System.out.println("Date is not within last month.");
        return false;
    }
}
