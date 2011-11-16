package org.contestorg.models;


import java.util.ArrayList;

import org.contestorg.common.Pair;
import org.contestorg.common.TrackableList;
import org.contestorg.common.Triple;
import org.contestorg.infos.InfosModelMatchPhasesQualifs;
import org.contestorg.infos.InfosModelParticipation;
import org.contestorg.infos.InfosModelParticipationObjectif;
import org.contestorg.interfaces.IUpdater;
import org.contestorg.log.Log;



/**
 * Match de phases qualificatives
 */
public class ModelMatchPhasesQualifs extends ModelMatchAbstract
{
	
	// Attributs objets
	private ModelPhaseQualificative phaseQualificative;
	
	// Constructeur
	public ModelMatchPhasesQualifs(ModelPhaseQualificative phaseQualificative, InfosModelMatchPhasesQualifs infos) {
		// Retenir la phase qualificative
		this.phaseQualificative = phaseQualificative;
		
		// Enregistrer les informations
		this.setInfos(infos);
	}
	protected ModelMatchPhasesQualifs(ModelPhaseQualificative phaseQualificative, ModelMatchPhasesQualifs match) {
		// Appeller le constructeur principal
		this(phaseQualificative, match.toInformation());
		
		// Récupérer l'id
		this.setId(match.getId());
	}
	
	// Getters
	public ModelPhaseQualificative getPhaseQualificative () {
		return this.phaseQualificative;
	}
	public int getNumero() {
		return this.phaseQualificative.getMatchs().indexOf(this);
	}
	
	// Setters
	protected void setInfos (InfosModelMatchPhasesQualifs infos) {
		// Appeller le setInfos du parent
		super.setInfos(infos);
		
		// Fire update
		this.fireUpdate();
	}
	
	// Clone
	protected ModelMatchPhasesQualifs clone (ModelPhaseQualificative phaseQualificative) {
		return new ModelMatchPhasesQualifs(phaseQualificative, this);
	}
	
	// ToInformations
	public InfosModelMatchPhasesQualifs toInformation () {
		InfosModelMatchPhasesQualifs infos = new InfosModelMatchPhasesQualifs(this.getDate(),this.getDetails());
		infos.setId(this.getId());
		return infos;
	}
	
	// Remove
	protected void delete (ArrayList<ModelAbstract> removers) throws ContestOrgModelException {
		if (!removers.contains(this)) {
			// Appeller le remove du parent
			super.delete(removers);
			
			// Retirer le match de la pahse qualificative
			if (this.phaseQualificative != null) {
				if (!removers.contains(this.phaseQualificative)) {
					this.phaseQualificative.removeMatch(this);
					if(this.phaseQualificative.getMatchs().size() == 0) {
						this.phaseQualificative.delete(removers);
					}
				}
				this.phaseQualificative = null;
				this.fireClear(ModelPhaseQualificative.class);
			}
			
			// Fire delete
			this.fireDelete();
		}
	}
	
	// Classe pour mettre à jour la liste des matchs d'une phase qualificative
	protected static class UpdaterForPhaseQualif implements IUpdater<Triple<Triple<String, TrackableList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>, Triple<String, TrackableList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>,InfosModelMatchPhasesQualifs>, ModelMatchPhasesQualifs>
	{
		
		// Phase qualificative
		private ModelPhaseQualificative phase;
		
		// Constructeur
		public UpdaterForPhaseQualif(ModelPhaseQualificative phase) {
			this.phase = phase;
		}

		// Implémentation de create
		@Override
		public ModelMatchPhasesQualifs create (Triple<Triple<String, TrackableList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>, Triple<String, TrackableList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>,InfosModelMatchPhasesQualifs> infos) {
			try {
				// Créer le match
				ModelMatchPhasesQualifs match = new ModelMatchPhasesQualifs(this.phase, infos.getThird());
				
				// Créer la participation A
				ModelEquipe equipeA = infos.getFirst().getFirst() == null ? null : this.phase.getPoule().getEquipeByNom(infos.getFirst().getFirst());
				ModelParticipation participationA = new ModelParticipation(equipeA, match, infos.getFirst().getThird());
				participationA.updateObjectifsRemportes(infos.getFirst().getSecond());
				match.setParticipationA(participationA);
				if(equipeA != null) {
					equipeA.addParticipation(participationA);
				}
			
				// Créer la participation B
				ModelEquipe equipeB = infos.getSecond().getFirst() == null ? null : this.phase.getPoule().getEquipeByNom(infos.getSecond().getFirst());
				ModelParticipation participationB = new ModelParticipation(equipeB, match, infos.getSecond().getThird());
				participationB.updateObjectifsRemportes(infos.getSecond().getSecond());
				match.setParticipationB(participationB);
				if(equipeB != null) {
					equipeB.addParticipation(participationB);
				}
				
				// Retourner le match
				return match;
			} catch (ContestOrgModelException e) {
				Log.getLogger().fatal("Erreur lors de la création d'un match de phases qualificatives.",e);
				return null;
			}
		}

		// Implémentation de update
		@Override
		public void update (ModelMatchPhasesQualifs match, Triple<Triple<String, TrackableList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>, Triple<String, TrackableList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>,InfosModelMatchPhasesQualifs> infos) {
			try {
				// Modifier le match
				match.setInfos(infos.getThird());
				
				// Modifier la participationA
				ModelParticipation participationA = match.getParticipationA();
				participationA.setInfos(infos.getFirst().getThird());
				ModelEquipe ancienneEquipeA = match.getParticipationA().getEquipe();
				ModelEquipe nouvelleEquipeA = this.phase.getPoule().getCategorie().getConcours().getEquipeByNom(infos.getFirst().getFirst());
				if((ancienneEquipeA == null && nouvelleEquipeA != null) || (ancienneEquipeA != null && !ancienneEquipeA.equals(nouvelleEquipeA))) {
					if(participationA.getEquipe() != null) {
						participationA.getEquipe().removeParticipation(participationA);
					}
					participationA.setEquipe(nouvelleEquipeA);
					if(nouvelleEquipeA != null) {
						nouvelleEquipeA.addParticipation(participationA);
					}
				}
				participationA.updateObjectifsRemportes(infos.getFirst().getSecond());
				
				// Modifier la participationB
				ModelParticipation participationB = match.getParticipationB();
				participationB.setInfos(infos.getSecond().getThird());
				ModelEquipe ancienneEquipeB = match.getParticipationB().getEquipe();
				ModelEquipe nouvelleEquipeB = this.phase.getPoule().getCategorie().getConcours().getEquipeByNom(infos.getSecond().getFirst());
				if((ancienneEquipeB == null && nouvelleEquipeB != null) || (ancienneEquipeB != null && !ancienneEquipeB.equals(nouvelleEquipeB))) {
					if(participationB.getEquipe() != null) {
						participationB.getEquipe().removeParticipation(participationB);
					}
					participationB.setEquipe(nouvelleEquipeB);
					if(nouvelleEquipeB != null) {
						nouvelleEquipeB.addParticipation(participationB);
					}
				}
				participationB.updateObjectifsRemportes(infos.getSecond().getSecond());
			} catch (ContestOrgModelException e) {
				Log.getLogger().fatal("Erreur lors de la modification d'un match de phases qualificatives.",e);
			}
		}
		
	}
	
}
