package views;

import infos.InfosModelEquipe;
import infos.InfosModelProprieteEquipe;

import common.TrackableList;
import common.Pair;
import common.Quintuple;

import controlers.ContestOrg;

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
