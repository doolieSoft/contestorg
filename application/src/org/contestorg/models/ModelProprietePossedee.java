package org.contestorg.models;

import java.util.ArrayList;

import org.contestorg.common.Pair;
import org.contestorg.infos.InfosModelProprietePossedee;
import org.contestorg.interfaces.IUpdater;
import org.contestorg.log.Log;

/**
 * Propriété possédée
 */
public class ModelProprietePossedee extends ModelAbstract
{
	
	// Attributs objets
	
	/** Propriété */
	private ModelPropriete propriete;
	
	/** Participant */
	private ModelParticipant participant;
	
	// Attributs scalaires
	
	/** Valeur */
	private String valeur;
	
	// Constructeur
	public ModelProprietePossedee(ModelPropriete propriete, ModelParticipant participant, InfosModelProprietePossedee infos) {
		// Retenir la propriété et le participant
		this.propriete = propriete;
		this.participant = participant;
		
		// Enregistrer les informations
		this.setInfos(infos);
	}
	protected ModelProprietePossedee(ModelPropriete propriete, ModelParticipant participant, ModelProprietePossedee proprieteParticipant) {
		// Appeller le constructeur principal
		this(propriete, participant, proprieteParticipant.getInfos());
		
		// Récupérer l'id
		this.setId(proprieteParticipant.getId());
	}
	
	// Getters
	
	/**
	 * Récupérer la valeur
	 * @return valeur
	 */
	public String getValeur () {
		return this.valeur;
	}
	
	/**
	 * Récupérer la propriété
	 * @return propriété
	 */
	public ModelPropriete getPropriete () {
		return this.propriete;
	}
	
	/**
	 * Récupérer le participant
	 * @return participant
	 */
	public ModelParticipant getParticipant () {
		return this.participant;
	}
	
	// Setters
	
	/**
	 * Définir les informations de la propriété possédée
	 * @param infos informations de la propriété possédée
	 */
	protected void setInfos (InfosModelProprietePossedee infos) {
		// Appeller le setInfos du parent
		super.setInfos(infos);
		
		// Enregistrer les informations
		this.valeur = infos.getValeur();
		
		// Fire update
		this.fireUpdate();
	}
	
	/**
	 * Cloner la propriété possédée
	 * @param propriete propriété
	 * @param participant participant
	 * @return clone de la propriété possédée
	 */
	protected ModelProprietePossedee clone (ModelPropriete propriete, ModelParticipant participant) {
		return new ModelProprietePossedee(propriete, participant, this);
	}
	
	/**
	 * @see ModelAbstract#getInfos()
	 */
	public InfosModelProprietePossedee getInfos () {
		InfosModelProprietePossedee infos = new InfosModelProprietePossedee(this.valeur);
		infos.setId(this.getId());
		return infos;
	}
	
	/**
	 * @see ModelAbstract#delete(ArrayList)
	 */
	protected void delete (ArrayList<ModelAbstract> removers) throws ContestOrgModelException {
		if (!removers.contains(this)) {
			// Ajouter la propriété de participant à la liste des removers
			removers.add(this);
			
			// Retirer la propriété de participant de la propriété
			if (this.propriete != null) {
				if (!removers.contains(this.propriete)) {
					this.propriete.removeProprietePossedee(this);
				}
				this.propriete = null;
				this.fireClear(ModelPropriete.class);
			}
			
			// Retirer la propriété de participant du participant
			if (this.participant != null) {
				if (!removers.contains(this.participant)) {
					this.participant.removeProprietePossedee(this);
				}
				this.participant = null;
				this.fireClear(ModelParticipant.class);
			}
			
			// Fire delete
			this.fireDelete();
		}
	}
	
	/**
	 * Classe pour mettre à jour la liste des propriétés possédées d'un participant
	 */
	protected static class UpdaterForParticipant implements IUpdater<Pair<String, InfosModelProprietePossedee>, ModelProprietePossedee>
	{
		
		/** Participant */
		private ModelParticipant participant;
		
		/**
		 * Constructeur
		 * @param participant participant
		 */
		public UpdaterForParticipant(ModelParticipant participant) {
			// Retenir le participant
			this.participant = participant;
		}
		
		/**
		 * @see IUpdater#create(Object)
		 */
		@Override
		public ModelProprietePossedee create (Pair<String, InfosModelProprietePossedee> infos) {
			try {
				// Récupérer la proprieté
				ModelPropriete propriete = this.participant.getPoule().getCategorie().getConcours().getProprieteByNom(infos.getFirst());
				
				// Créer la propriété de participant
				ModelProprietePossedee proprieteParticipant = new ModelProprietePossedee(propriete, this.participant, infos.getSecond());
				propriete.addProprietePossedee(proprieteParticipant);
				
				// Retourner la propriété de participant
				return proprieteParticipant;
			} catch (ContestOrgModelException e) {
				Log.getLogger().fatal("Erreur lors de la création d'une propriété possédée.",e);
				return null;
			}
		}
		
		/**
		 * @see IUpdater#update(Object, Object)
		 */
		@Override
		public void update (ModelProprietePossedee proprieteParticipant, Pair<String, InfosModelProprietePossedee> infos) {
			// Mettre à jour les information de la propriété de participant
			proprieteParticipant.setInfos(infos.getSecond());
		}
	}
	
}
