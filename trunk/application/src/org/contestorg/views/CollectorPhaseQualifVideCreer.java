package org.contestorg.views;

import org.contestorg.common.Triple;
import org.contestorg.controllers.ContestOrg;
import org.contestorg.infos.InfosModelPhaseQualificative;
import org.contestorg.interfaces.ICollector;

/**
 * Collecteur pour la création d'une phase qualificative au sein d'une catégorie
 */
public class CollectorPhaseQualifVideCreer extends CollectorAbstract<Triple<String,String,InfosModelPhaseQualificative>>
{
	/**
	 * @see ICollector#collect(Object)
	 */
	@Override
	public void collect (Triple<String,String,InfosModelPhaseQualificative> infos) {
		// Demander la création de la phase qualificative
		ContestOrg.get().getCtrlPhasesQualificatives().addPhaseQualifVide(infos.getFirst(), infos.getSecond(), infos.getThird());
		
		// Fermer le collector
		this.close();
	}
	
}
