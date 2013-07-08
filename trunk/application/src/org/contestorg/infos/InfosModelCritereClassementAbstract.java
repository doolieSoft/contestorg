package org.contestorg.infos;

/**
 * Conteneur d'informations pour la création ou la modification d'un critère de classement
 */
public abstract class InfosModelCritereClassementAbstract extends InfosModelAbstract
{
	
	/** Est-ce que le critère est inversé ? */
	private boolean inverse;

	/**
	 * Constructeur
	 * @param inverse est-ce que le critère est inversé ?
	 */
	public InfosModelCritereClassementAbstract(boolean inverse) {
		this.inverse = inverse;
	}

	/**
	 * Est-ce que le critère est inversé ?
	 * @return est-ce que le critère est inversé ?
	 */
	public boolean isInverse () {
		return this.inverse;
	}
	
	/**
	 * Savoir si un critère de classement est similaire à un autre
	 * @param infos critère de classement
	 * @return critère de classement similaire ?
	 */
	abstract public boolean equals(InfosModelCritereClassementAbstract infos);
	
	/**
	 * Savoir si le critère de classement peut-être utilisé plusieurs fois 
	 * @return le critère de classement peut-être utilisé plusieurs fois ?
	 */
	abstract public boolean isUtilisablePlusieursFois();
	
}
