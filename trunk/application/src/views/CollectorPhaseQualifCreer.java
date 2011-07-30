package views;

import infos.Configuration;
import infos.InfosModelMatchPhasesQualifs;
import infos.InfosModelPhaseQualificative;

import common.Triple;

import controlers.ContestOrg;

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
