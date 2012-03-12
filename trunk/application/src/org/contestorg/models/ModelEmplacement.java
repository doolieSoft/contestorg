package org.contestorg.models;

import java.util.ArrayList;

import org.contestorg.common.ContestOrgErrorException;
import org.contestorg.common.TrackableList;
import org.contestorg.infos.InfosModelEmplacement;
import org.contestorg.interfaces.ITrackableListValidator;
import org.contestorg.interfaces.IUpdater;

/**
 * Emplacement
 */
public class ModelEmplacement extends ModelAbstract
{
	
	// Attributs objets
	
	/** Lieu */
	private ModelLieu lieu;
	
	// Attributs scalaires
	
	/** Nom */
	private String nom;
	
	/** Description */
	private String description;
	
	// Constructeurs
	
	/**
	 * Constructeur
	 * @param lieu lieu
	 * @param infos informations de l'emplacement
	 */
	public ModelEmplacement(ModelLieu lieu, InfosModelEmplacement infos) {
		// Retenir le lieu
		this.lieu = lieu;
		
		// Enregistrer les informations
		this.setInfos(infos);
	}
	
	/**
	 * Constructeur par copie
	 * @param lieu lieu
	 * @param emplacement emplacement
	 */
	protected ModelEmplacement(ModelLieu lieu, ModelEmplacement emplacement) {
		// Appeller le constructeur principal
		this(lieu, emplacement.getInfos());
		
		// Récupérer l'id
		this.setId(emplacement.getId());
	}
	
	// Getters
	
	/**
	 * Récupérer le nom
	 * @return nom
	 */
	public String getNom () {
		return nom;
	}
	
	/**
	 * Récupérer la description
	 * @return description
	 */
	public String getDescription () {
		return description;
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
	 * Définir les informations de l'emplacement
	 * @param infos informations de l'emplacement
	 */
	protected void setInfos (InfosModelEmplacement infos) {
		// Appeller le setInfos du parent
		super.setInfos(infos);
		
		// Enregistrer les informations
		this.nom = infos.getNom();
		this.description = infos.getDescription();
		
		// Fire update
		this.fireUpdate();
	}
	
	/**
	 * Cloner l'emplacement
	 * @param lieu lieu
	 * @return clone de l'emplacement
	 */
	protected ModelEmplacement clone (ModelLieu lieu) {
		return new ModelEmplacement(lieu, this);
	}
	
	/**
	 * @see ModelAbstract#getInfos()
	 */
	public InfosModelEmplacement getInfos () {
		InfosModelEmplacement infos = new InfosModelEmplacement(this.nom, this.description);
		infos.setId(this.getId());
		return infos;
	}
	
	/**
	 * @see ModelAbstract#delete(ArrayList)
	 */
	protected void delete (ArrayList<ModelAbstract> removers) throws ContestOrgErrorException {
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
	
	/**
	 * Classe pour mettre à jour la liste des emplacements d'un lieu
	 */
	protected static class UpdaterForLieu implements IUpdater<InfosModelEmplacement, ModelEmplacement>
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
		public ModelEmplacement create (InfosModelEmplacement infos) {
			return new ModelEmplacement(this.lieu, infos);
		}
		
		/**
		 * @see IUpdater#update(Object, Object)
		 */
		@Override
		public void update (ModelEmplacement emplacement, InfosModelEmplacement infos) {
			emplacement.setInfos(infos);
		}
		
	}
	
	/**
	 * Classe pour valider les opérations sur la liste des emplacements d'un lieu
	 */
	protected static class ValidatorForLieu implements ITrackableListValidator<InfosModelEmplacement>
	{
		// Implémentation de ITrackableListValidator
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
