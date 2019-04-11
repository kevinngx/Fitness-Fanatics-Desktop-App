package sample;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateHelper {

    public static String longDateToString(long date) {

        char[] dateArray = Long.toString(date).toCharArray();
        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < dateArray.length; i++) {
//            System.out.println("Char: " + i + " " + dateArray[i]);
//        }
        // From yyyyMMdd to dd/mm/yyyy
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



    public static long getCurrentDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate localDate = LocalDate.now();
//        System.out.println(dtf.format(localDate)); //2016/11/16

        return Long.parseLong(dtf.format(localDate));
    }
}
