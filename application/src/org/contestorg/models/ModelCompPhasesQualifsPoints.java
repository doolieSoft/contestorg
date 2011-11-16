package org.contestorg.models;


import java.util.ArrayList;

import org.contestorg.infos.InfosModelCompPhasesQualifsPoints;

public class ModelCompPhasesQualifsPoints extends ModelCompPhasesQualifsAbstract
{
	
	// Constructeurs
	public ModelCompPhasesQualifsPoints(ModelConcours concours, InfosModelCompPhasesQualifsPoints infos) {
		// Appeller le constructeur parent
		super(concours);
		
		// Enregistrer les informations
		this.setInfos(infos);
	}
	
	// Setter
	protected void setInfos (InfosModelCompPhasesQualifsPoints infos) {
		// Appeller le setInfos du parent
		super.setInfos(infos);
		
		// Fire update
		this.fireUpdate();
	}
	
	// Implémentation de toInformation
	@Override
	public InfosModelCompPhasesQualifsPoints toInformation () {
		InfosModelCompPhasesQualifsPoints infos = new InfosModelCompPhasesQualifsPoints();
		infos.setId(this.getId());
		return infos;
	}
	
	// Implémentation de remove
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
