package org.contestorg.controlers;


import java.util.ArrayList;

import javax.swing.tree.TreeModel;

import org.contestorg.common.Pair;
import org.contestorg.common.TrackableList;
import org.contestorg.common.Triple;
import org.contestorg.infos.InfosModelCategorie;
import org.contestorg.infos.InfosModelEquipe;
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
		return FrontModel.get().getNbPhasesElims(nomCategorie);
	}
	
	// Récupérer le nombre de phases éliminatoires possibles pour une catégorie
	public int getNbPhasesElimsPossibles(String nomCategorie) {
		return FrontModel.get().getNbPhasesElimsPossibles(nomCategorie);
	}
	
	// Récupérer la liste des équipes participantes dans une catégorie
	public ArrayList<String> getListeEquipesParticipantes(String nomCategorie) {
		return FrontModel.get().getListeEquipesParticipantes(nomCategorie);
	}
	
	// Récupérer les informations sur un match de phase éliminatoire
	public Triple<Triple<String, ArrayList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>, Triple<String, ArrayList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>, InfosModelMatchPhasesElims> getInfosMatchPhasesElims(String nomCategorie, int numeroMatch) {
		return FrontModel.get().getInfosMatchPhaseElims(nomCategorie, numeroMatch);
	}
	public Triple<Triple<String, ArrayList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>, Triple<String, ArrayList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>, InfosModelMatchPhasesElims> getInfosMatchPetiteFinale(String nomCategorie) {
		return FrontModel.get().getInfosMatchPetiteFinale(nomCategorie);
	}
	
	// Savoir si les résultats d'un match sont éditables
	public boolean isMatchPhasesElimsResultatsEditables(String nomCategorie, int numeroMatch) {
		return FrontModel.get().isMatchPhasesElimsResultatsEditables(nomCategorie, numeroMatch);
	}

	// ==== Modifier des données
	
	// Générer/Modifier/Supprimer les phases éliminatoires
	public void genererPhasesElims(String nomCategorie, int nbPhases, InfosModelMatchPhasesElims infosMatchs, InfosModelPhaseEliminatoires infosPhaseEliminatoire) {
		try {
			FrontModel.get().genererPhasesElims(nomCategorie, nbPhases, infosMatchs, infosPhaseEliminatoire);
		} catch (Exception e) {
			ContestOrg.get().error("Erreur lors de la génération des phases éliminatoires", e);
		}
	}
	public void resetPhasesElims(String nomCategorie) {
		try {
			FrontModel.get().resetPhasesElims(nomCategorie);
		} catch (Exception e) {
			ContestOrg.get().error("Erreur lors du reset des phases éliminatoires", e);
		}
	}
	public void setEquipePhasesElims(String nomCategorie, int numeroCellule, String nomEquipe) {
		try {
			FrontModel.get().setEquipePhasesElims(nomCategorie, numeroCellule, nomEquipe);
		} catch (Exception e) {
			ContestOrg.get().error("Erreur lors du changement d'une équipe dans les phases éliminatoires", e);
		}
	}
	public void updateMatchPhasesElims(String nomCategorie, int numeroMatch, Triple<Pair<TrackableList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>, Pair<TrackableList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>, InfosModelMatchPhasesElims> infos) {
		try {
			FrontModel.get().updateMatchPhasesElims(nomCategorie, numeroMatch, infos);
		} catch (Exception e) {
			ContestOrg.get().error("Erreur lors du changement d'un match dans les phases éliminatoires", e);
		}
	}
	public void updateMatchPetiteFinale(String nomCategorie, Triple<Pair<TrackableList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>, Pair<TrackableList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>, InfosModelMatchPhasesElims> infos) {
		try {
			FrontModel.get().updateMatchPetiteFinale(nomCategorie, infos);
		} catch (Exception e) {
			ContestOrg.get().error("Erreur lors du changement de la petite finale dans les phases éliminatoires", e);
		}
	}

	// ==== TreeModel et GraphModels

	// Récupérer le TreeModel des catégories
	public TreeModel getTreeModelCategories() {
		return FrontModel.get().getTreeModelCategories();
	}
	
	// Récupérer le GraphModel de la grande et petite finale pour une catégorie
	public IGraphModel<InfosModelCategorie,InfosModelEquipe> getGraphModelPhasesElimsGrandeFinale(String nomCategorie) {
		return FrontModel.get().getGraphModelPhasesElimsGrandeFinale(nomCategorie);
	}
	public IGraphModel<InfosModelCategorie,InfosModelEquipe> getGraphModelPhasesElimsPetiteFinale(String nomCategorie) {
		return FrontModel.get().getGraphModelPhasesElimsPetiteFinale(nomCategorie);
	}
}
