package views;

import infos.Configuration;
import infos.InfosModelMatchPhasesQualifs;
import infos.InfosModelPhaseQualificative;

import common.Triple;

import controlers.ContestOrg;


public class CollectorPhaseQualifEditer extends CollectorAbstract<Triple<Configuration<String>,InfosModelPhaseQualificative,InfosModelMatchPhasesQualifs>>
{
	// Catégorie
	private String nomCategorie;
	
	// Poule
	private String nomPoule;
	
	// Phase qualificative
	@SuppressWarnings("unused")
	private int numeroPhase;
	
	// Constructeur
	public CollectorPhaseQualifEditer(String nomCategorie,String nomPoule,int numeroPhase) {
		// Retenir la catégorie, la poule et la phase qualificative 
		this.nomCategorie = nomCategorie;
		this.nomPoule = nomPoule;
		this.numeroPhase = numeroPhase;
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
