package edu.cftic.fichapp.util;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Fecha {
    public static Timestamp inicio( Timestamp t){
        Calendar cc = new GregorianCalendar();
        cc.clear();
        //GregorianCalendar gc = new GregorianCalendar();

        cc.setTimeInMillis(t.getTime());
        cc.set(Calendar.HOUR, 0);
        cc.set(Calendar.MINUTE, 0);
        cc.set(Calendar.SECOND, 0);
        cc.set(Calendar.AM_PM, 0);
        cc.set(Calendar.MILLISECOND, 0);
        return new Timestamp(cc.getTimeInMillis());
    }

    public static Timestamp fin( Timestamp t){
        Calendar cc = new GregorianCalendar();
        cc.clear();
        //GregorianCalendar gc = new GregorianCalendar();

        cc.setTimeInMillis(t.getTime());
        cc.set(Calendar.HOUR, 23);
        cc.set(Calendar.MINUTE, 59);
        cc.set(Calendar.SECOND, 59);
        cc.set(Calendar.AM_PM, 0);
        cc.set(Calendar.MILLISECOND, 999);
        return new Timestamp(cc.getTimeInMillis());
    }

    public static Timestamp fichadainicio( Timestamp t){
        Calendar cc = new GregorianCalendar();

        return new Timestamp(cc.getTimeInMillis());
    }
    public static Timestamp fichadafin( Timestamp t){
        Calendar cc = new GregorianCalendar();

        return new Timestamp(cc.getTimeInMillis());
    }
}
