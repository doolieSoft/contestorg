package org.contestorg.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.contestorg.common.ContestOrgErrorException;
import org.contestorg.common.Pair;
import org.contestorg.common.TrackableList;
import org.contestorg.common.Triple;
import org.contestorg.infos.InfosModelCategorie;
import org.contestorg.infos.InfosModelMatchPhasesElims;
import org.contestorg.infos.InfosModelObjectifRemporte;
import org.contestorg.infos.InfosModelParticipant;
import org.contestorg.infos.InfosModelParticipation;
import org.contestorg.infos.InfosModelPhasesEliminatoires;
import org.contestorg.infos.InfosModelPoule;
import org.contestorg.interfaces.IGraphModel;

/**
 * Point d'entrée aux modèles concernant les phases éliminatoires
 */
public class FrontModelPhasesEliminatoires
{
	/** Point d'entrée principal aux modèles */
	private FrontModel frontModel;
	
	/**
	 * Constructeur
	 * @param frontModel point d'entrée principal aux modèles
	 */
	public FrontModelPhasesEliminatoires(FrontModel frontModel) {
		this.frontModel = frontModel;
	}
	
	// Récupérer des données calculées
	
	/**
	 * Savoir si les résultats d'un match sont éditables
	 * @param nomCategorie nom de la catégorie
	 * @param numeroMatch numéro de match
	 * @return résultats du match éditables ?
	 */
	public boolean isMatchResultatsEditables(String nomCategorie, int numeroMatch) {
		// Récupérer le match
		ModelMatchPhasesElims match = this.frontModel.getConcours().getCategorieByNom(nomCategorie).getPhasesEliminatoires().getMatch(numeroMatch);
		
		// Vérifier si le match suivant est joué
		return match.getMatchSuivant() == null || !match.getMatchSuivant().isEffectue() && (!match.getMatchSuivant().isGrandeFinale() || !match.getPhasesEliminatoire().getPetiteFinale().isEffectue());
	}
	
	// Récupérer des données uniques
	
	/**
	 * Récupérer les informations sur un match d'une catégorie donnée
	 * @param nomCategorie nom de la catégorie
	 * @param numeroMatch numéro de match
	 * @return informations sur le match de la catégorie
	 */
	public Triple<Triple<String, ArrayList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>, Triple<String, ArrayList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>,InfosModelMatchPhasesElims> getInfosMatch(String nomCategorie, int numeroMatch) {
		// Récupérer le match
		ModelMatchPhasesElims match = this.frontModel.getConcours().getCategorieByNom(nomCategorie).getPhasesEliminatoires().getMatch(numeroMatch);
		
		// Récupérer les informations du match
		Pair<Triple<String, ArrayList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>, Triple<String, ArrayList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>> infos = this.getInfosMatch(match);
		
		// Retourner les informations du match
		return new Triple<Triple<String,ArrayList<Pair<String,InfosModelObjectifRemporte>>,InfosModelParticipation>, Triple<String,ArrayList<Pair<String,InfosModelObjectifRemporte>>,InfosModelParticipation>, InfosModelMatchPhasesElims>(infos.getFirst(), infos.getSecond(), match.getInfos());
	}
	
	/**
	 * Récupérer les informations sur la petite finale d'une catégorie donnée
	 * @param nomCategorie nom de la catégorie
	 * @return informations sur la petite finale de la catégorie
	 */
	public Triple<Triple<String, ArrayList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>, Triple<String, ArrayList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>,InfosModelMatchPhasesElims> getInfosMatchPetiteFinale(String nomCategorie) {
		// Récupérer le match
		ModelMatchPhasesElims match = this.frontModel.getConcours().getCategorieByNom(nomCategorie).getPhasesEliminatoires().getPetiteFinale();
		
		// Récupérer les informations du match
		Pair<Triple<String, ArrayList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>, Triple<String, ArrayList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>> infos = this.getInfosMatch(match);
		
		// Retourner les informations du match
		return new Triple<Triple<String,ArrayList<Pair<String,InfosModelObjectifRemporte>>,InfosModelParticipation>, Triple<String,ArrayList<Pair<String,InfosModelObjectifRemporte>>,InfosModelParticipation>, InfosModelMatchPhasesElims>(infos.getFirst(), infos.getSecond(), match.getInfos());
	}
	
	/**
	 * Récupérer les informations sur un match d'une catégorie donnée
	 * @param nomCategorie nom de la catégorie
	 * @param numeroMatch numéro de match
	 * @return informations sur le match de la catégorie
	 */
	private Pair<Triple<String, ArrayList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>, Triple<String, ArrayList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>> getInfosMatch(ModelMatchAbstract match) {
		// Récupérer les informations de participation du participant A
		String nomParticipantA = match.getParticipationA() == null || match.getParticipationA().getParticipant() == null ? null : match.getParticipationA().getParticipant().getNom();
		ArrayList<Pair<String, InfosModelObjectifRemporte>> objectifsRemportesA = new ArrayList<Pair<String,InfosModelObjectifRemporte>>();
		for(ModelObjectifRemporte objectifRemporte : match.getParticipationA().getObjectifsRemportes()) {
			objectifsRemportesA.add(new Pair<String, InfosModelObjectifRemporte>(objectifRemporte.getObjectif().getNom(), objectifRemporte.getInfos()));
		}
		Triple<String, ArrayList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation> infosParticipationA = new Triple<String, ArrayList<Pair<String,InfosModelObjectifRemporte>>, InfosModelParticipation>(nomParticipantA, objectifsRemportesA, match.getParticipationA().getInfos());
		
		// Récupérer les informations de participation du participant B
		String nomParticipantB = match.getParticipationB() == null || match.getParticipationB().getParticipant() == null ? null : match.getParticipationB().getParticipant().getNom();
		ArrayList<Pair<String, InfosModelObjectifRemporte>> objectifsRemportesB = new ArrayList<Pair<String,InfosModelObjectifRemporte>>();
		for(ModelObjectifRemporte objectifRemporte : match.getParticipationB().getObjectifsRemportes()) {
			objectifsRemportesB.add(new Pair<String, InfosModelObjectifRemporte>(objectifRemporte.getObjectif().getNom(), objectifRemporte.getInfos()));
		}
		Triple<String, ArrayList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation> infosParticipationB = new Triple<String, ArrayList<Pair<String,InfosModelObjectifRemporte>>, InfosModelParticipation>(nomParticipantB, objectifsRemportesB, match.getParticipationB().getInfos());
		
		// Assembler les informations demandées et les retourner
		return new Pair<Triple<String,ArrayList<Pair<String,InfosModelObjectifRemporte>>,InfosModelParticipation>, Triple<String,ArrayList<Pair<String,InfosModelObjectifRemporte>>,InfosModelParticipation>>(infosParticipationA, infosParticipationB);
	}
	
	// Générer/Modifier/Supprimer les phases éliminatoires
	
	/**
	 * Générer les phases éliminatoires d'une catégorie donnée
	 * @param nomCategorie nom de la catégorie
	 * @param nbPhases nombre de phases éliminatoires
	 * @param infosMatchs informations d'initialisation des matchs
	 * @param infosPhaseEliminatoire informations de la phase éliminatoire
	 * @throws ContestOrgErrorException
	 */
	public void genererPhasesElims(String nomCategorie, int nbPhases, InfosModelMatchPhasesElims infosMatchs, InfosModelPhasesEliminatoires infosPhaseEliminatoire) throws ContestOrgErrorException {
		// Démarrer l'action de génération
		this.frontModel.getHistory().start("Génération des phases éliminatoires pour la catégorie \""+nomCategorie+"\"");
		
		// Générer les phases éliminatoires
		this.frontModel.getConcours().getCategorieByNom(nomCategorie).genererPhasesEliminatoires(nbPhases, infosMatchs, infosPhaseEliminatoire);
		
		// Fermer l'action de génération
		this.frontModel.getHistory().close();
	}
	
	/**
	 * Vérifier si des participants sont ex-aequo à la qualification aux phases éliminatoires
	 * @param categorie catégorie
	 * @param nbPhases nombre de phases éliminatoires à générer
	 * @return participants ex-aequo à la qualification au phases éliminatoires
	 */
	public Map<InfosModelPoule,List<InfosModelParticipant>> verifierExAequo(String nomCategorie, int nbPhases) {
		Map<InfosModelPoule,List<InfosModelParticipant>> participantsExAequo = new HashMap<InfosModelPoule,List<InfosModelParticipant>>();
		for(Entry<ModelPoule, List<ModelParticipant>> participantsExAequoPoule : this.frontModel.getConcours().getCategorieByNom(nomCategorie).verifierExAequo(nbPhases).entrySet()) {
			List<InfosModelParticipant> participants = new ArrayList<InfosModelParticipant>();
			for(ModelParticipant participant : participantsExAequoPoule.getValue()) {
				participants.add(participant.getInfos());
			}
			participantsExAequo.put(participantsExAequoPoule.getKey().getInfos(), participants);
		}
		return participantsExAequo;
	}
	
	/**
	 * Remettre à zéro les phases éliminatoires d'une catégorie donnée
	 * @param nomCategorie nom de la catégorie
	 * @throws ContestOrgErrorException
	 */
	public void resetPhasesElims(String nomCategorie) throws ContestOrgErrorException {
		// Démarrer l'action de reset
		this.frontModel.getHistory().start("Reset des phases éliminatoires pour la catégorie \""+nomCategorie+"\"");
		
		// Générer les phases éliminatoires
		this.frontModel.getConcours().getCategorieByNom(nomCategorie).getPhasesEliminatoires().delete();
		
		// Fermer l'action de reset
		this.frontModel.getHistory().close();
	}
	
	/**
	 * Définir le participant d'une cellule donnée d'une catégorie donnée
	 * @param nomCategorie nom de la catégorie
	 * @param numeroCellule numéro de la cellule
	 * @param nomParticipant nom du participant
	 * @throws ContestOrgErrorException
	 */
	public void setParticipant(String nomCategorie, int numeroCellule, String nomParticipant) throws ContestOrgErrorException {
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
	
	/**
	 * Mettre à jour les informations d'un match donnée d'une catégorie donnée
	 * @param nomCategorie nom de la catégorie
	 * @param numeroMatch numéro du match
	 * @param infos nouvelles informations du match
	 * @throws ContestOrgErrorException
	 */
	public void updateMatch(String nomCategorie, int numeroMatch, Triple<Pair<TrackableList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>, Pair<TrackableList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>, InfosModelMatchPhasesElims> infos) throws ContestOrgErrorException {
		// Démarrer l'action de modification
		this.frontModel.getHistory().start("Modification d'un match dans les phase éliminatoires de la catégorie \""+nomCategorie+"\"");
		
		// Récupérer le match
		ModelMatchPhasesElims match = this.frontModel.getConcours().getCategorieByNom(nomCategorie).getPhasesEliminatoires().getMatch(numeroMatch);
		
		// Modifier le match
		new ModelMatchPhasesElims.Updater().update(match, infos);
		
		// Fermer l'action de modification
		this.frontModel.getHistory().close();
	}
	
	/**
	 * Mettre à jour les informations de la petite finale d'une catégorie donnée
	 * @param nomCategorie nom de la catégorie
	 * @param infos nouvelles informations de la petite finale
	 * @throws ContestOrgErrorException
	 */
	public void updateMatchPetiteFinale(String nomCategorie, Triple<Pair<TrackableList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>, Pair<TrackableList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>, InfosModelMatchPhasesElims> infos) throws ContestOrgErrorException {
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
	
	/**
	 * Récupérer le GraphModel de la grande finale d'une catégorie donnée
	 * @param nomCategorie nom de la catégorie
	 * @return GraphModel de la grande finale de la catégorie
	 */
	public IGraphModel<InfosModelCategorie,InfosModelParticipant> getGraphModelGrandeFinale(String nomCategorie) {
		return new GraphModelPhasesElimsGrandeFinale(this.frontModel.getConcours().getCategorieByNom(nomCategorie));
	}
	
	/**
	 * Récupérer le GraphModel de la petite finale d'une catégorie donnée
	 * @param nomCategorie nom de la catégorie
	 * @return GraphModel de la petite finale de la catégorie
	 */
	public IGraphModel<InfosModelCategorie,InfosModelParticipant> getGraphModelPetiteFinale(String nomCategorie) {
		return new GraphModelPhasesElimsPetiteFinale(this.frontModel.getConcours().getCategorieByNom(nomCategorie));
	}

}
