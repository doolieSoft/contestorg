package org.contestorg.controlers;


import java.util.ArrayList;

import javax.swing.tree.TreeModel;

import org.contestorg.common.Pair;
import org.contestorg.common.Quadruple;
import org.contestorg.common.TrackableList;
import org.contestorg.common.Triple;
import org.contestorg.infos.Configuration;
import org.contestorg.infos.InfosModelCategorie;
import org.contestorg.infos.InfosModelMatchPhasesQualifs;
import org.contestorg.infos.InfosModelObjectif;
import org.contestorg.infos.InfosModelParticipation;
import org.contestorg.infos.InfosModelParticipationObjectif;
import org.contestorg.infos.InfosModelPhaseQualificative;
import org.contestorg.infos.InfosModelPoule;
import org.contestorg.interfaces.IClosableTableModel;
import org.contestorg.interfaces.IGeneration;
import org.contestorg.models.FrontModel;



/**
 * Controleur des phases qualificatives
 */
public class CtrlPhasesQualificatives
{
	// ==== Récupérer des données
	
	// Récupérer les données manquantes (catégorie/poule/phase) à partir d'un numéro de match
	public Quadruple<String,String,Integer,Integer> getCategoriePoulePhase(String nomCategorie,String nomPoule,Integer numeroPhase,int numeroMatch) {
		return FrontModel.get().getCategoriePoulePhase(nomCategorie, nomPoule, numeroPhase, numeroMatch);
	}

	// Récupérer la structure catégories/poules/phases
	public ArrayList<Pair<InfosModelCategorie, ArrayList<Pair<InfosModelPoule,ArrayList<InfosModelPhaseQualificative>>>>> getListeCategoriesPoulesPhases() {
		return FrontModel.get().getListeCategoriesPoulesPhases();
	}
	
	// Récupérer des données sur une phase qualificative
	public Triple<Configuration<String>,InfosModelPhaseQualificative,InfosModelMatchPhasesQualifs> getInfosPhaseQualif(String nomCategorie,String nomPoule,int numeroPhase) {
		return FrontModel.get().getInfosPhaseQualif(nomCategorie, nomPoule, numeroPhase);
	}
				
	// Récupérer des données utiles à la génération d'une phase qualificative
	public int getNbVillesCommunes(Configuration<String> configuration) {
		return FrontModel.get().getNbVillesCommunes(configuration);
	}
	public int getNbMatchDejaJoues(Configuration<String> configuration) {
		return FrontModel.get().getNbMatchDejaJoues(configuration);
	}
	public int getNbRencontres(String nomParticipantA, String nomParticipantB) {
		return FrontModel.get().getNbRencontres(nomParticipantA, nomParticipantB);
	}
	public int getRang(String nomParticipant) {
		return FrontModel.get().getRangParticipantPhasesQualificatives(nomParticipant);
	}
	public ArrayList<String> getListeParticipants(String nomCategorie,String nomPoule) {
		return FrontModel.get().getListeParticipants(nomCategorie, nomPoule);
	}
		
	// Récupérer les informations sur un match
	public Triple<Triple<String, ArrayList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>, Triple<String, ArrayList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>,InfosModelMatchPhasesQualifs> getInfosMatchPhaseQualif(String nomCategorie,String nomPoule,int numeroPhase,int numeroMatch) {
		return FrontModel.get().getInfosMatchPhaseQualif(nomCategorie,nomPoule,numeroPhase,numeroMatch);
	}
	
	// Récupérer des données utile à la création de matchs
	public ArrayList<InfosModelObjectif> getListeObjectifs() {
		return FrontModel.get().getListeObjectifs();
	}
	public boolean isParticipantPhaseQualif(String nomCategorie, String nomPoule, int numeroPhase, String nomParticipant) {
		return FrontModel.get().isParticipantPhasesQualif(nomCategorie, nomPoule, numeroPhase, nomParticipant);
	}
	
	// ==== Modifier des données
	
	// Ajouter/Modifier/Supprimer une phase qualificative
	public IGeneration<Configuration<String>> getGenerationAvance(String nomCategorie, String nomPoule, ArrayList<String> participantes) {
		return FrontModel.get().genererPhaseQualifAvance(nomCategorie, nomPoule, participantes);
	}
	public IGeneration<Configuration<String>> getGenerationBasique(String nomCategorie, String nomPoule, ArrayList<String> participantes) {
		return FrontModel.get().genererPhaseQualifBasique(nomCategorie, nomPoule, participantes);
	}
	public void addPhaseQualif (String nomCategorie, String nomPoule, Triple<Configuration<String>,InfosModelPhaseQualificative,InfosModelMatchPhasesQualifs> infos) {
		try {
			FrontModel.get().addPhaseQualif(nomCategorie, nomPoule, infos);
		} catch (Exception e) {
			ContestOrg.get().error("Erreur lors de la création d'une phase qualificative", e);
		}
	}
	public void updatePhaseQualif (String nomCategorie, String nomPoule, int numeroPhase, Triple<Configuration<String>,InfosModelPhaseQualificative,InfosModelMatchPhasesQualifs> infos) {
		try {
			FrontModel.get().updatePhaseQualif(nomCategorie, nomPoule, numeroPhase, infos);
		} catch (Exception e) {
			ContestOrg.get().error("Erreur lors de la modification d'une phase qualificative", e);
		}
	}
	public void removePhaseQualif (String nomCategorie, String nomPoule, int numeroPhase) {
		try {
			FrontModel.get().removePhaseQualif(nomCategorie, nomPoule, numeroPhase);
		} catch (Exception e) {
			ContestOrg.get().error("Erreur lors de la suppression d'une phase qualificative", e);
		}
	}
	
	// Ajouter/Modifier/Supprimer un match de phase qualificative
	public void addMatchPhaseQualif (String nomCategorie, String nomPoule, int numeroPhase, Triple<Triple<String, TrackableList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>, Triple<String, TrackableList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>,InfosModelMatchPhasesQualifs> infos) {
		try {
			FrontModel.get().addMatchPhaseQualif(nomCategorie, nomPoule, numeroPhase, infos);
		} catch (Exception e) {
			ContestOrg.get().error("Erreur lors de l'ajout d'un match de phase qualificative", e);
		}
	}
	public void updateMatchPhaseQualif(String nomCategorie, String nomPoule, int numeroPhase, int numeroMatch, Triple<Triple<String, TrackableList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>, Triple<String, TrackableList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>,InfosModelMatchPhasesQualifs> infos) {
		FrontModel.get().updateMatchPhaseQualif(nomCategorie, nomPoule, numeroPhase, numeroMatch, infos);
	}
	public void removeMatchPhaseQualif(String nomCategorie, String nomPoule, int numeroPhase, int numeroMatch) {
		try {
			FrontModel.get().removeMatchPhaseQualif(nomCategorie, nomPoule, numeroPhase, numeroMatch);
		} catch (Exception e) {
			ContestOrg.get().error("Erreur lors de la suppression d'un match de phase qualificative", e);
		}
	}
	
	// ==== TreeModel et TableModels
	
	// Récupérer le TreeModel des phases qualificatives
	public TreeModel getTreeModelPhasesQualifs () {
		return FrontModel.get().getTreeModelPhasesQualifs();
	}
	
	// Récupérer des TableModels
	public IClosableTableModel getTableModelMatchsPhasesQualifs () {
		return FrontModel.get().getTableModelMatchsPhasesQualifs();
	}
	public IClosableTableModel getTableModelMatchsPhasesQualifs (String nomCategorie) {
		return FrontModel.get().getTableModelMatchsPhasesQualifs(nomCategorie);
	}
	public IClosableTableModel getTableModelMatchsPhasesQualifs (String nomCategorie, String nomPoule) {
		return FrontModel.get().getTableModelMatchsPhasesQualifs(nomCategorie, nomPoule);
	}
	public IClosableTableModel getTableModelMatchsPhasesQualifs (String nomCategorie, String nomPoule, int numeroPhase) {
		return FrontModel.get().getTableModelMatchsPhasesQualifs(nomCategorie, nomPoule, numeroPhase);
	}
	
}
