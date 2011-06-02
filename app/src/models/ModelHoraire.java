package models;

import infos.InfosModelHoraire;
import interfaces.IListValidator;
import interfaces.IUpdater;

import java.util.ArrayList;

import common.TrackableList;

/**
 * Horaire d'ouverture d'un lieu
 */
public class ModelHoraire extends ModelAbstract
{
	
	// Attributs objets
	private ModelLieu lieu;
	
	// Attributs scalaires
	private int jours;
	private int debut; // En minutes
	private int fin; // En minutes
	
	// Constructeur
	public ModelHoraire(ModelLieu lieu, InfosModelHoraire infos) {
		// Retenir le lieu
		this.lieu = lieu;
		
		// Enregistrer les informations
		this.setInfos(infos);
	}
	protected ModelHoraire(ModelLieu lieu, ModelHoraire horaire) {
		// Appeller le constructeur principal
		this(lieu, horaire.toInformation());
		
		// Récupérer l'id
		this.setId(horaire.getId());
	}
	
	// Getters
	public int getJours () {
		return this.jours;
	}
	public int getDebut () {
		return this.debut;
	}
	public int getFin () {
		return this.fin;
	}
	public ModelLieu getLieu () {
		return this.lieu;
	}
	
	// Setters
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
	
	// Clone
	protected ModelHoraire clone (ModelLieu lieu) {
		return new ModelHoraire(lieu, this);
	}
	
	// ToInformation
	public InfosModelHoraire toInformation () {
		InfosModelHoraire infos = new InfosModelHoraire(this.jours, this.debut, this.fin);
		infos.setId(this.getId());
		return infos;
	}
	
	// Remove
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
	
	// Classe pour mettre à jour la liste des horaires d'un lieu
	protected static class UpdaterForLieu implements IUpdater<InfosModelHoraire, ModelHoraire>
	{
		// Lieu
		private ModelLieu lieu;
		
		// Constructeur
		protected UpdaterForLieu(ModelLieu lieu) {
			this.lieu = lieu;
		}
		
		// Implémentation de create
		@Override
		public ModelHoraire create (InfosModelHoraire infos) {
			return new ModelHoraire(this.lieu, infos);
		}
		
		// Implémentation de update
		@Override
		public void update (ModelHoraire horaire, InfosModelHoraire infos) {
			horaire.setInfos(infos);
		}
	}
	
	// Classe pour valider les opérations sur la liste des horaires d'un lieu
	protected static class ValidatorForLieu implements IListValidator<InfosModelHoraire>
	{
		// Implémentation de IListValidator
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
