package org.contestorg.views;

import org.contestorg.common.Pair;
import org.contestorg.common.Quintuple;
import org.contestorg.common.TrackableList;
import org.contestorg.controlers.ContestOrg;
import org.contestorg.infos.InfosModelEquipe;
import org.contestorg.infos.InfosModelProprieteEquipe;




public class CollectorEquipeEditer extends CollectorAbstract<Quintuple<String,String,InfosModelEquipe, TrackableList<Pair<String, InfosModelProprieteEquipe>>, TrackableList<String>>>
{
	
	// Nom de l'équipe éditée
	private String nomEquipe;
	
	// Constructeur
	public CollectorEquipeEditer(String nomEquipe) {
		// Retenir le nom de l'équipe
		this.nomEquipe = nomEquipe;
	}

	// Implémentation de accept
	@Override
	public void accept (Quintuple<String, String, InfosModelEquipe, TrackableList<Pair<String, InfosModelProprieteEquipe>>, TrackableList<String>> infos) {
		// Demander la modification de l'équipe
		ContestOrg.get().getCtrlEquipes().updateEquipe(this.nomEquipe, infos);
		
		// Mettre le nom de l'équipe à nul
		this.nomEquipe = null;
		
		// Fermer le collector
		this.close();
	}
	
}
