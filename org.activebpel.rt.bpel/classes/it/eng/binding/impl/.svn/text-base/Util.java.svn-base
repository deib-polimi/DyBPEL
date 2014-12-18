package it.eng.binding.impl;

import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *@author Paolo Zampognaro Engineering Ingegneria Informatica spa
 * 
 *         Contains method to print/manipulate xml content for an easy
 *         debugging.
 **/

public class Util {
	
	private static DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

	/**
	 * DEBUG Print a DOM Element
	 */
	public static String domToString(Element element) {
		String result = null;

		if (element != null) {
			StringWriter strWtr = new StringWriter();
			StreamResult strResult = new StreamResult(strWtr);
			TransformerFactory tfac = TransformerFactory.newInstance();
			try {
				Transformer t = tfac.newTransformer();
				t.setOutputProperty(OutputKeys.ENCODING, "iso-8859-1");
				t.setOutputProperty(OutputKeys.INDENT, "yes");
				t.setOutputProperty(OutputKeys.METHOD, "xml"); // xml, html,
				// text
				t.setOutputProperty(
						"{http://xml.apache.org/xslt}indent-amount", "4");
				t.transform(new DOMSource(element), strResult);
			} catch (Exception e) {
				System.err.println("XML.toString(element): " + e);
			}
			result = strResult.getWriter().toString();
		}
		return result;
	}

	/**
	 * DEBUG Print a DOM Document
	 */
	public static String domToString(Document document) {
		return domToString(document.getDocumentElement());
	}
	
	public static Document getNewW3CDocument() {
		DocumentBuilder documentBuilder = null;
		try {
			documentBuilder = documentBuilderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e1) {
			e1.printStackTrace();
		}
		return documentBuilder.newDocument();
	}
}
