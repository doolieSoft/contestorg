package org.contestorg.models;

import java.util.ArrayList;

import org.contestorg.common.TrackableList;
import org.contestorg.infos.InfosModelHoraire;
import org.contestorg.interfaces.ITrackableListValidator;
import org.contestorg.interfaces.IUpdater;

/**
 * Horaire d'ouverture d'un lieu
 */
public class ModelHoraire extends ModelAbstract
{
	
	// Attributs objets
	
	/** Lieu */
	private ModelLieu lieu;
	
	// Attributs scalaires
	
	/** Jours concernés */
	private int jours;
	
	/** Début (en jours) */
	private int debut;
	
	/** Fin (en jours) */
	private int fin;
	
	// Constructeurs
	
	/**
	 * Constructeur
	 * @param lieu lieu
	 * @param infos informations de l'horaire
	 */
	public ModelHoraire(ModelLieu lieu, InfosModelHoraire infos) {
		// Retenir le lieu
		this.lieu = lieu;
		
		// Enregistrer les informations
		this.setInfos(infos);
	}
	
	/**
	 * Constructeur par copie
	 * @param lieu lieu
	 * @param horaire horaire
	 */
	protected ModelHoraire(ModelLieu lieu, ModelHoraire horaire) {
		// Appeller le constructeur principal
		this(lieu, horaire.getInfos());
		
		// Récupérer l'id
		this.setId(horaire.getId());
	}
	
	// Getters
	
	/**
	 * Récupérer les jours concernés
	 * @return jours concernés
	 */
	public int getJours () {
		return this.jours;
	}
	
	/**
	 * Récupérer le début (en minutes)
	 * @return début (en minutes)
	 */
	public int getDebut () {
		return this.debut;
	}
	
	/**
	 * Récupérer la fin (en minutes)
	 * @return fin (en minutes)
	 */
	public int getFin () {
		return this.fin;
	}
	
	/**
	 * Récupérer le lieu
	 * @return lieu
	 */
	public ModelLieu getLieu () {
		return this.lieu;
	}
	
	// Setters
	
	/**
	 * Définir les informations de l'horaire
	 * @param infos informations de l'horaire
	 */
	protected void setInfos (InfosModelHoraire infos) {
		// Appeller le setInfos du parent
		super.setInfos(infos);
		
		// Enregistrer les informations
		this.jours = infos.getJours();
		this.debut = infos.getDebut();
		this.fin = infos.getFin();
		
		// Fire update
		this.fireUpdate();
	}
	
	/**
	 * Cloner l'horaire
	 * @param lieu lieu
	 * @return clone de l'horaire
	 */
	protected ModelHoraire clone (ModelLieu lieu) {
		return new ModelHoraire(lieu, this);
	}
	
	/**
	 * @see ModelAbstract#getInfos()
	 */
	public InfosModelHoraire getInfos () {
		InfosModelHoraire infos = new InfosModelHoraire(this.jours, this.debut, this.fin);
		infos.setId(this.getId());
		return infos;
	}
	
	/**
	 * @see ModelAbstract#delete(ArrayList)
	 */
	protected void delete (ArrayList<ModelAbstract> removers) throws ContestOrgModelException {
		if (!removers.contains(this)) {
			// Ajouter l'horaire aux removers
			removers.add(this);
			
			// Retirer l'horaire du lieu
			if (this.lieu != null) {
				if (!removers.contains(this.lieu)) {
					this.lieu.removeHoraire(this);
				}
				this.lieu = null;
				this.fireClear(ModelLieu.class);
			}
			
			// Fire delete
			this.fireDelete();
		}
	}
	
	/**
	 * Classe pour mettre à jour la liste des horaires d'un lieu
	 */
	protected static class UpdaterForLieu implements IUpdater<InfosModelHoraire, ModelHoraire>
	{
		/** Lieu */
		private ModelLieu lieu;
		
		/**
		 * Constructeur
		 * @param lieu lieu
		 */
		protected UpdaterForLieu(ModelLieu lieu) {
			this.lieu = lieu;
		}
		
		/**
		 * @see IUpdater#create(Object)
		 */
		@Override
		public ModelHoraire create (InfosModelHoraire infos) {
			return new ModelHoraire(this.lieu, infos);
		}
		
		/**
		 * @see IUpdater#update(Object, Object)
		 */
		@Override
		public void update (ModelHoraire horaire, InfosModelHoraire infos) {
			horaire.setInfos(infos);
		}
	}
	
	/**
	 * Classe pour valider les opérations sur la liste des horaires d'un lieu
	 */
	protected static class ValidatorForLieu implements ITrackableListValidator<InfosModelHoraire>
	{
		// Implémentation de ITrackableListValidator
		@Override
		public String validateAdd (InfosModelHoraire infos, TrackableList<InfosModelHoraire> list) {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).chevauche(infos)) {
					return "L'horaire chevauche un horaire existant.";
				}
			}
			return null;
		}
		@Override
		public String validateUpdate (int row, InfosModelHoraire infos, TrackableList<InfosModelHoraire> list) {
			for (int i = 0; i < list.size(); i++) {
				if (i != row && list.get(i).chevauche(infos)) {
					return "L'horaire chevauche un horaire existant.";
				}
			}
			return null;
		}
		@Override
		public String validateDelete (int row, TrackableList<InfosModelHoraire> list) {
			return null;
		}
		@Override
		public String validateMove (int row, int movement, TrackableList<InfosModelHoraire> list) {
			return null;
		}
	}
	
}
