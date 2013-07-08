package org.contestorg.infos;

/**
 * Conteneur d'informations pour la création ou la modification d'un critère de classement prenant en compte le nombre de forfaits
 */
public class InfosModelCritereClassementNbForfaits extends InfosModelCritereClassementAbstract
{
	
	/**
	 * Constructeur
	 * @param isInverse est-ce que le critère est inversé ?
	 */
	public InfosModelCritereClassementNbForfaits(boolean isInverse) {
		super(isInverse);
	}
	
	/**
	 * @see InfosModelCritereClassementAbstract#equals(InfosModelCritereClassementAbstract)
	 */
	@Override
	public boolean equals (InfosModelCritereClassementAbstract infos) {
		if(infos == null) {
			return false;
		}
		return infos instanceof InfosModelCritereClassementNbForfaits;
	}

	/**
	 * @see InfosModelCritereClassementAbstract#isUtilisablePlusieursFois()
	 */
	@Override
	public boolean isUtilisablePlusieursFois () {
		return false;
	}

}
