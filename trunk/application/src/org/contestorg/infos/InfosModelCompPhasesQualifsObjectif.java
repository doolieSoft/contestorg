package org.contestorg.infos;

/**
 * Conteneur d'informations pour la création ou la modification d'un comparateur d'objectifs en vue de générer les phases qualificatives
 */
public class InfosModelCompPhasesQualifsObjectif extends InfosModelCompPhasesQualifsAbstract
{
	
	/** Informations de l'objectif associé */
	private InfosModelObjectif objectif;
	
	/**
	 * Constructeur
	 * @param objectif informations de l'objectif associé
	 */
	public InfosModelCompPhasesQualifsObjectif(InfosModelObjectif objectif) {
		this.objectif = objectif;
	}
	
	/**
	 * Récupérer les informations de l'objectif associé
	 * @return informations de l'objectif associé
	 */
	public InfosModelObjectif getObjectif() {
		return this.objectif;
	}
	
}
