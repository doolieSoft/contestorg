package org.contestorg.infos;

public class InfosModelCheminFTP extends InfosModelChemin
{

	// Attributs
	private InfosConnexionFTP infosFTP;

	// Constructeur
	public InfosModelCheminFTP(InfosConnexionFTP infosFTP) {
		this.infosFTP = infosFTP;
	}

	// Getters
	public InfosConnexionFTP getInfosFTP () {
		return this.infosFTP;
	}

	// Données par défaut
	public static InfosModelCheminFTP defaut () {
		return new InfosModelCheminFTP(InfosConnexionFTP.defaut());
	}

}
