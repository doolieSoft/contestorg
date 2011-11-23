package org.contestorg.models;

import java.util.ArrayList;

import javax.swing.tree.TreeModel;

import org.contestorg.common.Pair;
import org.contestorg.common.Quintuple;
import org.contestorg.common.TrackableList;
import org.contestorg.infos.Configuration;
import org.contestorg.infos.Couple;
import org.contestorg.infos.InfosModelCategorie;
import org.contestorg.infos.InfosModelParticipant;
import org.contestorg.infos.InfosModelPhaseQualificative;
import org.contestorg.infos.InfosModelPoule;
import org.contestorg.infos.InfosModelProprieteParticipant;
import org.contestorg.interfaces.IClosableTableModel;
import org.contestorg.interfaces.IListValidator;

public class FrontModelParticipants
{
	// Point d'entrée principal aux modèles
	private FrontModel frontModel;
	
	// TreeModels
	private TreeModelParticipants tm_categories;
	private TreeModelParticipants tm_poules;
	private TreeModelParticipants tm_participants;
	
	// Constructeur
	public FrontModelParticipants(FrontModel frontModel) {
		this.frontModel = frontModel;
	}
	
	// Récupérer des données calculées
	public int getNbParticipants() {
		return this.frontModel.getConcours().getNbParticipants();
	}
	public boolean isParticipantExiste (String nomParticipant) {
		return this.frontModel.getConcours().getParticipantByNom(nomParticipant) != null;
	}
	public int getNbRencontres(String nomParticipantA, String nomParticipantB) {
		return nomParticipantA != null ? this.frontModel.getConcours().getParticipantByNom(nomParticipantA).getNbRencontres(this.frontModel.getConcours().getParticipantByNom(nomParticipantB)) : (nomParticipantB == null ? 0 : this.frontModel.getConcours().getParticipantByNom(nomParticipantB).getNbRencontres(this.frontModel.getConcours().getParticipantByNom(nomParticipantA)));
	}
	public int getNbVillesCommunes(Configuration<String> configuration) {
		int nbVillesCommunes = 0;
		for(Couple<String> couple : configuration.getCouples()) {
			if(couple.getParticipantA() != null && couple.getParticipantB() != null) {
				ModelParticipant participantA = this.frontModel.getConcours().getParticipantByNom(couple.getParticipantA());
				ModelParticipant participantB = this.frontModel.getConcours().getParticipantByNom(couple.getParticipantB());
				if(participantA.getVille() != null && !participantA.getVille().isEmpty() && participantB.getVille() != null && !participantB.getVille().isEmpty() && participantA.getVille().equals(participantB.getVille())) {
					nbVillesCommunes++;
				}
			}
		}
		return nbVillesCommunes;
	}
	
	// Récupérer des données uniques
	public Quintuple<String, String, InfosModelParticipant, ArrayList<Pair<String, InfosModelProprieteParticipant>>, ArrayList<String>> getInfosParticipant (String nomParticipant) {
		// Récupérer le participant
		ModelParticipant participant = this.frontModel.getConcours().getParticipantByNom(nomParticipant);
		
		// Récupérer la liste des propriétés
		ArrayList<Pair<String, InfosModelProprieteParticipant>> proprietes = new ArrayList<Pair<String, InfosModelProprieteParticipant>>();
		for (ModelProprietePossedee propriete : participant.getProprietesParticipant()) {
			proprietes.add(new Pair<String, InfosModelProprieteParticipant>(propriete.getPropriete().getNom(), propriete.toInfos()));
		}
		
		// Récupérer la liste des prix
		ArrayList<String> prixs = new ArrayList<String>();
		for (ModelPrix prix : participant.getPrix()) {
			prixs.add(prix.getNom());
		}
		
		// Retourner les informations du participant
		return new Quintuple<String, String, InfosModelParticipant, ArrayList<Pair<String, InfosModelProprieteParticipant>>, ArrayList<String>>(participant.getPoule().getCategorie().getNom(), participant.getPoule().getNom(), participant.toInfos(), proprietes, prixs);
	}

	// Modifier les catégories et les poules
	public void updateCategories(TrackableList<InfosModelCategorie> categories) throws ContestOrgModelException {
		// Démarrer l'action de modification
		this.frontModel.getHistory().start("Modification des catégories");
		
		// Modifier les catégories
		this.frontModel.getConcours().updateCategories(categories);
		
		// Fermer l'action de modification
		this.frontModel.getHistory().close();
	}
	public void updatePoules(ArrayList<Pair<String,TrackableList<Pair<InfosModelPoule, ArrayList<String>>>>> categoriesPoules) throws ContestOrgModelException {
		// Démarrer l'action de modification
		this.frontModel.getHistory().start("Modification des poules");
		
		// Modifier les poules des catégories
		for(Pair<String,TrackableList<Pair<InfosModelPoule, ArrayList<String>>>> categoriePoules : categoriesPoules) {
			// Modifier les poules de la catégorie
			this.frontModel.getConcours().getCategorieByNom(categoriePoules.getFirst()).updatePoules(categoriePoules.getSecond());
		}
		
		// Fermer l'action de modification
		this.frontModel.getHistory().close();
	}
	
	// Ajouter/Modifier/Supprimer un participant
	public void addParticipant (Quintuple<String, String, InfosModelParticipant, TrackableList<Pair<String, InfosModelProprieteParticipant>>, TrackableList<String>> infos) throws ContestOrgModelException {
		// Démarrer l'action de création
		this.frontModel.getHistory().start("Ajout du participant \"" + infos.getThird().getNom() + "\"");
		
		// Ajouter le participant
		new ModelParticipant.Updater(this.frontModel.getConcours()).create(infos);
		
		// Fermer l'action de création
		this.frontModel.getHistory().close();
	}
	public void updateParticipant (String nomParticipant, Quintuple<String, String, InfosModelParticipant, TrackableList<Pair<String, InfosModelProprieteParticipant>>, TrackableList<String>> infos) throws ContestOrgModelException {
		// Récupérer le participant
		ModelParticipant participant = this.frontModel.getConcours().getParticipantByNom(nomParticipant);
		
		// Démarrer l'action de modification
		this.frontModel.getHistory().start("Modification du participant \"" + participant.getNom() + "\"");
		
		// Modifier le participant
		new ModelParticipant.Updater(this.frontModel.getConcours()).update(participant, infos);
		
		// Fermer l'action de modification
		this.frontModel.getHistory().close();
	}
	public void updateParticipant (String nomParticipant, InfosModelParticipant.Statut statut) {
		// Récupérer le participant
		ModelParticipant participant = this.frontModel.getConcours().getParticipantByNom(nomParticipant);
		
		// Démarrer l'action de modification
		this.frontModel.getHistory().start("Changement du statut du participant \"" + participant.getNom() + "\"");
		
		// Modifier le participant
		participant.setStatut(statut);
		
		// Fermer l'action de modification
		this.frontModel.getHistory().close();
	}
	public void updateParticipants(ArrayList<String> nomParticipants, InfosModelParticipant.Statut statut) {
		// Démarrer l'action de modification
		this.frontModel.getHistory().start("Changement du statut de plusieurs participants");
		
		// Pour chaque participant
		for(String nomParticipant : nomParticipants) {
			// Récupérer le participant et modifier son statut
			this.frontModel.getConcours().getParticipantByNom(nomParticipant).setStatut(statut);
		}
		
		// Fermer l'action de modification
		this.frontModel.getHistory().close();
	}
	public void removeParticipant (String nomParticipant) throws ContestOrgModelException {
		// Récupérer le participant
		ModelParticipant participant = this.frontModel.getConcours().getParticipantByNom(nomParticipant);
		
		// Démarrer l'action de suppression
		this.frontModel.getHistory().start("Suppression du participant \"" + participant.getNom() + "\"");
		
		// Chercher le participant et le supprimer
		participant.delete();
		
		// Fermer l'action de suppression
		this.frontModel.getHistory().close();
	}
	public void removeParticipants (ArrayList<String> nomParticipants) throws ContestOrgModelException {
		// Démarrer l'action de suppression
		this.frontModel.getHistory().start("Suppression de plusieurs participants");
		
		// Pour chaque participant
		for(String nomParticipant : nomParticipants) {
			// Récupérer le participant et le supprimer
			this.frontModel.getConcours().getParticipantByNom(nomParticipant).delete();
		}
		
		// Fermer l'action de suppression
		this.frontModel.getHistory().close();
	}
	
	// Récupérer des TreeModels
	public TreeModel getTreeModelCategories () {
		if (this.tm_categories == null) {
			this.tm_categories = new TreeModelParticipants(false, false);
		}
		return this.tm_categories;
	}
	public TreeModel getTreeModelPoules () {
		if (this.tm_poules == null) {
			this.tm_poules = new TreeModelParticipants(true, false);
		}
		return this.tm_poules;
	}
	public TreeModel getTreeModelParticipants () {
		if (this.tm_participants == null) {
			this.tm_participants = new TreeModelParticipants(true, true);
		}
		return this.tm_participants;
	}
	
	// Récupérer des TableModels
	public IClosableTableModel getTableModelParticipants () {
		return new TableModelParticipants(this.frontModel.getConcours());
	}
	public IClosableTableModel getTableModelParticipants (String nomCategorie) {
		return new TableModelParticipants(this.frontModel.getConcours().getCategorieByNom(nomCategorie));
	}
	public IClosableTableModel getTableModelParticipants (String nomCategorie, String nomPoule) {
		return new TableModelParticipants(this.frontModel.getConcours().getCategorieByNom(nomCategorie).getPouleByNom(nomPoule));
	}
	
	// Récupérer des validateurs de listes
	public IListValidator<InfosModelCategorie> getCategoriesValidator() {
		return new ModelCategorie.ValidatorForConcours();
	}
	public IListValidator<Pair<InfosModelPoule, ArrayList<String>>> getPoulesValidator() {
		return new ModelPoule.ValidatorForCategorie();
	}
	
	// Récupérer des données de liste
	public ArrayList<Pair<InfosModelCategorie, ArrayList<InfosModelPoule>>> getListeCategoriesPoules () {
		ArrayList<Pair<InfosModelCategorie, ArrayList<InfosModelPoule>>> categories = new ArrayList<Pair<InfosModelCategorie, ArrayList<InfosModelPoule>>>();
		for (ModelCategorie categorie : this.frontModel.getConcours().getCategories()) {
			ArrayList<InfosModelPoule> poules = new ArrayList<InfosModelPoule>();
			for (ModelPoule poule : categorie.getPoules()) {
				poules.add(poule.toInfos());
			}
			categories.add(new Pair<InfosModelCategorie, ArrayList<InfosModelPoule>>(categorie.toInfos(), poules));
		}
		return categories;
	}
	public ArrayList<Pair<InfosModelCategorie, ArrayList<Pair<InfosModelPoule,ArrayList<InfosModelParticipant>>>>> getListeCategoriesPoulesParticipants() {
		ArrayList<Pair<InfosModelCategorie, ArrayList<Pair<InfosModelPoule,ArrayList<InfosModelParticipant>>>>> categories = new ArrayList<Pair<InfosModelCategorie,ArrayList<Pair<InfosModelPoule,ArrayList<InfosModelParticipant>>>>>();
		for(ModelCategorie categorie : this.frontModel.getConcours().getCategories()) {
			ArrayList<Pair<InfosModelPoule,ArrayList<InfosModelParticipant>>> poules = new ArrayList<Pair<InfosModelPoule,ArrayList<InfosModelParticipant>>>();
			for(ModelPoule poule : categorie.getPoules()) {
				ArrayList<InfosModelParticipant> participants = new ArrayList<InfosModelParticipant>();
				for(ModelParticipant participant : poule.getParticipants()) {
					participants.add(participant.toInfos());
				}
				poules.add(new Pair<InfosModelPoule, ArrayList<InfosModelParticipant>>(poule.toInfos(), participants));
			}
			categories.add(new Pair<InfosModelCategorie, ArrayList<Pair<InfosModelPoule,ArrayList<InfosModelParticipant>>>>(categorie.toInfos(), poules));
		}
		return categories;
	}
	public ArrayList<Pair<InfosModelCategorie, ArrayList<Pair<InfosModelPoule,ArrayList<InfosModelPhaseQualificative>>>>> getListeCategoriesPoulesPhases() {
		ArrayList<Pair<InfosModelCategorie, ArrayList<Pair<InfosModelPoule,ArrayList<InfosModelPhaseQualificative>>>>> categories = new ArrayList<Pair<InfosModelCategorie,ArrayList<Pair<InfosModelPoule,ArrayList<InfosModelPhaseQualificative>>>>>();
		for(ModelCategorie categorie : this.frontModel.getConcours().getCategories()) {
			ArrayList<Pair<InfosModelPoule,ArrayList<InfosModelPhaseQualificative>>> poules = new ArrayList<Pair<InfosModelPoule,ArrayList<InfosModelPhaseQualificative>>>();
			for(ModelPoule poule : categorie.getPoules()) {
				ArrayList<InfosModelPhaseQualificative> phases = new ArrayList<InfosModelPhaseQualificative>();
				for(ModelPhaseQualificative phase : poule.getPhasesQualificatives()) {
					phases.add(phase.toInfos());
				}
				poules.add(new Pair<InfosModelPoule, ArrayList<InfosModelPhaseQualificative>>(poule.toInfos(), phases));
			}
			categories.add(new Pair<InfosModelCategorie, ArrayList<Pair<InfosModelPoule,ArrayList<InfosModelPhaseQualificative>>>>(categorie.toInfos(), poules));
		}
		return categories;
	}
	public ArrayList<String> getListeParticipantsParticipants(String nomCategorie,String nomPoule) {
		ArrayList<String> participantes = new ArrayList<String>();
		for(ModelParticipant participant : this.frontModel.getConcours().getCategorieByNom(nomCategorie).getPouleByNom(nomPoule).getParticipantsParticipants()) {
			participantes.add(participant.getNom());
		}
		return participantes;
	}
	public ArrayList<String> getListeParticipantsParticipants(String nomCategorie) {
		ArrayList<String> participantes = new ArrayList<String>();
		for(ModelParticipant participant : this.frontModel.getConcours().getCategorieByNom(nomCategorie).getParticipantsParticipants()) {
			participantes.add(participant.getNom());
		}
		return participantes;
	}
	public ArrayList<InfosModelCategorie> getListeCategories() {
		ArrayList<InfosModelCategorie> categories = new ArrayList<InfosModelCategorie>();
		for(ModelCategorie categorie : this.frontModel.getConcours().getCategories()) {
			categories.add(categorie.toInfos());
		}
		return categories;
	}
	public ArrayList<InfosModelPoule> getListePoules(String nomCategorie) {
		ArrayList<InfosModelPoule> poules = new ArrayList<InfosModelPoule>();
		for(ModelPoule poule : this.frontModel.getConcours().getCategorieByNom(nomCategorie).getPoules()) {
			poules.add(poule.toInfos());
		}
		return poules;
	}

	
}
