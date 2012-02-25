package org.contestorg.out;

import java.io.File;
import java.util.HashMap;

/**
 * Ressource fichier
 */
public class RessourceFichier extends RessourceAbstract
{
	/** Fichier */
	private File fichier;

	/**
	 * Constructeur
	 * @param cible chemin du fichier cible
	 * @param principale ressource principale ?
	 * @param parametres liste des paramètres
	 * @param fichiers liste des fichiers
	 * @param fichier fichier source
	 */
	public RessourceFichier(String cible, boolean principale, HashMap<String,String> parametres, HashMap<String,String> fichiers, File fichier) {
		// Appeler le constructeur parent
		super(cible, principale, parametres, fichiers);
		
		// Retenir le fichier
		this.fichier = fichier;
	}

	/**
	 * @see RessourceAbstract#getFichier()
	 */
	@Override
	public File getFichier () {
		return this.fichier;
	}

	/**
	 * @see RessourceAbstract#clean()
	 */
	@Override
	public void clean () {
	}

	/**
	 * @see RessourceAbstract#getContentType()
	 */
	@Override
	public String getContentType () {
		return RessourceAbstract.getContentType(this.fichier.getName());
	}
	
	/**
	 * @see RessourceAbstract#isTransformation()
	 */
	@Override
	public boolean isTransformation() {
		return false;
	}

	/**
	 * @see RessourceAbstract#refresh()
	 */
	@Override
	protected boolean refresh () {
		return this.fichier.exists();
	}

}
