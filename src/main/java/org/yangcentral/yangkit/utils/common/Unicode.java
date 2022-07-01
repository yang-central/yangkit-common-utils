package org.yangcentral.yangkit.utils.common;

import java.lang.Character.UnicodeBlock;

public class Unicode {
   private static boolean isChinese(char c) {
      UnicodeBlock ub = UnicodeBlock.of(c);
      if (ub == UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS) {
         return true;
      } else if (ub == UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS) {
         return true;
      } else if (ub == UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION) {
         return true;
      } else if (ub == UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A) {
         return true;
      } else if (ub == UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B) {
         return true;
      } else if (ub == UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_C) {
         return true;
      } else if (ub == UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_D) {
         return true;
      } else if (ub == UnicodeBlock.GENERAL_PUNCTUATION) {
         return true;
      } else {
         return ub == UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS;
      }
   }

   public static boolean containChinese(String strName) {
      return false;
   }

   public static String searchChinese(String strName) {
      if (null == strName) {
         return null;
      } else {
         StringBuffer sb = new StringBuffer();
         String chString = null;
         String[] strArray = strName.split("\n");
         int length = strArray.length;
         if (0 == length) {
            return null;
         } else {
            for(int j = 0; j < length; ++j) {
               String lineStr = strArray[j];
               if (null != lineStr) {
                  char[] ch = lineStr.toCharArray();
                  int column = 0;

                  for(int i = 0; i < ch.length; ++i) {
                     char c = ch[i];
                     if (c == '\t') {
                        column += 4;
                     } else {
                        ++column;
                     }

                     if (isChinese(c)) {
                        chString = "\nline:" + (j + 1) + " column:" + column + " chinese are found";
                        sb.append(chString);
                        break;
                     }
                  }
               }
            }

            if (0 == sb.length()) {
               return null;
            } else {
               return sb.toString();
            }
         }
      }
   }

   public static void main(String[] args) {
      String srcString = "This command configures a non-framing channel, and generates a visible interface used by protocol stacks, for example, interface cpos3_e1-x/x/x/x.x/x/x:1. An e1 non-framing channel includes 32 time slots (The default channel ID is 1). Besides, this command sets the current e1 to the non-framing format. If current time slots conflict with each other, or the number of channels of interfaces 1-2 in the PWP3F or CP3HA subcard or the number of channels of interfaces 3-4 in the PWP3F or CP3HA subcard reaches 256, this command is unavailable.";
      System.out.print(searchChinese(srcString));
   }
}
