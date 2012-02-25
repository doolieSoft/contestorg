package org.contestorg.infos;

/**
 * Conteneur d'informations pour la création ou la modification d'un chemin d'exportation en local
 */
public class InfosModelCheminLocal extends InfosModelChemin
{

	/** Chemin */
	private String chemin;

	/**
	 * Constructeur
	 * @param chemin chemin
	 */
	public InfosModelCheminLocal(String chemin) {
		this.chemin = chemin;
	}

	/**
	 * Récupérer le chemin
	 * @return chemin
	 */
	public String getChemin () {
		return this.chemin;
	}

	/**
	 * Récupérer les données par défaut
	 * @return données par défaut
	 */
	public static InfosModelCheminLocal defaut () {
		return new InfosModelCheminLocal("");
	}

}
