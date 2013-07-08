package org.contestorg.models;

import java.util.ArrayList;

import org.contestorg.common.ContestOrgErrorException;
import org.contestorg.infos.InfosModelCritereClassementGoalAverage;

/**
 * Critère de classement prenant en compte le goal-average
 */
public  class ModelCritereClassementGoalAverage extends ModelCritereClassementAbstract
{
	// Attributs scalaires
	
	/** Type */
	private int type;
	
	/** Méhode utilisée */
	private int methode;
	
	/** Donnée utilisée */
	private int donnee;
	
	// Attributs objets
	
	/** Objectif */
	private ModelObjectif objectif;
	
	/**
	 * Constructeur
	 * @param concours concours
	 * @param objectif objectif
	 * @param infos informations du critère de classement
	 */
	public ModelCritereClassementGoalAverage(ModelConcours concours, ModelObjectif objectif, InfosModelCritereClassementGoalAverage infos) {
		// Appeller le constructeur parent
		super(concours);
		
		// Retenir l'objectif
		this.objectif = objectif;
		
		// Enregistrer les informations
		this.setInfos(infos);
	}
	
	// Getters
	
	/**
	 * Récupérer le type
	 * @return type
	 */
	public int getType() {
		return this.type;
	}
	
	/**
	 * Récupérer la méthode utilisée
	 * @return méthode utilisée
	 */
	public int getMethode() {
		return this.methode;
	}
	
	/**
	 * Récupérer la donnée utilisée
	 * @return donnée utilisée
	 */
	public int getDonnee() {
		return this.donnee;
	}
	
	/**
	 * Récupérer l'objectif
	 * @return objectif
	 */
	public ModelObjectif getObjectif () {
		return this.objectif;
	}
	
	/**
	 * @see ModelCritereClassementAbstract#getInfos()
	 */
	@Override
	public InfosModelCritereClassementGoalAverage getInfos () {
		InfosModelCritereClassementGoalAverage infos = new InfosModelCritereClassementGoalAverage(this.isInverse,this.type,this.methode,this.donnee,this.donnee == InfosModelCritereClassementGoalAverage.DONNEE_QUANTITE_OBJECTIF ? this.objectif.getInfos() : null);
		infos.setId(this.getId());
		return infos;
	}
	
	// Setters
	
	/**
	 * Définir les informations du critère de classement
	 * @param infos informations du critère de classement
	 */
	protected void setInfos (InfosModelCritereClassementGoalAverage infos) {
		// Appeller le setInfos du parent
		super.setInfos(infos);

		// Enregistrer les informations
		this.type = infos.getType();
		this.methode = infos.getMethode();
		this.donnee = infos.getDonnee();
		
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
			
			// Retirer le critère de classement de l'objectif
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
