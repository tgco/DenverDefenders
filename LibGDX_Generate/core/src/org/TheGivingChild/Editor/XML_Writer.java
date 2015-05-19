package org.TheGivingChild.Editor;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;

import com.badlogic.gdx.utils.XmlWriter;

public class XML_Writer {
	/* QUICK NOTES ON THE XML WRITER
	 * 1: Setup a StringWriter(called xml in this case)
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
	 *	6: then just write the string to the file with a FileWriter
	 */
	private String XML_File = "";
	private String filename = "";
	private String packageName = "";
	//main method for testing onry
	public static void main(String cheese[]){
		XML_Writer sally = new XML_Writer();
		sally.writeToFile("testOut.xml");
	}
	
	public void XML_Writer(){
		
	}
	
	public void writeToFile(String newFilename){
		filename = newFilename;
		StringWriter text = new StringWriter();
		XmlWriter test = new XmlWriter(text);
		FileWriter bob;
		try{
			bob = new FileWriter(newFilename);
			test.element("TESTJUAN");
			test.attribute("TEST2", "SWAG");
			test.attribute("TEST3","HOLLER");
			test.pop();
			bob.write(text.toString());
			bob.close();
		}catch(Exception e){ System.out.println("ERRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR");}
	}
	
	public void compileXML_String(){
		
	}
	
	public void XML_test(){
		
	}
}
