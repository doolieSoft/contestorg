package org.contestorg.models;

import java.util.ArrayList;

import org.contestorg.common.ContestOrgErrorException;
import org.contestorg.infos.InfosModelCritereClassementNbEgalites;

/**
 * Critère de classement prenant en compte le nombre d'égalites
 */
public class ModelCritereClassementNbEgalites extends ModelCritereClassementAbstract
{
	/**
	 * Constructeur
	 * @param concours concours
	 * @param infos informations du critère de classement
	 */
	public ModelCritereClassementNbEgalites(ModelConcours concours, InfosModelCritereClassementNbEgalites infos) {
		// Appeller le constructeur parent
		super(concours);
		
		// Enregistrer les informations
		this.setInfos(infos);
	}
	
	/**
	 * @see ModelCritereClassementAbstract#getInfos()
	 */
	@Override
	public InfosModelCritereClassementNbEgalites getInfos () {
		InfosModelCritereClassementNbEgalites infos = new InfosModelCritereClassementNbEgalites(this.isInverse);
		infos.setId(this.getId());
		return infos;
	}
	
	// Setters
	
	/**
	 * Définir les informations du critère de classement
	 * @param infos informations du critère de classement
	 */
	protected void setInfos (InfosModelCritereClassementNbEgalites infos) {
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
