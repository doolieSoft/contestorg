package org.contestorg.out;

import java.io.File;
import java.util.HashMap;

public class RessourceFichier extends RessourceAbstract
{
	// Fichier
	private File fichier;

	// Constructeur
	public RessourceFichier(String cible, boolean principal, HashMap<String,String> parametres, HashMap<String,String> fichiers, File fichier) {
		// Appeler le constructeur parent
		super(cible,principal, parametres, fichiers);
		
		// Retenir le fichier
		this.fichier = fichier;
	}

	// Implémentation de getFichier
	@Override
	public File getFichier () {
		return this.fichier;
	}

	// Implémentation de clean
	@Override
	public void clean () {
	}

	// Implémentation de getContestType
	@Override
	public String getContentType () {
		return RessourceAbstract.getContentType(this.fichier.getName());
	}
	
	// Méthode à implémenter
	public boolean isTransformation() {
		return false;
	}

	// Implémentation de refresh
	@Override
	protected boolean refresh () {
		return this.fichier.exists();
	}

}
