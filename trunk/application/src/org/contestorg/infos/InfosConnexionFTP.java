package org.contestorg.infos;

/**
 * Conteneur d'informations pour une connexion a un serveur FTP
 */
public class InfosConnexionFTP extends InfosConnexionServeur
{

	/** Chemin */
	private String path;
	
	/** Mode de connexion */
	private int mode;

	// Modes de connexion
	
	/** Mode de connexion actif */
	public static final int MODE_ACTIF = 1;
	
	/** Mode de connexion passif */
	public static final int MODE_PASSIF = 2;

	/**
	 * Constructeur
	 * @param host hôte
	 * @param port numéro de port
	 * @param username nom d'utilisateur
	 * @param password mot de passe
	 * @param path chemin
	 * @param mode mode de connexion
	 */
	public InfosConnexionFTP(String host, int port, String username, String password, String path, int mode) {
		super(host, port, username, password);
		this.path = path;
		this.mode = mode;
	}

	/**
	 * Récupérer le chemin 
	 * @return chemin
	 */
	public String getPath () {
		return this.path;
	}
	
	/**
	 * Récupérer le mode de connexion
	 * @return mode de connexion
	 */
	public int getMode () {
		return this.mode;
	}

	/**
	 * Récupérer les données par défaut
	 * @return données par défaut
	 */
	public static InfosConnexionFTP defaut () {
		return new InfosConnexionFTP("", 21, "", "", "", InfosConnexionFTP.MODE_ACTIF);
	}

}
