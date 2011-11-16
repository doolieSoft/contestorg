package org.contestorg.models;


import java.util.ArrayList;

import org.contestorg.common.Pair;
import org.contestorg.infos.InfosModelParticipationObjectif;
import org.contestorg.interfaces.IUpdater;
import org.contestorg.log.Log;



public class ModelObjectifRemporte extends ModelAbstract
{
	
	// Attributs objets
	private ModelParticipation participation;
	private ModelObjectif objectif;
	
	// Attributs scalaires
	private int quantite;
	
	// Constructeur
	public ModelObjectifRemporte(ModelParticipation participation, ModelObjectif objectif, InfosModelParticipationObjectif infos) {
		// Retenir la participation et l'objectif
		this.participation = participation;
		this.objectif = objectif;
		
		// Enregistrer les informations
		this.setInfos(infos);
	}
	protected ModelObjectifRemporte(ModelParticipation participation, ModelObjectif objectif, ModelObjectifRemporte participationObjectif) {
		// Appeller le constructeur parent
		this(participation, objectif, participationObjectif.toInformation());
		
		// Récupérer l'id
		this.setId(participationObjectif.getId());
	}
	
	// Getters
	public int getQuantite () {
		return this.quantite;
	}
	public ModelParticipation getParticipation () {
		return this.participation;
	}
	public ModelObjectif getObjectif () {
		return this.objectif;
	}
	
	// Setters
	protected void setInfos (InfosModelParticipationObjectif infos) {
		// Appeller le setInfos du parent
		super.setInfos(infos);
		
		// Enregistrer les informations
		this.quantite = infos.getQuantite();
		
		// Fire update
		this.fireUpdate();
	}
	protected void setObjectif (ModelObjectif objectif) throws ContestOrgModelException {
		this.objectif.removeObjectifRemporte(this);
		this.objectif = objectif;
		this.objectif.addObjectifRemporte(this);
		this.fireUpdate();
	}
	
	// Clone
	protected ModelObjectifRemporte clone (ModelParticipation participation, ModelObjectif objectif) {
		return new ModelObjectifRemporte(participation, objectif, this);
	}
	
	// ToInformation
	public InfosModelParticipationObjectif toInformation () {
		InfosModelParticipationObjectif infos = new InfosModelParticipationObjectif(this.quantite);
		infos.setId(this.getId());
		return infos;
	}
	
	// Remove
	protected void delete (ArrayList<ModelAbstract> removers) throws ContestOrgModelException {
		if (!removers.contains(this)) {
			// Ajouter l'objectif remporte à la liste des removers
			removers.add(this);
			
			// Retirer l'objectif remporte de l'objectif
			if (this.objectif != null) {
				if (!removers.contains(this.objectif)) {
					this.objectif.removeObjectifRemporte(this);
				}
				this.objectif = null;
				this.fireClear(ModelObjectif.class);
			}
			
			// Retirer l'objectif remporte de la participation
			if (this.participation != null) {
				if (!removers.contains(this.participation)) {
					this.participation.removeObjectifRemporte(this);
				}
				this.participation = null;
				this.fireClear(ModelParticipation.class);
			}
			
			// Fire delete
			this.fireDelete();
		}
	}
	
	// Classe pour mettre à jour la liste des objectif remportés d'une participation
	protected static class Updater implements IUpdater<Pair<String, InfosModelParticipationObjectif>,ModelObjectifRemporte>
	{
		// Concours
		private ModelConcours concours;
		
		// Participation
		private ModelParticipation participation;
		
		// Constructeur
		public Updater(ModelConcours concours,ModelParticipation participation) {
			this.concours = concours;
			this.participation = participation;
		}

		// Implémentation de create
		@Override
		public ModelObjectifRemporte create (Pair<String, InfosModelParticipationObjectif> infos) {
			try {
				// Récupérer l'objectif
				ModelObjectif objectif = this.concours.getObjectifByNom(infos.getFirst());
				
				// Créer et retourner l'objectif remporté
				ModelObjectifRemporte objectifRemporte = new ModelObjectifRemporte(this.participation, objectif, infos.getSecond());
				objectif.addObjectifRemporte(objectifRemporte);
				return objectifRemporte;
			} catch (ContestOrgModelException e) {
				Log.getLogger().fatal("Erreur lors de la création d'un objectif remporté.",e);
				return null;
			}
		}

		// Implémentation de update
		@Override
		public void update (ModelObjectifRemporte objectifRemporte, Pair<String, InfosModelParticipationObjectif> infos) {
			try {
				// Changer l'objectif si nécéssaire
				if(!objectifRemporte.getObjectif().getNom().equals(infos.getFirst())) {
					objectifRemporte.setObjectif(this.concours.getObjectifByNom(infos.getFirst()));
				}
				
				// Modifier les informations
				objectifRemporte.setInfos(infos.getSecond());
			} catch (ContestOrgModelException e) {
				Log.getLogger().fatal("Erreur lors de la modification d'un objectif remporté.",e);
			}
		}
		
	}
	
}
