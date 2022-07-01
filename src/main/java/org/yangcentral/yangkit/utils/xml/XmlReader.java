package org.yangcentral.yangkit.utils.xml;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.annotation.Nonnull;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class XmlReader {
   public static Document readDocument(@Nonnull String xmlPath) throws IOException, ParserConfigurationException, SAXException {
      InputStream inputStream = new FileInputStream(xmlPath);
      Document doc = readXmlToDocument(inputStream);
      return doc;
   }

   public static Document readXmlToDocument(InputStream xmlContent) throws IOException, SAXException, ParserConfigurationException {
      DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
      domFactory.setNamespaceAware(true);
      DocumentBuilder builder = domFactory.newDocumentBuilder();
      Document doc = builder.parse(xmlContent);
      doc.getDocumentElement().normalize();
      return doc;
   }
}
