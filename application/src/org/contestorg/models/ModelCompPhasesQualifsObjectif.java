package org.contestorg.models;


import java.util.ArrayList;

import org.contestorg.infos.InfosModelCompPhasesQualifsObjectif;

public class ModelCompPhasesQualifsObjectif extends ModelCompPhasesQualifsAbstract
{
	
	// Attributs objets
	private ModelObjectif objectif;
	
	// Constructeurs
	public ModelCompPhasesQualifsObjectif(ModelConcours concours, ModelObjectif objectif, InfosModelCompPhasesQualifsObjectif infos) {
		// Appeller le constructeur parent
		super(concours);
		
		// Retenir l'objectif
		this.objectif = objectif;
		
		// Enregistrer les informations
		this.setInfos(infos);
	}
	
	// Getters
	public ModelObjectif getObjectif () {
		return this.objectif;
	}
	
	// Setter
	protected void setInfos (InfosModelCompPhasesQualifsObjectif infos) {
		// Appeller le setInfos du parent
		super.setInfos(infos);
		
		// Fire update
		this.fireUpdate();
	}
	
	// Implémentation de toInformation
	@Override
	public InfosModelCompPhasesQualifsObjectif toInfos () {
		InfosModelCompPhasesQualifsObjectif infos = new InfosModelCompPhasesQualifsObjectif(this.objectif.toInfos());
		infos.setId(this.getId());
		return infos;
	}
	
	// Implémentation de remove
	@Override
	protected void delete (ArrayList<ModelAbstract> removers) throws ContestOrgModelException {
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
