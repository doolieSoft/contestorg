package org.contestorg.out;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.output.NullOutputStream;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.contestorg.log.Log;

/**
 * Classe d'aide à la réalisation d'opérations FOP
 */
public class FOPHelper
{
	/**
	 * Transformer un fichier XSL-FO en fichier PDF
	 * @param xslfo ficher XSL-FO
	 * @param pdf fichier PDF
	 * @return opération réussie ?
	 */
	public static boolean transformPDF(File xslfo, File pdf) {
		// Déclarer le flux de sortie
		OutputStream out = null;
		
		try {
			// Créer le flux d'entée
            Source streamSource = new StreamSource(new InputStreamReader(new FileInputStream(xslfo),"UTF8"));
            
			// Créer le flux de sortie
			out = new BufferedOutputStream(new FileOutputStream(pdf));

            // Configurer le processeur XSLT
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
			
        	// Configurer le traitement FOP qui va capturer le résultat du processeur XSLT
    		FopFactory fopFactory = FopFactory.newInstance();
            FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
            Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out);
            Result streamResult = new SAXResult(fop.getDefaultHandler());

            // Transformation XSLT et traitement FOP
            transformer.transform(streamSource, streamResult);
	        
			// L'opération s'est bien déroulée
			return true;
		} catch (Exception e) {
			Log.getLogger().error("Erreur lors de la transformation FOP.", e);
			return false;
		} finally {
			// Fermer le flux de sortie
			try { out.close(); } catch (IOException e) { }
		}
	}
	
	/**
	 * Récupérer le nombre de pages d'un fichier généré via FOP
	 * @param xslfo fichier XSL-FO
	 * @return nombre de pages du fichier généré via FOP
	 */
	public static int getNbPages(File xslfo) {
		// Déclarer le flux de sortie
		OutputStream out = null;
		
		try {
			// Créer le flux d'entée
            Source streamSource = new StreamSource(new InputStreamReader(new FileInputStream(xslfo),"UTF8"));
            
			// Créer le flux de sortie
			out = new NullOutputStream();

            // Configurer le processeur XSLT
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
			
        	// Configurer le traitement FOP qui va capturer le résultat du processeur XSLT
    		FopFactory fopFactory = FopFactory.newInstance();
            FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
            Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out);
            Result streamResult = new SAXResult(fop.getDefaultHandler());

            // Transformation XSLT et traitement FOP
            transformer.transform(streamSource, streamResult);
	        
			// L'opération s'est bien déroulée
			return fop.getResults().getPageCount();
		} catch (Exception e) {
			Log.getLogger().error("Erreur lors de la transformation FOP.", e);
			return -1;
		} finally {
			// Fermer le flux de sortie
			try { out.close(); } catch (IOException e) { }
		}
	}
}
