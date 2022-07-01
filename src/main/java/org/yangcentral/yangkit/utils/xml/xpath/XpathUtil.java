package org.yangcentral.yangkit.utils.xml.xpath;

import org.yangcentral.yangkit.common.api.AbsolutePath;
import org.yangcentral.yangkit.common.api.NamespaceContextDom4j;
import org.yangcentral.yangkit.common.api.XPathStep;
import org.yangcentral.yangkit.utils.xml.Converter;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;
import org.apache.commons.lang3.RandomStringUtils;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.Node;
import org.dom4j.QName;
import org.dom4j.XPath;

public class XpathUtil {
   public static Node findXpath(Map<String, String> namespaceURIs, String xpathStr, Element element) {
      XPath xpath = element.createXPath(xpathStr);
      xpath.setNamespaceURIs(namespaceURIs);
      return xpath.selectSingleNode(element);
   }

   private static String generateRandomPrefix(NamespaceContextDom4j original, String prefix) {
      if (null == prefix || prefix.length() == 0) {
         prefix = "ns";
      }

      while(original.translateNamespacePrefixToUri(prefix) != null) {
         prefix = prefix + RandomStringUtils.randomAlphanumeric(2);
      }

      return prefix;
   }

   private static String getPrefix(NamespaceContextDom4j original, Namespace namespace, NamespaceContextDom4j referencedNamespaceContext) {
      String prefix = namespace.getPrefix();
      if (null != prefix && prefix.length() != 0) {
         return original.translateNamespacePrefixToUri(namespace.getURI()) != null ? generateRandomPrefix(original, prefix) : prefix;
      } else {
         if (null != referencedNamespaceContext) {
            prefix = referencedNamespaceContext.translateNamespaceUriToPrefix(namespace.getURI());
         }

         prefix = generateRandomPrefix(original, prefix);
         return prefix;
      }
   }

   public static NamespaceContextDom4j buildNamespaceContext(NamespaceContextDom4j original, Element element, NamespaceContextDom4j referencedNamespaceContext) {
      NamespaceContextDom4j result = new NamespaceContextDom4j();
      result.merge(original);
      if (null == element) {
         return result;
      } else {
         if (null != element.declaredNamespaces()) {
            Iterator var4 = element.declaredNamespaces().iterator();

            while(var4.hasNext()) {
               Namespace namespace = (Namespace)var4.next();
               if (null != namespace) {
                  String oldPrefix = result.translateNamespaceUriToPrefix(namespace.getURI());
                  if (oldPrefix != null) {
                     result.removePrefixNSPair(oldPrefix);
                  }

                  String prefix = getPrefix(result, namespace, referencedNamespaceContext);
                  result.addPrefixNSPair(prefix, namespace.getURI());
               }
            }
         }

         return result;
      }
   }

   public static NamespaceContextDom4j buildNamespaceContext(NamespaceContextDom4j original, Element element) {
      return buildNamespaceContext(original, element, (NamespaceContextDom4j)null);
   }

   public static XPath getXPath(Element element, NamespaceContextDom4j namespaceContext) {
      if (null == element) {
         return null;
      } else if (null == namespaceContext) {
         return null;
      } else {
         Element curElement = element;
         Stack<XPathStep> steps = new Stack();

         XPathStep step;
         do {
            QName qName = curElement.getQName();
            step = new XPathStep(Converter.convert(qName));
            steps.push(step);
            if (curElement.isRootElement()) {
               break;
            }

            curElement = curElement.getParent();
         } while(null != curElement);

         AbsolutePath absolutePath = new AbsolutePath();

         while(!steps.isEmpty()) {
            step = (XPathStep)steps.pop();
            absolutePath.addStep(step);
         }

         return getXPath(absolutePath, namespaceContext);
      }
   }

   public static XPath getXPath(AbsolutePath path, NamespaceContextDom4j namespaceContext) {
      XPath xPath = DocumentHelper.createXPath(path.toString(namespaceContext));
      xPath.setNamespaceContext(namespaceContext);
      return xPath;
   }
}
