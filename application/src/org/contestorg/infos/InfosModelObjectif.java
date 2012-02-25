package org.contestorg.infos;

/**
 * Conteneur d'informations pour la création ou la modification d'un objectif
 */
public abstract class InfosModelObjectif extends InfosModelAbstract
{

	/** Nom */
	private String nom;

	/**
	 * Constructeur
	 * @param nom nom
	 */
	public InfosModelObjectif(String nom) {
		this.nom = nom;
	}

	/**
	 * Récupérer le nom
	 * @return nom
	 */
	public String getNom () {
		return this.nom;
	}

}
