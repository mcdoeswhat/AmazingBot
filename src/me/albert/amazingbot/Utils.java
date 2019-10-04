package me.albert.amazingbot;

import me.albert.amazingbot.config.Data;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class Utils {
    public static boolean isSameDay(Date date1, Date date2) {
        if(date1 != null && date2 != null) {
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(date1);
            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(date2);
            return isSameDay(cal1, cal2);
        } else {
            throw new IllegalArgumentException("The date must not be null");
        }
    }
    private static boolean isSameDay(Calendar cal1, Calendar cal2) {
        if(cal1 != null && cal2 != null) {
            return cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) &&
                    cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                    cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
        } else {
            throw new IllegalArgumentException("The date must not be null");
        }
    }
    public static UUID getPlayer(long userid){
        if (Data.getData("data.yml").getString(String.valueOf(userid)) != null){
            return UUID.fromString(Data.getData("data.yml").getString(String.valueOf(userid)));
        }
        return null;
    }
    public static boolean notNumeric(String num){
        try {
            Integer.parseInt(num);
        } catch (NumberFormatException e){
            return true;
        }
        return false;
    }
}
