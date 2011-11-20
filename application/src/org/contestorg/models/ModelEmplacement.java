package org.contestorg.models;


import java.util.ArrayList;

import org.contestorg.common.TrackableList;
import org.contestorg.infos.InfosModelEmplacement;
import org.contestorg.interfaces.IListValidator;
import org.contestorg.interfaces.IUpdater;


/**
 * Emplacement où peut se dérouler un match
 */
public class ModelEmplacement extends ModelAbstract
{
	
	// Attributs objets
	private ModelLieu lieu;
	
	// Attributs scalaires
	private String nom;
	private String description;
	
	// Constructeur
	public ModelEmplacement(ModelLieu lieu, InfosModelEmplacement infos) {
		// Retenir le lieu
		this.lieu = lieu;
		
		// Enregistrer les informations
		this.setInfos(infos);
	}
	protected ModelEmplacement(ModelLieu lieu, ModelEmplacement emplacement) {
		// Appeller le constructeur principal
		this(lieu, emplacement.toInfos());
		
		// Récupérer l'id
		this.setId(emplacement.getId());
	}
	
	// Getters
	public String getNom () {
		return nom;
	}
	public String getDescription () {
		return description;
	}
	public ModelLieu getLieu () {
		return this.lieu;
	}
	
	// Setters
	protected void setInfos (InfosModelEmplacement infos) {
		// Appeller le setInfos du parent
		super.setInfos(infos);
		
		// Enregistrer les informations
		this.nom = infos.getNom();
		this.description = infos.getDescription();
		
		// Fire update
		this.fireUpdate();
	}
	
	// Clone
	protected ModelEmplacement clone (ModelLieu lieu) {
		return new ModelEmplacement(lieu, this);
	}
	
	// ToInformation
	public InfosModelEmplacement toInfos () {
		InfosModelEmplacement infos = new InfosModelEmplacement(this.nom, this.description);
		infos.setId(this.getId());
		return infos;
	}
	
	// Remove
	protected void delete (ArrayList<ModelAbstract> removers) throws ContestOrgModelException {
		if (!removers.contains(this)) {
			// Ajouter l'emplacement à la liste des removers
			removers.add(this);
			
			// Retirer l'emplacement du lieu
			if (this.lieu != null) {
				if (!removers.contains(this.lieu)) {
					this.lieu.removeEmplacement(this);
				}
				this.lieu = null;
				this.fireClear(ModelLieu.class);
			}
			
			// Fire delete
			this.fireDelete();
		}
	}
	
	// Classe pour mettre à jour la liste des emplacements d'un lieu
	protected static class UpdaterForLieu implements IUpdater<InfosModelEmplacement, ModelEmplacement>
	{
		// Lieu
		private ModelLieu lieu;
		
		// Constructeur
		protected UpdaterForLieu(ModelLieu lieu) {
			this.lieu = lieu;
		}
		
		// Implémentation de create
		@Override
		public ModelEmplacement create (InfosModelEmplacement infos) {
			return new ModelEmplacement(this.lieu, infos);
		}
		
		// Implémentation de update
		@Override
		public void update (ModelEmplacement emplacement, InfosModelEmplacement infos) {
			emplacement.setInfos(infos);
		}
		
	}
	
	// Classe pour valider les opérations sur la liste des emplacements d'un lieu
	protected static class ValidatorForLieu implements IListValidator<InfosModelEmplacement>
	{
		// Implémentation de IListValidator
		@Override
		public String validateAdd (InfosModelEmplacement infos, TrackableList<InfosModelEmplacement> list) {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getNom().equals(infos.getNom())) {
					return "Un emplacement existant possède déjà le même nom.";
				}
			}
			return null;
		}
		@Override
		public String validateUpdate (int row, InfosModelEmplacement infos, TrackableList<InfosModelEmplacement> list) {
			for (int i = 0; i < list.size(); i++) {
				if (i != row && list.get(i).getNom().equals(infos.getNom())) {
					return "Un emplacement existant possède déjà le même nom.";
				}
			}
			return null;
		}
		@Override
		public String validateDelete (int row, TrackableList<InfosModelEmplacement> list) {
			return null;
		}
		@Override
		public String validateMove (int row, int movement, TrackableList<InfosModelEmplacement> list) {
			return null;
		}
	}
	
}
