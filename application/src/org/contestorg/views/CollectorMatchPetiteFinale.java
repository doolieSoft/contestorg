package org.contestorg.views;

import org.contestorg.common.Pair;
import org.contestorg.common.TrackableList;
import org.contestorg.common.Triple;
import org.contestorg.controlers.ContestOrg;
import org.contestorg.infos.InfosModelMatchPhasesElims;
import org.contestorg.infos.InfosModelParticipation;
import org.contestorg.infos.InfosModelParticipationObjectif;



public class CollectorMatchPetiteFinale extends CollectorAbstract<Triple<Pair<TrackableList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>, Pair<TrackableList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>, InfosModelMatchPhasesElims>>
{

	// Nom de la catégorie
	private String nomCategorie;

	// Constructeur
	public CollectorMatchPetiteFinale(String nomCategorie) {
		// Retenir le nom de la catégorie
		this.nomCategorie = nomCategorie;
	}
	
	// Implémentation de accept
	@Override
	public void accept (Triple<Pair<TrackableList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>, Pair<TrackableList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>, InfosModelMatchPhasesElims> infos) {
		// Demander la modification du match
		ContestOrg.get().getCtrlPhasesEliminatoires().updateMatchPetiteFinale(this.nomCategorie, infos);
		
		// Fermer le collector
		this.close();
	}
	
}
