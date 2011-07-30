package controlers;

import infos.InfosModelCategorie;
import infos.InfosModelEquipe;
import infos.InfosModelPoule;
import infos.InfosModelPrix;
import infos.InfosModelPropriete;
import infos.InfosModelProprieteEquipe;
import interfaces.IClosableTableModel;

import java.util.ArrayList;

import javax.swing.tree.TreeModel;

import models.FrontModel;

import common.TrackableList;
import common.Pair;
import common.Quintuple;

/**
 * Controleur des équipes
 */
public class CtrlEquipes
{
	
	// ==== Récupérer des données

	// Récupérer des données sur le concours
	public ArrayList<InfosModelPropriete> getListeProprietes() {
		return FrontModel.get().getListeProprietes();
	}
	public ArrayList<InfosModelPrix> getListePrix () {
		return FrontModel.get().getListePrix();
	}
	
	// Récupérer des données sur les équipes
	public Quintuple<String,String,InfosModelEquipe,ArrayList<Pair<String,InfosModelProprieteEquipe>>,ArrayList<String>> getInfosEquipe(String nomEquipe) {
		return FrontModel.get().getInfosEquipe(nomEquipe);
	}
	public boolean isEquipeExiste(String nomEquipe) {
		return FrontModel.get().isEquipeExiste(nomEquipe);
	}
	public int getNbEquipes() {
		return FrontModel.get().getNbEquipes();
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
	public ArrayList<Pair<InfosModelCategorie, ArrayList<Pair<InfosModelPoule,ArrayList<InfosModelEquipe>>>>> getListeCategoriesPoulesEquipes() {
		return FrontModel.get().getListeCategoriesPoulesEquipes();
	}
	
	// ==== Modifier des données

	// Ajouter/Modifier/Supprimer une équipe
	public void addEquipe(Quintuple<String,String,InfosModelEquipe,TrackableList<Pair<String, InfosModelProprieteEquipe>>, TrackableList<String>> infos) {
		try {
			FrontModel.get().addEquipe(infos);
		} catch (Exception e) {
			ContestOrg.get().error("Erreur lors de la création d'une équipe", e);
		}
	}
	public void updateEquipe(String nomEquipe,Quintuple<String,String,InfosModelEquipe,TrackableList<Pair<String, InfosModelProprieteEquipe>>, TrackableList<String>> infos) {
		try {
			FrontModel.get().updateEquipe(nomEquipe, infos);
		} catch (Exception e) {
			ContestOrg.get().error("Erreur lors de la modification d'une équipe", e);
		}
	}
	public void updateEquipe(String nomEquipe,InfosModelEquipe.Statut statut) {
		FrontModel.get().updateEquipe(nomEquipe, statut);
	}
	public void updateEquipes(ArrayList<String> nomEquipes,InfosModelEquipe.Statut statut) {
		FrontModel.get().updateEquipes(nomEquipes, statut);
	}
	public void removeEquipe(String nomEquipe) {
		try {
			FrontModel.get().removeEquipe(nomEquipe);
		} catch (Exception e) {
			ContestOrg.get().error("Erreur lors de la modification d'une équipe", e);
		}	
	}
	public void removeEquipes(ArrayList<String> nomEquipes) {
		try {
			FrontModel.get().removeEquipes(nomEquipes);
		} catch (Exception e) {
			ContestOrg.get().error("Erreur lors de la modification de plusieurs équipes", e);
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
	public IClosableTableModel getTableModelEquipes() {
		return FrontModel.get().getTableModelEquipes();
	}
	public IClosableTableModel getTableModelEquipes(String nomCategorie) {
		return FrontModel.get().getTableModelEquipes(nomCategorie);
	}
	public IClosableTableModel getTableModelEquipes(String nomCategorie,String nomPoule) {
		return FrontModel.get().getTableModelEquipes(nomCategorie,nomPoule);
	}
	
}
