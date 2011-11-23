package org.contestorg.models;

import java.util.ArrayList;

import org.contestorg.common.Pair;
import org.contestorg.common.TrackableList;
import org.contestorg.common.Triple;
import org.contestorg.infos.InfosModelCategorie;
import org.contestorg.infos.InfosModelMatchPhasesElims;
import org.contestorg.infos.InfosModelParticipant;
import org.contestorg.infos.InfosModelParticipation;
import org.contestorg.infos.InfosModelParticipationObjectif;
import org.contestorg.infos.InfosModelPhaseEliminatoires;
import org.contestorg.interfaces.IGraphModel;

public class FrontModelPhasesEliminatoires
{
	// Point d'entrée principal aux modèles
	private FrontModel frontModel;
	
	// Constructeur
	public FrontModelPhasesEliminatoires(FrontModel frontModel) {
		this.frontModel = frontModel;
	}
	
	// Récupérer des données calculées
	public boolean isMatchPhasesElimsResultatsEditables(String nomCategorie, int numeroMatch) {
		// Récupérer le match
		ModelMatchPhasesElims match = this.frontModel.getConcours().getCategorieByNom(nomCategorie).getPhasesEliminatoires().getMatch(numeroMatch);
		
		// Vérifier si le match suivant est joué
		return match.getMatchSuivant() == null || !match.getMatchSuivant().isEffectue() && (!match.getMatchSuivant().isGrandeFinale() || !match.getPhasesEliminatoire().getPetiteFinale().isEffectue());
	}
	
	// Récupérer des données uniques
	public Triple<Triple<String, ArrayList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>, Triple<String, ArrayList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>,InfosModelMatchPhasesElims> getInfosMatchPhaseElims(String nomCategorie, int numeroMatch) {
		// Récupérer le match
		ModelMatchPhasesElims match = this.frontModel.getConcours().getCategorieByNom(nomCategorie).getPhasesEliminatoires().getMatch(numeroMatch);
		
		// Récupérer les informations du match
		Pair<Triple<String, ArrayList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>, Triple<String, ArrayList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>> infos = this.getInfosMatch(match);
		
		// Retourner les informations du match
		return new Triple<Triple<String,ArrayList<Pair<String,InfosModelParticipationObjectif>>,InfosModelParticipation>, Triple<String,ArrayList<Pair<String,InfosModelParticipationObjectif>>,InfosModelParticipation>, InfosModelMatchPhasesElims>(infos.getFirst(), infos.getSecond(), match.toInfos());
	}
	public Triple<Triple<String, ArrayList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>, Triple<String, ArrayList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>,InfosModelMatchPhasesElims> getInfosMatchPetiteFinale(String nomCategorie) {
		// Récupérer le match
		ModelMatchPhasesElims match = this.frontModel.getConcours().getCategorieByNom(nomCategorie).getPhasesEliminatoires().getPetiteFinale();
		
		// Récupérer les informations du match
		Pair<Triple<String, ArrayList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>, Triple<String, ArrayList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>> infos = this.getInfosMatch(match);
		
		// Retourner les informations du match
		return new Triple<Triple<String,ArrayList<Pair<String,InfosModelParticipationObjectif>>,InfosModelParticipation>, Triple<String,ArrayList<Pair<String,InfosModelParticipationObjectif>>,InfosModelParticipation>, InfosModelMatchPhasesElims>(infos.getFirst(), infos.getSecond(), match.toInfos());
	}
	private Pair<Triple<String, ArrayList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>, Triple<String, ArrayList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>> getInfosMatch(ModelMatchAbstract match) {
		// Récupérer les informations de participation du participant A
		String nomParticipantA = match.getParticipationA() == null || match.getParticipationA().getParticipant() == null ? null : match.getParticipationA().getParticipant().getNom();
		ArrayList<Pair<String, InfosModelParticipationObjectif>> objectifsRemportesA = new ArrayList<Pair<String,InfosModelParticipationObjectif>>();
		for(ModelObjectifRemporte objectifRemporte : match.getParticipationA().getObjectifsRemportes()) {
			objectifsRemportesA.add(new Pair<String, InfosModelParticipationObjectif>(objectifRemporte.getObjectif().getNom(), objectifRemporte.toInfos()));
		}
		Triple<String, ArrayList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation> infosParticipationA = new Triple<String, ArrayList<Pair<String,InfosModelParticipationObjectif>>, InfosModelParticipation>(nomParticipantA, objectifsRemportesA, match.getParticipationA().toInfos());
		
		// Récupérer les informations de participation du participant B
		String nomParticipantB = match.getParticipationB() == null || match.getParticipationB().getParticipant() == null ? null : match.getParticipationB().getParticipant().getNom();
		ArrayList<Pair<String, InfosModelParticipationObjectif>> objectifsRemportesB = new ArrayList<Pair<String,InfosModelParticipationObjectif>>();
		for(ModelObjectifRemporte objectifRemporte : match.getParticipationB().getObjectifsRemportes()) {
			objectifsRemportesB.add(new Pair<String, InfosModelParticipationObjectif>(objectifRemporte.getObjectif().getNom(), objectifRemporte.toInfos()));
		}
		Triple<String, ArrayList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation> infosParticipationB = new Triple<String, ArrayList<Pair<String,InfosModelParticipationObjectif>>, InfosModelParticipation>(nomParticipantB, objectifsRemportesB, match.getParticipationB().toInfos());
		
		// Assembler les informations demandées et les retourner
		return new Pair<Triple<String,ArrayList<Pair<String,InfosModelParticipationObjectif>>,InfosModelParticipation>, Triple<String,ArrayList<Pair<String,InfosModelParticipationObjectif>>,InfosModelParticipation>>(infosParticipationA, infosParticipationB);
	}
	
	// Générer/Modifier/Supprimer les phases éliminatoires
	public void genererPhasesElims(String nomCategorie, int nbPhases, InfosModelMatchPhasesElims infosMatchs, InfosModelPhaseEliminatoires infosPhaseEliminatoire) throws ContestOrgModelException {
		// Démarrer l'action de génération
		this.frontModel.getHistory().start("Génération des phases éliminatoires pour la catégorie \""+nomCategorie+"\"");
		
		// Générer les phases éliminatoires
		this.frontModel.getConcours().getCategorieByNom(nomCategorie).genererPhasesEliminatoires(nbPhases, infosMatchs, infosPhaseEliminatoire);
		
		// Fermer l'action de génération
		this.frontModel.getHistory().close();
	}
	public void resetPhasesElims(String nomCategorie) throws ContestOrgModelException {
		// Démarrer l'action de reset
		this.frontModel.getHistory().start("Reset des phases éliminatoires pour la catégorie \""+nomCategorie+"\"");
		
		// Générer les phases éliminatoires
		this.frontModel.getConcours().getCategorieByNom(nomCategorie).getPhasesEliminatoires().delete();
		
		// Fermer l'action de reset
		this.frontModel.getHistory().close();
	}
	public void setParticipantPhasesElims(String nomCategorie, int numeroCellule, String nomParticipant) throws ContestOrgModelException {
		// Démarrer l'action de modification
		this.frontModel.getHistory().start("Modification d'un participant dans les phase éliminatoires de la catégorie \""+nomCategorie+"\"");
		
		// Récupérer la phases éliminatoires
		ModelPhasesEliminatoires phases = this.frontModel.getConcours().getCategorieByNom(nomCategorie).getPhasesEliminatoires();
		
		// Récupérer le match
		ModelMatchPhasesElims match = phases.getMatch((numeroCellule-(numeroCellule%2))/2);
		
		// Récupérer la participation
		ModelParticipation participation = numeroCellule%2 == 0 ? match.getParticipationA() : match.getParticipationB();
		
		// Récupérer le participant
		ModelParticipant participant = this.frontModel.getConcours().getParticipantByNom(nomParticipant);
		
		// Retirer la participation de l'ancien participant
		if(participation.getParticipant() != null) {
			participation.getParticipant().removeParticipation(participation);
		}
		
		// Modifier le participant de la participation
		participation.setParticipant(participant);
		
		// Ajouter la participation au nouveau participant
		participant.addParticipation(participation);
		
		// Fermer l'action de modification
		this.frontModel.getHistory().close();
	}
	public void updateMatchPhasesElims(String nomCategorie, int numeroMatch, Triple<Pair<TrackableList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>, Pair<TrackableList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>, InfosModelMatchPhasesElims> infos) throws ContestOrgModelException {
		// Démarrer l'action de modification
		this.frontModel.getHistory().start("Modification d'un match dans les phase éliminatoires de la catégorie \""+nomCategorie+"\"");
		
		// Récupérer le match
		ModelMatchPhasesElims match = this.frontModel.getConcours().getCategorieByNom(nomCategorie).getPhasesEliminatoires().getMatch(numeroMatch);
		
		// Modifier le match
		new ModelMatchPhasesElims.Updater().update(match, infos);
		
		// Fermer l'action de modification
		this.frontModel.getHistory().close();
	}
	public void updateMatchPetiteFinale(String nomCategorie, Triple<Pair<TrackableList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>, Pair<TrackableList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>, InfosModelMatchPhasesElims> infos) throws ContestOrgModelException {
		// Démarrer l'action de modification
		this.frontModel.getHistory().start("Modification de la petite finale dans les phase éliminatoires de la catégorie \""+nomCategorie+"\"");
		
		// Récupérer le match
		ModelMatchPhasesElims match = this.frontModel.getConcours().getCategorieByNom(nomCategorie).getPhasesEliminatoires().getPetiteFinale();
		
		// Modifier le match
		new ModelMatchPhasesElims.Updater().update(match, infos);
		
		// Fermer l'action de modification
		this.frontModel.getHistory().close();
	}
	
	// Récupérer des GrapheModels
	public IGraphModel<InfosModelCategorie,InfosModelParticipant> getGraphModelPhasesElimsGrandeFinale(String nomCategorie) {
		return new GraphModelPhasesElimsGrandeFinale(this.frontModel.getConcours().getCategorieByNom(nomCategorie));
	}
	public IGraphModel<InfosModelCategorie,InfosModelParticipant> getGraphModelPhasesElimsPetiteFinale(String nomCategorie) {
		return new GraphModelPhasesElimsPetiteFinale(this.frontModel.getConcours().getCategorieByNom(nomCategorie));
	}

}
