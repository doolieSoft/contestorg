package org.contestorg.views;

import org.contestorg.common.Pair;
import org.contestorg.common.TrackableList;
import org.contestorg.common.Triple;
import org.contestorg.controlers.ContestOrg;
import org.contestorg.infos.InfosModelMatchPhasesQualifs;
import org.contestorg.infos.InfosModelParticipation;
import org.contestorg.infos.InfosModelParticipationObjectif;




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
