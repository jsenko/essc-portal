
package org.jboss.essc.web.util;

import java.util.Date;


/**
 *
 * @author ozizka@redhat.com
 */
public class SimpleRelativeDateFormatter {

    
    public static String format( Date date ){
        return format( date, new Date() );
    }

    public static String format( Date when, Date now ) {

        long diff = (now.getTime() - when.getTime()) / 1000;
        boolean past = diff >= 0;
        if( ! past )  diff = -diff;
        if( diff < 10 )  return "now";
        
        boolean sameDay = now.getDate() == when.getDate();
        
        return diffTimeString( diff, sameDay, past );        
    }

    
    /**
     * 
     * @param diffSec  Difference in seconds. Non-negative number.
     * @param sameDay  Are the two moments in the same day? (For determining yesterday.)
     * @param past     Is it in the past or future?
     * @returns        String like "23 hours ago" or "in a month".
     */
    private static String diffTimeString( final long diffSec, boolean sameDay, boolean past ) {
        String in  = past ? "" : "in ";
        String ago = past ? " ago" : "";
        
        long diff = diffSec;
        if( diff <     30 )  return in + "few moments" + ago;
        if( diff <     80 )  return in + "a minute" + ago;
        if( diff <    140 )  return in + "2 minutes" + ago;
        if( diff <   3540 )  return in + diff/60 + " minutes" + ago;
        if( diff <   4000 )  return in + "an hour" + ago;
        //if( diff <   7200 )  return "an hour and something" + ago;
        
        // Hours
        diff /= 3600;
        if( diff < 24 )  return in + diff + " hours" + ago;
        if( diff < 48 && ! sameDay )  return "yesterday";
        
        long diffDays = diff /= 24;  // Days
        if( diff < 7 )  return in + diff + " days" + ago;
        if( diff < 8 )  return in + "a week" + ago;
        if( diff < 14 ) return in + diff + " days" + ago;
        
        long weeks = diff / 7;
        if( weeks < 4 ) return in + diff + " weeks" + ago;
        if( weeks < 5 ) return in + "a month" + ago;
        if( weeks < 8 ) return in + diff + " weeks" + ago;
        
        diff /= 30;  // Months, cca.
        if( diff < 12 ) return in + diff + " months" + ago;
        if( diff < 13 ) return in + "a year" + ago;
        if( diff < 48 ) return in + (diff/12) + " years and " + diff + " months" + ago;
        
        diff = diffDays / 365;  // Years, cca.
        if( diff < 200 ) return in + diff + " years" + ago;
        if( diff < 1000 ) return in + "few centuries" + ago;
        
        return past ? "ages ago" : "in eternity";
    }
    
}
