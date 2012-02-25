package org.contestorg.infos;

/**
 * Conteneur d'informations pour la création ou la modification d'un categorie
 */
public class InfosModelCategorie extends InfosModelAbstract
{

	/** Nom */
	private String nom;

	/**
	 * Constructeur
	 * @param nom nom
	 */
	public InfosModelCategorie(String nom) {
		this.nom = nom;
	}

	/**
	 * Récupérer le nom
	 * @return nom
	 */
	public String getNom () {
		return this.nom;
	}

	/**
	 * Récupérer les données par défaut
	 * @return données par défaut
	 */
	public static InfosModelCategorie defaut () {
		return new InfosModelCategorie("");
	}

}
