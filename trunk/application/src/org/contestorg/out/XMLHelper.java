package org.contestorg.out;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import org.jdom.Document;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class XMLHelper
{
	public static boolean save(Document document, File file) {
		try {
			// Enregistrer le document JDom
			XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
			OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file),"UTF8");
			sortie.output(PersistanceXML.getConcoursDocument(), writer);
			writer.close();
			
			// L'opération s'est bien déroulée
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
