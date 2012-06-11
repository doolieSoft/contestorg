package org.contestorg.views;

import org.contestorg.common.Pair;
import org.contestorg.common.Quadruple;
import org.contestorg.common.TrackableList;
import org.contestorg.controllers.ContestOrg;
import org.contestorg.infos.InfosModelMatchPhasesElims;
import org.contestorg.infos.InfosModelObjectifRemporte;
import org.contestorg.infos.InfosModelParticipation;
import org.contestorg.interfaces.ICollector;

/**
 * Collecteur pour l'édition d'un match des phases éliminatoires d'une catégorie
 */
public class CollectorMatchPhasesElims extends CollectorAbstract<Quadruple<Pair<TrackableList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>, Pair<TrackableList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>, Pair<String, String>, InfosModelMatchPhasesElims>>
{
	/** Nom de la catégorie */
	private String nomCategorie;
	
	/** Numéro du match */
	private int numeroMatch;

	/**
	 * Constructeur
	 * @param nomCategorie nom de la catégorie
	 * @param numeroMatch numéro du match
	 */
	public CollectorMatchPhasesElims(String nomCategorie, int numeroMatch) {
		this.nomCategorie = nomCategorie;
		this.numeroMatch = numeroMatch;
	}

	/**
	 * @see ICollector#collect(Object)
	 */
	@Override
	public void collect (Quadruple<Pair<TrackableList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>, Pair<TrackableList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>, Pair<String, String>, InfosModelMatchPhasesElims> infos) {
		// Demander la modification du match
		ContestOrg.get().getCtrlPhasesEliminatoires().updateMatch(this.nomCategorie, this.numeroMatch, infos);
		
		// Fermer le collector
		this.close();
	}
	
}
