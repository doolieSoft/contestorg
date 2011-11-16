package org.contestorg.infos;

/**
 * Cette classe est un conteneur d'information pour une connexion a un serveur
 */
public class InfosConnexionServeur
{

	// Attributs
	private String host;
	private int port;
	private String username;
	private String password;

	// Constructeur
	public InfosConnexionServeur(String host, int port, String username, String password) {
		this.host = host;
		this.port = port;
		this.username = username;
		this.password = password;
	}

	// Getters
	public String getHost () {
		return this.host;
	}
	public int getPort () {
		return this.port;
	}
	public String getUsername () {
		return this.username;
	}
	public String getPassword () {
		return this.password;
	}

	// Informations par défaut
	public static InfosConnexionServeur defaut () {
		return new InfosConnexionServeur("127.0.0.1", 3500, "admin", "admin");
	}

}
