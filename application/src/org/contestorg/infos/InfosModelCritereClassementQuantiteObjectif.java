package org.contestorg.infos;

/**
 * Conteneur d'informations pour la création ou la modification d'un critère de classement prenant en compte la quantité remportée d'un objectif
 */
public class InfosModelCritereClassementQuantiteObjectif extends InfosModelCritereClassementAbstract
{
	
	/** Informations de l'objectif associé */
	private InfosModelObjectif objectif;
	
	/**
	 * Constructeur
	 * @param isInverse est-ce que le critère est inversé ?
	 * @param objectif informations de l'objectif associé
	 */
	public InfosModelCritereClassementQuantiteObjectif(boolean isInverse,InfosModelObjectif objectif) {
		super(isInverse);
		this.objectif = objectif;
	}
	
	/**
	 * Récupérer les informations de l'objectif associé
	 * @return informations de l'objectif associé
	 */
	public InfosModelObjectif getObjectif() {
		return this.objectif;
	}
	
	/**
	 * @see InfosModelCritereClassementAbstract#equals(InfosModelCritereClassementAbstract)
	 */
	@Override
	public boolean equals (InfosModelCritereClassementAbstract infos) {
		if(infos == null) {
			return false;
		}
		if(!(infos instanceof InfosModelCritereClassementQuantiteObjectif)) {
			return false;
		}
		return this.getObjectif().getNom().equals(((InfosModelCritereClassementQuantiteObjectif)infos).getObjectif().getNom());
	}

	/**
	 * @see InfosModelCritereClassementAbstract#isUtilisablePlusieursFois()
	 */
	@Override
	public boolean isUtilisablePlusieursFois () {
		return false;
	}
	
}
