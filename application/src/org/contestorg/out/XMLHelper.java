package org.contestorg.out;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import org.jdom.Document;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 * Classe d'aide à la réalisation d'opérations XML
 */
public class XMLHelper
{
	/**
	 * Sauvegarder un document JDom dans un fichier
	 * @param document document JDom
	 * @param target fichier cible
	 * @return opération réussie ?
	 */
	public static boolean save(Document document, File target) {
		try {
			// Enregistrer le document JDom
			XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
			OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(target),"UTF8");
			sortie.output(PersistanceXML.getConcoursDocument(), writer);
			writer.close();
			
			// L'opération s'est bien déroulée
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
