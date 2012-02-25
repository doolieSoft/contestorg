package org.contestorg.views;

import org.contestorg.common.Pair;
import org.contestorg.common.Quintuple;
import org.contestorg.common.TrackableList;
import org.contestorg.controllers.ContestOrg;
import org.contestorg.infos.InfosModelParticipant;
import org.contestorg.infos.InfosModelProprietePossedee;
import org.contestorg.interfaces.ICollector;

/**
 * Collecteur pour l'édition d'un participant
 */
public class CollectorParticipantEditer extends CollectorAbstract<Quintuple<String,String,InfosModelParticipant, TrackableList<Pair<String, InfosModelProprietePossedee>>, TrackableList<String>>>
{
	
	/** Nom du participant (avant l'édition) */
	private String nomParticpant;
	
	/**
	 * Constructeur
	 * @param nomParticipant nom du participant (avant l'édition)
	 */
	public CollectorParticipantEditer(String nomParticipant) {
		// Retenir le nom du participant
		this.nomParticpant = nomParticipant;
	}

	/**
	 * @see ICollector#collect(Object)
	 */
	@Override
	public void collect (Quintuple<String, String, InfosModelParticipant, TrackableList<Pair<String, InfosModelProprietePossedee>>, TrackableList<String>> infos) {
		// Demander la modification du participant
		ContestOrg.get().getCtrlParticipants().updateParticipant(this.nomParticpant, infos);
		
		// Mettre le nom du participant à nul
		this.nomParticpant = null;
		
		// Fermer le collector
		this.close();
	}
	
}
