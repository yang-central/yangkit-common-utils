package org.yangcentral.yangkit.utils.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import org.mozilla.universalchardet.CharsetListener;
import org.mozilla.universalchardet.UniversalDetector;

public class FileUtil {
   public static final String UTF8_BOM = "\ufeff";

   public static boolean copy(File src, File dst) throws IOException {
      if (null == src) {
         return false;
      } else if (null == dst) {
         return false;
      } else if (!src.exists()) {
         return false;
      } else {
         if (!dst.exists()) {
            dst.createNewFile();
         }

         if (src.isDirectory() && dst.isFile()) {
            return false;
         } else {
            if (src.isFile() && dst.isFile()) {
               copyFile(src, dst);
            } else if (src.isFile() && dst.isDirectory()) {
               File dstFile = new File(dst, src.getName());
               copyFile(src, dstFile);
            } else {
               if (!src.isDirectory() || !dst.isDirectory()) {
                  return false;
               }

               File[] files = src.listFiles();
               int size = files.length;

               for(int i = 0; i < size; ++i) {
                  File file = files[i];
                  if (null != file) {
                     copy(file, dst);
                  }
               }
            }

            return true;
         }
      }
   }

   public static boolean copyFile(File src, File dst) throws IOException {
      if (src != null && dst != null) {
         if (!src.isDirectory() && !dst.isDirectory()) {
            String srcString = readFile2String(src);
            File dstBak = null;
            if (dst.exists()) {
               dstBak = new File(dst.getName() + ".bak");
               dst.renameTo(dstBak);
               dst.delete();
               dst.createNewFile();
            }

            if (writeUtf8File(srcString, dst)) {
               if (null != dstBak) {
                  dstBak.delete();
               }

               return true;
            } else {
               if (null != dstBak) {
                  dstBak.renameTo(dst);
                  dstBak.delete();
               }

               return false;
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public static String readFile(File file) throws IOException {
      byte[] buf = new byte[4096];
      FileInputStream fis = new FileInputStream(file);
      StringBuffer sb = new StringBuffer();

      int nread;
      while((nread = fis.read(buf)) > 0) {
         for(int i = 0; i < nread; ++i) {
            sb.append((char)buf[i]);
         }
      }

      fis.close();
      if (sb.length() > 0) {
         return sb.toString();
      } else {
         return null;
      }
   }

   public static String readFile2String(String path) throws IOException {
      return null == path ? null : readFile2String(new File(path));
   }

   public static String readFile2String(String path, String charSetName) throws IOException {
      return null == path ? null : readFile2String(new File(path), charSetName);
   }

   public static String readFile2String(File file) throws IOException {
      return readFile2String(file, "utf-8");
   }

   public static String readFile2String(File file, String charSetName) throws IOException {
      if (null == file) {
         return null;
      } else if (file.exists() && !file.isDirectory()) {
         InputStreamReader read = new InputStreamReader(new FileInputStream(file), charSetName);
         BufferedReader br = new BufferedReader(read);
         String temp = null;
         StringBuffer sb = new StringBuffer();

         for(temp = br.readLine(); temp != null; temp = br.readLine()) {
            sb.append(temp + "\n");
         }

         br.close();
         return sb.toString();
      } else {
         System.out.println("File:" + file.getName() + " path:" + file.getAbsolutePath());
         throw new FileNotFoundException();
      }
   }

   public static void save2File(String buffer, String out) {
      FileWriter fw = null;

      try {
         fw = new FileWriter(out);

         try {
            fw.write(buffer);
         } catch (IOException var4) {
            var4.printStackTrace();
            fw.close();
         }

         fw.close();
      } catch (IOException var5) {
         var5.printStackTrace();
      }

   }

   public static boolean save2File(String buffer, File out) {
      FileWriter fw = null;
      buffer = buffer.replaceAll("\\\\\\\\", "\\\\");
      buffer = removeUTF8BOM(buffer);

      try {
         fw = new FileWriter(out);

         try {
            fw.write(buffer);
         } catch (IOException var4) {
            var4.printStackTrace();
            fw.close();
            return false;
         }

         fw.close();
         return true;
      } catch (IOException var5) {
         var5.printStackTrace();
         return false;
      }
   }

   public static void writeUtf8File(String fileName, String fileBody) {
      FileOutputStream fos = null;
      OutputStreamWriter osw = null;

      try {
         fileBody = fileBody.replaceAll("\\\\\\\\", "\\\\");
         fos = new FileOutputStream(fileName);
         osw = new OutputStreamWriter(fos, "utf-8");
         osw.write(fileBody);
      } catch (Exception var17) {
         var17.printStackTrace();
      } finally {
         if (osw != null) {
            try {
               osw.close();
            } catch (IOException var16) {
               var16.printStackTrace();
            }
         }

         if (fos != null) {
            try {
               fos.close();
            } catch (IOException var15) {
               var15.printStackTrace();
            }
         }

      }

   }

   public static boolean writeUtf8File(String fileBody, File out) {
      FileOutputStream fos = null;
      OutputStreamWriter osw = null;

      boolean var5;
      try {
         fileBody = fileBody.replaceAll("\\\\\\\\", "\\\\");
         fileBody = removeUTF8BOM(fileBody);
         fos = new FileOutputStream(out);
         osw = new OutputStreamWriter(fos, "utf-8");
         osw.write(fileBody);
         return true;
      } catch (Exception var19) {
         var19.printStackTrace();
         var5 = false;
      } finally {
         if (osw != null) {
            try {
               osw.close();
            } catch (IOException var18) {
               var18.printStackTrace();
            }
         }

         if (fos != null) {
            try {
               fos.close();
            } catch (IOException var17) {
               var17.printStackTrace();
            }
         }

      }

      return var5;
   }

   public static String removeUTF8BOM(String s) {
      while(s.startsWith("\ufeff")) {
         s = s.substring(1);
      }

      return s;
   }

   public static String getCharset(File file) throws IOException {
      byte[] buf = new byte[4096];
      FileInputStream fis = new FileInputStream(file);
      UniversalDetector detector = new UniversalDetector((CharsetListener)null);

      int nread;
      while((nread = fis.read(buf)) > 0 && !detector.isDone()) {
         detector.handleData(buf, 0, nread);
      }

      fis.close();
      detector.dataEnd();
      String encoding = detector.getDetectedCharset();
      if (null == encoding) {
         encoding = "gbk";
      }

      detector.reset();
      return encoding;
   }
}
