package org.contestorg.controlers;


import java.util.ArrayList;

import javax.swing.tree.TreeModel;

import org.contestorg.common.Pair;
import org.contestorg.common.TrackableList;
import org.contestorg.common.Triple;
import org.contestorg.infos.InfosModelCategorie;
import org.contestorg.infos.InfosModelParticipant;
import org.contestorg.infos.InfosModelMatchPhasesElims;
import org.contestorg.infos.InfosModelParticipation;
import org.contestorg.infos.InfosModelParticipationObjectif;
import org.contestorg.infos.InfosModelPhaseEliminatoires;
import org.contestorg.interfaces.IGraphModel;
import org.contestorg.models.FrontModel;



/**
 * Controleur des phases finales
 */
public class CtrlPhasesEliminatoires
{
	// ==== Récupérer des données
	
	// Récupérer le nombre de phases éliminatoires générées pour une catégorie
	public int getNbPhasesElims(String nomCategorie) {
		return FrontModel.get().getFrontModelPhasesQualificatives().getNbPhasesElims(nomCategorie);
	}
	
	// Récupérer le nombre de phases éliminatoires possibles pour une catégorie
	public int getNbPhasesElimsPossibles(String nomCategorie) {
		return FrontModel.get().getFrontModelPhasesQualificatives().getNbPhasesElimsPossibles(nomCategorie);
	}
	
	// Récupérer la liste des participants dans une catégorie
	public ArrayList<String> getListeParticipants(String nomCategorie) {
		return FrontModel.get().getFrontModelParticipants().getListeParticipantsParticipants(nomCategorie);
	}
	
	// Récupérer les informations sur un match de phase éliminatoire
	public Triple<Triple<String, ArrayList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>, Triple<String, ArrayList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>, InfosModelMatchPhasesElims> getInfosMatchPhasesElims(String nomCategorie, int numeroMatch) {
		return FrontModel.get().getFrontModelPhasesEliminatoires().getInfosMatchPhaseElims(nomCategorie, numeroMatch);
	}
	public Triple<Triple<String, ArrayList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>, Triple<String, ArrayList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>, InfosModelMatchPhasesElims> getInfosMatchPetiteFinale(String nomCategorie) {
		return FrontModel.get().getFrontModelPhasesEliminatoires().getInfosMatchPetiteFinale(nomCategorie);
	}
	
	// Savoir si les résultats d'un match sont éditables
	public boolean isMatchPhasesElimsResultatsEditables(String nomCategorie, int numeroMatch) {
		return FrontModel.get().getFrontModelPhasesEliminatoires().isMatchPhasesElimsResultatsEditables(nomCategorie, numeroMatch);
	}

	// ==== Modifier des données
	
	// Générer/Modifier/Supprimer les phases éliminatoires
	public void genererPhasesElims(String nomCategorie, int nbPhases, InfosModelMatchPhasesElims infosMatchs, InfosModelPhaseEliminatoires infosPhaseEliminatoire) {
		try {
			FrontModel.get().getFrontModelPhasesEliminatoires().genererPhasesElims(nomCategorie, nbPhases, infosMatchs, infosPhaseEliminatoire);
		} catch (Exception e) {
			ContestOrg.get().error("Erreur lors de la génération des phases éliminatoires", e);
		}
	}
	public void resetPhasesElims(String nomCategorie) {
		try {
			FrontModel.get().getFrontModelPhasesEliminatoires().resetPhasesElims(nomCategorie);
		} catch (Exception e) {
			ContestOrg.get().error("Erreur lors du reset des phases éliminatoires", e);
		}
	}
	public void setParticipantPhasesElims(String nomCategorie, int numeroCellule, String nomParticipant) {
		try {
			FrontModel.get().getFrontModelPhasesEliminatoires().setParticipantPhasesElims(nomCategorie, numeroCellule, nomParticipant);
		} catch (Exception e) {
			ContestOrg.get().error("Erreur lors du changement d'un participant dans les phases éliminatoires", e);
		}
	}
	public void updateMatchPhasesElims(String nomCategorie, int numeroMatch, Triple<Pair<TrackableList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>, Pair<TrackableList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>, InfosModelMatchPhasesElims> infos) {
		try {
			FrontModel.get().getFrontModelPhasesEliminatoires().updateMatchPhasesElims(nomCategorie, numeroMatch, infos);
		} catch (Exception e) {
			ContestOrg.get().error("Erreur lors du changement d'un match dans les phases éliminatoires", e);
		}
	}
	public void updateMatchPetiteFinale(String nomCategorie, Triple<Pair<TrackableList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>, Pair<TrackableList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>, InfosModelMatchPhasesElims> infos) {
		try {
			FrontModel.get().getFrontModelPhasesEliminatoires().updateMatchPetiteFinale(nomCategorie, infos);
		} catch (Exception e) {
			ContestOrg.get().error("Erreur lors du changement de la petite finale dans les phases éliminatoires", e);
		}
	}

	// ==== TreeModel et GraphModels

	// Récupérer le TreeModel des catégories
	public TreeModel getTreeModelCategories() {
		return FrontModel.get().getFrontModelParticipants().getTreeModelCategories();
	}
	
	// Récupérer le GraphModel de la grande et petite finale pour une catégorie
	public IGraphModel<InfosModelCategorie,InfosModelParticipant> getGraphModelPhasesElimsGrandeFinale(String nomCategorie) {
		return FrontModel.get().getFrontModelPhasesEliminatoires().getGraphModelPhasesElimsGrandeFinale(nomCategorie);
	}
	public IGraphModel<InfosModelCategorie,InfosModelParticipant> getGraphModelPhasesElimsPetiteFinale(String nomCategorie) {
		return FrontModel.get().getFrontModelPhasesEliminatoires().getGraphModelPhasesElimsPetiteFinale(nomCategorie);
	}
}
