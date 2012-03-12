package org.contestorg.models;

import java.util.ArrayList;

import javax.swing.tree.TreeModel;

import org.contestorg.common.ContestOrgErrorException;
import org.contestorg.common.ContestOrgWarningException;
import org.contestorg.common.Pair;
import org.contestorg.common.Quintuple;
import org.contestorg.common.TrackableList;
import org.contestorg.infos.Configuration;
import org.contestorg.infos.Couple;
import org.contestorg.infos.InfosModelCategorie;
import org.contestorg.infos.InfosModelConcours;
import org.contestorg.infos.InfosModelParticipant;
import org.contestorg.infos.InfosModelPhaseQualificative;
import org.contestorg.infos.InfosModelPoule;
import org.contestorg.infos.InfosModelProprietePossedee;
import org.contestorg.interfaces.IClosableTableModel;
import org.contestorg.interfaces.ITrackableListValidator;

/**
 * Point d'entrée aux modèles concernant les participants
 */
public class FrontModelParticipants
{
	/** Point d'entrée principal aux modèles */
	private FrontModel frontModel;
	
	// TreeModels
	
	/** TreeModel des catégories */
	private TreeModelParticipants tm_categories;

	/** TreeModel des poules */
	private TreeModelParticipants tm_poules;
	
	/** TreeModel des participants */
	private TreeModelParticipants tm_participants;
	
	/**
	 * Constructeur
	 * @param frontModel point d'entrée principal aux modèles
	 */
	public FrontModelParticipants(FrontModel frontModel) {
		this.frontModel = frontModel;
	}
	
	// Récupérer des données calculées
	
	/**
	 * Récupérer le nombre de participants
	 * @return nombre de participants
	 */
	public int getNbParticipants() {
		return this.frontModel.getConcours().getNbParticipants();
	}
	
	/**
	 * Récupérer le nombre de participants d'une catégorie
	 * @param nomCategorie nom de la catégorie
	 * @return nombre de participants de la catégorie
	 */
	public int getNbParticipants(String nomCategorie) {
		return this.frontModel.getConcours().getCategorieByNom(nomCategorie).getNbParticipants();
	}
	
	/**
	 * Vérifier si un participant existe
	 * @param nomParticipant nom du participant
	 * @return participant existant ?
	 */
	public boolean isParticipantExiste (String nomParticipant) {
		return this.frontModel.getConcours().getParticipantByNom(nomParticipant) != null;
	}
	
	/**
	 * Récupérer le nombre de villes communes au sein d'une configuration
	 * @param configuration configuration
	 * @return nombre de villes communes au sein de la configuration
	 */
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
	
	/**
	 * Récupérer les informations d'un participant
	 * @param nomParticipant nom du participant
	 * @return informations du participant
	 */
	public Quintuple<String, String, InfosModelParticipant, ArrayList<Pair<String, InfosModelProprietePossedee>>, ArrayList<String>> getInfosParticipant (String nomParticipant) {
		// Récupérer le participant
		ModelParticipant participant = this.frontModel.getConcours().getParticipantByNom(nomParticipant);
		
		// Récupérer la liste des propriétés
		ArrayList<Pair<String, InfosModelProprietePossedee>> proprietes = new ArrayList<Pair<String, InfosModelProprietePossedee>>();
		for (ModelProprietePossedee propriete : participant.getProprietesPossedees()) {
			proprietes.add(new Pair<String, InfosModelProprietePossedee>(propriete.getPropriete().getNom(), propriete.getInfos()));
		}
		
		// Récupérer la liste des prix
		ArrayList<String> prixs = new ArrayList<String>();
		for (ModelPrix prix : participant.getPrix()) {
			prixs.add(prix.getNom());
		}
		
		// Retourner les informations du participant
		return new Quintuple<String, String, InfosModelParticipant, ArrayList<Pair<String, InfosModelProprietePossedee>>, ArrayList<String>>(participant.getPoule().getCategorie().getNom(), participant.getPoule().getNom(), participant.getInfos(), proprietes, prixs);
	}

	// Modifier les catégories et les poules
	
	/**
	 * Mettre à jour les catégories
	 * @param categories informations sur les catégories
	 * @throws ContestOrgErrorException
	 */
	public void updateCategories(TrackableList<InfosModelCategorie> categories) throws ContestOrgErrorException {
		// Démarrer l'action de modification
		this.frontModel.getHistory().start("Modification des catégories");
		
		// Modifier les catégories
		this.frontModel.getConcours().updateCategories(categories);
		
		// Fermer l'action de modification
		this.frontModel.getHistory().close();
	}
	
	/**
	 * Mettre à jour les poules
	 * @param categoriesPoules informations sur les poules
	 * @throws ContestOrgErrorException
	 */
	public void updatePoules(ArrayList<Pair<String,TrackableList<Pair<InfosModelPoule, ArrayList<String>>>>> categoriesPoules) throws ContestOrgErrorException {
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
	
	/**
	 * Ajouter un participant
	 * @param infos informations du participant
	 * @throws ContestOrgErrorException
	 */
	public void addParticipant (Quintuple<String, String, InfosModelParticipant, TrackableList<Pair<String, InfosModelProprietePossedee>>, TrackableList<String>> infos) throws ContestOrgErrorException {
		// Démarrer l'action de création
		this.frontModel.getHistory().start("Ajout du participant \"" + infos.getThird().getNom() + "\"");
		
		// Ajouter le participant
		new ModelParticipant.Updater(this.frontModel.getConcours()).create(infos);
		
		// Fermer l'action de création
		this.frontModel.getHistory().close();
	}
	
	/**
	 * Modifier un participant
	 * @param nomParticipant nom du participant
	 * @param infos nouvelles informations du participant
	 * @throws ContestOrgErrorException
	 */
	public void updateParticipant (String nomParticipant, Quintuple<String, String, InfosModelParticipant, TrackableList<Pair<String, InfosModelProprietePossedee>>, TrackableList<String>> infos) throws ContestOrgErrorException {
		// Récupérer le participant
		ModelParticipant participant = this.frontModel.getConcours().getParticipantByNom(nomParticipant);
		
		// Démarrer l'action de modification
		this.frontModel.getHistory().start("Modification du participant \"" + participant.getNom() + "\"");
		
		// Modifier le participant
		new ModelParticipant.Updater(this.frontModel.getConcours()).update(participant, infos);
		
		// Fermer l'action de modification
		this.frontModel.getHistory().close();
	}
	
	/**
	 * Modifier le statut d'un participant
	 * @param nomParticipant nom du participant
	 * @param statut nouveau statut du participant
	 */
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
	
	/**
	 * Modifier le statut d'une liste de participants
	 * @param nomParticipants nom des participants
	 * @param statut nouveau statut des participants
	 */
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
	
	/**
	 * Supprimer un participant
	 * @param nomParticipant nom du participant
	 * @throws ContestOrgErrorException
	 * @throws ContestOrgWarningException 
	 */
	public void removeParticipant (String nomParticipant) throws ContestOrgErrorException, ContestOrgWarningException {
		// Récupérer le participant
		ModelParticipant participant = this.frontModel.getConcours().getParticipantByNom(nomParticipant);
		
		// Vérifier si le participant peut être participant
		for(ModelParticipation participation : participant.getParticipations()) {
			// Vérifier si la participation est associé à un match effectué des phases éliminatoires
			ModelMatchAbstract match = participation.getMatch();
			if(match != null && match instanceof ModelMatchPhasesElims) {
				throw new ContestOrgWarningException(FrontModel.get().getConcours().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ?
					"L'équipe "+participant.getNom()+" ne peut pas être supprimée car elle participe à un match des phases éliminatoires." :
					"Le joueur "+participant.getNom()+" ne peut pas être supprimé car il participe à un match des phases éliminatoires."
				);
			}
		}
		
		// Démarrer l'action de suppression
		this.frontModel.getHistory().start("Suppression du participant \"" + participant.getNom() + "\"");
		
		// Supprimer le participant
		participant.delete();
		
		// Fermer l'action de suppression
		this.frontModel.getHistory().close();
	}
	
	/**
	 * Supprimer une liste de participants
	 * @param nomParticipants nom des participants
	 * @throws ContestOrgErrorException
	 * @throws ContestOrgWarningException 
	 */
	public void removeParticipants (ArrayList<String> nomParticipants) throws ContestOrgErrorException, ContestOrgWarningException {
		// Pour chaque participant
		for(String nomParticipant : nomParticipants) {
			// Récupérer le participant
			ModelParticipant participant = this.frontModel.getConcours().getParticipantByNom(nomParticipant);
			
			// Vérifier si le participant peut être participant
			for(ModelParticipation participation : participant.getParticipations()) {
				// Vérifier si la participation est associé à un match des phases éliminatoires
				ModelMatchAbstract match = participation.getMatch();
				if(match != null && match instanceof ModelMatchPhasesElims) {
					throw new ContestOrgWarningException(FrontModel.get().getConcours().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ?
						"L'équipe "+participant.getNom()+" ne peut pas être supprimée car elle participe à un match des phases éliminatoires." :
						"Le joueur "+participant.getNom()+" ne peut pas être supprimé car il participe à un match des phases éliminatoires."
					);
				}
			}
		}
		
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
	
	/**
	 * Récupérer le TreeModel des catégories
	 * @return TreeModel des catégories
	 */
	public TreeModel getTreeModelCategories () {
		if (this.tm_categories == null) {
			this.tm_categories = new TreeModelParticipants(false, false);
		}
		return this.tm_categories;
	}
	
	/**
	 * Récupérer le TreeModel des poules
	 * @return TreeModel des poules
	 */
	public TreeModel getTreeModelPoules () {
		if (this.tm_poules == null) {
			this.tm_poules = new TreeModelParticipants(true, false);
		}
		return this.tm_poules;
	}
	
	/**
	 * Récupérer le TreeModel des participants
	 * @return TreeModel des participants
	 */
	public TreeModel getTreeModelParticipants () {
		if (this.tm_participants == null) {
			this.tm_participants = new TreeModelParticipants(true, true);
		}
		return this.tm_participants;
	}
	
	// Récupérer des TableModels
	
	/**
	 * Récupérer le TableModel de tous les participants
	 * @return TableModel de tous les participants
	 */
	public IClosableTableModel getTableModelParticipants () {
		return new TableModelParticipants(this.frontModel.getConcours());
	}
	
	/**
	 * Récupérer le TableModel des participants d'une catégorie donnée
	 * @param nomCategorie nom de la catégorie
	 * @return TableModel des participants de la catégorie
	 */
	public IClosableTableModel getTableModelParticipants (String nomCategorie) {
		return new TableModelParticipants(this.frontModel.getConcours().getCategorieByNom(nomCategorie));
	}
	
	/**
	 * Récupérer le TableModel des participants d'une poule d'une catégorie donnée
	 * @param nomCategorie nom de la catégorie
	 * @param nomPoule nom de la poule
	 * @return TableModel des participants de la poule de la catégorie
	 */
	public IClosableTableModel getTableModelParticipants (String nomCategorie, String nomPoule) {
		return new TableModelParticipants(this.frontModel.getConcours().getCategorieByNom(nomCategorie).getPouleByNom(nomPoule));
	}
	
	// Récupérer des validateurs de listes
	
	/**
	 * Récupérer un validateur de liste de catégories
	 * @return validateur de liste de catégories
	 */
	public ITrackableListValidator<InfosModelCategorie> getCategoriesValidator() {
		return new ModelCategorie.ValidatorForConcours();
	}
	
	/**
	 * Récupérer un validateur de liste de poules
	 * @return validateur de liste de poules
	 */
	public ITrackableListValidator<Pair<InfosModelPoule, ArrayList<String>>> getPoulesValidator() {
		return new ModelPoule.ValidatorForCategorie();
	}
	
	// Récupérer des données de liste
	
	/**
	 * Récupérer la structure des catégories et poules
	 * @return structure des catégories et poules
	 */
	public ArrayList<Pair<InfosModelCategorie, ArrayList<InfosModelPoule>>> getListeCategoriesPoules () {
		ArrayList<Pair<InfosModelCategorie, ArrayList<InfosModelPoule>>> categories = new ArrayList<Pair<InfosModelCategorie, ArrayList<InfosModelPoule>>>();
		for (ModelCategorie categorie : this.frontModel.getConcours().getCategories()) {
			ArrayList<InfosModelPoule> poules = new ArrayList<InfosModelPoule>();
			for (ModelPoule poule : categorie.getPoules()) {
				poules.add(poule.getInfos());
			}
			categories.add(new Pair<InfosModelCategorie, ArrayList<InfosModelPoule>>(categorie.getInfos(), poules));
		}
		return categories;
	}
	
	/**
	 * Récupérer la structure des catégories, poules et participants
	 * @return structure des catégories, poules et participants
	 */
	public ArrayList<Pair<InfosModelCategorie, ArrayList<Pair<InfosModelPoule,ArrayList<InfosModelParticipant>>>>> getListeCategoriesPoulesParticipants() {
		ArrayList<Pair<InfosModelCategorie, ArrayList<Pair<InfosModelPoule,ArrayList<InfosModelParticipant>>>>> categories = new ArrayList<Pair<InfosModelCategorie,ArrayList<Pair<InfosModelPoule,ArrayList<InfosModelParticipant>>>>>();
		for(ModelCategorie categorie : this.frontModel.getConcours().getCategories()) {
			ArrayList<Pair<InfosModelPoule,ArrayList<InfosModelParticipant>>> poules = new ArrayList<Pair<InfosModelPoule,ArrayList<InfosModelParticipant>>>();
			for(ModelPoule poule : categorie.getPoules()) {
				ArrayList<InfosModelParticipant> participants = new ArrayList<InfosModelParticipant>();
				for(ModelParticipant participant : poule.getParticipants()) {
					participants.add(participant.getInfos());
				}
				poules.add(new Pair<InfosModelPoule, ArrayList<InfosModelParticipant>>(poule.getInfos(), participants));
			}
			categories.add(new Pair<InfosModelCategorie, ArrayList<Pair<InfosModelPoule,ArrayList<InfosModelParticipant>>>>(categorie.getInfos(), poules));
		}
		return categories;
	}
	
	/**
	 * Récupérer la structure catégories, poules et phases qualificatives
	 * @return structure catégories, poules et phases qualificatives
	 */
	public ArrayList<Pair<InfosModelCategorie, ArrayList<Pair<InfosModelPoule,ArrayList<InfosModelPhaseQualificative>>>>> getListeCategoriesPoulesPhases() {
		ArrayList<Pair<InfosModelCategorie, ArrayList<Pair<InfosModelPoule,ArrayList<InfosModelPhaseQualificative>>>>> categories = new ArrayList<Pair<InfosModelCategorie,ArrayList<Pair<InfosModelPoule,ArrayList<InfosModelPhaseQualificative>>>>>();
		for(ModelCategorie categorie : this.frontModel.getConcours().getCategories()) {
			ArrayList<Pair<InfosModelPoule,ArrayList<InfosModelPhaseQualificative>>> poules = new ArrayList<Pair<InfosModelPoule,ArrayList<InfosModelPhaseQualificative>>>();
			for(ModelPoule poule : categorie.getPoules()) {
				ArrayList<InfosModelPhaseQualificative> phases = new ArrayList<InfosModelPhaseQualificative>();
				for(ModelPhaseQualificative phase : poule.getPhasesQualificatives()) {
					phases.add(phase.getInfos());
				}
				poules.add(new Pair<InfosModelPoule, ArrayList<InfosModelPhaseQualificative>>(poule.getInfos(), phases));
			}
			categories.add(new Pair<InfosModelCategorie, ArrayList<Pair<InfosModelPoule,ArrayList<InfosModelPhaseQualificative>>>>(categorie.getInfos(), poules));
		}
		return categories;
	}
	
	/**
	 * Récupérer la liste des participants d'une poule donnée d'une catégorie donnée qui peuvent participer
	 * @param nomCategorie nom de la catégorie
	 * @param nomPoule nom de la poule
	 * @return liste des participants de la poule de la catégorie qui peuvent participer
	 */
	public ArrayList<String> getListeParticipantsParticipants(String nomCategorie,String nomPoule) {
		ArrayList<String> participantes = new ArrayList<String>();
		for(ModelParticipant participant : this.frontModel.getConcours().getCategorieByNom(nomCategorie).getPouleByNom(nomPoule).getParticipantsParticipants()) {
			participantes.add(participant.getNom());
		}
		return participantes;
	}
	
	/**
	 * Récupérer la liste des participants d'une catégorie donnée pouvant participer
	 * @param nomCategorie nom de la catégorie
	 * @return liste des participants de la catégorie pouvant participer
	 */
	public ArrayList<String> getListeParticipantsParticipants(String nomCategorie) {
		ArrayList<String> participantes = new ArrayList<String>();
		for(ModelParticipant participant : this.frontModel.getConcours().getCategorieByNom(nomCategorie).getParticipantsParticipants()) {
			participantes.add(participant.getNom());
		}
		return participantes;
	}
	
	/**
	 * Récupérer la liste des catégories
	 * @return liste des catégories
	 */
	public ArrayList<InfosModelCategorie> getListeCategories() {
		ArrayList<InfosModelCategorie> categories = new ArrayList<InfosModelCategorie>();
		for(ModelCategorie categorie : this.frontModel.getConcours().getCategories()) {
			categories.add(categorie.getInfos());
		}
		return categories;
	}
	
	/**
	 * Récupérer la liste des poules d'une catégorie donnée
	 * @param nomCategorie nom de la catégorie
	 * @return liste des poules de la catégorie
	 */
	public ArrayList<InfosModelPoule> getListePoules(String nomCategorie) {
		ArrayList<InfosModelPoule> poules = new ArrayList<InfosModelPoule>();
		for(ModelPoule poule : this.frontModel.getConcours().getCategorieByNom(nomCategorie).getPoules()) {
			poules.add(poule.getInfos());
		}
		return poules;
	}

	
}
