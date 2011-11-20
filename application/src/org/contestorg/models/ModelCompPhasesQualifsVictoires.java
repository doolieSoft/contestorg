package org.contestorg.models;


import java.util.ArrayList;

import org.contestorg.infos.InfosModelCompPhasesQualifsVictoires;

public class ModelCompPhasesQualifsVictoires extends ModelCompPhasesQualifsAbstract
{
	// Constructeurs
	public ModelCompPhasesQualifsVictoires(ModelConcours concours, InfosModelCompPhasesQualifsVictoires infos) {
		// Appeller le constructeur parent
		super(concours);
		
		// Enregistrer les informations
		this.setInfos(infos);
	}
	
	// Setter
	protected void setInfos (InfosModelCompPhasesQualifsVictoires infos) {
		// Appeller le setInfos du parent
		super.setInfos(infos);
		
		// Fire update
		this.fireUpdate();
	}
	
	// Implémentation de toInformation
	@Override
	public InfosModelCompPhasesQualifsVictoires toInfos () {
		InfosModelCompPhasesQualifsVictoires infos = new InfosModelCompPhasesQualifsVictoires();
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
