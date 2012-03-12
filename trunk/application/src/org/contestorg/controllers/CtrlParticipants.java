package org.contestorg.controllers;


import java.util.ArrayList;

import javax.swing.tree.TreeModel;

import org.contestorg.common.ContestOrgErrorException;
import org.contestorg.common.ContestOrgWarningException;
import org.contestorg.common.Pair;
import org.contestorg.common.Quintuple;
import org.contestorg.common.TrackableList;
import org.contestorg.infos.InfosModelCategorie;
import org.contestorg.infos.InfosModelParticipant;
import org.contestorg.infos.InfosModelPoule;
import org.contestorg.infos.InfosModelPrix;
import org.contestorg.infos.InfosModelPropriete;
import org.contestorg.infos.InfosModelProprietePossedee;
import org.contestorg.interfaces.IClosableTableModel;
import org.contestorg.models.FrontModel;
import org.contestorg.views.ViewHelper;

/**
 * Controleur des participants
 */
public class CtrlParticipants
{
	
	// ==== Récupérer des données

	// Récupérer des données sur le concours
	
	/**
	 * Récupérer la liste des propriétés
	 * @return liste des propriétés
	 */
	public ArrayList<InfosModelPropriete> getListeProprietes() {
		return FrontModel.get().getFrontModelConfiguration().getListeProprietes();
	}
	
	/**
	 * Récupérer la liste des prix
	 * @return liste des prix
	 */
	public ArrayList<InfosModelPrix> getListePrix () {
		return FrontModel.get().getFrontModelConfiguration().getListePrix();
	}
	
	// Récupérer des données sur les participants
	
	/**
	 * Récupérer les informations d'un participant
	 * @param nomParticipant nom du participant
	 * @return informations du participant
	 */
	public Quintuple<String,String,InfosModelParticipant,ArrayList<Pair<String,InfosModelProprietePossedee>>,ArrayList<String>> getInfosParticipant(String nomParticipant) {
		return FrontModel.get().getFrontModelParticipants().getInfosParticipant(nomParticipant);
	}
	
	/**
	 * Vérifier si un participant existe
	 * @param nomParticipant nom du participant
	 * @return participant existant ?
	 */
	public boolean isParticipantExiste(String nomParticipant) {
		return FrontModel.get().getFrontModelParticipants().isParticipantExiste(nomParticipant);
	}
	
	/**
	 * Récupérer le nombre de participants
	 * @return nombre de participants
	 */
	public int getNbParticipants() {
		return FrontModel.get().getFrontModelParticipants().getNbParticipants();
	}
	
	// Récupérer des données sur les catégories et les poules
	
	/**
	 * Récupérer la liste des catégories
	 * @return liste des catégories
	 */
	public ArrayList<InfosModelCategorie> getListeCategories() {
		return FrontModel.get().getFrontModelParticipants().getListeCategories();
	}
	
	/**
	 * Récupérer la liste des poules d'une catégorie donnée
	 * @param nomCategorie nom de la catégorie
	 * @return liste des poules de la catégorie
	 */
	public ArrayList<InfosModelPoule> getListePoules(String nomCategorie) {
		return FrontModel.get().getFrontModelParticipants().getListePoules(nomCategorie);
	}
	
	/**
	 * Récupérer la structure des catégories et poules
	 * @return structure des catégories et poules
	 */
	public ArrayList<Pair<InfosModelCategorie, ArrayList<InfosModelPoule>>> getListeCategoriesPoules() {
		return FrontModel.get().getFrontModelParticipants().getListeCategoriesPoules();
	}
	
	/**
	 * Récupérer la structure des catégories, poules et participants
	 * @return structure des catégories, poules et participants
	 */
	public ArrayList<Pair<InfosModelCategorie, ArrayList<Pair<InfosModelPoule,ArrayList<InfosModelParticipant>>>>> getListeCategoriesPoulesParticipants() {
		return FrontModel.get().getFrontModelParticipants().getListeCategoriesPoulesParticipants();
	}
	
	// ==== Modifier des données

	// Participants
	
	/**
	 * Ajouter un participant
	 * @param infos informations du participant
	 */
	public void addParticipant(Quintuple<String,String,InfosModelParticipant,TrackableList<Pair<String, InfosModelProprietePossedee>>, TrackableList<String>> infos) {
		try {
			FrontModel.get().getFrontModelParticipants().addParticipant(infos);
		} catch (Exception e) {
			ContestOrg.get().error("Erreur lors de la création d'un participant", e);
		}
	}
	
	/**
	 * Modifier un participant
	 * @param nomParticipant nom du participant
	 * @param infos nouvelles informations du participant
	 */
	public void updateParticipant(String nomParticipant,Quintuple<String,String,InfosModelParticipant,TrackableList<Pair<String, InfosModelProprietePossedee>>, TrackableList<String>> infos) {
		try {
			FrontModel.get().getFrontModelParticipants().updateParticipant(nomParticipant, infos);
		} catch (Exception e) {
			ContestOrg.get().error("Erreur lors de la modification d'un participant", e);
		}
	}
	
	/**
	 * Modifier le statut d'un participant
	 * @param nomParticipant nom du participant
	 * @param statut nouveau statut du participant
	 */
	public void updateParticipant(String nomParticipant,InfosModelParticipant.Statut statut) {
		FrontModel.get().getFrontModelParticipants().updateParticipant(nomParticipant, statut);
	}
	
	/**
	 * Modifier le statut d'une liste de participants
	 * @param nomParticipants noms des participants
	 * @param statut nouveau statut des participants
	 */
	public void updateParticipants(ArrayList<String> nomParticipants,InfosModelParticipant.Statut statut) {
		FrontModel.get().getFrontModelParticipants().updateParticipants(nomParticipants, statut);
	}
	
	/**
	 * Supprimer un participant
	 * @param nomParticipant nom du participant
	 */
	public void removeParticipant(String nomParticipant) {
		try {
			FrontModel.get().getFrontModelParticipants().removeParticipant(nomParticipant);
		} catch (ContestOrgWarningException e) {
			ViewHelper.derror(ContestOrg.get().getFenetrePrincipale(), e.getMessage());
		} catch (ContestOrgErrorException e) {
			ContestOrg.get().error("Erreur lors de la modification d'un participant", e);
		}
	}
	
	/**
	 * Supprimer une liste de participants
	 * @param nomParticipants noms des participants
	 */
	public void removeParticipants(ArrayList<String> nomParticipants) {
		try {
			FrontModel.get().getFrontModelParticipants().removeParticipants(nomParticipants);
		} catch (ContestOrgWarningException e) {
			ViewHelper.derror(ContestOrg.get().getFenetrePrincipale(), e.getMessage());
		} catch (ContestOrgErrorException e) {
			ContestOrg.get().error("Erreur lors de la modification de plusieurs participants", e);
		}	
	}
		
	// Catégories
	
	/**
	 * Mettre à jour les catégories
	 * @param categories informations sur les catégories
	 */
	public void updateCategories(TrackableList<InfosModelCategorie> categories) {
		try {
			FrontModel.get().getFrontModelParticipants().updateCategories(categories);
		} catch (Exception e) {
			ContestOrg.get().error("Erreur lors de la modification des catégories", e);
		}
	}

	// Poules
	
	/**
	 * Mettre à jour les poules
	 * @param categoriesPoules informations sur les poules
	 */
	public void updatePoules(ArrayList<Pair<String,TrackableList<Pair<InfosModelPoule, ArrayList<String>>>>> categoriesPoules) {
		try {
			FrontModel.get().getFrontModelParticipants().updatePoules(categoriesPoules);
		} catch(Exception e) {
			ContestOrg.get().error("Erreur lors de la modification des poules des catégories", e);
		}
	}
	
	// ==== TreeModel et TableModels
	
	// TreeModels
	
	/**
	 * Récupérer le TreeModel des poules
	 * @return TreeModel des poules
	 */
	public TreeModel getTreeModelPoules() {
		return FrontModel.get().getFrontModelParticipants().getTreeModelPoules();
	}
	
	// TableModels
	
	/**
	 * Récupérer le TableModel des participants
	 * @return TableModel des participants
	 */
	public IClosableTableModel getTableModelParticipants() {
		return FrontModel.get().getFrontModelParticipants().getTableModelParticipants();
	}
	
	/**
	 * Récupérer le TableModel des participants d'une catégorie donnée
	 * @param nomCategorie nom de la catégorie
	 * @return TableModel des participants de la catégorie
	 */
	public IClosableTableModel getTableModelParticipants(String nomCategorie) {
		return FrontModel.get().getFrontModelParticipants().getTableModelParticipants(nomCategorie);
	}
	
	/**
	 * Récupérer le TableModel des participants d'une poule donnée d'une catégorie donnée
	 * @param nomCategorie nom de la catégorie
	 * @param nomPoule nom de la poule
	 * @return TableModel des participants de la poule de la catégorie
	 */
	public IClosableTableModel getTableModelParticipants(String nomCategorie,String nomPoule) {
		return FrontModel.get().getFrontModelParticipants().getTableModelParticipants(nomCategorie,nomPoule);
	}
	
}
