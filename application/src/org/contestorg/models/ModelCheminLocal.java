package org.contestorg.models;

import java.util.ArrayList;

import org.contestorg.common.ContestOrgErrorException;
import org.contestorg.infos.InfosModelCheminLocal;

/**
 * Chemin d'exportation local
 */
public class ModelCheminLocal extends ModelCheminAbstract
{
	
	// Attributs scalaires
	
	/** Chemin */
	private String chemin;
	
	// Constructeurs
	
	/**
	 * Constructeur
	 * @param infos informations du chemin d'exportation local
	 */
	public ModelCheminLocal(InfosModelCheminLocal infos) {
		// Enregistrer les informations
		this.setInfos(infos);
	}
	
	/**
	 * Constructeur par copie
	 * @param chemin chemin d'exportation local
	 */
	protected ModelCheminLocal(ModelCheminLocal chemin) {
		// Appeller le constructeur principal
		this(chemin.getInfos());
		
		// Récupérer l'id
		this.setId(chemin.getId());
	}
	
	// Getters
	
	/**
	 * Récupérer le chemin
	 * @return chemin
	 */
	public String getChemin () {
		return this.chemin;
	}
	
	/**
	 * @see ModelCheminAbstract#getInfos()
	 */
	public InfosModelCheminLocal getInfos () {
		InfosModelCheminLocal infos = new InfosModelCheminLocal(this.chemin);
		infos.setId(this.getId());
		return infos;
	}
	
	// Setters
	
	/**
	 * Définir les informations du chemin d'exportation local
	 * @param infos informations du chemin d'exportation local
	 */
	protected void setInfos (InfosModelCheminLocal infos) {
		// Appeller le setInfos du parent
		super.setInfos(infos);
		
		// Enregistrer les informations
		this.chemin = infos.getChemin();
		
		// Fire update
		this.fireUpdate();
	}
	
	/**
	 * Cloner le chemin d'exportation local
	 * @return clone du chemin d'exportation local
	 */
	protected ModelCheminLocal clone () {
		return new ModelCheminLocal(this);
	}
	
	/**
	 * @see ModelAbstract#delete()
	 */
	protected void delete (ArrayList<ModelAbstract> removers) throws ContestOrgErrorException {
		if (!removers.contains(this)) {
			// Appeller le remove du parent
			super.delete(removers);
			
			// Fire delete
			this.fireDelete();
		}
	}
	
}
