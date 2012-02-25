package org.contestorg.infos;

/**
 * Conteneur d'informations pour la création ou la modification d'un chemin d'exportation FTP
 */
public class InfosModelCheminFTP extends InfosModelChemin
{

	/** Conteneur d'informations sur la connexion FTP */
	private InfosConnexionFTP infosFTP;

	/**
	 * Constructeur
	 * @param infosFTP conteneur d'informations sur la connexion FTP 
	 */
	public InfosModelCheminFTP(InfosConnexionFTP infosFTP) {
		this.infosFTP = infosFTP;
	}

	/**
	 * Récupérer le conteneur d'informations sur la connexion FTP
	 * @return conteneur d'informations sur la connexion FTP
	 */
	public InfosConnexionFTP getInfosFTP () {
		return this.infosFTP;
	}

	/**
	 * Récupérer les données par défaut
	 * @return données par défaut
	 */
	public static InfosModelCheminFTP defaut () {
		return new InfosModelCheminFTP(InfosConnexionFTP.defaut());
	}

}
