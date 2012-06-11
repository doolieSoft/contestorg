package org.contestorg.views;

import org.contestorg.common.Pair;
import org.contestorg.common.Quadruple;
import org.contestorg.common.TrackableList;
import org.contestorg.common.Triple;
import org.contestorg.controllers.ContestOrg;
import org.contestorg.infos.InfosModelMatchPhasesQualifs;
import org.contestorg.infos.InfosModelObjectifRemporte;
import org.contestorg.infos.InfosModelParticipation;
import org.contestorg.interfaces.ICollector;

/**
 * Collecteur pour l'édition d'un match au sein d'une phase qualificative d'une catégorie
 */
public class CollectorMatchPhasesQualifsEditer extends CollectorAbstract<Quadruple<Triple<String, TrackableList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>, Triple<String, TrackableList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>, Pair<String, String>, InfosModelMatchPhasesQualifs>>
{
	
	/** Nom de la catégorie */
	private String nomCategorie;
	
	/** Nom de la poule */
	private String nomPoule;
	
	/** Numéro de la phase qualificative */
	private int numeroPhase;
	
	/** Numéro du match */
	private int numeroMatch;
	
	/**
	 * Constructeur
	 * @param nomCategorie nom de la catégorie
	 * @param nomPoule nom de la poule
	 * @param numeroPhase numéro de la phase qualificative
	 * @param numeroMatch numéro du match
	 */
	public CollectorMatchPhasesQualifsEditer(String nomCategorie,String nomPoule,int numeroPhase, int numeroMatch) {
		// Retenir la catégorie, la poule, la phase qualificative et le match
		this.nomCategorie = nomCategorie;
		this.nomPoule = nomPoule;
		this.numeroPhase = numeroPhase;
		this.numeroMatch = numeroMatch;
	}

	/**
	 * @see ICollector#collect(Object)
	 */
	@Override
	public void collect (Quadruple<Triple<String, TrackableList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>, Triple<String, TrackableList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>, Pair<String, String>, InfosModelMatchPhasesQualifs> infos) {
		// Demander l'édition du match
		ContestOrg.get().getCtrlPhasesQualificatives().updateMatch(this.nomCategorie, this.nomPoule, this.numeroPhase, this.numeroMatch, infos);
		
		// Fermer le collector
		this.close();
	}
	
}
