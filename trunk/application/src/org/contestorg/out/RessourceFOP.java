package org.contestorg.out;

import java.io.File;
import java.util.HashMap;

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
	public RessourceFOP(String cible, boolean principale, HashMap<String,String> parametres, HashMap<String,String> fichiers, File xsl) throws ContestOrgOutException {
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
			throw new ContestOrgOutException("La transformation FOP a échoué.");
		}
	}
	
	/**
	 * @see RessourceAbstract#refresh()
	 */
	@Override
	protected boolean refresh () {
		Document document = PersistanceXML.getConcoursDocument();
		if(document != null) {
			return XMLHelper.save(document, this.xml) && XSLTHelper.transform(this.xml, this.xsl, this.xslfo, this.getParametres()) && FOPHelper.transformPDF(this.xslfo, this.target);
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
