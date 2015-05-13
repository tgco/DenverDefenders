package org.TheGivingChild.Editor;

import java.io.IOException;
import java.io.StringWriter;

import com.badlogic.gdx.utils.XmlWriter;

public class XML_Writer {
	/* QUICK NOTES ON THE XML WRITER
	 * 1: Setup a StringWriter
	 * 2: Setup a XmlWriter and pass the constructor the StringWriter
	 * 3: xml_writer.element adds a new element
	 * 4: xml_writer.attribute("attribute name","attribute value") adds an attribute
	 * 5: xml_writer.pop() goes up a level in the tree
	 * EX: 	xml.element("ELEMENT_JUAN");
	 *		xml.attribute("ATTRIBUTE","VALUE");
	 *		xml.element("ELEMENT_TWO");
	 *		xml.attribute("ATTR","VAL");
	 *		xml.pop();
	 *		xml.pop();
	 *		
	 *		returns a STRING with the value of:
	 *		<ELEMENT_JUAN ATTRIBUTE="VALUE">
	 *			<ELEMENT_TWO ATTR="VAL"/>
	 *		</ELEMENT_JUAN>
	 */
	
	public void XML_test(){
		
	}
}
