package org.contestorg.infos;

/**
 * Conteneur d'informations pour la création ou la modification d'un objectif remporté
 */
public class InfosModelObjectifRemporte extends InfosModelAbstract
{

	/** Quantité */
	private int quantite;

	/**
	 * Constructeur
	 * @param quantite quantité
	 */
	public InfosModelObjectifRemporte(int quantite) {
		this.quantite = quantite;
	}

	/**
	 * Récupérer la quantité
	 * @return quantité
	 */
	public int getQuantite () {
		return this.quantite;
	}

	/**
	 * Récupérer les données par défaut
	 * @return données par défaut
	 */
	public static InfosModelObjectifRemporte defaut () {
		return new InfosModelObjectifRemporte(0);
	}

}
