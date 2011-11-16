package out;

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

import log.Log;

public class XSLTHelper
{
	// Méthode permettant de transformer un document XML avec une feuille de style XSL
	public static boolean transform (File xml, File xsl, File result, HashMap<String, String> parameters) {	
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
			StreamResult streamResult = new StreamResult(new OutputStreamWriter(new FileOutputStream(result), "UTF8"));
			StreamSource streamSource = new StreamSource(new InputStreamReader(new FileInputStream(xml),"UTF8"));
			transformer.transform(streamSource, streamResult);
			
			// L'opération s'est bien déroulée
			return true;
		} catch (Exception e) {
			Log.getLogger().error("Erreur lors de la transformation XSLT.", e);
			return false;
		}
	}
}
