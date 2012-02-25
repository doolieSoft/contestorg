package org.contestorg.views;

import org.contestorg.common.Triple;
import org.contestorg.controllers.ContestOrg;
import org.contestorg.infos.Configuration;
import org.contestorg.infos.InfosModelMatchPhasesQualifs;
import org.contestorg.infos.InfosModelPhaseQualificative;
import org.contestorg.interfaces.ICollector;

/**
 * Collecteur pour l'édition d'une phase qualificative au sein d'une catégorie
 */
public class CollectorPhaseQualifEditer extends CollectorAbstract<Triple<Configuration<String>,InfosModelPhaseQualificative,InfosModelMatchPhasesQualifs>>
{
	/** Nom de la catégorie */
	private String nomCategorie;
	
	/** Nom de la poule */
	private String nomPoule;
	
	/** Numéro de la phase qualificative */
	private int numeroPhase;
	
	/**
	 * Constructeur
	 * @param nomCategorie nom de la phase qualificative
	 * @param nomPoule nom de la poule
	 * @param numeroPhase numéro de la phase qualificative
	 */
	public CollectorPhaseQualifEditer(String nomCategorie,String nomPoule,int numeroPhase) {
		// Retenir la catégorie, la poule et la phase qualificative 
		this.nomCategorie = nomCategorie;
		this.nomPoule = nomPoule;
		this.numeroPhase = numeroPhase;
		
		// Demander la suppression de la phase qualificative
		ContestOrg.get().getCtrlPhasesQualificatives().removePhaseQualif(this.nomCategorie, this.nomPoule, this.numeroPhase);
	}

	/**
	 * @see ICollector#collect(Object)
	 */
	@Override
	public void collect (Triple<Configuration<String>,InfosModelPhaseQualificative,InfosModelMatchPhasesQualifs> infos) {
		// Demander la création de la phase qualificative
		ContestOrg.get().getCtrlPhasesQualificatives().addPhaseQualif(this.nomCategorie, this.nomPoule, infos);
		
		// Fermer le collector
		this.close();
	}
}
