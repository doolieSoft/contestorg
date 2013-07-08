package org.contestorg.out;

import java.io.File;
import java.util.HashMap;

/**
 * Ressource fichier de thème d'exportation/diffusion
 */
public class ThemeRessourceFichier extends ThemeRessourceAbstract
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
	public ThemeRessourceFichier(String cible, boolean principale, HashMap<String,String> parametres, HashMap<String,String> fichiers, File fichier) {
		// Appeler le constructeur parent
		super(cible, principale, parametres, fichiers);
		
		// Retenir le fichier
		this.fichier = fichier;
	}

	/**
	 * @see ThemeRessourceAbstract#getFichier()
	 */
	@Override
	public File getFichier () {
		return this.fichier;
	}

	/**
	 * @see ThemeRessourceAbstract#clean()
	 */
	@Override
	public void clean () {
	}

	/**
	 * @see ThemeRessourceAbstract#getContentType()
	 */
	@Override
	public String getContentType () {
		return ThemeRessourceAbstract.getContentType(this.fichier.getName());
	}
	
	/**
	 * @see ThemeRessourceAbstract#isTransformation()
	 */
	@Override
	public boolean isTransformation() {
		return false;
	}

	/**
	 * @see ThemeRessourceAbstract#refresh()
	 */
	@Override
	protected boolean refresh () {
		return this.fichier.exists();
	}

}
