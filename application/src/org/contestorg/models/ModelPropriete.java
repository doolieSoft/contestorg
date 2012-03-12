package org.contestorg.models;

import java.util.ArrayList;

import org.contestorg.common.ContestOrgErrorException;
import org.contestorg.common.TrackableList;
import org.contestorg.infos.InfosModelPropriete;
import org.contestorg.interfaces.ITrackableListValidator;
import org.contestorg.interfaces.IUpdater;

/**
 * Propriété
 */
public class ModelPropriete extends ModelAbstract
{
	
	// Attributs objets
	
	/** Concours */
	private ModelConcours concours;
	
	/** Liste des propriétés possédées */
	private ArrayList<ModelProprietePossedee> proprietesPossedees = new ArrayList<ModelProprietePossedee>();
	
	// Attributs scalaires
	
	/** Nom */
	private String nom;
	
	/** Type */
	private int type;
	
	/** Propriété obligatoire ? */
	private boolean obligatoire;
	
	// Constructeurs
	
	/**
	 * Constructeur
	 * @param concours concours
	 * @param infos informations de la propriété
	 */
	public ModelPropriete(ModelConcours concours, InfosModelPropriete infos) {
		// Retenir le concours
		this.concours = concours;
		
		// Enregistrer les informations
		this.setInfos(infos);
	}
	
	/**
	 * Constructeur par copie
	 * @param concours concours
	 * @param propriete propriété
	 */
	protected ModelPropriete(ModelConcours concours, ModelPropriete propriete) {
		// Appeller le constructeur principal
		this(concours, propriete.getInfos());
		
		// Récupérer l'id
		this.setId(propriete.getId());
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
	 * Récupérer le type
	 * @return type
	 */
	public int getType () {
		return this.type;
	}
	
	/**
	 * Vérifier s'il s'agit d'une propriété obligatoire
	 * @return propriété obligatoire ?
	 */
	public boolean isObligatoire () {
		return this.obligatoire;
	}
	
	/**
	 * Récupérer la liste des propriétés possédées
	 * @return liste des propriétés possédées
	 */
	public ArrayList<ModelProprietePossedee> getProprietesPossedees () {
		return new ArrayList<ModelProprietePossedee>(this.proprietesPossedees);
	}
	
	// Setters
	
	/**
	 * Définir les informations de la propriété
	 * @param infos informations de la propriété
	 */
	protected void setInfos (InfosModelPropriete infos) {
		// Appeller le setInfos du parent
		super.setInfos(infos);
		
		// Enregistrer les informations
		this.nom = infos.getNom();
		this.type = infos.getType();
		this.obligatoire = infos.isObligatoire();
		
		// Fire update
		this.fireUpdate();
	}
	
	// Adders
	
	/**
	 * Ajouter une propriété possédée
	 * @param proprietePossedee propriété possedée
	 * @throws ContestOrgErrorException
	 */
	public void addProprietePossedee (ModelProprietePossedee proprietePossedee) throws ContestOrgErrorException {
		if (!this.proprietesPossedees.contains(proprietePossedee)) {
			// Ajouter la propriété possédée
			this.proprietesPossedees.add(proprietePossedee);
			
			// Fire add
			this.fireAdd(proprietePossedee, this.proprietesPossedees.size() - 1);
		} else {
			throw new ContestOrgErrorException("La propriété possédée existe déjà dans la propriété");
		}
	}
	
	// Removers
	
	/**
	 * Supprimer une propriété possédée
	 * @param proprietePossedee propriété possédée
	 * @throws ContestOrgErrorException
	 */
	protected void removeProprietePossedee (ModelProprietePossedee proprietePossedee) throws ContestOrgErrorException {
		// Retirer la propriété possédée
		if (this.proprietesPossedees.remove(proprietePossedee)) {
			// Fire remove
			this.fireRemove(proprietePossedee, this.proprietesPossedees.size() - 1);
		} else {
			throw new ContestOrgErrorException("La propriété possédée n'existe pas dans la propriété");
		}
	}
	
	/**
	 * Cloner la propriété
	 * @param concours concours
	 * @return clone de la propriété
	 */
	protected ModelPropriete clone (ModelConcours concours) {
		return new ModelPropriete(concours, this);
	}
	
	/**
	 * @see ModelAbstract#getInfos()
	 */
	public InfosModelPropriete getInfos () {
		InfosModelPropriete infos = new InfosModelPropriete(this.nom, this.type, this.obligatoire);
		infos.setId(this.getId());
		return infos;
	}
	
	/**
	 * @see ModelAbstract#delete(ArrayList)
	 */
	@Override
	protected void delete (ArrayList<ModelAbstract> removers) throws ContestOrgErrorException {
		if (!removers.contains(this)) {
			// Ajouter la propriété à la liste des removers
			removers.add(this);
			
			// Retirer l'objectif du concours
			if (this.concours != null) {
				if (!removers.contains(this.concours)) {
					this.concours.removePropriete(this);
				}
				this.concours = null;
				this.fireClear(ModelConcours.class);
			}
			
			// Supprimer les propriétés de participant
			for (ModelProprietePossedee propriete : this.proprietesPossedees) {
				if (!removers.contains(propriete)) {
					propriete.delete(removers);
				}
			}
			this.proprietesPossedees.clear();
			this.fireClear(ModelProprietePossedee.class);
			
			// Fire delete
			this.fireDelete();
		}
	}
	
	/**
	 * Classe pour mettre à jour la liste des propriétés d'un concours
	 */
	protected static class UpdaterForConcours implements IUpdater<InfosModelPropriete, ModelPropriete>
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
		public ModelPropriete create (InfosModelPropriete infos) {
			return new ModelPropriete(this.concours, infos);
		}
		
		/**
		 * @see IUpdater#update(Object, Object)
		 */
		@Override
		public void update (ModelPropriete propriete, InfosModelPropriete infos) {
			propriete.setInfos(infos);
		}
	}
	
	/**
	 * Classe pour valider les opérations sur la liste des propriétés d'un concours
	 */
	protected static class ValidatorForConcours implements ITrackableListValidator<InfosModelPropriete>
	{
		// Implémentation de ITrackableListValidator
		@Override
		public String validateAdd (InfosModelPropriete infos, TrackableList<InfosModelPropriete> list) {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getNom().equals(infos.getNom())) {
					return "Une propriété existante possède déjà le même nom.";
				}
			}
			return null;
		}
		@Override
		public String validateUpdate (int row, InfosModelPropriete infos, TrackableList<InfosModelPropriete> list) {
			for (int i = 0; i < list.size(); i++) {
				if (i != row && list.get(i).getNom().equals(infos.getNom())) {
					return "Une propriété existante possède déjà le même nom.";
				}
			}
			return null;
		}
		@Override
		public String validateMove (int row, int movement, TrackableList<InfosModelPropriete> list) {
			return null;
		}
		@Override
		public String validateDelete (int row, TrackableList<InfosModelPropriete> list) {
			return null;
		}
	}
}
