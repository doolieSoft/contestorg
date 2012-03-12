package org.contestorg.models;

import java.util.ArrayList;

import org.contestorg.common.ContestOrgErrorException;
import org.contestorg.common.Pair;
import org.contestorg.common.TrackableList;
import org.contestorg.infos.InfosModelCategorie;
import org.contestorg.infos.InfosModelMatchPhasesElims;
import org.contestorg.infos.InfosModelPhasesEliminatoires;
import org.contestorg.infos.InfosModelPoule;
import org.contestorg.interfaces.ITrackableListValidator;
import org.contestorg.interfaces.IUpdater;
import org.contestorg.log.Log;

/**
 * Conteneur de poules
 */
public class ModelCategorie extends ModelAbstract
{
	
	// Attributs objets
	
	/** Concours */
	private ModelConcours concours;
	
	/** Phases éliminatoires */
	private ModelPhasesEliminatoires phasesEliminatoires;
	
	/** Liste des poules */
	private ArrayList<ModelPoule> poules = new ArrayList<ModelPoule>();
	
	// Attributs scalaires
	
	/** Nom */
	private String nom;
	
	// Constructeurs
	
	/**
	 * Constructeur
	 * @param concours concours
	 * @param infos informations de la catégorie
	 */
	public ModelCategorie(ModelConcours concours, InfosModelCategorie infos) {
		// Retenir le concours
		this.concours = concours;
		
		// Enregistrer les informations
		this.setInfos(infos);
	}
	
	/**
	 * Constructeur par copie
	 * @param concours concours
	 * @param categorie catégorie
	 */
	protected ModelCategorie(ModelConcours concours, ModelCategorie categorie) {
		// Appeller le constructeur principal
		this(concours, categorie.getInfos());
		
		// Récupérer l'id
		this.setId(categorie.getId());
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
	 * Récupérer le concours
	 * @return concours
	 */
	public ModelConcours getConcours () {
		return this.concours;
	}
	
	/**
	 * Récupérer la liste des poules
	 * @return liste des poules
	 */
	public ArrayList<ModelPoule> getPoules () {
		return new ArrayList<ModelPoule>(this.poules);
	}
	
	/**
	 * Récupérer une poule d'après son nom
	 * @param nom nom de la poule
	 * @return poule trouvée
	 */
	public ModelPoule getPouleByNom (String nom) {
		for (ModelPoule poule : this.poules) {
			if (poule.getNom().equals(nom)) {
				return poule;
			}
		}
		return null;
	}
	
	/**
	 * Récupérer la liste des participants
	 * @return liste des participants
	 */
	public ArrayList<ModelParticipant> getParticipants () {
		// Initialiser la liste des participants
		ArrayList<ModelParticipant> participants = new ArrayList<ModelParticipant>();
		
		// Ajouter les participants
		for (ModelPoule poule : this.poules) {
			participants.addAll(poule.getParticipants());
		}
		
		// Retourner la liste des participants
		return participants;
	}
	
	/**
	 * Récupérer le nombre de participants
	 * @return nombre de participants
	 */
	public int getNbParticipants() {
		int nbParticipants = 0;
		for(ModelPoule poule : this.poules) {
			nbParticipants += poule.getNbParticipants();
		}
		return nbParticipants;
	}
	
	/**
	 * Récupérer un participant d'après son nom
	 * @param nom nom du participant
	 * @return participant trouvé
	 */
	public ModelParticipant getParticipantByNom (String nom) {
		for (ModelParticipant participant : this.getParticipants()) {
			if (participant.getNom().equals(nom)) {
				return participant;
			}
		}
		return null;
	}
	
	/**
	 * Récupérer la liste des matchs des phases qualificatives
	 * @return liste des matchs des phases qualificatives
	 */
	public ArrayList<ModelMatchPhasesQualifs> getMatchsPhasesQualifs () {
		ArrayList<ModelMatchPhasesQualifs> matchs = new ArrayList<ModelMatchPhasesQualifs>();
		for (ModelPoule poule : this.poules) {
			matchs.addAll(poule.getMatchsPhasesQualifs());
		}
		return matchs;
	}
	
	/**
	 * Récupérer les phases éliminatoires
	 * @return phases éliminatoires
	 */
	public ModelPhasesEliminatoires getPhasesEliminatoires () {
		return this.phasesEliminatoires;
	}
	
	/**
	 * Récupérer la liste des participants qui peuvent participer
	 * @return liste des participants qui peuvent participer
	 */
	public ArrayList<ModelParticipant> getParticipantsParticipants () {
		// Créer et retourner la liste des participants qui peuvent participer
		ArrayList<ModelParticipant> participantes = new ArrayList<ModelParticipant>();
		for (ModelPoule poule : this.poules) {
			participantes.addAll(poule.getParticipantsParticipants());
		}
		return participantes;
	}
	
	/**
	 * Récupérer le numéro
	 * @return numéro
	 */
	public int getNumero() {
		return this.concours.getCategories().indexOf(this);
	}
	
	// Setters
	
	/**
	 * Définir les informations de la catégorie
	 * @param infos informations de la catégorie
	 */
	protected void setInfos (InfosModelCategorie infos) {
		// Appeller le setInfos du parent
		super.setInfos(infos);
		
		// Enregistrer les informations
		this.nom = infos.getNom();
		
		// Fire update
		this.fireUpdate();
	}
	
	/**
	 * Définir les phases éliminatoires
	 * @param phasesEliminatoires phases éliminatoires
	 */
	public void setPhasesEliminatoires (ModelPhasesEliminatoires phasesEliminatoires) {
		// Enregistrer la phase éliminatoire
		this.phasesEliminatoires = phasesEliminatoires;
		
		// Fire update
		this.fireUpdate();
	}
	
	// Adders
	
	/**
	 * Ajouter un poule
	 * @param poule poule à ajouter
	 * @throws ContestOrgErrorException
	 */
	public void addPoule (ModelPoule poule) throws ContestOrgErrorException {
		if (!this.poules.contains(poule)) {
			// Ajouter la poule
			this.poules.add(poule);
			
			// Fire add
			this.fireAdd(poule, this.poules.size() - 1);
		} else {
			throw new ContestOrgErrorException("La poule existe déjà dans la catégorie");
		}
	}
	
	// Removers
	
	/**
	 * Retirer une poule
	 * @param poule poule à retirer
	 * @throws ContestOrgErrorException
	 */
	protected void removePoule (ModelPoule poule) throws ContestOrgErrorException {
		// Retirer la poule
		int index;
		if ((index = this.poules.indexOf(poule)) != -1) {
			// Remove
			this.poules.remove(poule);
			
			// Fire remove
			this.fireRemove(poule, index);
		} else {
			throw new ContestOrgErrorException("La poule n'existe pas dans la catégorie");
		}
	}
	
	// Updaters
	
	/**
	 * Mettre à jour la liste des poules
	 * @param source liste des poules source
	 * @throws ContestOrgErrorException
	 */
	protected void updatePoules(TrackableList<Pair<InfosModelPoule, ArrayList<String>>> source) throws ContestOrgErrorException {
		// Mettre à jour la liste des poules
		this.updates(new ModelPoule.UpdaterForCategorie(this), this.poules, source, true, null);
	}
	
	// Actions
	
	/**
	 * Génerer les phases éliminatoires
	 * @param nbPhases nombre de phases éliminatoires
	 * @param infosMatchs informations des matchs
	 * @param infosPhasesEliminatoires informations des phases éliminatoires
	 * @throws ContestOrgErrorException
	 */
	protected void genererPhasesEliminatoires (int nbPhases, InfosModelMatchPhasesElims infosMatchs, InfosModelPhasesEliminatoires infosPhasesEliminatoires) throws ContestOrgErrorException {
		// Supprimer les phases éliminatoires actuelles si nécéssaire
		if (this.phasesEliminatoires != null) {
			// Supprimer les phases éliminatoires
			this.phasesEliminatoires.delete(this);
			
			// Perdre la référence des phases éliminatoires
			this.phasesEliminatoires = null;
		}
		
		// Générer les phases éliminatoires
		this.phasesEliminatoires = ModelPhasesEliminatoires.genererPhasesEliminatoires(this, nbPhases, infosMatchs, infosPhasesEliminatoires);
		
		// Fire update
		this.fireUpdate();
	}
	
	/**
	 * Cloner la catégorie
	 * @param concours concours associé
	 * @return clone de la catégorie
	 */
	protected ModelCategorie clone (ModelConcours concours) {
		return new ModelCategorie(concours, this);
	}
	
	/**
	 * @see ModelAbstract#getInfos()
	 */
	public InfosModelCategorie getInfos () {
		InfosModelCategorie infos = new InfosModelCategorie(this.nom);
		infos.setId(this.getId());
		return infos;
	}
	
	/**
	 * @see ModelAbstract#delete(ArrayList)
	 */
	protected void delete (ArrayList<ModelAbstract> removers) throws ContestOrgErrorException {
		if (!removers.contains(this)) {
			// Ajouter la catégorie a la liste des removers
			removers.add(this);
			
			// Retirer la catégorie du concours
			if (this.concours != null) {
				if (!removers.contains(this.concours)) {
					this.concours.removeCategorie(this);
				}
				this.concours = null;
				this.fireClear(ModelConcours.class);
			}
			
			// Supprimer les phases éliminatoires
			if (this.phasesEliminatoires != null) {
				if (!removers.contains(this.phasesEliminatoires)) {
					this.phasesEliminatoires.delete(removers);
				}
				this.phasesEliminatoires = null;
				this.fireClear(ModelPhasesEliminatoires.class);
			}
			
			// Supprimer les poules
			for (ModelPoule poule : this.poules) {
				if (!removers.contains(poule))
					poule.delete(removers);
			}
			this.poules.clear();
			this.fireClear(ModelPoule.class);
			
			// Fire delete
			this.fireDelete();
		}
	}
	
	/**
	 * Classe pour mettre à jour la liste des catégories d'un concours
	 */
	protected static class UpdaterForConcours implements IUpdater<InfosModelCategorie, ModelCategorie>
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
		public ModelCategorie create (InfosModelCategorie infos) {
			try {
				// Créer, configurer et retourner la catégorie
				ModelCategorie categorie = new ModelCategorie(this.concours, infos);
				categorie.addPoule(new ModelPoule(categorie, new InfosModelPoule("Défaut")));
				return categorie;
			} catch (ContestOrgErrorException e) {
				Log.getLogger().fatal("Erreur lors de la création d'une catégorie.",e);
				return null;
			}
		}
		
		/**
		 * @see IUpdater#update(Object, Object)
		 */
		@Override
		public void update (ModelCategorie categorie, InfosModelCategorie infos) {
			categorie.setInfos(infos);
		}
	}
	
	/**
	 * Classe pour valider les opérations sur la liste des catégories d'un concours
	 */
	protected static class ValidatorForConcours implements ITrackableListValidator<InfosModelCategorie>
	{
		// Implémentation de ITrackableListValidator
		@Override
		public String validateAdd (InfosModelCategorie infos, TrackableList<InfosModelCategorie> list) {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getNom().equals(infos.getNom())) {
					return "Une catégorie existante possède déjà le même nom.";
				}
			}
			return null;
		}
		@Override
		public String validateUpdate (int row, InfosModelCategorie infos, TrackableList<InfosModelCategorie> list) {
			if(row == 0) {
				return "La catégorie \""+list.get(row).getNom()+"\" ne peut pas être modifiée.";
			}
			if(infos.getNom().isEmpty()) {
				return "Le nom d'une catégorie ne peut pas être vide.";
			}
			for (int i = 0; i < list.size(); i++) {
				if (i != row && list.get(i).getNom().equals(infos.getNom())) {
					return "Une catégorie existante possède déjà le même nom.";
				}
			}
			return null;
		}
		@Override
		public String validateDelete (int row, TrackableList<InfosModelCategorie> list) {
			return row == 0 ? "La catégorie \""+list.get(row).getNom()+"\" ne peut pas être supprimée." : null;
		}
		@Override
		public String validateMove (int row, int movement, TrackableList<InfosModelCategorie> list) {
			return row == 0 ? "La catégorie \""+list.get(row).getNom()+"\" ne peut pas être déplacée." : null;
		}
	}
	
}
