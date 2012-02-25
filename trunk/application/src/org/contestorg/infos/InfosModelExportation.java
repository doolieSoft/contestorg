package org.contestorg.infos;

/**
 * Conteneur d'informations pour la création ou la modification d'une exportation
 */
public class InfosModelExportation extends InfosModelAbstract
{

	/** Nom */
	private String nom;
	
	/** Est-ce une exportation automatique ? */
	private boolean auto;

	/**
	 * Constructeur
	 * @param nom nom
	 * @param auto est-ce une exportation automatique ?
	 */
	public InfosModelExportation(String nom, boolean auto) {
		this.nom = nom;
		this.auto = auto;
	}

	/**
	 * Récupérer le nom
	 * @return nom
	 */
	public String getNom () {
		return this.nom;
	}
	
	/**
	 * Savoir s'il s'agit d'une exportation automatique
	 * @return exportation automatique ?
	 */
	public boolean isAuto () {
		return this.auto;
	}

}
