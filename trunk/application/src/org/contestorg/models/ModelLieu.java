package org.contestorg.models;

import java.util.ArrayList;

import org.contestorg.common.TrackableList;
import org.contestorg.common.Triple;
import org.contestorg.infos.InfosModelEmplacement;
import org.contestorg.infos.InfosModelHoraire;
import org.contestorg.infos.InfosModelLieu;
import org.contestorg.interfaces.ITrackableListValidator;
import org.contestorg.interfaces.IUpdater;
import org.contestorg.log.Log;

/**
 * Conteneur d'emplacements
 */
public class ModelLieu extends ModelAbstract
{
	
	// Attributs objets
	
	/** Concours */
	private ModelConcours concours;
	
	/** Liste des emplacements */
	private ArrayList<ModelEmplacement> emplacements = new ArrayList<ModelEmplacement>();
	
	/** Liste des horaires */
	private ArrayList<ModelHoraire> horaires = new ArrayList<ModelHoraire>();
	
	// Attributs
	
	/** Nom */
	private String nom;
	
	/** Lieu */
	private String lieu;
	
	/** Téléphone */
	private String telephone;
	
	/** Email */
	private String email;
	
	/** Description */
	private String description;
	
	// Constructeurs
	
	/**
	 * Constructeur
	 * @param concours concours
	 * @param infos informations du lieu
	 */
	public ModelLieu(ModelConcours concours, InfosModelLieu infos) {
		// Retenir le concours
		this.concours = concours;
		
		// Enregistrer les informations
		this.setInfos(infos);
	}
	
	/**
	 * Constructeur par copie
	 * @param concours concours
	 * @param lieu lieu
	 */
	protected ModelLieu(ModelConcours concours, ModelLieu lieu) {
		// Appeller le constructeur principal
		this(concours, lieu.getInfos());
		
		// Récupérer l'id
		this.setId(lieu.getId());
	}
	
	// Getters
	
	/**
	 * Récupérer le nom
	 * @return nom
	 */
	public String getNom () {
		return this.nom;
	}
	
	/**
	 * Récupérer le lieu
	 * @return lieu
	 */
	public String getLieu () {
		return this.lieu;
	}
	
	/**
	 * Récupérer le téléphone
	 * @return téléphone
	 */
	public String getTelephone () {
		return this.telephone;
	}
	
	/**
	 * Récupérer l'email
	 * @return email
	 */
	public String getEmail () {
		return this.email;
	}
	
	/**
	 * Récupérer la description
	 * @return description
	 */
	public String getDescription () {
		return this.description;
	}
	
	/**
	 * Récupérer la liste des horaires
	 * @return liste des horaires
	 */
	public ArrayList<ModelHoraire> getHoraires () {
		return new ArrayList<ModelHoraire>(this.horaires);
	}
	
	/**
	 * Récupérer la liste des emplacements
	 * @return liste des emplacements
	 */
	public ArrayList<ModelEmplacement> getEmplacements () {
		return new ArrayList<ModelEmplacement>(this.emplacements);
	}
	
	// Setters
	
	/**
	 * Définir les informations du lieu
	 * @param infos informations du lieu
	 */
	protected void setInfos (InfosModelLieu infos) {
		// Appeller le setInfos du parent
		super.setInfos(infos);
		
		// Enregistrer les informations
		this.nom = infos.getNom();
		this.lieu = infos.getLieu();
		this.telephone = infos.getTelephone();
		this.email = infos.getEmail();
		this.description = infos.getDescription();
		
		// Fire update
		this.fireUpdate();
	}
	
	// Adders
	
	/**
	 * Ajouter un emplacmeent
	 * @param emplacement emplacement
	 * @throws ContestOrgModelException
	 */
	public void addEmplacement (ModelEmplacement emplacement) throws ContestOrgModelException {
		if (!this.emplacements.contains(emplacement)) {
			// Ajouter l'emplacement
			this.emplacements.add(emplacement);
			
			// Fire add
			this.fireAdd(emplacement, this.emplacements.size() - 1);
		} else {
			throw new ContestOrgModelException("L'emplacement existe déjà dans le lieu");
		}
	}
	
	/**
	 * Ajouter un horaire
	 * @param horaire horaire
	 * @throws ContestOrgModelException
	 */
	public void addHoraire (ModelHoraire horaire) throws ContestOrgModelException {
		if (!this.horaires.contains(horaire)) {
			// Ajouter l'horaire
			this.horaires.add(horaire);
			
			// Fire add
			this.fireAdd(horaire, this.horaires.size() - 1);
		} else {
			throw new ContestOrgModelException("L'horaire existe déjà dans le lieu");
		}
	}
	
	// Removers
	
	/**
	 * Supprimer un emplacement
	 * @param emplacement emplacement
	 * @throws ContestOrgModelException
	 */
	protected void removeEmplacement (ModelEmplacement emplacement) throws ContestOrgModelException {
		// Retirer l'emplacement
		int index;
		if ((index = this.emplacements.indexOf(emplacement)) != -1) {
			// Remove
			this.emplacements.remove(emplacement);
			
			// Fire remove
			this.fireRemove(emplacement, index);
		} else {
			throw new ContestOrgModelException("L'emplacement n'existe pas dans le lieu");
		}
	}
	
	/**
	 * Supprimer un horaire
	 * @param horaire horaire
	 * @throws ContestOrgModelException
	 */
	protected void removeHoraire (ModelHoraire horaire) throws ContestOrgModelException {
		// Retirer l'horaire
		int index;
		if ((index = this.horaires.indexOf(horaire)) != -1) {
			// Remove
			this.horaires.remove(horaire);
			
			// Fire remove
			this.fireRemove(horaire, index);
		} else {
			throw new ContestOrgModelException("L'horaire n'existe pas dans le lieu");
		}
	}
	
	// Updaters
	
	/**
	 * Mettre à jour la liste des emplacements
	 * @param list liste d'emplacement source
	 * @throws ContestOrgModelException
	 */
	protected void updateEmplacements (TrackableList<InfosModelEmplacement> list) throws ContestOrgModelException {
		// Mettre à jour la liste des emplacements
		this.updates(new ModelEmplacement.UpdaterForLieu(this), this.emplacements, list, true, null);
	}
	
	/**
	 * Mettre à jour la liste des horaires
	 * @param list liste des horaires source
	 * @throws ContestOrgModelException
	 */
	protected void updateHoraires (TrackableList<InfosModelHoraire> list) throws ContestOrgModelException {
		// Mettre à jour la liste des emplacements
		this.updates(new ModelHoraire.UpdaterForLieu(this), this.horaires, list, true, null);
	}
	
	/**
	 * Cloner le lieu
	 * @param concours concours
	 * @return clone du lieu
	 */
	protected ModelLieu clone (ModelConcours concours) {
		return new ModelLieu(concours, this);
	}
	
	/**
	 * @see ModelAbstract#getInfos()
	 */
	public InfosModelLieu getInfos () {
		InfosModelLieu infos = new InfosModelLieu(this.nom, this.lieu, this.telephone, this.email, this.description);
		infos.setId(this.getId());
		return infos;
	}
	
	/**
	 * @see ModelAbstract#delete(ArrayList)
	 */
	protected void delete (ArrayList<ModelAbstract> removers) throws ContestOrgModelException {
		if (!removers.contains(this)) {
			// Ajouter le lieu aux removers
			removers.add(this);
			
			// Retirer le lieu du concours
			if (this.concours != null) {
				if (!removers.contains(this.concours)) {
					this.concours.removeLieu(this);
				}
				this.concours = null;
				this.fireClear(ModelConcours.class);
			}
			
			// Supprimer les emplacements du lieu
			for (ModelEmplacement emplacement : this.emplacements) {
				if (!removers.contains(emplacement)) {
					emplacement.delete(this);
				}
			}
			this.emplacements.clear();
			this.fireClear(ModelEmplacement.class);
			
			// Supprimer les horaires du lieu
			for (ModelHoraire horaire : this.horaires) {
				if (!removers.contains(horaire)) {
					horaire.delete(this);
				}
			}
			this.horaires.clear();
			this.fireClear(ModelHoraire.class);
			
			// Fire delete
			this.fireDelete();
		}
	}
	
	/**
	 * Classe pour mettre à jour la liste des lieux d'un concours ainsi que les lieux et emplacements rattachés
	 */
	protected static class UpdaterForConcours implements IUpdater<Triple<InfosModelLieu, TrackableList<InfosModelEmplacement>, TrackableList<InfosModelHoraire>>, ModelLieu>
	{
		/** Concours */
		private ModelConcours concours;
		
		/**
		 * Constructeur
		 * @param concours concours
		 */
		protected UpdaterForConcours(ModelConcours concours) {
			this.concours = concours;
		}
		
		/**
		 * @see IUpdater#create(Object)
		 */
		@Override
		public ModelLieu create (Triple<InfosModelLieu, TrackableList<InfosModelEmplacement>, TrackableList<InfosModelHoraire>> infos) {
			try {
				// Créer, configurer et retourner le lieu
				ModelLieu lieu = new ModelLieu(this.concours, infos.getFirst());
				lieu.updateEmplacements(infos.getSecond());
				lieu.updateHoraires(infos.getThird());
				return lieu;
			} catch (ContestOrgModelException e) {
				Log.getLogger().fatal("Erreur lors de la création d'un lieu.",e);
				return null;
			}
		}
		
		/**
		 * @see IUpdater#update(Object, Object)
		 */
		@Override
		public void update (ModelLieu lieu, Triple<InfosModelLieu, TrackableList<InfosModelEmplacement>, TrackableList<InfosModelHoraire>> infos) {
			try {
				// Configurer le lieu
				lieu.setInfos(infos.getFirst());
				lieu.updateEmplacements(infos.getSecond());
				lieu.updateHoraires(infos.getThird());
			} catch (ContestOrgModelException e) {
				Log.getLogger().fatal("Erreur lors de la modification d'un lieu.",e);
			}
		}
	}
	
	/**
	 * Classe pour valider les opérations sur la liste des lieux d'un concours
	 */
	protected static class ValidatorForConcours implements ITrackableListValidator<Triple<InfosModelLieu, TrackableList<InfosModelEmplacement>, TrackableList<InfosModelHoraire>>>
	{
		// Implémentation de ITrackableListValidator
		@Override
		public String validateAdd (Triple<InfosModelLieu, TrackableList<InfosModelEmplacement>, TrackableList<InfosModelHoraire>> infos, TrackableList<Triple<InfosModelLieu, TrackableList<InfosModelEmplacement>, TrackableList<InfosModelHoraire>>> list) {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getFirst().getNom().equals(infos.getFirst().getNom())) {
					return "Un lieu existant possède déjà le même nom.";
				}
			}
			return null;
		}
		@Override
		public String validateUpdate (int row, Triple<InfosModelLieu, TrackableList<InfosModelEmplacement>, TrackableList<InfosModelHoraire>> infos, TrackableList<Triple<InfosModelLieu, TrackableList<InfosModelEmplacement>, TrackableList<InfosModelHoraire>>> list) {
			for (int i = 0; i < list.size(); i++) {
				if (i != row && list.get(i).getFirst().getNom().equals(infos.getFirst().getNom())) {
					return "Un lieu existant possède déjà le même nom.";
				}
			}
			return null;
		}
		@Override
		public String validateDelete (int row, TrackableList<Triple<InfosModelLieu, TrackableList<InfosModelEmplacement>, TrackableList<InfosModelHoraire>>> list) {
			return null;
		}
		@Override
		public String validateMove (int row, int movement, TrackableList<Triple<InfosModelLieu, TrackableList<InfosModelEmplacement>, TrackableList<InfosModelHoraire>>> list) {
			return null;
		}
	}
	
}
