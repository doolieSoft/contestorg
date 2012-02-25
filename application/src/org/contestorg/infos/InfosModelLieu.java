package org.contestorg.infos;

/**
 * Conteneur d'informations pour la création ou la modification d'un lieu
 */
public class InfosModelLieu extends InfosModelAbstract
{

	/** Nom */
	private String nom;
	
	/** Lieu */
	private String lieu;
	
	/** Téléphone */
	private String telephone;
	
	/** Email */
	private String email;
	
	/** Description */
	private String description;

	/**
	 * Constructeur
	 * @param nom nom
	 * @param lieu lieu
	 * @param telephone téléphone
	 * @param email email
	 * @param description description
	 */
	public InfosModelLieu(String nom, String lieu, String telephone, String email, String description) {
		this.nom = nom;
		this.lieu = lieu;
		this.telephone = telephone;
		this.email = email;
		this.description = description;
	}

	/**
	 * Récupérer le nom
	 * @return nom
	 */
	public String getNom () {
		return this.nom;
	}
	
	/**
	 * Récupérer le lieu
	 * @return lieu
	 */
	public String getLieu () {
		return this.lieu;
	}
	
	/**
	 * Récupérer le téléphone
	 * @return téléphone
	 */
	public String getTelephone () {
		return this.telephone;
	}
	
	/**
	 * Email
	 * @return email
	 */
	public String getEmail () {
		return this.email;
	}
	
	/**
	 * Récupérer la description
	 * @return description
	 */
	public String getDescription () {
		return this.description;
	}

	/**
	 * Récupérer les données par défaut
	 * @return données par défaut
	 */
	public static InfosModelLieu defaut () {
		return new InfosModelLieu("", "", "", "", "");
	}

}
