package org.contestorg.infos;

/**
 * Conteneur d'informations pour une connexion a un serveur
 */
public class InfosConnexionServeur
{

	/** Hôte */
	private String host;
	
	/** Port */
	private int port;
	
	/** Nom d'utilisateur */
	private String username;
	private String password;

	/**
	 * Constructeur
	 * @param host hôte
	 * @param port numéro de port
	 * @param username nom d'utilisateur
	 * @param password mot de passe
	 */
	public InfosConnexionServeur(String host, int port, String username, String password) {
		this.host = host;
		this.port = port;
		this.username = username;
		this.password = password;
	}

	/**
	 * Récupérer l'hôte
	 * @return hôte
	 */
	public String getHost () {
		return this.host;
	}
	
	/**
	 * Récupérer le port
	 * @return port
	 */
	public int getPort () {
		return this.port;
	}
	
	/**
	 * Récupérer le nom d'utilisateur
	 * @return nom d'utilisateur
	 */
	public String getUsername () {
		return this.username;
	}
	
	/**
	 * Récupérer le mot de passe
	 * @return mot de passe
	 */
	public String getPassword () {
		return this.password;
	}

	/**
	 * Récupérer les données par défaut
	 * @return données par défaut
	 */
	public static InfosConnexionServeur defaut () {
		return new InfosConnexionServeur("127.0.0.1", 3500, "admin", "admin");
	}

}
