package org.contestorg.out;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.contestorg.log.Log;
import org.jdom.Document;
import org.jdom.transform.JDOMResult;
import org.jdom.transform.JDOMSource;

/**
 * Classe d'aide à la réalisation d'opérations XSLT
 */
public class XSLTHelper
{
	/**
	 * Transformer un document XML avec une feuille de style XSL
	 * @param xml fichier XML
	 * @param xsl fichier XSL
	 * @param target fichier cible
	 * @param parameters liste des paramètres XSL
	 * @return opération réussie ?
	 */
	public static boolean transform (File xml, File xsl, File target, HashMap<String, String> parameters) {	
		try {
			// Configuration du transformer
			TransformerFactory factory = TransformerFactory.newInstance();
			StreamSource stylesource = new StreamSource(xsl);
			Transformer transformer = factory.newTransformer(stylesource);
			if (parameters != null) {
				for (Entry<String, String> parameter : parameters.entrySet()) {
					transformer.setParameter(parameter.getKey(), parameter.getValue() == null ? "" : parameter.getValue());
				}
			}
			
			// Transformation
			StreamResult streamResult = new StreamResult(new OutputStreamWriter(new FileOutputStream(target), "UTF8"));
			StreamSource streamSource = new StreamSource(new InputStreamReader(new FileInputStream(xml),"UTF8"));
			transformer.transform(streamSource, streamResult);
			
			// L'opération s'est bien déroulée
			return true;
		} catch (Exception e) {
			Log.getLogger().error("Erreur lors de la transformation XSLT.", e);
			return false;
		}
	}
	
	/**
	 * Transformer un document XML avec une feuille de style XSL
	 * @param xml document XML
	 * @param xsl feuille de style XSL
	 * @param parameters liste des paramètres XSL
	 * @return résultat de la transformation
	 */
	public static Document transform(Document xml, Document xsl, HashMap<String, String> parameters) {
		try {
			// Configuration du transformer
			TransformerFactory factory = TransformerFactory.newInstance();
			JDOMSource stylesource = new JDOMSource(xsl);
			Transformer transformer = factory.newTransformer(stylesource);
			if (parameters != null) {
				for (Entry<String, String> parameter : parameters.entrySet()) {
					transformer.setParameter(parameter.getKey(), parameter.getValue() == null ? "" : parameter.getValue());
				}
			}
			
			// Créer le résultat de la transformation
			JDOMResult result = new JDOMResult();
			
			// Transformation
			transformer.transform(new JDOMSource(xml), result);
			
			// Récupérer et retourner le document associé au résultat de la transformation
			return result.getDocument();
		} catch (Exception e) {
			Log.getLogger().error("Erreur lors de la transformation XSLT.", e);
			return null;
		}
	}
}
