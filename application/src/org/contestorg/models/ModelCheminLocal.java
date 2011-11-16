package org.contestorg.models;


import java.util.ArrayList;

import org.contestorg.infos.InfosModelCheminLocal;

public class ModelCheminLocal extends ModelCheminAbstract
{
	
	// Attributs scalaires
	private String chemin;
	
	// Constructeur
	public ModelCheminLocal(InfosModelCheminLocal infos) {
		// Enregistrer les informations
		this.setInfos(infos);
	}
	protected ModelCheminLocal(ModelCheminLocal chemin) {
		// Appeller le constructeur principal
		this(chemin.toInformation());
		
		// Récupérer l'id
		this.setId(chemin.getId());
	}
	
	// Getters
	public String getChemin () {
		return this.chemin;
	}
	
	// Setters
	protected void setInfos (InfosModelCheminLocal infos) {
		// Appeller le setInfos du parent
		super.setInfos(infos);
		
		// Enregistrer les informations
		this.chemin = infos.getChemin();
		
		// Fire update
		this.fireUpdate();
	}
	
	// Clone
	protected ModelCheminLocal clone () {
		return new ModelCheminLocal(this);
	}
	
	// ToInformation
	public InfosModelCheminLocal toInformation () {
		InfosModelCheminLocal infos = new InfosModelCheminLocal(this.chemin);
		infos.setId(this.getId());
		return infos;
	}
	
	// Remove
	protected void delete (ArrayList<ModelAbstract> removers) throws ContestOrgModelException {
		if (!removers.contains(this)) {
			// Appeller le remove du parent
			super.delete(removers);
			
			// Fire delete
			this.fireDelete();
		}
	}
	
}
