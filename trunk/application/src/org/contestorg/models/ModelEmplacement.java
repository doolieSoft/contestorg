package org.contestorg.models;

import java.util.ArrayList;
import java.util.List;

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
	
	/** Liste des matchs */
	private List<ModelMatchAbstract> matchs = new ArrayList<ModelMatchAbstract>();
	
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
	
	/**
	 * Récupérer la liste des matchs
	 * @return liste des matchs
	 */
	public List<ModelMatchAbstract> getMatchs() {
		return this.matchs;
	}
	
	/**
	 * @see ModelAbstract#getInfos()
	 */
	public InfosModelEmplacement getInfos () {
		InfosModelEmplacement infos = new InfosModelEmplacement(this.nom, this.description);
		infos.setId(this.getId());
		return infos;
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
	
	// Adders
	
	/**
	 * Ajouter un match
	 * @param match match
	 * @throws ContestOrgErrorException
	 */
	public void addMatch (ModelMatchAbstract match) throws ContestOrgErrorException {
		if (!this.matchs.contains(match)) {
			// Ajouter le match
			this.matchs.add(match);
			
			// Fire add
			this.fireAdd(match, this.matchs.size() - 1);
		} else {
			throw new ContestOrgErrorException("Le match existe déjà dans l'emplacement");
		}
	}
	
	// Removers
	
	/**
	 * Supprimer un match
	 * @param match match
	 * @throws ContestOrgErrorException
	 */
	protected void removeMatch (ModelMatchAbstract match) throws ContestOrgErrorException {
		// Retirer le match
		int index;
		if ((index = this.matchs.indexOf(match)) != -1) {
			// Remove
			this.matchs.remove(match);
			
			// Fire remove
			this.fireRemove(match, index);
		} else {
			throw new ContestOrgErrorException("Le match n'existe pas dans l'emplacement");
		}
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
			
			// Retirer l'emplacement des matchs
			for (ModelMatchAbstract match : this.matchs) {
				if (!removers.contains(match)) {
					match.setEmplacement(null);
				}
			}
			this.matchs.clear();
			this.fireClear(ModelParticipant.class);
			
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
		public ModelEmplacement update (ModelEmplacement emplacement, InfosModelEmplacement infos) {
			emplacement.setInfos(infos);
			return null;
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
