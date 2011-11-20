package org.contestorg.controlers;


import java.util.ArrayList;

import javax.swing.tree.TreeModel;

import org.contestorg.common.Pair;
import org.contestorg.common.Quintuple;
import org.contestorg.common.TrackableList;
import org.contestorg.infos.InfosModelCategorie;
import org.contestorg.infos.InfosModelParticipant;
import org.contestorg.infos.InfosModelPoule;
import org.contestorg.infos.InfosModelPrix;
import org.contestorg.infos.InfosModelPropriete;
import org.contestorg.infos.InfosModelProprieteParticipant;
import org.contestorg.interfaces.IClosableTableModel;
import org.contestorg.models.FrontModel;



/**
 * Controleur des participants
 */
public class CtrlParticipants
{
	
	// ==== Récupérer des données

	// Récupérer des données sur le concours
	public ArrayList<InfosModelPropriete> getListeProprietes() {
		return FrontModel.get().getListeProprietes();
	}
	public ArrayList<InfosModelPrix> getListePrix () {
		return FrontModel.get().getListePrix();
	}
	
	// Récupérer des données sur les participants
	public Quintuple<String,String,InfosModelParticipant,ArrayList<Pair<String,InfosModelProprieteParticipant>>,ArrayList<String>> getInfosParticipant(String nomParticipant) {
		return FrontModel.get().getInfosParticipant(nomParticipant);
	}
	public boolean isParticipantExiste(String nomParticipant) {
		return FrontModel.get().isParticipantExiste(nomParticipant);
	}
	public int getNbParticipants() {
		return FrontModel.get().getNbParticipants();
	}
	
	// Récupérer des données sur les catégories et les poules
	public ArrayList<InfosModelCategorie> getListeCategories() {
		return FrontModel.get().getListeCategories();
	}
	public ArrayList<InfosModelPoule> getListePoules(String nomCategorie) {
		return FrontModel.get().getListePoules(nomCategorie);
	}
	public ArrayList<Pair<InfosModelCategorie, ArrayList<InfosModelPoule>>> getListeCategoriesPoules() {
		return FrontModel.get().getListeCategoriesPoules();
	}
	public ArrayList<Pair<InfosModelCategorie, ArrayList<Pair<InfosModelPoule,ArrayList<InfosModelParticipant>>>>> getListeCategoriesPoulesParticipants() {
		return FrontModel.get().getListeCategoriesPoulesParticipants();
	}
	
	// ==== Modifier des données

	// Ajouter/Modifier/Supprimer un participant
	public void addParticipant(Quintuple<String,String,InfosModelParticipant,TrackableList<Pair<String, InfosModelProprieteParticipant>>, TrackableList<String>> infos) {
		try {
			FrontModel.get().addParticipant(infos);
		} catch (Exception e) {
			ContestOrg.get().error("Erreur lors de la création d'un participant", e);
		}
	}
	public void updateParticipant(String nomParticipant,Quintuple<String,String,InfosModelParticipant,TrackableList<Pair<String, InfosModelProprieteParticipant>>, TrackableList<String>> infos) {
		try {
			FrontModel.get().updateParticipant(nomParticipant, infos);
		} catch (Exception e) {
			ContestOrg.get().error("Erreur lors de la modification d'un participant", e);
		}
	}
	public void updateParticipant(String nomParticipant,InfosModelParticipant.Statut statut) {
		FrontModel.get().updateParticipant(nomParticipant, statut);
	}
	public void updateParticipants(ArrayList<String> nomParticipants,InfosModelParticipant.Statut statut) {
		FrontModel.get().updateParticipants(nomParticipants, statut);
	}
	public void removeParticipant(String nomParticipant) {
		try {
			FrontModel.get().removeParticipant(nomParticipant);
		} catch (Exception e) {
			ContestOrg.get().error("Erreur lors de la modification d'un participant", e);
		}	
	}
	public void removeParticipants(ArrayList<String> nomParticipants) {
		try {
			FrontModel.get().removeParticipants(nomParticipants);
		} catch (Exception e) {
			ContestOrg.get().error("Erreur lors de la modification de plusieurs participants", e);
		}	
	}
		
	// Modifier les catégories
	public void updateCategories(TrackableList<InfosModelCategorie> categories) {
		try {
			FrontModel.get().updateCategories(categories);
		} catch (Exception e) {
			ContestOrg.get().error("Erreur lors de la modification des catégories", e);
		}
	}

	// Modifier les poules
	public void updatePoules(ArrayList<Pair<String,TrackableList<Pair<InfosModelPoule, ArrayList<String>>>>> categoriesPoules) {
		try {
			FrontModel.get().updatePoules(categoriesPoules);
		} catch(Exception e) {
			ContestOrg.get().error("Erreur lors de la modification des poules des catégories", e);
		}
	}
	
	// ==== TreeModel et TableModels
	
	// Récupérer le TreeModel des poules
	public TreeModel getTreeModelPoules() {
		return FrontModel.get().getTreeModelPoules();
	}
	
	// Récupérer des TableModels
	public IClosableTableModel getTableModelParticipants() {
		return FrontModel.get().getTableModelParticipants();
	}
	public IClosableTableModel getTableModelParticipants(String nomCategorie) {
		return FrontModel.get().getTableModelParticipants(nomCategorie);
	}
	public IClosableTableModel getTableModelParticipants(String nomCategorie,String nomPoule) {
		return FrontModel.get().getTableModelParticipants(nomCategorie,nomPoule);
	}
	
}
