package com.projects.notdefterim;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    public static String saatgetir() {
        Date now = new Date();
        DateFormat hourFormat = new SimpleDateFormat("HH:mm");
        return hourFormat.format(now);
    }

    public static String tarihgetir() {
        Date now = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        return dateFormat.format(now);
    }

}
