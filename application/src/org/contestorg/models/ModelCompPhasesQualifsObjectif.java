package org.contestorg.models;

import java.util.ArrayList;

import org.contestorg.common.ContestOrgErrorException;
import org.contestorg.infos.InfosModelCompPhasesQualifsObjectif;

/**
 * Critère de classement des phases qualificatives prenant en compte le nombre d'objectifs
 */
public class ModelCompPhasesQualifsObjectif extends ModelCompPhasesQualifsAbstract
{
	
	// Attributs objets
	
	/** Objectif */
	private ModelObjectif objectif;
	
	/**
	 * Constructeurs
	 * @param concours concours
	 * @param objectif objectif
	 * @param infos informations du critère de classement
	 */
	public ModelCompPhasesQualifsObjectif(ModelConcours concours, ModelObjectif objectif, InfosModelCompPhasesQualifsObjectif infos) {
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
	
	// Setters
	
	/**
	 * Définir les informations du critère de classement
	 * @param infos informations du critère de classement
	 */
	protected void setInfos (InfosModelCompPhasesQualifsObjectif infos) {
		// Appeller le setInfos du parent
		super.setInfos(infos);
		
		// Fire update
		this.fireUpdate();
	}
	
	/**
	 * @see ModelCompPhasesQualifsAbstract#getInfos()
	 */
	@Override
	public InfosModelCompPhasesQualifsObjectif getInfos () {
		InfosModelCompPhasesQualifsObjectif infos = new InfosModelCompPhasesQualifsObjectif(this.objectif.getInfos());
		infos.setId(this.getId());
		return infos;
	}
	
	/**
	 * @see ModelAbstract#delete(ArrayList)
	 */
	@Override
	protected void delete (ArrayList<ModelAbstract> removers) throws ContestOrgErrorException {
		if (!removers.contains(this)) {
			// Appeller le remove parent
			super.delete(removers);
			
			// Retirer le comparateur de l'objectif
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
