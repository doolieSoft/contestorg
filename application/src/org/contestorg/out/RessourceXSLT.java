package org.contestorg.out;

import java.io.File;
import java.util.HashMap;

import org.jdom.Document;

/**
 * Ressource XSLT
 */
public class RessourceXSLT extends RessourceAbstract
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
	 * @param principale ressource principale ?
	 * @param parametres liste des paramètres
	 * @param fichiers liste des fichiers
	 * @param xsl fichier XSL
	 * @throws ContestOrgOutException
	 */
	public RessourceXSLT(String cible, boolean principal, HashMap<String,String> parametres, HashMap<String,String> fichiers, File xsl) throws ContestOrgOutException {
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
			throw new ContestOrgOutException("La transformation XSLT a échoué.");
		}
	}

	/**
	 * @see RessourceAbstract#refresh()
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
		return RessourceAbstract.getContentType(this.cible);
	}
	
}
