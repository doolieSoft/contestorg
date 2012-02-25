package org.contestorg.views;

import org.contestorg.common.Triple;
import org.contestorg.controllers.ContestOrg;
import org.contestorg.infos.Configuration;
import org.contestorg.infos.InfosModelMatchPhasesQualifs;
import org.contestorg.infos.InfosModelPhaseQualificative;
import org.contestorg.interfaces.ICollector;

/**
 * Collecteur pour la création d'une phase qualificative au sein d'une catégorie
 */
public class CollectorPhaseQualifCreer extends CollectorAbstract<Triple<Configuration<String>,InfosModelPhaseQualificative,InfosModelMatchPhasesQualifs>>
{
	/** Nom de la catégorie */
	private String nomCategorie;
	
	/** Nom de la poule */
	private String nomPoule;
	
	/**
	 * Constructeur
	 * @param nomCategorie nom de la catégorie
	 * @param nomPoule nom de la poule
	 */
	public CollectorPhaseQualifCreer(String nomCategorie,String nomPoule) {
		// Retenir la catégorie et la poule 
		this.nomCategorie = nomCategorie;
		this.nomPoule = nomPoule;
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
