package org.contestorg.out;

import java.io.File;
import java.util.HashMap;

import org.jdom.Document;

/**
 * Classe d'exportation vers un fichier PDF
 */
public class RessourceFOP extends RessourceAbstract
{
	// Fichier XML
	private File xml;
	
	// Fichier XSL
	private File xsl;
	
	// Fichier XSL-FO
	private File xslfo;
	
	// Fichier résultat
	private File result;

	// Constructeur
	public RessourceFOP(String cible, boolean principal, HashMap<String,String> parametres, HashMap<String,String> fichiers, File xsl) throws ContestOrgOutException {
		// Constructeur parent
		super(cible,principal, parametres, fichiers);
		
		// Retenir le fichier XSL
		this.xsl = xsl;
		
		// Déclarer les fichiers temporaire
		this.xml = new File("temp/xml-"+this.toString());
		this.xslfo = new File("temp/fo-"+this.toString());
		this.result = new File("temp/result-"+this.toString());
		
		// S'assurer que les fichiers temporaires soient détruit à la fermeture de l'application
		this.xml.deleteOnExit();
		this.xslfo.deleteOnExit();
		this.result.deleteOnExit();
		
		// Rafraichir le fichier
		if(!this.refresh()) {
			throw new ContestOrgOutException("La transformation FOP a échoué.");
		}
	}
	
	// Implémentation de refresh
	@Override
	protected boolean refresh () {
		Document document = PersistanceXML.getConcoursDocument();
		if(document != null) {
			return XMLHelper.save(document, this.xml) && XSLTHelper.transform(this.xml, this.xsl, this.xslfo, this.getParametres()) && FOPHelper.transformPDF(this.xslfo, this.result);
		} else {
			this.clean();
			return false;
		}
	}

	// Implémentation de getFichier
	@Override
	public File getFichier () {
		return this.result;
	}

	// Implémentation de clean
	@Override
	public void clean () {
		if(this.xml != null && this.xml.exists()) {
			this.xml.delete();
		}
		if(this.xslfo != null && this.xslfo.exists()) {
			this.xslfo.delete();
		}
		if(this.result != null && this.result.exists()) {
			this.result.delete();
		}
	}
	
	// Méthode à implémenter
	public boolean isTransformation() {
		return true;
	}

	// Implémentation de getContestType
	@Override
	public String getContentType () {
		return "application/pdf";
	}

}
