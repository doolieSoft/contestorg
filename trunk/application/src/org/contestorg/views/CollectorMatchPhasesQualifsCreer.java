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
 * Collecteur pour la création d'un match au sein d'une phase qualificative d'une catégorie
 */
public class CollectorMatchPhasesQualifsCreer extends CollectorAbstract<Quadruple<Triple<String, TrackableList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>, Triple<String, TrackableList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>, Pair<String, String>, InfosModelMatchPhasesQualifs>>
{
	
	/** Nom de la catégorie */
	private String nomCategorie;
	
	/** Nom de la poule */
	private String nomPoule;
	
	/** Numéro de la phase qualificative */
	private int numeroPhase;
	
	/** Constructeur */
	public CollectorMatchPhasesQualifsCreer(String nomCategorie,String nomPoule,int numeroPhase) {
		// Retenir la catégorie, la poule et la phase qualificative
		this.nomCategorie = nomCategorie;
		this.nomPoule = nomPoule;
		this.numeroPhase = numeroPhase;
	}

	/**
	 * @see ICollector#collect(Object)
	 */
	@Override
	public void collect (Quadruple<Triple<String, TrackableList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>, Triple<String, TrackableList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>, Pair<String, String>, InfosModelMatchPhasesQualifs> infos) {
		// Demander la création du match
		ContestOrg.get().getCtrlPhasesQualificatives().addMatch(this.nomCategorie, this.nomPoule, this.numeroPhase, infos);
		
		// Fermer le collector
		this.close();
	}
	
}
