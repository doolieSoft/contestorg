package org.contestorg.infos;

/**
 * Conteneur d'informations pour la création ou la modification d'une propriété possédée
 */
public class InfosModelProprietePossedee extends InfosModelAbstract
{

	/** Valeur */
	private String valeur;

	/**
	 * Constructeur
	 * @param valeur valeur
	 */
	public InfosModelProprietePossedee(String valeur) {
		this.valeur = valeur;
	}

	/**
	 * Récupérer la valeur
	 * @return valeur
	 */
	public String getValeur () {
		return this.valeur;
	}

	/**
	 * Récupérer les données par défaut
	 * @return données par défaut
	 */
	public static InfosModelProprietePossedee defaut () {
		return new InfosModelProprietePossedee("");
	}

}
