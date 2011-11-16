package org.contestorg.views;

import org.contestorg.common.Pair;
import org.contestorg.common.Quintuple;
import org.contestorg.common.TrackableList;
import org.contestorg.controlers.ContestOrg;
import org.contestorg.infos.InfosModelEquipe;
import org.contestorg.infos.InfosModelProprieteEquipe;




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
