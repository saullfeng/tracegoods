package com.jmtc.tracegoods.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Chris
 * @date 2021/6/7 20:18
 * @Email:gang.wu@nexgaming.com
 */
public class DateUtils {
    public static String getNowDateTimeStr() {
        Date dNow = new Date( );
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return ft.format(dNow) ;
    }

    public static String getStringDateLong(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(date);
        return dateString;
    }
}
