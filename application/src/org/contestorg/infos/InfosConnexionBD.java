package org.contestorg.infos;

/**
 * Conteneur d'informations pour une connexion à un serveur BD
 */
public class InfosConnexionBD extends InfosConnexionServeur
{

	/** Base de données */
	private String database;

	/**
	 * Constructeur
	 * @param host hôte
	 * @param port numéro de port
	 * @param login nom d'utilisateur
	 * @param password mot de passe
	 * @param database base de donnée
	 */
	public InfosConnexionBD(String host, int port, String login, String password, String database) {
		super(host, port, login, password);
		this.database = database;
	}

	/**
	 * Récupérer la base de données
	 * @return base de données
	 */
	public String getDatabase () {
		return this.database;
	}

	/**
	 * Données par défaut
	 * @return données par défaut
	 */
	public static InfosConnexionBD defaut () {
		return new InfosConnexionBD("127.0.0.1", 3500, "root", "root", "");
	}

}
