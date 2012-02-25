package org.contestorg.infos;

/**
 * Conteneur d'informations pour la création ou la modification d'un emplacement
 */
public class InfosModelEmplacement extends InfosModelAbstract
{

	/** Nom */
	private String nom;
	
	/** Description */
	private String description;

	/**
	 * Constructeur
	 * @param nom nom
	 * @param description description
	 */
	public InfosModelEmplacement(String nom, String description) {
		this.nom = nom;
		this.description = description;
	}

	/**
	 * Récupérer le nom
	 * @return nom
	 */
	public String getNom () {
		return nom;
	}
	
	/**
	 * Récupérer la description
	 * @return description
	 */
	public String getDescription () {
		return description;
	}

	/**
	 * Récupérer les données par défaut
	 * @return données par défaut
	 */
	public static InfosModelEmplacement defaut () {
		return new InfosModelEmplacement("", "");
	}

}
