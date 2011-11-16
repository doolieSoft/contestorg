package org.contestorg.infos;

public class InfosModelCheminLocal extends InfosModelChemin
{

	// Attributs
	private String chemin;

	// Constructeur
	public InfosModelCheminLocal(String chemin) {
		this.chemin = chemin;
	}

	// Getters
	public String getChemin () {
		return this.chemin;
	}

	// Données par défaut
	public static InfosModelCheminLocal defaut () {
		return new InfosModelCheminLocal("");
	}

}
