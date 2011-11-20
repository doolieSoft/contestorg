package org.contestorg.models;


import java.util.ArrayList;

import org.contestorg.infos.InfosModelChemin;
import org.contestorg.infos.InfosModelCheminFTP;
import org.contestorg.infos.InfosModelCheminLocal;

public abstract class ModelCheminAbstract extends ModelAbstract
{
	
	// Constructeur statique
	protected static ModelCheminAbstract create (InfosModelChemin infos) {
		// Créer er retourner le chemin correspondant aux informations
		if (infos instanceof InfosModelCheminLocal) {
			return new ModelCheminLocal((InfosModelCheminLocal)infos);
		} else if (infos instanceof InfosModelCheminFTP) {
			return new ModelCheminFTP((InfosModelCheminFTP)infos);
		}
		
		// Retourner null si les informations ne correspondent pas à un type de chemin connu
		return null;
	}
	
	// Setter
	protected void setInfos (InfosModelChemin infos) {
		// Appeller le setInfos du parent
		super.setInfos(infos);
		
		// Fire update
		this.fireUpdate();
	}
	
	// Clone
	protected abstract ModelCheminAbstract clone ();
	
	// "Implémentation" de toInformation
	public abstract InfosModelChemin toInfos ();
	
	// Remove
	protected void delete (ArrayList<ModelAbstract> removers) throws ContestOrgModelException {
		if (!removers.contains(this)) {
			// Ajouter le chemin à la liste des removers
			removers.add(this);
		}
	}
	
}
