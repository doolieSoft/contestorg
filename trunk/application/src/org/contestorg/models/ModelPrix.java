package org.contestorg.models;


import java.util.ArrayList;

import org.contestorg.common.ContestOrgErrorException;
import org.contestorg.common.TrackableList;
import org.contestorg.infos.InfosModelPrix;
import org.contestorg.interfaces.ITrackableListValidator;
import org.contestorg.interfaces.IUpdater;


/**
 * Prix
 */
public class ModelPrix extends ModelAbstract
{
	
	// Attributs objets
	
	/** Concours */
	private ModelConcours concours;
	
	/** Participants */
	private ArrayList<ModelParticipant> participants = new ArrayList<ModelParticipant>();
	
	// Attributs scalaires
	
	/** Nom */
	private String nom;
	
	// Constructeurs
	
	/**
	 * Constructeur
	 * @param concours concours
	 * @param infos informations du prix
	 */
	public ModelPrix(ModelConcours concours, InfosModelPrix infos) {
		// Retenir le concours
		this.concours = concours;
		
		// Enregistrer les informations
		this.setInfos(infos);
	}
	
	/**
	 * Constructeur par copie
	 * @param concours concours
	 * @param prix prix
	 */
	protected ModelPrix(ModelConcours concours, ModelPrix prix) {
		// Appeller le constructeur principal
		this(concours, prix.getInfos());
		
		// Récupérer l'id
		this.setId(prix.getId());
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
	 * Récupérer la liste des participants
	 * @return liste des participants
	 */
	public ArrayList<ModelParticipant> getParticipants () {
		return new ArrayList<ModelParticipant>(this.participants);
	}
	
	// Setters
	
	/**
	 * Définir les informations du prix
	 * @param infos informations du prix
	 */
	protected void setInfos (InfosModelPrix infos) {
		// Appeller le setInfos du parent
		super.setInfos(infos);
		
		// Enregistrer les informations
		this.nom = infos.getNom();
		
		// Fire update
		this.fireUpdate();
	}
	
	// Adders
	
	/**
	 * Ajouter un participant
	 * @param participant participant
	 * @throws ContestOrgErrorException
	 */
	public void addParticipant (ModelParticipant participant) throws ContestOrgErrorException {
		if (!this.participants.contains(participant)) {
			// Enregistrer le participant
			this.participants.add(participant);
			
			// Fire add
			this.fireAdd(participant, this.participants.size() - 1);
		} else {
			throw new ContestOrgErrorException("Le participant existe déjà dans le prix");
		}
	}
	
	// Removers
	
	/**
	 * Supprimer un participant
	 * @param participant participant
	 * @throws ContestOrgErrorException
	 */
	protected void removeParticipant (ModelParticipant participant) throws ContestOrgErrorException {
		// Supprimer le participant
		int index;
		if ((index = this.participants.indexOf(participant)) != -1) {
			// Remove
			this.participants.remove(participant);
			
			// Fire remove
			this.fireRemove(participant, index);
		} else {
			throw new ContestOrgErrorException("Le participant n'existe pas dans le prix");
		}
	}
	
	/**
	 * Cloner le prix
	 * @param concours concours
	 * @return clone du prix
	 */
	protected ModelPrix clone (ModelConcours concours) {
		return new ModelPrix(concours, this);
	}
	
	/**
	 * @see ModelAbstract#getInfos()
	 */
	public InfosModelPrix getInfos () {
		InfosModelPrix infos = new InfosModelPrix(this.nom);
		infos.setId(this.getId());
		return infos;
	}
	
	/**
	 * @see ModelAbstract#delete(ArrayList)
	 */
	@Override
	protected void delete (ArrayList<ModelAbstract> removers) throws ContestOrgErrorException {
		if (!removers.contains(this)) {
			// Ajouter la poule à la liste des removers
			removers.add(this);
			
			// Retirer le prix du concours
			if (this.concours != null) {
				if (!removers.contains(this.concours)) {
					this.concours.removePrix(this);
				}
				this.concours = null;
				this.fireClear(ModelConcours.class);
			}
			
			// Retirer le prix des participants
			for (ModelParticipant participant : this.participants) {
				if (!removers.contains(participant)) {
					participant.removePrix(this);
				}
			}
			this.participants.clear();
			this.fireClear(ModelParticipant.class);
			
			// Fire delete
			this.fireDelete();
		}
	}
	
	/**
	 * Classe pour mettre à jour la liste des prix d'un concours
	 * @author Cyril
	 *
	 */
	protected static class UpdaterForConcours implements IUpdater<InfosModelPrix, ModelPrix>
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
		public ModelPrix create (InfosModelPrix infos) {
			return new ModelPrix(this.concours, infos);
		}
		
		/**
		 * @see IUpdater#update(Object, Object)
		 */
		@Override
		public void update (ModelPrix prix, InfosModelPrix infos) {
			prix.setInfos(infos);
		}
		
	}
	
	/**
	 * Classe pour valider les opérations sur la liste des prix d'un concours
	 */
	protected static class ValidatorForConcours implements ITrackableListValidator<InfosModelPrix>
	{
		// Implémentation de ITrackableListValidator
		@Override
		public String validateAdd (InfosModelPrix infos, TrackableList<InfosModelPrix> list) {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getNom().equals(infos.getNom())) {
					return "Un prix existant possède déjà le même nom.";
				}
			}
			return null;
		}
		@Override
		public String validateUpdate (int row, InfosModelPrix infos, TrackableList<InfosModelPrix> list) {
			for (int i = 0; i < list.size(); i++) {
				if (i != row && list.get(i).getNom().equals(infos.getNom())) {
					return "Un prix existant possède déjà le même nom.";
				}
			}
			return null;
		}
		@Override
		public String validateDelete (int row, TrackableList<InfosModelPrix> list) {
			return null;
		}
		@Override
		public String validateMove (int row, int movement, TrackableList<InfosModelPrix> list) {
			return null;
		}
	}
	
}
