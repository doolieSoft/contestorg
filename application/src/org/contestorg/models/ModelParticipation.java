package org.contestorg.models;


import java.util.ArrayList;

import org.contestorg.common.Pair;
import org.contestorg.common.TrackableList;
import org.contestorg.infos.InfosModelParticipation;
import org.contestorg.infos.InfosModelParticipationObjectif;


/**
 * Lien entre un participant et un match
 */
public class ModelParticipation extends ModelAbstract
{
	
	// Attributs objets
	private ModelParticipant participant;
	private ModelMatchAbstract match;
	private ArrayList<ModelObjectifRemporte> objectifsRemportes = new ArrayList<ModelObjectifRemporte>();
	
	// Attributs scalaires
	private int resultat;
	
	// Constructeur
	public ModelParticipation(ModelParticipant participant, ModelMatchAbstract match, InfosModelParticipation infos) {
		// Retenir le participant et le match
		this.participant = participant;
		this.match = match;
		
		// Enregistrer les informations
		this.setInfos(infos);
	}
	protected ModelParticipation(ModelParticipant participant, ModelMatchAbstract match, ModelParticipation participation) {
		// Appeller le constructeur principal
		this(participant, match, participation.toInfos());
		
		// Récupérer l'id
		this.setId(participation.getId());
	}
	
	// Getters
	public int getResultat () {
		return resultat;
	}
	public ModelParticipant getParticipant () {
		return this.participant;
	}
	public ModelMatchAbstract getMatch () {
		return this.match;
	}
	public ModelObjectifRemporte getObjectifRemporte (ModelObjectif objectif) {
		// Rechercher l'objectif remporte correspondant à l'objet
		for (ModelObjectifRemporte objectifRemporte : this.objectifsRemportes) {
			if (objectif.equals(objectifRemporte.getObjectif())) {
				return objectifRemporte;
			}
		}
		
		// Retourner null si pas d'objectif remporte correspondant
		return null;
	}
	public ArrayList<ModelObjectifRemporte> getObjectifsRemportes () {
		return new ArrayList<ModelObjectifRemporte>(this.objectifsRemportes);
	}
	public double getPoints () {
		// Initialiser le nombre de points
		double points = 0;
		
		// Comptabiliser les points des objectifs
		for(ModelObjectif objectif : FrontModel.get().getConcours().getObjectifs()) {
			// Récupérer l'objectif remporté
			ModelObjectifRemporte objectifRemporte = this.getObjectifRemporte(objectif);
				
			// Vérifier l'objectif a bien été remporté
			if(objectifRemporte != null) {
				// Récupérer la quantité d'objectif remporté
				int quantite = objectifRemporte.getQuantite();
					
				// Incrémenter le nombre de points via l'opération de l'objectif autant de fois que nécéssaire
				for (int i = 0; i < quantite; i++) {
					points = objectif.operation(points);
				}
			}
		}
		
		// Points de victoire, d'égalité et de défaite
		ModelConcours concours = FrontModel.get().getConcours();
		double pvictoire = concours.getPointsVictoire();
		double pegalite = concours.getPointsEgalite();
		double pdefaite = concours.getPointsDefaite();
		
		// Comptabiliser les points du résultat
		switch (this.getResultat()) {
			case InfosModelParticipation.RESULTAT_VICTOIRE:
				points += pvictoire;
				break;
			case InfosModelParticipation.RESULTAT_EGALITE:
				points += pegalite;
				break;
			case InfosModelParticipation.RESULTAT_DEFAITE:
				points += pdefaite;
				break;
		}
		
		// Retourner le nombre de points
		return points;
	}
	
	// Setters
	protected void setInfos (InfosModelParticipation infos) {
		// Appeller le setInfos du parent
		super.setInfos(infos);
		
		// Enregistrer les informations
		this.resultat = infos.getResultat();
		
		// Fire update
		this.fireUpdate();
	}
	protected void setParticipant(ModelParticipant participant) throws ContestOrgModelException {
		// Retenir le nouveau participant
		this.participant = participant;
		
		// Fire update
		this.fireUpdate();
	}
	
	// Adders
	public void addObjectifRemporte (ModelObjectifRemporte objectifRemporte) throws ContestOrgModelException {
		if (!this.objectifsRemportes.contains(objectifRemporte)) {
			// Vérifier si l'objectif de l'objectif remporté n'a pas déjà été remporté
			if (this.getObjectifRemporte(objectifRemporte.getObjectif()) != null) {
				throw new ContestOrgModelException("L'objectif a déjà été remporté");
			}
			
			// Ajouter l'objectif remporté
			this.objectifsRemportes.add(objectifRemporte);
			
			// Fire add
			this.fireAdd(objectifRemporte, this.objectifsRemportes.size() - 1);
		} else {
			throw new ContestOrgModelException("L'objectif remporté existe déjà dans la participation");
		}
	}
	
	// Removers
	protected void removeObjectifRemporte (ModelObjectifRemporte objectifRemporte) throws ContestOrgModelException {
		// Retirer l'objectif remporté
		int index;
		if ((index = this.objectifsRemportes.indexOf(objectifRemporte)) != -1) {
			// Remove
			this.objectifsRemportes.remove(objectifRemporte);
			
			// Fire remove
			this.fireRemove(objectifRemporte, index);
		} else {
			throw new ContestOrgModelException("L'objectif remporté n'existe pas dans la participation");
		}
	}
	
	// Updaters
	public void updateObjectifsRemportes(TrackableList<Pair<String, InfosModelParticipationObjectif>> list) throws ContestOrgModelException {
		this.updates(new ModelObjectifRemporte.Updater(FrontModel.get().getConcours(), this), this.objectifsRemportes, list, true, null);
	}
	
	// Clone
	protected ModelParticipation clone (ModelParticipant participant, ModelMatchAbstract match) {
		return new ModelParticipation(participant, match, this);
	}
	
	// ToInformation
	public InfosModelParticipation toInfos () {
		InfosModelParticipation infos = new InfosModelParticipation(this.resultat);
		infos.setId(this.getId());
		return infos;
	}
	
	// Remove
	protected void delete (ArrayList<ModelAbstract> removers) throws ContestOrgModelException {
		if (!removers.contains(this)) {
			// Ajouter la participation à la liste des removers
			removers.add(this);
			
			// Retirer la participation du match
			for (ModelObjectifRemporte objectifRemporte : this.objectifsRemportes) {
				if (!removers.contains(objectifRemporte)) {
					objectifRemporte.delete(removers);
				}
			}
			this.fireClear(ModelObjectif.class);
			
			// Retirer la participation du participant
			if (this.participant != null) {
				if (!removers.contains(this.participant)) {
					this.participant.removeParticipation(this);
				}
				this.participant = null;
				this.fireClear(ModelParticipant.class);
			}
			
			// Retirer la participation du match
			if (this.match != null) {
				if (!removers.contains(this.match)) {
					this.match.removeParticipation(this);
				}
				this.match = null;
				this.fireClear(ModelMatchAbstract.class);
			}
			
			// Fire delete
			this.fireDelete();
		}
	}
	
}
