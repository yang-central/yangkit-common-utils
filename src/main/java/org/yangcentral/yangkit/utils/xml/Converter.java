package org.yangcentral.yangkit.utils.xml;

import org.yangcentral.yangkit.common.api.Attribute;
import org.yangcentral.yangkit.common.api.NamespaceContextDom4j;
import org.yangcentral.yangkit.common.api.QName;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nonnull;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.DOMReader;
import org.xml.sax.InputSource;

public class Converter {
   public static Document parse(org.w3c.dom.Document doc) throws Exception {
      if (doc == null) {
         return null;
      } else {
         DOMReader xmlReader = new DOMReader();
         return xmlReader.read(doc);
      }
   }

   public static org.w3c.dom.Document parse(Document doc) throws Exception {
      if (doc == null) {
         return null;
      } else {
         StringReader reader = new StringReader(doc.asXML());
         InputSource source = new InputSource(reader);
         DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
         return documentBuilder.parse(source);
      }
   }

   public static QName convert(org.dom4j.QName dom4jQName) {
      return new QName(dom4jQName.getNamespaceURI(), dom4jQName.getNamespacePrefix(), dom4jQName.getName());
   }

   public static QName convert(org.dom4j.QName dom4jQName, NamespaceContextDom4j namespaceContext) {
      if (null == dom4jQName) {
         return null;
      } else {
         String prefix = dom4jQName.getNamespacePrefix();
         if (null == prefix) {
            prefix = namespaceContext.translateNamespaceUriToPrefix(dom4jQName.getNamespaceURI());
         }

         return new QName(dom4jQName.getNamespaceURI(), prefix, dom4jQName.getName());
      }
   }

   public static org.dom4j.QName convert2Dom4jQName(QName qName) {
      return null == qName ? null : org.dom4j.QName.get(qName.getLocalName(), qName.getPrefix(), qName.getNamespace().toASCIIString());
   }

   public static Attribute convert(org.dom4j.Attribute dom4jAttr) {
      return null == dom4jAttr ? null : new Attribute(new QName(dom4jAttr.getNamespaceURI(), dom4jAttr.getNamespacePrefix(), dom4jAttr.getName()), dom4jAttr.getValue());
   }

   public static org.dom4j.Attribute convert2Dom4jAttr(@Nonnull Element element, @NonNull Attribute attribute) {
      return DocumentHelper.createAttribute(element, convert2Dom4jQName(attribute.getName()), attribute.getValue());
   }

   public static List<org.dom4j.Attribute> convert2Dom4jAttr(@Nonnull Element element, @Nonnull List<Attribute> attributes) {
      List<org.dom4j.Attribute> dom4jAttrs = new ArrayList();
      if (null != attributes && attributes.size() != 0) {
         Iterator var3 = attributes.iterator();

         while(var3.hasNext()) {
            Attribute attribute = (Attribute)var3.next();
            if (null != attribute) {
               dom4jAttrs.add(DocumentHelper.createAttribute(element, convert2Dom4jQName(attribute.getName()), attribute.getValue()));
            }
         }

         return dom4jAttrs;
      } else {
         return dom4jAttrs;
      }
   }
}
