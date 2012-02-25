package org.contestorg.views;

import org.contestorg.common.Pair;
import org.contestorg.common.TrackableList;
import org.contestorg.common.Triple;
import org.contestorg.controllers.ContestOrg;
import org.contestorg.infos.InfosModelMatchPhasesElims;
import org.contestorg.infos.InfosModelObjectifRemporte;
import org.contestorg.infos.InfosModelParticipation;
import org.contestorg.interfaces.ICollector;

/**
 * Collecteur pour l'édition de la petite finale d'une catégorie
 */
public class CollectorMatchPetiteFinale extends CollectorAbstract<Triple<Pair<TrackableList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>, Pair<TrackableList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>, InfosModelMatchPhasesElims>>
{

	/** Nom de la catégorie */
	private String nomCategorie;

	/**
	 * Constructeur
	 * @param nomCategorie nom de la catégorie
	 */
	public CollectorMatchPetiteFinale(String nomCategorie) {
		// Retenir le nom de la catégorie
		this.nomCategorie = nomCategorie;
	}
	
	/**
	 * @see ICollector#collect(Object)
	 */
	@Override
	public void collect (Triple<Pair<TrackableList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>, Pair<TrackableList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>, InfosModelMatchPhasesElims> infos) {
		// Demander la modification du match
		ContestOrg.get().getCtrlPhasesEliminatoires().updateMatchPetiteFinale(this.nomCategorie, infos);
		
		// Fermer le collector
		this.close();
	}
	
}
