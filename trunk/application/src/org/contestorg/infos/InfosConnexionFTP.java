package org.contestorg.infos;

/**
 * Cette classe est un conteneur d'information pour une connexion a un serveur FTP
 */
public class InfosConnexionFTP extends InfosConnexionServeur
{

	// Attributs
	private String path;
	private int mode;

	// Modes
	public static final int MODE_ACTIF = 1;
	public static final int MODE_PASSIF = 2;

	// Constructeur
	public InfosConnexionFTP(String host, int port, String username, String password, String path, int mode) {
		super(host, port, username, password);
		this.path = path;
		this.mode = mode;
	}

	// Getters
	public String getPath () {
		return this.path;
	}
	public int getMode () {
		return this.mode;
	}

	// Données par défaut
	public static InfosConnexionFTP defaut () {
		return new InfosConnexionFTP("", 21, "", "", "", InfosConnexionFTP.MODE_ACTIF);
	}

}
