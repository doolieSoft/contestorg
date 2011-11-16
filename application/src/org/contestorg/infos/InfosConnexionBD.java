package org.contestorg.infos;

/**
 * Cette classe est un conteneur d'information pour une connexion a un serveur BD
 */
public class InfosConnexionBD extends InfosConnexionServeur
{

	// Attributs
	private String database;

	// Constructeur
	public InfosConnexionBD(String host, int port, String login, String password, String database) {
		super(host, port, login, password);
		this.database = database;
	}

	// Getters
	public String getDatabase () {
		return this.database;
	}

	// Données par défaut
	public static InfosConnexionBD defaut () {
		return new InfosConnexionBD("127.0.0.1", 3500, "root", "root", "");
	}

}
