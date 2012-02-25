package org.contestorg.models;

import java.util.ArrayList;

import org.contestorg.infos.InfosModelCompPhasesQualifsPoints;

/**
 * Critère de classement des phases qualificatives prenant en compte le nombre de points
 */
public class ModelCompPhasesQualifsPoints extends ModelCompPhasesQualifsAbstract
{
	
	/**
	 * Constructeur
	 * @param concours concours
	 * @param infos informations du critère de classement
	 */
	public ModelCompPhasesQualifsPoints(ModelConcours concours, InfosModelCompPhasesQualifsPoints infos) {
		// Appeller le constructeur parent
		super(concours);
		
		// Enregistrer les informations
		this.setInfos(infos);
	}
	
	// Setters
	
	/**
	 * Définir les informations de critère de classement
	 * @param infos informations de critère de classement
	 */
	protected void setInfos (InfosModelCompPhasesQualifsPoints infos) {
		// Appeller le setInfos du parent
		super.setInfos(infos);
		
		// Fire update
		this.fireUpdate();
	}
	
	/**
	 * @see ModelCompPhasesQualifsAbstract#getInfos()
	 */
	@Override
	public InfosModelCompPhasesQualifsPoints getInfos () {
		InfosModelCompPhasesQualifsPoints infos = new InfosModelCompPhasesQualifsPoints();
		infos.setId(this.getId());
		return infos;
	}
	
	/**
	 * @see ModelAbstract#delete(ArrayList)
	 */
	@Override
	protected void delete (ArrayList<ModelAbstract> removers) throws ContestOrgModelException {
		if (!removers.contains(this)) {
			// Appeller le remove parent
			super.delete(removers);
			
			// Fire delete
			this.fireDelete();
		}
	}
	
}
