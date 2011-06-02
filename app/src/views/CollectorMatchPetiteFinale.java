package views;

import infos.InfosModelMatchPhasesElims;
import infos.InfosModelParticipation;
import infos.InfosModelParticipationObjectif;

import common.TrackableList;
import common.Pair;
import common.Triple;
import controlers.ContestOrg;

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
