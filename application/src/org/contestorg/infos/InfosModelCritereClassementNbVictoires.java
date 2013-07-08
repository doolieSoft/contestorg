package org.contestorg.infos;

/**
 * Conteneur d'informations pour la création ou la modification d'un critère de classement prenant en compte le nombre de victoires
 */
public class InfosModelCritereClassementNbVictoires extends InfosModelCritereClassementAbstract
{
	
	/**
	 * Constructeur
	 * @param isInverse est-ce que le critère est inversé ?
	 */
	public InfosModelCritereClassementNbVictoires(boolean isInverse) {
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
		return infos instanceof InfosModelCritereClassementNbVictoires;
	}

	/**
	 * @see InfosModelCritereClassementAbstract#isUtilisablePlusieursFois()
	 */
	@Override
	public boolean isUtilisablePlusieursFois () {
		return false;
	}

}
