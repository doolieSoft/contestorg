package org.contestorg.out;

import java.io.File;
import java.util.HashMap;

import org.jdom.Document;

public class RessourceXSLT extends RessourceAbstract
{
	// Fichier xml
	private File xml;
	
	// Feuille XSL
	private File xsl;
	
	// Fichier résultat
	private File result;
	
	// Constructeur
	public RessourceXSLT(String cible, boolean principal, HashMap<String,String> parametres, HashMap<String,String> fichiers, File xsl) throws ContestOrgOutException {
		// Appeller le constructeur parent
		super(cible, principal, parametres, fichiers);
		
		// Detenir le XSL et définir les fichiers XML/résultat
		this.xsl = xsl; 
		this.xml = new File("temp/xml-"+this.toString());
		this.result = new File("temp/result-"+this.toString());
		
		// S'assurer que les fichiers temporaires soient détruit à la fermeture de l'application
		this.xml.deleteOnExit();
		this.result.deleteOnExit();
		
		// Rafraichir le fichier
		if(!this.refresh()) {
			throw new ContestOrgOutException("La transformation XSLT a échoué.");
		}
	}

	// Implémentation de refresh
	@Override
	protected boolean refresh () {
		Document document = PersistanceXML.getConcoursDocument();
		if(document != null) {
			return XMLHelper.save(document, this.xml) && XSLTHelper.transform(this.xml, this.xsl, this.result, this.getParametres());
		} else {
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
		return RessourceAbstract.getContentType(this.cible);
	}
	
}
