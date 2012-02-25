package org.contestorg.infos;

/**
 * Conteneur d'informations pour la création ou la modification d'une poule
 */
public class InfosModelPoule extends InfosModelAbstract
{

	/** Nom */
	private String nom;
	
	/**
	 * Constructeur
	 * @param nom nom
	 */
	public InfosModelPoule(String nom) {
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
