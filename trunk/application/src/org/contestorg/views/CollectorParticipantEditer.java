package org.contestorg.views;

import org.contestorg.common.Pair;
import org.contestorg.common.Quintuple;
import org.contestorg.common.TrackableList;
import org.contestorg.controlers.ContestOrg;
import org.contestorg.infos.InfosModelParticipant;
import org.contestorg.infos.InfosModelProprieteParticipant;




public class CollectorParticipantEditer extends CollectorAbstract<Quintuple<String,String,InfosModelParticipant, TrackableList<Pair<String, InfosModelProprieteParticipant>>, TrackableList<String>>>
{
	
	// Nom du participant édité
	private String nomParticpant;
	
	// Constructeur
	public CollectorParticipantEditer(String nomParticipant) {
		// Retenir le nom du participant
		this.nomParticpant = nomParticipant;
	}

	// Implémentation de accept
	@Override
	public void accept (Quintuple<String, String, InfosModelParticipant, TrackableList<Pair<String, InfosModelProprieteParticipant>>, TrackableList<String>> infos) {
		// Demander la modification du participant
		ContestOrg.get().getCtrlParticipants().updateParticipant(this.nomParticpant, infos);
		
		// Mettre le nom du participant à nul
		this.nomParticpant = null;
		
		// Fermer le collector
		this.close();
	}
	
}
