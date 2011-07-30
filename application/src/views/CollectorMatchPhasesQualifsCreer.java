package views;

import infos.InfosModelMatchPhasesQualifs;
import infos.InfosModelParticipation;
import infos.InfosModelParticipationObjectif;

import common.TrackableList;
import common.Pair;
import common.Triple;

import controlers.ContestOrg;

public class CollectorMatchPhasesQualifsCreer extends CollectorAbstract<Triple<Triple<String, TrackableList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>, Triple<String, TrackableList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>,InfosModelMatchPhasesQualifs>>
{
	
	// Catégorie
	private String nomCategorie;
	
	// Poule
	private String nomPoule;
	
	// Phase qualificative
	private int numeroPhase;
	
	// Constructeur
	public CollectorMatchPhasesQualifsCreer(String nomCategorie,String nomPoule,int numeroPhase) {
		// Retenir la catégorie, la poule et la phase qualificative
		this.nomCategorie = nomCategorie;
		this.nomPoule = nomPoule;
		this.numeroPhase = numeroPhase;
	}

	// Implémentation de accept
	@Override
	public void accept (Triple<Triple<String, TrackableList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>, Triple<String, TrackableList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>, InfosModelMatchPhasesQualifs> infos) {
		// Demander la création du match
		ContestOrg.get().getCtrlPhasesQualificatives().addMatchPhaseQualif(nomCategorie, nomPoule, numeroPhase, infos);
		
		// Fermer le collector
		this.close();
	}
	
}
