package org.yangcentral.yangkit.utils.common;

public class StringUtil {
   public static String toUpperFirst(String string) {
      if (null == string) {
         return null;
      } else {
         String first = string.substring(0, 1);
         String other = string.substring(1);
         first = first.toUpperCase();
         return first.concat(other);
      }
   }

   public static String toLowerCamelCase(String string) {
      if (null == string) {
         return null;
      } else {
         String[] strs = string.split("-");
         boolean isFirst = true;
         StringBuffer sb = new StringBuffer();
         String[] var4 = strs;
         int var5 = strs.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            String subString = var4[var6];
            if (isFirst) {
               isFirst = false;
               sb.append(subString);
            } else {
               subString = toUpperFirst(subString);
               sb.append(subString);
            }
         }

         return sb.toString();
      }
   }

   public static String toUpperCamelCase(String string) {
      if (null == string) {
         return null;
      } else {
         String[] strs = string.split("-");
         StringBuffer sb = new StringBuffer();
         String[] var3 = strs;
         int var4 = strs.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            String subString = var3[var5];
            subString = toUpperFirst(subString);
            sb.append(subString);
         }

         return sb.toString();
      }
   }
}
