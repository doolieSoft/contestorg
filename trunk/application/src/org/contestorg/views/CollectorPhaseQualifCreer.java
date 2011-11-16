package org.contestorg.views;

import org.contestorg.common.Triple;
import org.contestorg.controlers.ContestOrg;
import org.contestorg.infos.Configuration;
import org.contestorg.infos.InfosModelMatchPhasesQualifs;
import org.contestorg.infos.InfosModelPhaseQualificative;




public class CollectorPhaseQualifCreer extends CollectorAbstract<Triple<Configuration<String>,InfosModelPhaseQualificative,InfosModelMatchPhasesQualifs>>
{
	// Catégorie
	private String nomCategorie;
	
	// Poule
	private String nomPoule;
	
	// Constructeur
	public CollectorPhaseQualifCreer(String nomCategorie,String nomPoule) {
		// Retenir la catégorie et la poule 
		this.nomCategorie = nomCategorie;
		this.nomPoule = nomPoule;
	}

	// Implémentation de accept
	@Override
	public void accept (Triple<Configuration<String>,InfosModelPhaseQualificative,InfosModelMatchPhasesQualifs> infos) {
		// Demander la création de la phase qualificative
		ContestOrg.get().getCtrlPhasesQualificatives().addPhaseQualif(this.nomCategorie, this.nomPoule, infos);
		
		// Fermer le collector
		this.close();
	}
	
}
