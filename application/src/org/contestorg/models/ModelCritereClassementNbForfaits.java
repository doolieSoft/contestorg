package org.contestorg.models;

import java.util.ArrayList;

import org.contestorg.common.ContestOrgErrorException;
import org.contestorg.infos.InfosModelCritereClassementNbForfaits;

/**
 * Critère de classement prenant en compte le nombre de forfaits
 */
public class ModelCritereClassementNbForfaits extends ModelCritereClassementAbstract
{
	/**
	 * Constructeur
	 * @param concours concours
	 * @param infos informations du critère de classement
	 */
	public ModelCritereClassementNbForfaits(ModelConcours concours, InfosModelCritereClassementNbForfaits infos) {
		// Appeller le constructeur parent
		super(concours);
		
		// Enregistrer les informations
		this.setInfos(infos);
	}
	
	// Getters
	
	/**
	 * @see ModelCritereClassementAbstract#getInfos()
	 */
	@Override
	public InfosModelCritereClassementNbForfaits getInfos () {
		InfosModelCritereClassementNbForfaits infos = new InfosModelCritereClassementNbForfaits(this.isInverse);
		infos.setId(this.getId());
		return infos;
	}
	
	// Setters
	
	/**
	 * Définir les informations du critère de classement
	 * @param infos informations du critère de classement
	 */
	protected void setInfos (InfosModelCritereClassementNbForfaits infos) {
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
			
			// Fire delete
			this.fireDelete();
		}
	}
	
}
