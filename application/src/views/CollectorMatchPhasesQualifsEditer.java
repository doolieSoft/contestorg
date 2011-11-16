package views;

import infos.InfosModelMatchPhasesQualifs;
import infos.InfosModelParticipation;
import infos.InfosModelParticipationObjectif;

import common.TrackableList;
import common.Pair;
import common.Triple;

import controlers.ContestOrg;

public class CollectorMatchPhasesQualifsEditer extends CollectorAbstract<Triple<Triple<String, TrackableList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>, Triple<String, TrackableList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>, InfosModelMatchPhasesQualifs>>
{
	
	// Catégorie
	private String nomCategorie;
	
	// Poule
	private String nomPoule;
	
	// Phase qualificative
	private int numeroPhase;
	
	// Match
	private int numeroMatch;
	
	// Constructeur
	public CollectorMatchPhasesQualifsEditer(String nomCategorie,String nomPoule,int numeroPhase, int numeroMatch) {
		// Retenir la catégorie, la poule, la phase qualificative et le match
		this.nomCategorie = nomCategorie;
		this.nomPoule = nomPoule;
		this.numeroPhase = numeroPhase;
		this.numeroMatch = numeroMatch;
	}

	// Implémentation de accept
	@Override
	public void accept (Triple<Triple<String, TrackableList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>, Triple<String, TrackableList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>, InfosModelMatchPhasesQualifs> infos) {
		// Demander l'édition du match
		ContestOrg.get().getCtrlPhasesQualificatives().updateMatchPhaseQualif(this.nomCategorie, this.nomPoule, this.numeroPhase, this.numeroMatch, infos);
		
		// Fermer le collector
		this.close();
	}
	
}
