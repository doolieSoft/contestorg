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
		return FrontModel.get().getFrontModelConfiguration().getListeProprietes();
	}
	public ArrayList<InfosModelPrix> getListePrix () {
		return FrontModel.get().getFrontModelConfiguration().getListePrix();
	}
	
	// Récupérer des données sur les participants
	public Quintuple<String,String,InfosModelParticipant,ArrayList<Pair<String,InfosModelProprieteParticipant>>,ArrayList<String>> getInfosParticipant(String nomParticipant) {
		return FrontModel.get().getFrontModelParticipants().getInfosParticipant(nomParticipant);
	}
	public boolean isParticipantExiste(String nomParticipant) {
		return FrontModel.get().getFrontModelParticipants().isParticipantExiste(nomParticipant);
	}
	public int getNbParticipants() {
		return FrontModel.get().getFrontModelParticipants().getNbParticipants();
	}
	
	// Récupérer des données sur les catégories et les poules
	public ArrayList<InfosModelCategorie> getListeCategories() {
		return FrontModel.get().getFrontModelParticipants().getListeCategories();
	}
	public ArrayList<InfosModelPoule> getListePoules(String nomCategorie) {
		return FrontModel.get().getFrontModelParticipants().getListePoules(nomCategorie);
	}
	public ArrayList<Pair<InfosModelCategorie, ArrayList<InfosModelPoule>>> getListeCategoriesPoules() {
		return FrontModel.get().getFrontModelParticipants().getListeCategoriesPoules();
	}
	public ArrayList<Pair<InfosModelCategorie, ArrayList<Pair<InfosModelPoule,ArrayList<InfosModelParticipant>>>>> getListeCategoriesPoulesParticipants() {
		return FrontModel.get().getFrontModelParticipants().getListeCategoriesPoulesParticipants();
	}
	
	// ==== Modifier des données

	// Ajouter/Modifier/Supprimer un participant
	public void addParticipant(Quintuple<String,String,InfosModelParticipant,TrackableList<Pair<String, InfosModelProprieteParticipant>>, TrackableList<String>> infos) {
		try {
			FrontModel.get().getFrontModelParticipants().addParticipant(infos);
		} catch (Exception e) {
			ContestOrg.get().error("Erreur lors de la création d'un participant", e);
		}
	}
	public void updateParticipant(String nomParticipant,Quintuple<String,String,InfosModelParticipant,TrackableList<Pair<String, InfosModelProprieteParticipant>>, TrackableList<String>> infos) {
		try {
			FrontModel.get().getFrontModelParticipants().updateParticipant(nomParticipant, infos);
		} catch (Exception e) {
			ContestOrg.get().error("Erreur lors de la modification d'un participant", e);
		}
	}
	public void updateParticipant(String nomParticipant,InfosModelParticipant.Statut statut) {
		FrontModel.get().getFrontModelParticipants().updateParticipant(nomParticipant, statut);
	}
	public void updateParticipants(ArrayList<String> nomParticipants,InfosModelParticipant.Statut statut) {
		FrontModel.get().getFrontModelParticipants().updateParticipants(nomParticipants, statut);
	}
	public void removeParticipant(String nomParticipant) {
		try {
			FrontModel.get().getFrontModelParticipants().removeParticipant(nomParticipant);
		} catch (Exception e) {
			ContestOrg.get().error("Erreur lors de la modification d'un participant", e);
		}	
	}
	public void removeParticipants(ArrayList<String> nomParticipants) {
		try {
			FrontModel.get().getFrontModelParticipants().removeParticipants(nomParticipants);
		} catch (Exception e) {
			ContestOrg.get().error("Erreur lors de la modification de plusieurs participants", e);
		}	
	}
		
	// Modifier les catégories
	public void updateCategories(TrackableList<InfosModelCategorie> categories) {
		try {
			FrontModel.get().getFrontModelParticipants().updateCategories(categories);
		} catch (Exception e) {
			ContestOrg.get().error("Erreur lors de la modification des catégories", e);
		}
	}

	// Modifier les poules
	public void updatePoules(ArrayList<Pair<String,TrackableList<Pair<InfosModelPoule, ArrayList<String>>>>> categoriesPoules) {
		try {
			FrontModel.get().getFrontModelParticipants().updatePoules(categoriesPoules);
		} catch(Exception e) {
			ContestOrg.get().error("Erreur lors de la modification des poules des catégories", e);
		}
	}
	
	// ==== TreeModel et TableModels
	
	// Récupérer le TreeModel des poules
	public TreeModel getTreeModelPoules() {
		return FrontModel.get().getFrontModelParticipants().getTreeModelPoules();
	}
	
	// Récupérer des TableModels
	public IClosableTableModel getTableModelParticipants() {
		return FrontModel.get().getFrontModelParticipants().getTableModelParticipants();
	}
	public IClosableTableModel getTableModelParticipants(String nomCategorie) {
		return FrontModel.get().getFrontModelParticipants().getTableModelParticipants(nomCategorie);
	}
	public IClosableTableModel getTableModelParticipants(String nomCategorie,String nomPoule) {
		return FrontModel.get().getFrontModelParticipants().getTableModelParticipants(nomCategorie,nomPoule);
	}
	
}
