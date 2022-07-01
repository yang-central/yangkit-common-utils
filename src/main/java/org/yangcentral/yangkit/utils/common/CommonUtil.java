package org.yangcentral.yangkit.utils.common;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class CommonUtil {
   public static String getCurTime() {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
      sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
      String date = sdf.format(new Date(System.currentTimeMillis()));
      return date;
   }

   public static String getConsumTime(long consumeTimemills) {
      int hours = (int)(consumeTimemills / 1000L / 3600L);
      int minutes = (int)((consumeTimemills - (long)(hours * 3600 * 1000)) / 60000L);
      int seconds = (int)((consumeTimemills - (long)(hours * 3600 * 1000) - (long)(minutes * '\uea60')) / 1000L);
      return hours + " hours " + minutes + " minutes " + seconds + " seconds";
   }

   public static String number2Str(Number number) {
      NumberFormat nf = NumberFormat.getInstance();
      nf.setGroupingUsed(false);
      nf.setMaximumFractionDigits(9);
      return nf.format(number);
   }
}
