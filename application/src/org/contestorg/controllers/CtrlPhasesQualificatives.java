package org.contestorg.controllers;


import java.util.ArrayList;

import javax.swing.tree.TreeModel;

import org.contestorg.common.Pair;
import org.contestorg.common.Quadruple;
import org.contestorg.common.TrackableList;
import org.contestorg.common.Triple;
import org.contestorg.infos.InfosConfiguration;
import org.contestorg.infos.InfosModelCategorie;
import org.contestorg.infos.InfosModelCritereClassementAbstract;
import org.contestorg.infos.InfosModelMatchPhasesQualifs;
import org.contestorg.infos.InfosModelObjectif;
import org.contestorg.infos.InfosModelObjectifRemporte;
import org.contestorg.infos.InfosModelParticipation;
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
	
	/**
	 * Récupérer le type des phases qualificatives
	 * @return le type des phases qualificatives
	 */
	public int getTypePhasesQualificatives() {
		return FrontModel.get().getConcours().getTypePhasesQualificatives();
	}
	
	/**
	 * Savoir si l'égalité est activée
	 * @return égalité activée ?
	 */
	public boolean isEgaliteActivee() {
		return FrontModel.get().getConcours().isEgaliteActivee();
	}
	
	/**
	 * Récupérer la liste des critères de classement
	 * @return liste des critères de classement
	 */
	public ArrayList<InfosModelCritereClassementAbstract> getListeCriteresClassement() {
		return FrontModel.get().getFrontModelConfiguration().getListeCriteresClassement();
	}
	
	/**
	 * Récupérer les données manquantes à partir d'un numéro de match
	 * @param nomCategorie nom de la catégorie si elle est connue
	 * @param nomPoule nom de la poule si elle est connue
	 * @param numeroPhase numero de la phase qualificative si elle est connue
	 * @param numeroMatch numéro de match
	 * @return données manquantes retrouvées à partir du numéro de match
	 */
	public Quadruple<String,String,Integer,Integer> getCategoriePoulePhase(String nomCategorie,String nomPoule,Integer numeroPhase,int numeroMatch) {
		return FrontModel.get().getFrontModelPhasesQualificatives().getCategoriePoulePhaseQualif(nomCategorie, nomPoule, numeroPhase, numeroMatch);
	}

	/**
	 * Récupérer la structure catégories, poules et phases qualificatives
	 * @return structure catégories, poules et phases qualificatives
	 */
	public ArrayList<Pair<InfosModelCategorie, ArrayList<Pair<InfosModelPoule,ArrayList<InfosModelPhaseQualificative>>>>> getListeCategoriesPoulesPhases() {
		return FrontModel.get().getFrontModelParticipants().getListeCategoriesPoulesPhases();
	}
	
	/**
	 * Récupérer des informations sur une phase qualificative
	 * @param nomCategorie nom de la catégorie
	 * @param nomPoule nom de la poule
	 * @param numeroPhase numéro de la phase qualificative
	 * @return informations sur la phase qualificative
	 */
	public Triple<InfosConfiguration<String>,InfosModelPhaseQualificative,InfosModelMatchPhasesQualifs> getInfosPhaseQualif(String nomCategorie,String nomPoule,int numeroPhase) {
		return FrontModel.get().getFrontModelPhasesQualificatives().getInfosPhaseQualif(nomCategorie, nomPoule, numeroPhase);
	}
	
	/**
	 * Récupérer les informations sur un match
	 * @param nomCategorie nom de la catégorie
	 * @param nomPoule nom de la poule
	 * @param numeroPhase numéro de la phase qualificative
	 * @param numeroMatch numéro du match
	 * @return informations sur le match
	 */
	public Quadruple<Triple<String, ArrayList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>, Triple<String, ArrayList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>, Pair<String, String>, InfosModelMatchPhasesQualifs> getInfosMatch(String nomCategorie,String nomPoule,int numeroPhase,int numeroMatch) {
		return FrontModel.get().getFrontModelPhasesQualificatives().getInfosMatch(nomCategorie,nomPoule,numeroPhase,numeroMatch);
	}
	
	// Récupérer des données utile à la création de matchs
	
	/**
	 * Récupérer la liste des objectifs
	 * @return liste des objectifs
	 */
	public ArrayList<InfosModelObjectif> getListeObjectifs() {
		return FrontModel.get().getFrontModelConfiguration().getListeObjectifs();
	}
	
	/**
	 * Vérifier si un participant peut participer aux phases qualificatives
	 * @param nomCategorie nom de la catégorie
	 * @param nomPoule nom de la poule
	 * @param numeroPhase numéro de la phase qualificative
	 * @param nomParticipant nom du participant
	 * @return le participant peut participer aux phases qualificatives ?
	 */
	public boolean isParticipantPhaseQualif(String nomCategorie, String nomPoule, int numeroPhase, String nomParticipant) {
		return FrontModel.get().getFrontModelPhasesQualificatives().isParticipantPhasesQualif(nomCategorie, nomPoule, numeroPhase, nomParticipant);
	}
	
	/**
	 * Récupérer le nombre de phases qualificatives d'une poule d'une catégorie
	 * @param nomCategorie nom de la catégorie
	 * @param nomPoule nom de la poule
	 * @return nombre de phases qualificatives d'une poule d'une catégorie
	 */
	public int getNbPhasesQualifs(String nomCategorie, String nomPoule) {
		return FrontModel.get().getFrontModelPhasesQualificatives().getNbPhasesQualifs(nomCategorie, nomPoule);
	}
				
	// Récupérer des données utiles à la génération d'une phase qualificative
	
	/**
	 * Récupérer le nombre de villes communes au sein d'une configuration
	 * @param configuration configuration
	 * @return nombre de villes communes au sein de la configuration
	 */
	public int getNbVillesCommunes(InfosConfiguration<String> configuration) {
		return FrontModel.get().getFrontModelParticipants().getNbVillesCommunes(configuration);
	}
	
	/**
	 * Récupérer le nombre de matchs déjà joués au sein d'une configuration
	 * @param configuration configuration
	 * @return nombre de matchs déjà joués au sein de la configuration
	 */
	public int getNbMatchDejaJoues(InfosConfiguration<String> configuration) {
		return FrontModel.get().getFrontModelPhasesQualificatives().getNbMatchDejaJoues(configuration);
	}
	
	/**
	 * Récupérer le nombre de rencontres entre deux participants
	 * @param nomParticipantA nom du premier participant
	 * @param nomParticipantB nom du deuxième participant
	 * @return nombre de rencontres entre deux participants
	 */
	public int getNbRencontres(String nomParticipantA, String nomParticipantB) {
		return FrontModel.get().getFrontModelPhasesQualificatives().getNbRencontres(nomParticipantA, nomParticipantB);
	}
	
	/**
	 * Récupérer le rang d'un participant aux phases qualificatives 
	 * @param nomParticipant nom du participant
	 * @return rang du participant aux phases qualificatives
	 */
	public int getRang(String nomParticipant) {
		return FrontModel.get().getFrontModelPhasesQualificatives().getRangPhasesQualifs(nomParticipant);
	}
	
	/**
	 * Récupérer la liste des participants d'une poule donnée d'une catégorie donnée qui peuvent participer
	 * @param nomCategorie nom de la catégorie
	 * @param nomPoule nom de la poule
	 * @return liste des participants de la poule de la catégorie qui peuvent participer
	 */
	public ArrayList<String> getListeParticipantsParticipants(String nomCategorie,String nomPoule) {
		return FrontModel.get().getFrontModelParticipants().getListeParticipantsParticipants(nomCategorie, nomPoule);
	}
	
	// ==== Modifier des données
	
	// Opérations sur les phases qualificatives
	
	/**
	 * Générer une phase qualificative en mode avancé
	 * @param nomCategorie nom de la catégorie
	 * @param nomPoule nom de la poule
	 * @param participantes liste des participants
	 * @return générateur de phase qualificative en mode avancé
	 */
	public IGeneration<InfosConfiguration<String>> getGenerationAvance(String nomCategorie, String nomPoule, ArrayList<String> participantes) {
		return FrontModel.get().getFrontModelPhasesQualificatives().getGenerationAvance(nomCategorie, nomPoule, participantes);
	}
	
	/**
	 * Générer une phase qualificative en mode basique
	 * @param nomCategorie nom de la catégorie
	 * @param nomPoule nom de la poule
	 * @param participantes liste des participants
	 * @return générateur de phase qualificative en mode basique
	 */
	public IGeneration<InfosConfiguration<String>> getGenerationBasique(String nomCategorie, String nomPoule, ArrayList<String> participantes) {
		return FrontModel.get().getFrontModelPhasesQualificatives().getGenerationBasique(nomCategorie, nomPoule, participantes);
	}
	
	/**
	 * Ajouter une phase qualificative
	 * @param nomCategorie nom de la catégorie
	 * @param nomPoule nom de la poule
	 * @param infos informations de la phase qualificative
	 */
	public void addPhaseQualif (String nomCategorie, String nomPoule, Triple<InfosConfiguration<String>,InfosModelPhaseQualificative,InfosModelMatchPhasesQualifs> infos) {
		try {
			FrontModel.get().getFrontModelPhasesQualificatives().addPhaseQualif(nomCategorie, nomPoule, infos);
		} catch (Exception e) {
			ContestOrg.get().erreur("Erreur lors de la création d'une phase qualificative", e);
		}
	}

	/**
	 * Ajouter une phase qualificative vide
	 * @param nomCategorie nom de la catégorie
	 * @param nomPoule nom de la poule
	 * @param infos informations de la phase qualificative
	 */
	public void addPhaseQualifVide (String nomCategorie, String nomPoule, InfosModelPhaseQualificative infos) {
		try {
			FrontModel.get().getFrontModelPhasesQualificatives().addPhaseQualifVide(nomCategorie, nomPoule, infos);
		} catch (Exception e) {
			ContestOrg.get().erreur("Erreur lors de la création d'une phase qualificative vide", e);
		}
	}
	
	/**
	 * Modifier une phase qualificative
	 * @param nomCategorie nom de la catégorie
	 * @param nomPoule nom de la poule
	 * @param numeroPhase numéro de la phase qualificative
	 * @param infos nouvelles informations de la phase qualificative
	 */
	public void updatePhaseQualif (String nomCategorie, String nomPoule, int numeroPhase, Triple<InfosConfiguration<String>,InfosModelPhaseQualificative,InfosModelMatchPhasesQualifs> infos) {
		try {
			FrontModel.get().getFrontModelPhasesQualificatives().updatePhaseQualif(nomCategorie, nomPoule, numeroPhase, infos);
		} catch (Exception e) {
			ContestOrg.get().erreur("Erreur lors de la modification d'une phase qualificative", e);
		}
	}
	
	/**
	 * Supprimer une phase qualificative
	 * @param nomCategorie nom de la catégorie
	 * @param nomPoule nom de la poule
	 * @param numeroPhase numéro de la phase qualificative
	 */
	public void removePhaseQualif (String nomCategorie, String nomPoule, int numeroPhase) {
		try {
			FrontModel.get().getFrontModelPhasesQualificatives().removePhaseQualif(nomCategorie, nomPoule, numeroPhase);
		} catch (Exception e) {
			ContestOrg.get().erreur("Erreur lors de la suppression d'une phase qualificative", e);
		}
	}
	
	// Opérations sur les matchs de phases qualificatives
	
	/**
	 * Ajouter un match
	 * @param nomCategorie nom de la catégorie
	 * @param nomPoule nom de la poule
	 * @param numeroPhase numéro de la phase qualificative
	 * @param infos informations du match
	 */
	public void addMatch (String nomCategorie, String nomPoule, int numeroPhase, Quadruple<Triple<String, TrackableList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>, Triple<String, TrackableList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>, Pair<String, String>, InfosModelMatchPhasesQualifs> infos) {
		try {
			FrontModel.get().getFrontModelPhasesQualificatives().addMatchPhaseQualif(nomCategorie, nomPoule, numeroPhase, infos);
		} catch (Exception e) {
			ContestOrg.get().erreur("Erreur lors de l'ajout d'un match de phase qualificative", e);
		}
	}
	
	/**
	 * Modifier un match
	 * @param nomCategorie nom de la catégorie
	 * @param nomPoule nom de la poule
	 * @param numeroPhase numéro de la phase qualificative
	 * @param numeroMatch numéro du match
	 * @param infos nouvelles informations du match
	 */
	public void updateMatch(String nomCategorie, String nomPoule, int numeroPhase, int numeroMatch, Quadruple<Triple<String, TrackableList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>, Triple<String, TrackableList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>, Pair<String, String>, InfosModelMatchPhasesQualifs> infos) {
		FrontModel.get().getFrontModelPhasesQualificatives().updateMatch(nomCategorie, nomPoule, numeroPhase, numeroMatch, infos);
	}
	
	/**
	 * Supprimer un match
	 * @param nomCategorie nom de la catégorie
	 * @param nomPoule nom de la poule
	 * @param numeroPhase numéro de la phase qualificative
	 * @param numeroMatch numéro du match
	 */
	public void removeMatch(String nomCategorie, String nomPoule, int numeroPhase, int numeroMatch) {
		try {
			FrontModel.get().getFrontModelPhasesQualificatives().removeMatch(nomCategorie, nomPoule, numeroPhase, numeroMatch);
		} catch (Exception e) {
			ContestOrg.get().erreur("Erreur lors de la suppression d'un match de phase qualificative", e);
		}
	}
	
	// ==== TreeModel et TableModels
	
	// TreeModels
	
	/**
	 * Récupérer le TreeModel des phases qualificatives
	 * @return TreeModel des phases qualificatives
	 */
	public TreeModel getTreeModelPhasesQualifs () {
		return FrontModel.get().getFrontModelPhasesQualificatives().getTreeModelPhasesQualifs();
	}
	
	// TableModels
	
	/**
	 * Récupérer le TableModel des matchs des phases qualificatives
	 * @return TableModel des matchs des phases qualificatives
	 */
	public IClosableTableModel getTableModelMatchsPhasesQualifs () {
		return FrontModel.get().getFrontModelPhasesQualificatives().getTableModelMatchsPhasesQualifs();
	}
	
	/**
	 * Récupérer le TableModel des matchs des phases qualificatives d'une catégorie donnée
	 * @param nomCategorie nom de la catégorie
	 * @return TableModel des matchs des phases qualificatives de la catégorie
	 */
	public IClosableTableModel getTableModelMatchsPhasesQualifs (String nomCategorie) {
		return FrontModel.get().getFrontModelPhasesQualificatives().getTableModelMatchsPhasesQualifs(nomCategorie);
	}
	
	/**
	 * Récupérer le TableModel des matchs des phases qualificatives d'une poule donnée d'une catégorie donnée
	 * @param nomCategorie nom de la catégorie
	 * @param nomPoule nom de la poule
	 * @return TableModel des matchs des phases qualificatives de la poule de la catégorie
	 */
	public IClosableTableModel getTableModelMatchsPhasesQualifs (String nomCategorie, String nomPoule) {
		return FrontModel.get().getFrontModelPhasesQualificatives().getTableModelMatchsPhasesQualifs(nomCategorie, nomPoule);
	}
	
	/**
	 * Récupérer le TableModel des matchs d'une phase qualificative
	 * @param nomCategorie nom de la catégorie
	 * @param nomPoule nom de la poule
	 * @param numeroPhase numéro de la phase qualificative
	 * @return TableModel des matchs de la phase qualificative
	 */
	public IClosableTableModel getTableModelMatchsPhasesQualifs (String nomCategorie, String nomPoule, int numeroPhase) {
		return FrontModel.get().getFrontModelPhasesQualificatives().getTableModelMatchsPhasesQualifs(nomCategorie, nomPoule, numeroPhase);
	}
	
}
