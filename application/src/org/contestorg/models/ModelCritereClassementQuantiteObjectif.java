package org.contestorg.models;

import java.util.ArrayList;

import org.contestorg.common.ContestOrgErrorException;
import org.contestorg.infos.InfosModelCritereClassementQuantiteObjectif;

/**
 * Critère de classement prenant en compte la quantité remportée d'un objectif
 */
public class ModelCritereClassementQuantiteObjectif extends ModelCritereClassementAbstract
{
	
	// Attributs objets
	
	/** Objectif */
	private ModelObjectif objectif;
	
	/**
	 * Constructeur
	 * @param concours concours
	 * @param objectif objectif
	 * @param infos informations du critère de classement
	 */
	public ModelCritereClassementQuantiteObjectif(ModelConcours concours, ModelObjectif objectif, InfosModelCritereClassementQuantiteObjectif infos) {
		// Appeller le constructeur parent
		super(concours);
		
		// Retenir l'objectif
		this.objectif = objectif;
		
		// Enregistrer les informations
		this.setInfos(infos);
	}
	
	// Getters
	
	/**
	 * Récupérer l'objectif
	 * @return objectif
	 */
	public ModelObjectif getObjectif () {
		return this.objectif;
	}
	
	/**
	 * @see ModelCritereClassementAbstract#getInfos()
	 */
	@Override
	public InfosModelCritereClassementQuantiteObjectif getInfos () {
		InfosModelCritereClassementQuantiteObjectif infos = new InfosModelCritereClassementQuantiteObjectif(this.isInverse,this.objectif.getInfos());
		infos.setId(this.getId());
		return infos;
	}
	
	// Setters
	
	/**
	 * Définir les informations du critère de classement
	 * @param infos informations du critère de classement
	 */
	protected void setInfos (InfosModelCritereClassementQuantiteObjectif infos) {
		// Appeller le setInfos du parent
		super.setInfos(infos);
		
		// Fire update
		this.fireUpdate();
	}
	
	/**
	 * @see ModelAbstract#delete(ArrayList)
	 */
	@Override
	protected void delete (ArrayList<ModelAbstract> removers) throws ContestOrgErrorException {
		if (!removers.contains(this)) {
			// Appeller le remove parent
			super.delete(removers);
			
			// Retirer le critère de classement de l'objectif
			if (this.objectif != null) {
				if (!removers.contains(this.objectif)) {
					this.objectif.removeCompPhasesQualifs(this);
				}
				this.objectif = null;
				this.fireClear(ModelObjectif.class);
			}
			
			// Fire delete
			this.fireDelete();
		}
	}
	
}
