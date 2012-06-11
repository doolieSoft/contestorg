package org.contestorg.controllers;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.tree.TreeModel;

import org.contestorg.common.Pair;
import org.contestorg.common.Quadruple;
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
import org.contestorg.models.FrontModel;



/**
 * Controleur des phases finales
 */
public class CtrlPhasesEliminatoires
{
	// ==== Récupérer des données
	
	/**
	 * Récupérer le nombre de phases éliminatoires générées d'une catégorie donnée
	 * @param nomCategorie nom de la catégorie
	 * @return nombre de phases éliminatoires générées pour la catégorie
	 */
	public int getNbPhasesElims(String nomCategorie) {
		return FrontModel.get().getFrontModelPhasesQualificatives().getNbPhasesElims(nomCategorie);
	}
	
	/**
	 * Récupérer le nombre de phases éliminatoires possibles d'une catégorie donnée
	 * @param nomCategorie nom de la catégorie
	 * @return nombre de phases éliminatoires possible pour la catégorie
	 */
	public int getNbPhasesElimsPossibles(String nomCategorie) {
		return FrontModel.get().getFrontModelPhasesQualificatives().getNbPhasesElimsPossibles(nomCategorie);
	}
	
	/**
	 * Récupérer la liste des participants d'une catégorie donnée pouvant participer
	 * @param nomCategorie nom de la catégorie
	 * @return liste des participants de la catégorie pouvant participer
	 */
	public ArrayList<String> getListeParticipants(String nomCategorie) {
		return FrontModel.get().getFrontModelParticipants().getListeParticipantsParticipants(nomCategorie);
	}
	
	/**
	 * Récupérer les informations sur un match d'une catégorie donnée
	 * @param nomCategorie nom de la catégorie
	 * @param numeroMatch numéro de match
	 * @return informations sur le match de la catégorie
	 */
	public Quadruple<Triple<String, ArrayList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>, Triple<String, ArrayList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>, Pair<String,String>, InfosModelMatchPhasesElims> getInfosMatch(String nomCategorie, int numeroMatch) {
		return FrontModel.get().getFrontModelPhasesEliminatoires().getInfosMatch(nomCategorie, numeroMatch);
	}
	
	/**
	 * Récupérer les informations sur la petite finale d'une catégorie donnée
	 * @param nomCategorie nom de la catégorie
	 * @return informations sur la petite finale de la catégorie
	 */
	public Quadruple<Triple<String, ArrayList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>, Triple<String, ArrayList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>, Pair<String,String>, InfosModelMatchPhasesElims> getInfosMatchPetiteFinale(String nomCategorie) {
		return FrontModel.get().getFrontModelPhasesEliminatoires().getInfosMatchPetiteFinale(nomCategorie);
	}
	
	/**
	 * Savoir si les résultats d'un match sont éditables
	 * @param nomCategorie nom de la catégorie
	 * @param numeroMatch numéro de match dans la catégorie
	 * @return résultats du match éditables ?
	 */
	public boolean isMatchResultatsEditables(String nomCategorie, int numeroMatch) {
		return FrontModel.get().getFrontModelPhasesEliminatoires().isMatchResultatsEditables(nomCategorie, numeroMatch);
	}
	
	/**
	 * Vérifier si des participants sont ex-aequo à la qualification aux phases éliminatoires
	 * @param categorie catégorie
	 * @param nbPhases nombre de phases éliminatoires à générer
	 * @return participants ex-aequo à la qualification au phases éliminatoires
	 */
	public Map<InfosModelPoule,List<InfosModelParticipant>> verifierExAequo(String nomCategorie, int nbPhases) {
		return FrontModel.get().getFrontModelPhasesEliminatoires().verifierExAequo(nomCategorie, nbPhases);
	}

	// ==== Modifier des données
	
	/**
	 * Générer les phases éliminatoires d'une catégorie donnée
	 * @param nomCategorie nom de la catégorie
	 * @param nbPhases nombre de phases éliminatoires
	 * @param infosMatchs informations d'initialisation des matchs
	 * @param infosPhaseEliminatoire informations de la phase éliminatoire
	 */
	public void genererPhasesElims(String nomCategorie, int nbPhases, InfosModelMatchPhasesElims infosMatchs, InfosModelPhasesEliminatoires infosPhaseEliminatoire) {
		try {
			FrontModel.get().getFrontModelPhasesEliminatoires().genererPhasesElims(nomCategorie, nbPhases, infosMatchs, infosPhaseEliminatoire);
		} catch (Exception e) {
			ContestOrg.get().error("Erreur lors de la génération des phases éliminatoires", e);
		}
	}
	
	/**
	 * Remettre à zéro les phases éliminatoires d'une catégorie donnée
	 * @param nomCategorie nom de la catégorie
	 */
	public void resetPhasesElims(String nomCategorie) {
		try {
			FrontModel.get().getFrontModelPhasesEliminatoires().resetPhasesElims(nomCategorie);
		} catch (Exception e) {
			ContestOrg.get().error("Erreur lors du reset des phases éliminatoires", e);
		}
	}
	
	/**
	 * Définir le participant d'une cellule donnée d'une catégorie donnée
	 * @param nomCategorie nom de la catégorie
	 * @param numeroCellule numéro de la cellule
	 * @param nomParticipant nom du participant
	 */
	public void setParticipant(String nomCategorie, int numeroCellule, String nomParticipant) {
		try {
			FrontModel.get().getFrontModelPhasesEliminatoires().setParticipant(nomCategorie, numeroCellule, nomParticipant);
		} catch (Exception e) {
			ContestOrg.get().error("Erreur lors du changement d'un participant dans les phases éliminatoires", e);
		}
	}
	
	/**
	 * Mettre à jour les informations d'un match donnée d'une catégorie donnée
	 * @param nomCategorie nom de la catégorie
	 * @param numeroMatch numéro du match
	 * @param infos nouvelles informations du match
	 */
	public void updateMatch(String nomCategorie, int numeroMatch, Quadruple<Pair<TrackableList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>, Pair<TrackableList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>, Pair<String, String>, InfosModelMatchPhasesElims> infos) {
		try {
			FrontModel.get().getFrontModelPhasesEliminatoires().updateMatch(nomCategorie, numeroMatch, infos);
		} catch (Exception e) {
			ContestOrg.get().error("Erreur lors du changement d'un match dans les phases éliminatoires", e);
		}
	}
	
	/**
	 * Mettre à jour les informations de la petite finale d'une catégorie donnée
	 * @param nomCategorie nom de la catégorie
	 * @param infos nouvelles informations de la petite finale
	 */
	public void updateMatchPetiteFinale(String nomCategorie, Quadruple<Pair<TrackableList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>, Pair<TrackableList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>, Pair<String, String>, InfosModelMatchPhasesElims> infos) {
		try {
			FrontModel.get().getFrontModelPhasesEliminatoires().updateMatchPetiteFinale(nomCategorie, infos);
		} catch (Exception e) {
			ContestOrg.get().error("Erreur lors du changement de la petite finale dans les phases éliminatoires", e);
		}
	}

	// ==== TreeModel et GraphModels

	// TreeModels
	
	/**
	 * Récupérer le TreeModel des catégories
	 * @return TreeModel des catégories
	 */
	public TreeModel getTreeModelCategories() {
		return FrontModel.get().getFrontModelParticipants().getTreeModelCategories();
	}
	
	// GraphModels
	
	/**
	 * Récupérer le GraphModel de la grande finale d'une catégorie donnée
	 * @param nomCategorie nom de la catégorie
	 * @return GraphModel de la grande finale de la catégorie
	 */
	public IGraphModel<InfosModelCategorie,InfosModelParticipant> getGraphModelGrandeFinale(String nomCategorie) {
		return FrontModel.get().getFrontModelPhasesEliminatoires().getGraphModelGrandeFinale(nomCategorie);
	}
	
	/**
	 * Récupérer le GraphModel de la petite finale d'une catégorie donnée
	 * @param nomCategorie nom de la catégorie
	 * @return GraphModel de la petite finale de la catégorie
	 */
	public IGraphModel<InfosModelCategorie,InfosModelParticipant> getGraphModelPetiteFinale(String nomCategorie) {
		return FrontModel.get().getFrontModelPhasesEliminatoires().getGraphModelPetiteFinale(nomCategorie);
	}
}
