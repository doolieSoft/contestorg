package views;

import infos.InfosModelMatchPhasesElims;
import infos.InfosModelParticipation;
import infos.InfosModelParticipationObjectif;

import common.TrackableList;
import common.Pair;
import common.Triple;
import controlers.ContestOrg;

public class CollectorMatchPhasesElims extends CollectorAbstract<Triple<Pair<TrackableList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>, Pair<TrackableList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>, InfosModelMatchPhasesElims>>
{
	// Nom de la catégorie
	private String nomCategorie;
	
	// Numéro de match
	private int numeroMatch;

	// Constructeur
	public CollectorMatchPhasesElims(String nomCategorie, int numeroMatch) {
		this.nomCategorie = nomCategorie;
		this.numeroMatch = numeroMatch;
	}

	// Implémentation de accept
	@Override
	public void accept (Triple<Pair<TrackableList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>, Pair<TrackableList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>, InfosModelMatchPhasesElims> infos) {
		// Demander la modification du match
		ContestOrg.get().getCtrlPhasesEliminatoires().updateMatchPhasesElims(this.nomCategorie, this.numeroMatch, infos);
		
		// Fermer le collector
		this.close();
	}
	
}
