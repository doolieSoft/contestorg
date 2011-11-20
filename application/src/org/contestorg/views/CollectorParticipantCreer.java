package org.contestorg.views;

import org.contestorg.common.Pair;
import org.contestorg.common.Quintuple;
import org.contestorg.common.TrackableList;
import org.contestorg.controlers.ContestOrg;
import org.contestorg.infos.InfosModelParticipant;
import org.contestorg.infos.InfosModelProprieteParticipant;




public class CollectorParticipantCreer extends CollectorAbstract<Quintuple<String,String,InfosModelParticipant, TrackableList<Pair<String, InfosModelProprieteParticipant>>, TrackableList<String>>>
{

	// Implémentation de accept
	@Override
	public void accept (Quintuple<String, String, InfosModelParticipant, TrackableList<Pair<String, InfosModelProprieteParticipant>>, TrackableList<String>> infos) {
		// Demander la création du participant
		ContestOrg.get().getCtrlParticipants().addParticipant(infos);
		
		// Fermer le collector
		this.close();
	}
	
}
