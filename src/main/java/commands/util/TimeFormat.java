package commands.util;

public class TimeFormat {
    public static String  hoursMinutesSeconds(long milliseconds){
        long seconds = (milliseconds/1000) % 60;
        long minutes = ((milliseconds / (1000*60)) % 60);
        long hours   = ((milliseconds / (1000*60*60)) % 24);
        return String.format("%02d:%02d:%02d",hours,minutes,seconds);
    }
}
