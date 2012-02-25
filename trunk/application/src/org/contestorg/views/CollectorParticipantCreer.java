package org.contestorg.views;

import org.contestorg.common.Pair;
import org.contestorg.common.Quintuple;
import org.contestorg.common.TrackableList;
import org.contestorg.controllers.ContestOrg;
import org.contestorg.infos.InfosModelParticipant;
import org.contestorg.infos.InfosModelProprietePossedee;

/**
 * Collecteur pour la création d'un participant
 */
public class CollectorParticipantCreer extends CollectorAbstract<Quintuple<String,String,InfosModelParticipant, TrackableList<Pair<String, InfosModelProprietePossedee>>, TrackableList<String>>>
{

	/**
	 * @see ICollector#collect(Object)
	 */
	@Override
	public void collect (Quintuple<String, String, InfosModelParticipant, TrackableList<Pair<String, InfosModelProprietePossedee>>, TrackableList<String>> infos) {
		// Demander la création du participant
		ContestOrg.get().getCtrlParticipants().addParticipant(infos);
		
		// Fermer le collector
		this.close();
	}
	
}
