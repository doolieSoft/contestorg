package org.contestorg.views;

import org.contestorg.common.Quintuple;
import org.contestorg.common.Triple;
import org.contestorg.controllers.ContestOrg;
import org.contestorg.infos.Configuration;
import org.contestorg.infos.InfosModelMatchPhasesQualifs;
import org.contestorg.infos.InfosModelPhaseQualificative;
import org.contestorg.interfaces.ICollector;

/**
 * Collecteur pour la création d'une phase qualificative au sein d'une catégorie
 */
public class CollectorPhaseQualifCreer extends CollectorAbstract<Quintuple<String,String,Configuration<String>,InfosModelPhaseQualificative,InfosModelMatchPhasesQualifs>>
{
	/**
	 * @see ICollector#collect(Object)
	 */
	@Override
	public void collect (Quintuple<String,String,Configuration<String>,InfosModelPhaseQualificative,InfosModelMatchPhasesQualifs> infos) {
		// Demander la création de la phase qualificative
		ContestOrg.get().getCtrlPhasesQualificatives().addPhaseQualif(infos.getFirst(), infos.getSecond(), new Triple<Configuration<String>, InfosModelPhaseQualificative, InfosModelMatchPhasesQualifs>(infos.getThird(), infos.getFourth(), infos.getFifth()));
		
		// Fermer le collector
		this.close();
	}
	
}
