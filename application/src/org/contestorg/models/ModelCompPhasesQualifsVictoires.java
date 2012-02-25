package org.contestorg.models;

import java.util.ArrayList;

import org.contestorg.infos.InfosModelCompPhasesQualifsVictoires;

/**
 * Critère de classement des phases qualificatives prenant en compte le nombre de victoires
 */
public class ModelCompPhasesQualifsVictoires extends ModelCompPhasesQualifsAbstract
{
	/**
	 * Constructeur
	 * @param concours concours
	 * @param infos informations du critère de classement
	 */
	public ModelCompPhasesQualifsVictoires(ModelConcours concours, InfosModelCompPhasesQualifsVictoires infos) {
		// Appeller le constructeur parent
		super(concours);
		
		// Enregistrer les informations
		this.setInfos(infos);
	}
	
	// Setters
	
	/**
	 * Définir les informations du critère de classement
	 * @param infos informations du critère de classement
	 */
	protected void setInfos (InfosModelCompPhasesQualifsVictoires infos) {
		// Appeller le setInfos du parent
		super.setInfos(infos);
		
		// Fire update
		this.fireUpdate();
	}
	
	/**
	 * @see ModelCompPhasesQualifsAbstract#getInfos()
	 */
	@Override
	public InfosModelCompPhasesQualifsVictoires getInfos () {
		InfosModelCompPhasesQualifsVictoires infos = new InfosModelCompPhasesQualifsVictoires();
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
