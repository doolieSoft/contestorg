package views;

import infos.InfosModelEquipe;
import infos.InfosModelProprieteEquipe;

import common.TrackableList;
import common.Pair;
import common.Quintuple;

import controlers.ContestOrg;

public class CollectorEquipeCreer extends CollectorAbstract<Quintuple<String,String,InfosModelEquipe, TrackableList<Pair<String, InfosModelProprieteEquipe>>, TrackableList<String>>>
{

	// Implémentation de accept
	@Override
	public void accept (Quintuple<String, String, InfosModelEquipe, TrackableList<Pair<String, InfosModelProprieteEquipe>>, TrackableList<String>> infos) {
		// Demander la création de l'équipe
		ContestOrg.get().getCtrlEquipes().addEquipe(infos);
		
		// Fermer le collector
		this.close();
	}
	
}
