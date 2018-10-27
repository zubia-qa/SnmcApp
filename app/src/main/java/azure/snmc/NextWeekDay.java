package azure.snmc;

/**
 * Created by acer on 2018-02-01.
 */
import java.util.Calendar;

public class NextWeekDay {

    private static int dayOfWeek = Calendar.WEDNESDAY;
    int hour      = 10; // 10 AM
    int minute    = 0;

    public static Calendar nextDayOfWeek(int dow) {
        Calendar date = Calendar.getInstance();
        int diff = dow - date.get(Calendar.DAY_OF_WEEK);
        if (diff <= 0) {
            diff += 7;
        }
        date.add(Calendar.DAY_OF_MONTH, diff);
        return date;
    }
    public static void main(String[] args) {
        System.out.printf(
                "%ta, %<tb %<te, %<tY",
                nextDayOfWeek(Calendar.WEDNESDAY)
        );
/*OR*/

        Calendar cal = Calendar.getInstance(); // Today, now
        if (cal.get(Calendar.DAY_OF_WEEK) != dayOfWeek) {
            cal.add(Calendar.DAY_OF_MONTH, (dayOfWeek + 7 - cal.get(Calendar.DAY_OF_WEEK)) % 7);
        } else {
            int minOfDay = cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE);
//        if (minOfDay >= hour * 60 + minute)
            cal.add(Calendar.DAY_OF_MONTH, 7); // Bump to next week
        }
        System.out.println(cal.getTime()); // Prints: Wed May 10 10:00:00 EDT 2017
    }

}