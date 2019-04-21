package sample;

import sun.util.calendar.BaseCalendar;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/***************************************************************************************
 *    This is a helper class I made to manage the various date formats that were used within
 *    the assignment, whether it be from the JavaFX date element; or from our database
 *    NOTE: a decision was made to store our dates in the format of a long: yyyyMMdd
 *    this was to simplify queries and date comparisons
 ***************************************************************************************/

public class DateHelper {

    // Converts our date from yyyyMMdd to dd/MM/yyyy
    public static String longDateToString(long date) {

        char[] dateArray = Long.toString(date).toCharArray();
        StringBuilder sb = new StringBuilder();
        sb.append(dateArray[6]);
        sb.append(dateArray[7]);
        sb.append("/");
        sb.append(dateArray[4]);
        sb.append(dateArray[5]);
        sb.append("/");
        sb.append(dateArray[0]);
        sb.append(dateArray[1]);
        sb.append(dateArray[2]);
        sb.append(dateArray[3]);
        return sb.toString();

    }

    // Converts our date from yyyyMMdd to dd/MM
    public static String longDateToStringWithoutYear(long date) {

        char[] dateArray = Long.toString(date).toCharArray();
        StringBuilder sb = new StringBuilder();
        sb.append(dateArray[6]);
        sb.append(dateArray[7]);
        sb.append("/");
        sb.append(dateArray[4]);
        sb.append(dateArray[5]);
        return sb.toString();

    }

    //Converts our date from a dd/MM/yyyy to yyyyMMdd
    public static long inputStringToLongDate(String dateString) {
//        System.out.println("Input: " + dateString);
        // We want yyyyMMdd
        String [] dateParts = dateString.split("/");
        String day = dateParts[0];
        String month = dateParts[1];
        String year = dateParts[2];
        StringBuilder sb = new StringBuilder();
        sb.append(year);
        sb.append(month);
        if (Integer.parseInt(day) < 10) {
            sb.append('0');
        }
        sb.append(day);
//        System.out.println("Output: " + sb.toString());
        return Long.parseLong(sb.toString());

    }


    //Gets the current date in the form of a long: yyyyMMdd
    public static long getCurrentDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate localDate = LocalDate.now();
//        System.out.println(dtf.format(localDate)); //2016/11/16

        return Long.parseLong(dtf.format(localDate));
    }

    public static long getDaysInBetween(long currentDate, long previousDate) {
        char[] currentDaysArray = Long.toString(currentDate).toCharArray();
        char[] previousDaysArray = Long.toString(previousDate).toCharArray();
        long daysInBetween = 0;

        StringBuilder currentDays = new StringBuilder();
        currentDays.append(currentDaysArray[6]);
        currentDays.append(currentDaysArray[7]);
        StringBuilder previousDays = new StringBuilder();
        previousDays.append(previousDaysArray[6]);
        previousDays.append(previousDaysArray[7]);
        daysInBetween += (Long.parseLong(currentDays.toString()) - Long.parseLong(previousDays.toString()));

        StringBuilder currentMonths = new StringBuilder();
        currentMonths.append(currentDaysArray[4]);
        currentMonths.append(currentDaysArray[5]);
        StringBuilder previousMonths = new StringBuilder();
        previousMonths.append(previousDaysArray[4]);
        previousMonths.append(previousDaysArray[5]);
        daysInBetween += (Long.parseLong(currentMonths.toString()) - Long.parseLong(previousMonths.toString()))* 30;

        StringBuilder currentYears = new StringBuilder();
        currentYears.append(currentDaysArray[1]);
        currentYears.append(currentDaysArray[2]);
        currentYears.append(currentDaysArray[3]);
        currentYears.append(currentDaysArray[4]);
        StringBuilder previousYears = new StringBuilder();
        previousYears.append(previousDaysArray[1]);
        previousYears.append(previousDaysArray[2]);
        previousYears.append(previousDaysArray[3]);
        previousYears.append(previousDaysArray[4]);
        daysInBetween += (Long.parseLong(currentYears.toString()) - Long.parseLong(previousYears.toString()))* 30;

        return daysInBetween;
    }
}
