package org.contestorg.out;

import java.io.File;
import java.util.HashMap;

import org.contestorg.common.ContestOrgWarningException;
import org.jdom.Document;

/**
 * Ressource FOP
 */
public class RessourceFOP extends RessourceAbstract
{
	/** Fichier XML */
	private File xml;
	
	/** Fichier XSL */
	private File xsl;
	
	/** Fichier XSL-FO */
	private File xslfo;
	
	/** Fichier cible */
	private File target;

	/**
	 * Constructeur
	 * @param cible chemin du fichier cible
	 * @param principale ressource principale ?
	 * @param parametres liste des paramètres
	 * @param fichiers liste des fichiers
	 * @param xsl fichier XSL
	 * @throws ContestOrgOutException
	 */
	public RessourceFOP(String cible, boolean principale, HashMap<String,String> parametres, HashMap<String,String> fichiers, File xsl) throws ContestOrgWarningException {
		// Constructeur parent
		super(cible, principale, parametres, fichiers);
		
		// Retenir le fichier XSL
		this.xsl = xsl;
		
		// Déclarer les fichiers temporaire
		this.xml = new File("temp/xml-"+this.toString());
		this.xslfo = new File("temp/fo-"+this.toString());
		this.target = new File("temp/target-"+this.toString());
		
		// S'assurer que les fichiers temporaires soient détruit à la fermeture de l'application
		this.xml.deleteOnExit();
		this.xslfo.deleteOnExit();
		this.target.deleteOnExit();
		
		// Rafraichir le fichier
		if(!this.refresh()) {
			throw new ContestOrgWarningException("La transformation FOP a échoué.");
		}
	}
	
	/**
	 * @see RessourceAbstract#refresh()
	 */
	@Override
	protected boolean refresh () {
		Document document = PersistanceXML.getConcoursDocument();
		if(document != null) {
			// Sauvegarder le fichier XML
			if(!XMLHelper.save(document, this.xml)) {
				return false;
			}
			
			// Récupérer les paramètres
			HashMap<String, String> parametres = this.getParametres();
			
			// Transformation XSLT
			if(!XSLTHelper.transform(this.xml, this.xsl, this.xslfo, parametres)) {
				return false;
			}
			
			// Récupérer le nombre de pages du futur fichier PDF
			int nbPages = FOPHelper.getNbPages(this.xslfo);
			parametres.put("nbPages", String.valueOf(nbPages));
			
			// Transformation XSLT
			if(!XSLTHelper.transform(this.xml, this.xsl, this.xslfo, parametres)) {
				return false;
			}
			
			// Transformation FOP
			return FOPHelper.transformPDF(this.xslfo, this.target);
		} else {
			this.clean();
			return false;
		}
	}

	/**
	 * @see RessourceAbstract#getFichier()
	 */
	@Override
	public File getFichier () {
		return this.target;
	}

	/**
	 * @see RessourceAbstract#clean()
	 */
	@Override
	public void clean () {
		if(this.xml != null && this.xml.exists()) {
			this.xml.delete();
		}
		if(this.xslfo != null && this.xslfo.exists()) {
			this.xslfo.delete();
		}
		if(this.target != null && this.target.exists()) {
			this.target.delete();
		}
	}
	
	/**
	 * @see RessourceAbstract#isTransformation()
	 */
	@Override
	public boolean isTransformation() {
		return true;
	}

	/**
	 * @see RessourceAbstract#getContentType()
	 */
	@Override
	public String getContentType () {
		return "application/pdf";
	}

}
