package org.contestorg.models;


import java.util.ArrayList;

import org.contestorg.common.Pair;
import org.contestorg.infos.InfosModelProprieteParticipant;
import org.contestorg.interfaces.IUpdater;
import org.contestorg.log.Log;



public class ModelProprietePossedee extends ModelAbstract
{
	
	// Attributs objets
	private ModelPropriete propriete;
	private ModelParticipant participant;
	
	// Attributs scalaires
	private String value;
	
	// Constructeur
	public ModelProprietePossedee(ModelPropriete propriete, ModelParticipant participant, InfosModelProprieteParticipant infos) {
		// Retenir la propriété et le participant
		this.propriete = propriete;
		this.participant = participant;
		
		// Enregistrer les informations
		this.setInfos(infos);
	}
	protected ModelProprietePossedee(ModelPropriete propriete, ModelParticipant participant, ModelProprietePossedee proprieteParticipant) {
		// Appeller le constructeur principal
		this(propriete, participant, proprieteParticipant.toInfos());
		
		// Récupérer l'id
		this.setId(proprieteParticipant.getId());
	}
	
	// Getters
	public String getValue () {
		return this.value;
	}
	public ModelPropriete getPropriete () {
		return this.propriete;
	}
	public ModelParticipant getParticipant () {
		return this.participant;
	}
	
	// Setters
	protected void setInfos (InfosModelProprieteParticipant infos) {
		// Appeller le setInfos du parent
		super.setInfos(infos);
		
		// Enregistrer les informations
		this.value = infos.getValue();
		
		// Fire update
		this.fireUpdate();
	}
	
	// Clone
	protected ModelProprietePossedee clone (ModelPropriete propriete, ModelParticipant participant) {
		return new ModelProprietePossedee(propriete, participant, this);
	}
	
	// ToInformation
	public InfosModelProprieteParticipant toInfos () {
		InfosModelProprieteParticipant infos = new InfosModelProprieteParticipant(this.value);
		infos.setId(this.getId());
		return infos;
	}
	
	// Remove
	protected void delete (ArrayList<ModelAbstract> removers) throws ContestOrgModelException {
		if (!removers.contains(this)) {
			// Ajouter la propriété de participant à la liste des removers
			removers.add(this);
			
			// Retirer la propriété de participant de la propriété
			if (this.propriete != null) {
				if (!removers.contains(this.propriete)) {
					this.propriete.removeProprieteParticipant(this);
				}
				this.propriete = null;
				this.fireClear(ModelPropriete.class);
			}
			
			// Retirer la propriété de participant du participant
			if (this.participant != null) {
				if (!removers.contains(this.participant)) {
					this.participant.removeProprieteParticipant(this);
				}
				this.participant = null;
				this.fireClear(ModelParticipant.class);
			}
			
			// Fire delete
			this.fireDelete();
		}
	}
	
	// Classe pour mettre à jour la liste des propriétés de participant d'un participant
	protected static class UpdaterForParticipant implements IUpdater<Pair<String, InfosModelProprieteParticipant>, ModelProprietePossedee>
	{
		
		// Participant
		private ModelParticipant participant;
		
		// Constructeur
		public UpdaterForParticipant(ModelParticipant participant) {
			// Retenir le participant
			this.participant = participant;
		}
		
		// Implémentation de create
		@Override
		public ModelProprietePossedee create (Pair<String, InfosModelProprieteParticipant> infos) {
			try {
				// Récupérer la proprieté
				ModelPropriete propriete = this.participant.getPoule().getCategorie().getConcours().getProprieteByNom(infos.getFirst());
				
				// Créer la propriété de participant
				ModelProprietePossedee proprieteParticipant = new ModelProprietePossedee(propriete, this.participant, infos.getSecond());
				propriete.addProprieteParticipant(proprieteParticipant);
				
				// Retourner la propriété de participant
				return proprieteParticipant;
			} catch (ContestOrgModelException e) {
				Log.getLogger().fatal("Erreur lors de la création d'une propriété possédée.",e);
				return null;
			}
		}
		
		// Implémentation de update
		@Override
		public void update (ModelProprietePossedee proprieteParticipant, Pair<String, InfosModelProprieteParticipant> infos) {
			// Mettre à jour les information de la propriété de participant
			proprieteParticipant.setInfos(infos.getSecond());
		}
	}
	
}
