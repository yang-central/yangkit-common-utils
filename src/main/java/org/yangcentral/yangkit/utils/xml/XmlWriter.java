package org.yangcentral.yangkit.utils.xml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.w3c.dom.Document;

public class XmlWriter {
   public static void writeXmlDocumentToXmlFile(Document xmlDocument, String fileName) {
      TransformerFactory tf = TransformerFactory.newInstance();

      try {
         Transformer transformer = tf.newTransformer();
         transformer.setOutputProperty("indent", "yes");
         transformer.setOutputProperty("standalone", "omit");
         transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
         FileOutputStream outStream = new FileOutputStream(new File(fileName));
         transformer.transform(new DOMSource(xmlDocument), new StreamResult(outStream));
         outStream.close();
      } catch (TransformerException var5) {
         var5.printStackTrace();
      } catch (Exception var6) {
         var6.printStackTrace();
      }

   }

   public static String writeXmlDocumentToString(Document xmlDocument) {
      TransformerFactory tf = TransformerFactory.newInstance();

      try {
         Transformer transformer = tf.newTransformer();
         transformer.setOutputProperty("indent", "yes");
         transformer.setOutputProperty("standalone", "omit");
         transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
         StringWriter writer = new StringWriter();
         transformer.transform(new DOMSource(xmlDocument), new StreamResult(writer));
         String str = writer.toString();
         writer.close();
         return str;
      } catch (TransformerException var5) {
         var5.printStackTrace();
      } catch (Exception var6) {
         var6.printStackTrace();
      }

      return null;
   }

   public static String transDom4jDoc2String(org.dom4j.Document doc) {
      OutputFormat format = OutputFormat.createPrettyPrint();
      StringWriter stringWriter = new StringWriter();
      XMLWriter writer = new XMLWriter(stringWriter, format);

      try {
         writer.write(doc);
         String str = stringWriter.toString();
         writer.close();
         return str;
      } catch (IOException var5) {
         var5.printStackTrace();
         return null;
      }
   }

   public static void writeDom4jDoc(org.dom4j.Document doc, String fileName) {
      OutputFormat format = OutputFormat.createPrettyPrint();

      try {
         OutputStream out = new FileOutputStream(fileName);
         XMLWriter writer = new XMLWriter(out, format);
         writer.write(doc);
         writer.close();
         out.close();
      } catch (UnsupportedEncodingException | FileNotFoundException var5) {
         var5.printStackTrace();
      } catch (IOException var6) {
         var6.printStackTrace();
      }

   }
}
