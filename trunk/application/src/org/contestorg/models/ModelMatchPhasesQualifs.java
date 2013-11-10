package org.contestorg.models;

import java.util.ArrayList;

import org.contestorg.common.ContestOrgErrorException;
import org.contestorg.common.Pair;
import org.contestorg.common.Quadruple;
import org.contestorg.common.TrackableList;
import org.contestorg.common.Triple;
import org.contestorg.infos.InfosModelMatchPhasesQualifs;
import org.contestorg.infos.InfosModelObjectifRemporte;
import org.contestorg.infos.InfosModelParticipation;
import org.contestorg.interfaces.IUpdater;
import org.contestorg.log.Log;

/**
 * Match des phases qualificatives
 */
public class ModelMatchPhasesQualifs extends ModelMatchAbstract
{
	
	// Attributs objets
	
	/** Phase qualificative */
	private ModelPhaseQualificative phaseQualificative;
	
	// Constructeurs
	
	/**
	 * Constructeur
	 * @param phaseQualificative phase qualificative
	 * @param emplacement emplacement
	 * @param infos informations du match des phases qualificatives
	 */
	public ModelMatchPhasesQualifs(ModelPhaseQualificative phaseQualificative, ModelEmplacement emplacement, InfosModelMatchPhasesQualifs infos) {
		// Appeller le constructeur parent
		super(emplacement,infos);
		
		// Retenir la phase qualificative
		this.phaseQualificative = phaseQualificative;
	}
	
	/**
	 * Constructeur par copie
	 * @param phaseQualificative phase qualificative
	 * @param emplacement emplacement
	 * @param match match des phases qualificatives
	 */
	protected ModelMatchPhasesQualifs(ModelPhaseQualificative phaseQualificative, ModelEmplacement emplacement, ModelMatchPhasesQualifs match) {
		// Appeller le constructeur principal
		this(phaseQualificative, emplacement, match.getInfos());
		
		// Récupérer l'id
		this.setId(match.getId());
	}
	
	// Getters
	
	/**
	 * Récupérer la phase qualificative
	 * @return phase qualificative
	 */
	public ModelPhaseQualificative getPhaseQualificative () {
		return this.phaseQualificative;
	}
	
	/**
	 * Récupérer le numéro
	 * @return numéro
	 */
	public int getNumero() {
		return this.phaseQualificative.getMatchs().indexOf(this);
	}
	
	/**
	 * @see ModelAbstract#getInfos()
	 */
	public InfosModelMatchPhasesQualifs getInfos () {
		InfosModelMatchPhasesQualifs infos = new InfosModelMatchPhasesQualifs(this.getDate(), this.getDetails());
		infos.setId(this.getId());
		return infos;
	}
	
	// Setters
	
	/**
	 * Définir les informations du match des phases qualificatives
	 * @param infos informations du match des phases qualificatives
	 */
	protected void setInfos (InfosModelMatchPhasesQualifs infos) {
		// Appeller le setInfos du parent
		super.setInfos(infos);
		
		// Fire update
		this.fireUpdate();
	}
	
	/**
	 * Cloner le match des phases qualificatives
	 * @param phaseQualificative phase qualificative
	 * @param emplacement emplacement
	 * @return clone du match des phases qualificatives
	 */
	protected ModelMatchPhasesQualifs clone (ModelPhaseQualificative phaseQualificative, ModelEmplacement emplacement) {
		return new ModelMatchPhasesQualifs(phaseQualificative, emplacement, this);
	}
	
	/**
	 * @see ModelMatchAbstract#delete(ArrayList)
	 */
	protected void delete (ArrayList<ModelAbstract> removers) throws ContestOrgErrorException {
		if (!removers.contains(this)) {
			// Appeller le remove du parent
			super.delete(removers);
			
			// Retirer le match de la pahse qualificative
			if (this.phaseQualificative != null) {
				if (!removers.contains(this.phaseQualificative)) {
					this.phaseQualificative.removeMatch(this);
				}
				this.phaseQualificative = null;
				this.fireClear(ModelPhaseQualificative.class);
			}
			
			// Fire delete
			this.fireDelete();
		}
	}
	
	/**
	 * Classe pour mettre à jour la liste des matchs d'une phase qualificative
	 */
	protected static class UpdaterForPhaseQualif implements IUpdater<Quadruple<Triple<String, TrackableList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>, Triple<String, TrackableList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>,Pair<String,String>,InfosModelMatchPhasesQualifs>, ModelMatchPhasesQualifs>
	{	
		/** Concours */
		private ModelConcours concours;
		
		/** Phase qualificative */
		private ModelPhaseQualificative phaseQualif;
		
		/**
		 * Constructeur
		 * @param concours concours
		 * @param phaseQualif phase qualificative
		 */
		public UpdaterForPhaseQualif(ModelConcours concours,ModelPhaseQualificative phaseQualif) {
			this.concours = concours;
			this.phaseQualif = phaseQualif;
		}

		/**
		 * @see IUpdater#create(Object)
		 */
		@Override
		public ModelMatchPhasesQualifs create (Quadruple<Triple<String, TrackableList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>, Triple<String, TrackableList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>,Pair<String,String>,InfosModelMatchPhasesQualifs> infos) {
			try {
				// Récupérer l'emplacement
				ModelEmplacement emplacement = null;
				if(infos.getThird() != null && infos.getThird().getFirst() != null && infos.getThird().getSecond() != null) {
					emplacement = this.concours.getLieuByNom(infos.getThird().getFirst()).getEmplacementByNom(infos.getThird().getSecond());
				}
				
				// Créer le match
				ModelMatchPhasesQualifs match = new ModelMatchPhasesQualifs(this.phaseQualif, emplacement, infos.getFourth());
				
				// Ajouter le match à l'emplacement
				if(emplacement != null) {
					emplacement.addMatch(match);
				}
				
				// Créer la participation A
				ModelParticipant participantA = infos.getFirst().getFirst() == null ? null : this.phaseQualif.getPoule().getParticipantByNom(infos.getFirst().getFirst());
				ModelParticipation participationA = new ModelParticipation(participantA, match, infos.getFirst().getThird());
				participationA.updateObjectifsRemportes(infos.getFirst().getSecond());
				match.setParticipationA(participationA);
				if(participantA != null) {
					participantA.addParticipation(participationA);
				}
			
				// Créer la participation B
				ModelParticipant participantB = infos.getSecond().getFirst() == null ? null : this.phaseQualif.getPoule().getParticipantByNom(infos.getSecond().getFirst());
				ModelParticipation participationB = new ModelParticipation(participantB, match, infos.getSecond().getThird());
				participationB.updateObjectifsRemportes(infos.getSecond().getSecond());
				match.setParticipationB(participationB);
				if(participantB != null) {
					participantB.addParticipation(participationB);
				}
				
				// Retourner le match
				return match;
			} catch (ContestOrgErrorException e) {
				Log.getLogger().fatal("Erreur lors de la création d'un match de phases qualificatives.",e);
				return null;
			}
		}

		/**
		 * @see IUpdater#update(Object, Object)
		 */
		@Override
		public ModelMatchPhasesQualifs update (ModelMatchPhasesQualifs match, Quadruple<Triple<String, TrackableList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>, Triple<String, TrackableList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>,Pair<String,String>,InfosModelMatchPhasesQualifs> infos) {
			try {
				// Modifier le match
				match.setInfos(infos.getFourth());
				
				// Modifier l'emplacement
				if(infos.getThird() != null && infos.getThird().getFirst() != null && infos.getThird().getSecond() != null) {
					if(match.getEmplacement() == null || !match.getEmplacement().getNom().equals(infos.getThird().getFirst()) || !match.getEmplacement().getLieu().getNom().equals(infos.getThird().getSecond())) {
						if(match.getEmplacement() != null) {
							match.getEmplacement().removeMatch(match);
						}
						ModelEmplacement emplacement = this.concours.getLieuByNom(infos.getThird().getFirst()).getEmplacementByNom(infos.getThird().getSecond());
						match.setEmplacement(emplacement);
						emplacement.addMatch(match);
					}
				} else if(match.getEmplacement() != null) {
					match.getEmplacement().removeMatch(match);
					match.setEmplacement(null);
				}
				
				// Modifier la participationA
				ModelParticipation participationA = match.getParticipationA();
				participationA.setInfos(infos.getFirst().getThird());
				ModelParticipant ancienParticipantA = match.getParticipationA().getParticipant();
				ModelParticipant nouveauParticipantA = this.phaseQualif.getPoule().getCategorie().getConcours().getParticipantByNom(infos.getFirst().getFirst());
				if((ancienParticipantA == null && nouveauParticipantA != null) || (ancienParticipantA != null && !ancienParticipantA.equals(nouveauParticipantA))) {
					if(participationA.getParticipant() != null) {
						participationA.getParticipant().removeParticipation(participationA);
					}
					participationA.setParticipant(nouveauParticipantA);
					if(nouveauParticipantA != null) {
						nouveauParticipantA.addParticipation(participationA);
					}
				}
				participationA.updateObjectifsRemportes(infos.getFirst().getSecond());
				
				// Modifier la participationB
				ModelParticipation participationB = match.getParticipationB();
				participationB.setInfos(infos.getSecond().getThird());
				ModelParticipant ancienParticipantB = match.getParticipationB().getParticipant();
				ModelParticipant nouveauParticipantB = this.phaseQualif.getPoule().getCategorie().getConcours().getParticipantByNom(infos.getSecond().getFirst());
				if((ancienParticipantB == null && nouveauParticipantB != null) || (ancienParticipantB != null && !ancienParticipantB.equals(nouveauParticipantB))) {
					if(participationB.getParticipant() != null) {
						participationB.getParticipant().removeParticipation(participationB);
					}
					participationB.setParticipant(nouveauParticipantB);
					if(nouveauParticipantB != null) {
						nouveauParticipantB.addParticipation(participationB);
					}
				}
				participationB.updateObjectifsRemportes(infos.getSecond().getSecond());
			} catch (ContestOrgErrorException e) {
				Log.getLogger().fatal("Erreur lors de la modification d'un match de phases qualificatives.",e);
			}
			return null;
		}
		
	}
	
}
