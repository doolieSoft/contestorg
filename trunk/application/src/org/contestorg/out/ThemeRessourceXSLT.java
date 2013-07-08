package org.contestorg.out;

import java.io.File;
import java.util.HashMap;

import org.contestorg.common.ContestOrgWarningException;
import org.jdom.Document;

/**
 * Ressource XSLT de thème d'exportation/diffusion
 */
public class ThemeRessourceXSLT extends ThemeRessourceAbstract
{
	/** Fichier XML */
	private File xml;
	
	/** Feuille XSL */
	private File xsl;
	
	/** Fichier cible */
	private File target;
	
	/**
	 * Constructeur
	 * @param cible chemin du fichier cible
	 * @param principal ressource principale ?
	 * @param parametres liste des paramètres
	 * @param fichiers liste des fichiers
	 * @param xsl fichier XSL
	 * @throws ContestOrgOutException
	 */
	public ThemeRessourceXSLT(String cible, boolean principal, HashMap<String,String> parametres, HashMap<String,String> fichiers, File xsl) throws ContestOrgWarningException {
		// Appeller le constructeur parent
		super(cible, principal, parametres, fichiers);
		
		// Detenir le XSL et définir les fichiers XML/résultat
		this.xsl = xsl; 
		this.xml = new File("temp/xml-"+this.toString());
		this.target = new File("temp/target-"+this.toString());
		
		// S'assurer que les fichiers temporaires soient détruit à la fermeture de l'application
		this.xml.deleteOnExit();
		this.target.deleteOnExit();
		
		// Rafraichir le fichier
		if(!this.refresh()) {
			throw new ContestOrgWarningException("La transformation XSLT a échoué.");
		}
	}

	/**
	 * @see ThemeRessourceAbstract#refresh()
	 */
	@Override
	protected boolean refresh () {
		Document document = PersistanceXML.getConcoursDocument();
		if(document != null) {
			return XMLHelper.save(document, this.xml) && XSLTHelper.transform(this.xml, this.xsl, this.target, this.getParametres());
		} else {
			return false;
		}
	}
	
	/**
	 * @see ThemeRessourceAbstract#getFichier()
	 */
	@Override
	public File getFichier () {
		return this.target;
	}
	
	/**
	 * @see ThemeRessourceAbstract#clean()
	 */
	@Override
	public void clean () {
		if(this.xml != null && this.xml.exists()) {
			this.xml.delete();
		}
		if(this.target != null && this.target.exists()) {
			this.target.delete();
		}
	}
	
	/**
	 * @see ThemeRessourceAbstract#isTransformation()
	 */
	@Override
	public boolean isTransformation() {
		return true;
	}

	/**
	 * @see ThemeRessourceAbstract#getContentType()
	 */
	@Override
	public String getContentType () {
		return ThemeRessourceAbstract.getContentType(this.cible);
	}
	
}
