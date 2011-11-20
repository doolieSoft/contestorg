package org.contestorg.models;


import java.util.ArrayList;

import javax.swing.tree.TreeModel;

import org.contestorg.common.GenerationDecorator;
import org.contestorg.common.Pair;
import org.contestorg.common.Quadruple;
import org.contestorg.common.Quintuple;
import org.contestorg.common.TrackableList;
import org.contestorg.common.Triple;
import org.contestorg.comparators.CompGenerationPhasesQualifs;
import org.contestorg.events.History;
import org.contestorg.infos.Configuration;
import org.contestorg.infos.Couple;
import org.contestorg.infos.InfosModelCategorie;
import org.contestorg.infos.InfosModelChemin;
import org.contestorg.infos.InfosModelCompPhasesQualifsAbstract;
import org.contestorg.infos.InfosModelConcours;
import org.contestorg.infos.InfosModelDiffusion;
import org.contestorg.infos.InfosModelEmplacement;
import org.contestorg.infos.InfosModelParticipant;
import org.contestorg.infos.InfosModelExportation;
import org.contestorg.infos.InfosModelHoraire;
import org.contestorg.infos.InfosModelLieu;
import org.contestorg.infos.InfosModelMatchPhasesElims;
import org.contestorg.infos.InfosModelMatchPhasesQualifs;
import org.contestorg.infos.InfosModelObjectif;
import org.contestorg.infos.InfosModelParticipation;
import org.contestorg.infos.InfosModelParticipationObjectif;
import org.contestorg.infos.InfosModelPhaseEliminatoires;
import org.contestorg.infos.InfosModelPhaseQualificative;
import org.contestorg.infos.InfosModelPoule;
import org.contestorg.infos.InfosModelPrix;
import org.contestorg.infos.InfosModelPropriete;
import org.contestorg.infos.InfosModelProprieteParticipant;
import org.contestorg.infos.InfosModelTheme;
import org.contestorg.interfaces.IClosableTableModel;
import org.contestorg.interfaces.IGeneration;
import org.contestorg.interfaces.IGraphModel;
import org.contestorg.interfaces.IListValidator;
import org.contestorg.interfaces.ITransformer;



public class FrontModel
{
	// Instance unique de front
	private static FrontModel front;
	
	// Concours
	private ModelConcours concours;
	
	// Historique
	private History history;
	
	// TreeModels
	private TreeModelParticipants tm_categories;
	private TreeModelParticipants tm_poules;
	private TreeModelParticipants tm_participants;
	private TreeModelPhasesQualifs tm_qualifs;
	
	// Récupérer l'instance du front
	public static FrontModel get () {
		// Créer l'instance du FrontModel si nécéssaire
		if (FrontModel.front == null) {
			// Créer l'instance du FrontModel
			FrontModel.front = new FrontModel();
			
			// Créer l'historique et l'abonner aux évenements du concours
			FrontModel.front.history = new History();
			ModelAbstract.addStaticListener(FrontModel.front.history);
		}
		
		// Retourner l'instance de Front
		return FrontModel.front;
	}
	
	// Récupérer le concours
	public ModelConcours getConcours () {
		return this.concours;
	}
	
	// Récupérer l'historique
	public History getHistory () {
		return this.history;
	}
	
	// Nettoyer les modeles
	public void clean() {
		// Réinitialiser l'historique
		this.history.init();
		
		// Nettoyer les propriétés statiques de ModelAbstract
		ModelAbstract.clean();
	}
	
	// Fermer le concours
	public void close () throws ContestOrgModelException {
		if (this.concours != null) {
			// Vider le concours et en perdre la référence
			this.history.start("Fermeture du concours");
			this.concours.delete();
			this.concours = null;
			this.history.close();
		}
		
		// Nettoyer les modèles
		this.clean();
	}
	
	// Créer/Configurer un nouveau concours
	public void nouveauConcours (ModelConcours concours) throws ContestOrgModelException {
		// Démarrer l'action de création
		this.history.start("Chargement du concours");
		
		// Retenir le nouveau concours
		this.concours = concours;
		
		// Fermer l'action de création
		this.history.close();
		
		// Initialiser l'historique
		this.history.init();
	}
	public void nouveauConcours (InfosModelConcours infos) throws ContestOrgModelException {
		// Démarrer l'action de création
		this.history.start("Création du concours");
		
		// Créer et retenir le nouveau concours
		this.concours = new ModelConcours(infos);
		
		// Créer la catégorie et la poule par défaut
		this.concours.addCategorie(new ModelCategorie.UpdaterForConcours(this.concours).create(new InfosModelCategorie("Défaut")));
		
		// Fermer l'action de création
		this.history.close();
		
		// Initialiser l'historique
		this.history.init();
	}
	public void configurerConcours (InfosModelConcours infos, TrackableList<InfosModelObjectif> objectifs, TrackableList<InfosModelCompPhasesQualifsAbstract> comparateurs, TrackableList<Triple<InfosModelExportation, InfosModelChemin, InfosModelTheme>> exportations, int publication, TrackableList<Pair<InfosModelDiffusion, InfosModelTheme>> diffusions, TrackableList<InfosModelPrix> prix, TrackableList<Triple<InfosModelLieu, TrackableList<InfosModelEmplacement>, TrackableList<InfosModelHoraire>>> lieux, TrackableList<InfosModelPropriete> proprietes) throws ContestOrgModelException {
		// Démarrer l'action de configuration
		this.history.start("Configuration du concours");
		
		// Configurer le concours
		this.concours.setInfos(infos);
		
		// Mettre à jour les données du concours
		this.concours.updateObjectifs(objectifs);
		this.concours.updateCompPhasesQualifs(comparateurs);
		this.concours.updateExportations(exportations);
		this.concours.setPublication(publication == -1 ? null : this.concours.getExportations().get(publication));
		this.concours.updateDiffusions(diffusions);
		this.concours.updatePrix(prix);
		this.concours.updateLieux(lieux);
		this.concours.updateProprietes(proprietes);
		
		// Fermer l'action de configuration
		this.history.close();
	}
	
	// Récupérer des données calculées
	public int getNbParticipants() {
		return this.concours.getNbParticipants();
	}
	public boolean isParticipantExiste (String nomParticipant) {
		return this.concours.getParticipantByNom(nomParticipant) != null;
	}
	public int getNbVillesCommunes(Configuration<String> configuration) {
		int nbVillesCommunes = 0;
		for(Couple<String> couple : configuration.getCouples()) {
			if(couple.getParticipantA() != null && couple.getParticipantB() != null) {
				ModelParticipant participantA = this.concours.getParticipantByNom(couple.getParticipantA());
				ModelParticipant participantB = this.concours.getParticipantByNom(couple.getParticipantB());
				if(participantA.getVille() != null && !participantA.getVille().isEmpty() && participantB.getVille() != null && !participantB.getVille().isEmpty() && participantA.getVille().equals(participantB.getVille())) {
					nbVillesCommunes++;
				}
			}
		}
		return nbVillesCommunes;
	}
	public int getNbMatchDejaJoues(Configuration<String> configuration) {
		int nbMatchDejaJoues = 0;
		for(Couple<String> couple : configuration.getCouples()) {
			if(couple.getParticipantA() != null && couple.getParticipantB() != null) {
				ModelParticipant participantA = this.concours.getParticipantByNom(couple.getParticipantA());
				ModelParticipant participantB = this.concours.getParticipantByNom(couple.getParticipantB());
				nbMatchDejaJoues += participantA.getNbRencontres(participantB);
			}
		}
		return nbMatchDejaJoues;
	}
	public boolean isParticipantPhasesQualif(String nomCategorie, String nomPoule, int numeroPhase, String nomParticipant) {
		ModelPhaseQualificative phase = this.concours.getCategorieByNom(nomCategorie).getPouleByNom(nomPoule).getPhasesQualificatives().get(numeroPhase);
		for(ModelMatchPhasesQualifs match : phase.getMatchs()) {
			if(match.getParticipationA().getParticipant() != null && match.getParticipationA().getParticipant().getNom().equals(nomParticipant)) {
				return true;
			}
			if(match.getParticipationB().getParticipant() != null && match.getParticipationB().getParticipant().getNom().equals(nomParticipant)) {
				return true;
			}
		}
		return false;
	}
	public Quadruple<String,String,Integer,Integer> getCategoriePoulePhase(String nomCategorie,String nomPoule,Integer numeroPhase,int numeroMatch) {
		// Récupérer les informations
		ModelCategorie categorie = null;
		ModelPoule poule = null;
		ModelPhaseQualificative phase = null;
		ModelMatchPhasesQualifs match;
		if(nomCategorie == null) {
			match = this.concours.getMatchsPhasesQualifs().get(numeroMatch);
			phase = match.getPhaseQualificative();
			poule = phase.getPoule();
			categorie = poule.getCategorie();
		} else if(nomPoule == null) {
			categorie = this.concours.getCategorieByNom(nomCategorie);
			match = categorie.getMatchsPhasesQualifs().get(numeroMatch);
			phase = match.getPhaseQualificative();
			poule = phase.getPoule();
		} else if(numeroPhase == null) {
			categorie = this.concours.getCategorieByNom(nomCategorie);
			poule = categorie.getPouleByNom(nomPoule);
			match = poule.getMatchsPhasesQualifs().get(numeroMatch);
			phase = match.getPhaseQualificative();
		} else {
			return new Quadruple<String,String,Integer,Integer>(nomCategorie,nomPoule,numeroPhase,numeroMatch);
		}
		
		// Retourner les informations
		return new Quadruple<String,String,Integer,Integer>(categorie.getNom(),poule.getNom(),phase.getNumero(),match.getNumero());
	}
	public int getRangParticipantPhasesQualificatives(String nomParticipant) {
		return this.concours.getParticipantByNom(nomParticipant).getRangPhasesQualifs();
	}
	public int getRangParticipantPhasesEliminatoires(String nomParticipant) {
		return this.concours.getParticipantByNom(nomParticipant).getRangPhasesElims();
	}
	public int getNbPhasesElimsPossibles(String nomCategorie) {
		// Récupérer le nombre de participants
		int nbParticipants = this.concours.getCategorieByNom(nomCategorie).getParticipantsParticipants().size();
		
		// Compter le nombre de phases possibles
		int nbPhases = 0;
		for(int i=nbParticipants;i>1;i=i/2) {
			nbPhases++;
		}
		
		// Retourner le nombre de phases possibles
		return nbPhases;
	}
	public int getNbPhasesElims(String nomCategorie) {
		ModelCategorie categorie = this.concours.getCategorieByNom(nomCategorie);
		if(categorie != null && categorie.getPhasesEliminatoires() != null) {
			return categorie.getPhasesEliminatoires().getNbPhases();
		} else {
			return 0;
		}
	}
	public int getNbRencontres(String nomParticipantA, String nomParticipantB) {
		return nomParticipantA != null ? this.concours.getParticipantByNom(nomParticipantA).getNbRencontres(this.concours.getParticipantByNom(nomParticipantB)) : (nomParticipantB == null ? 0 : this.concours.getParticipantByNom(nomParticipantB).getNbRencontres(this.concours.getParticipantByNom(nomParticipantA)));
	}
	public boolean isMatchPhasesElimsResultatsEditables(String nomCategorie, int numeroMatch) {
		// Récupérer le match
		ModelMatchPhasesElims match = this.concours.getCategorieByNom(nomCategorie).getPhasesEliminatoires().getMatch(numeroMatch);
		
		// Vérifier si le match suivant est joué
		return match.getMatchSuivant() == null || !match.getMatchSuivant().isEffectue() && (!match.getMatchSuivant().isGrandeFinale() || !match.getPhasesEliminatoire().getPetiteFinale().isEffectue());
	}
	
	// Récupérer des données unique
	public int getPublicationIndex () {
		if (concours.getPublication() != null) {
			return concours.getExportations().indexOf(concours.getPublication());
		}
		return -1;
	}
	public Triple<InfosModelExportation, InfosModelChemin, InfosModelTheme> getInfosPublication () {
		if (concours.getPublication() != null) {
			return new Triple<InfosModelExportation, InfosModelChemin, InfosModelTheme>(concours.getPublication().toInfos(), concours.getPublication().getChemin().toInfos(), concours.getPublication().getTheme().toInfos());
		}
		return null;
	}
	public Quintuple<String, String, InfosModelParticipant, ArrayList<Pair<String, InfosModelProprieteParticipant>>, ArrayList<String>> getInfosParticipant (String nomParticipant) {
		// Récupérer le participant
		ModelParticipant participant = this.concours.getParticipantByNom(nomParticipant);
		
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
	public Triple<Triple<String, ArrayList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>, Triple<String, ArrayList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>,InfosModelMatchPhasesQualifs> getInfosMatchPhaseQualif(String nomCategorie, String nomPoule, Integer numeroPhase, int numeroMatch) {
		// Récupérer le match
		ModelMatchPhasesQualifs match;
		if(nomCategorie == null) {
			match = this.concours.getMatchsPhasesQualifs().get(numeroMatch);
		} else if(nomPoule == null) {
			match = this.concours.getCategorieByNom(nomCategorie).getMatchsPhasesQualifs().get(numeroMatch);
		} else if(numeroPhase == null) {
			match = this.concours.getCategorieByNom(nomCategorie).getPouleByNom(nomPoule).getMatchsPhasesQualifs().get(numeroMatch);
		} else {
			match = this.concours.getCategorieByNom(nomCategorie).getPouleByNom(nomPoule).getPhasesQualificatives().get(numeroPhase).getMatchs().get(numeroMatch);
		}
		
		// Récupérer les informations du match
		Pair<Triple<String, ArrayList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>, Triple<String, ArrayList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>> infos = this.getInfosMatch(match);
		
		// Retourner les informations du match
		return new Triple<Triple<String,ArrayList<Pair<String,InfosModelParticipationObjectif>>,InfosModelParticipation>, Triple<String,ArrayList<Pair<String,InfosModelParticipationObjectif>>,InfosModelParticipation>, InfosModelMatchPhasesQualifs>(infos.getFirst(), infos.getSecond(), match.toInfos());
	}
	public Triple<Triple<String, ArrayList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>, Triple<String, ArrayList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>,InfosModelMatchPhasesElims> getInfosMatchPhaseElims(String nomCategorie, int numeroMatch) {
		// Récupérer le match
		ModelMatchPhasesElims match = this.concours.getCategorieByNom(nomCategorie).getPhasesEliminatoires().getMatch(numeroMatch);
		
		// Récupérer les informations du match
		Pair<Triple<String, ArrayList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>, Triple<String, ArrayList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>> infos = this.getInfosMatch(match);
		
		// Retourner les informations du match
		return new Triple<Triple<String,ArrayList<Pair<String,InfosModelParticipationObjectif>>,InfosModelParticipation>, Triple<String,ArrayList<Pair<String,InfosModelParticipationObjectif>>,InfosModelParticipation>, InfosModelMatchPhasesElims>(infos.getFirst(), infos.getSecond(), match.toInfos());
	}
	public Triple<Triple<String, ArrayList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>, Triple<String, ArrayList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>,InfosModelMatchPhasesElims> getInfosMatchPetiteFinale(String nomCategorie) {
		// Récupérer le match
		ModelMatchPhasesElims match = this.concours.getCategorieByNom(nomCategorie).getPhasesEliminatoires().getPetiteFinale();
		
		// Récupérer les informations du match
		Pair<Triple<String, ArrayList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>, Triple<String, ArrayList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>> infos = this.getInfosMatch(match);
		
		// Retourner les informations du match
		return new Triple<Triple<String,ArrayList<Pair<String,InfosModelParticipationObjectif>>,InfosModelParticipation>, Triple<String,ArrayList<Pair<String,InfosModelParticipationObjectif>>,InfosModelParticipation>, InfosModelMatchPhasesElims>(infos.getFirst(), infos.getSecond(), match.toInfos());
	}
	private Pair<Triple<String, ArrayList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>, Triple<String, ArrayList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>> getInfosMatch(ModelMatchAbstract match) {
		// Récupérer les informations de participation du participant A
		String nomParticipantA = match.getParticipationA() == null || match.getParticipationA().getParticipant() == null ? null : match.getParticipationA().getParticipant().getNom();
		ArrayList<Pair<String, InfosModelParticipationObjectif>> objectifsRemportesA = new ArrayList<Pair<String,InfosModelParticipationObjectif>>();
		for(ModelObjectifRemporte objectifRemporte : match.getParticipationA().getObjectifsRemportes()) {
			objectifsRemportesA.add(new Pair<String, InfosModelParticipationObjectif>(objectifRemporte.getObjectif().getNom(), objectifRemporte.toInfos()));
		}
		Triple<String, ArrayList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation> infosParticipationA = new Triple<String, ArrayList<Pair<String,InfosModelParticipationObjectif>>, InfosModelParticipation>(nomParticipantA, objectifsRemportesA, match.getParticipationA().toInfos());
		
		// Récupérer les informations de participation du participant B
		String nomParticipantB = match.getParticipationB() == null || match.getParticipationB().getParticipant() == null ? null : match.getParticipationB().getParticipant().getNom();
		ArrayList<Pair<String, InfosModelParticipationObjectif>> objectifsRemportesB = new ArrayList<Pair<String,InfosModelParticipationObjectif>>();
		for(ModelObjectifRemporte objectifRemporte : match.getParticipationB().getObjectifsRemportes()) {
			objectifsRemportesB.add(new Pair<String, InfosModelParticipationObjectif>(objectifRemporte.getObjectif().getNom(), objectifRemporte.toInfos()));
		}
		Triple<String, ArrayList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation> infosParticipationB = new Triple<String, ArrayList<Pair<String,InfosModelParticipationObjectif>>, InfosModelParticipation>(nomParticipantB, objectifsRemportesB, match.getParticipationB().toInfos());
		
		// Assembler les informations demandées et les retourner
		return new Pair<Triple<String,ArrayList<Pair<String,InfosModelParticipationObjectif>>,InfosModelParticipation>, Triple<String,ArrayList<Pair<String,InfosModelParticipationObjectif>>,InfosModelParticipation>>(infosParticipationA, infosParticipationB);
	}
	public Triple<Configuration<String>,InfosModelPhaseQualificative,InfosModelMatchPhasesQualifs> getInfosPhaseQualif(String nomCategorie,String nomPoule,int numeroPhase) {
		// Récupérer la phase qualificative
		ModelPhaseQualificative phase = this.concours.getCategorieByNom(nomCategorie).getPouleByNom(nomPoule).getPhasesQualificatives().get(numeroPhase);

		// Construire la configuration
		Configuration<String> configuration = new Configuration<String>(phase.getMatchs().size());
		for(ModelMatchPhasesQualifs match : phase.getMatchs()) {
			ModelParticipant participantA = match.getParticipationA().getParticipant();
			ModelParticipant participantB = match.getParticipationB().getParticipant();
			configuration.addCouple(new Couple<String>(participantA == null ? null : participantA.getNom(), participantB == null ? null : participantB.getNom()));
		}
		
		// Récupérer les informations de la phase qualificative
		InfosModelPhaseQualificative infosPhase = phase.toInfos();
		
		// Récupérer les informations des matchs de la phase qualificative
		InfosModelMatchPhasesQualifs infosMatchs = new InfosModelMatchPhasesQualifs(null,null);
		
		// Assembler les informations demandées et les retourner
		return new Triple<Configuration<String>, InfosModelPhaseQualificative, InfosModelMatchPhasesQualifs>(configuration, infosPhase, infosMatchs);
	}
	
	// Récupérer des données liste
	public ArrayList<InfosModelObjectif> getListeObjectifs () {
		ArrayList<InfosModelObjectif> objectifs = new ArrayList<InfosModelObjectif>();
		for (ModelObjectif objectif : concours.getObjectifs()) {
			objectifs.add(objectif.toInfos());
		}
		return objectifs;
	}
	public ArrayList<Triple<InfosModelExportation, InfosModelChemin, InfosModelTheme>> getListeExportations () {
		ArrayList<Triple<InfosModelExportation, InfosModelChemin, InfosModelTheme>> exportations = new ArrayList<Triple<InfosModelExportation, InfosModelChemin, InfosModelTheme>>();
		for (ModelExportation exportation : concours.getExportations()) {
			exportations.add(new Triple<InfosModelExportation, InfosModelChemin, InfosModelTheme>(exportation.toInfos(), exportation.getChemin().toInfos(), exportation.getTheme().toInfos()));
		}
		return exportations;
	}
	public ArrayList<Pair<InfosModelDiffusion, InfosModelTheme>> getListeDiffusions () {
		ArrayList<Pair<InfosModelDiffusion, InfosModelTheme>> diffusions = new ArrayList<Pair<InfosModelDiffusion, InfosModelTheme>>();
		for (ModelDiffusion diffusion : concours.getDiffusions()) {
			diffusions.add(new Pair<InfosModelDiffusion, InfosModelTheme>(diffusion.toInfos(), diffusion.getTheme().toInfos()));
		}
		return diffusions;
	}
	public ArrayList<InfosModelPrix> getListePrix () {
		ArrayList<InfosModelPrix> prixs = new ArrayList<InfosModelPrix>();
		for (ModelPrix prix : concours.getPrix()) {
			prixs.add(prix.toInfos());
		}
		return prixs;
	}
	public ArrayList<Triple<InfosModelLieu, ArrayList<InfosModelEmplacement>, ArrayList<InfosModelHoraire>>> getListeLieux () {
		ArrayList<Triple<InfosModelLieu, ArrayList<InfosModelEmplacement>, ArrayList<InfosModelHoraire>>> lieux = new ArrayList<Triple<InfosModelLieu, ArrayList<InfosModelEmplacement>, ArrayList<InfosModelHoraire>>>();
		for (ModelLieu lieu : concours.getLieux()) {
			ArrayList<InfosModelEmplacement> emplacements = new ArrayList<InfosModelEmplacement>();
			for (ModelEmplacement emplacement : lieu.getEmplacements()) {
				emplacements.add(emplacement.toInfos());
			}
			ArrayList<InfosModelHoraire> horaires = new ArrayList<InfosModelHoraire>();
			for (ModelHoraire horaire : lieu.getHoraires()) {
				horaires.add(horaire.toInfos());
			}
			lieux.add(new Triple<InfosModelLieu, ArrayList<InfosModelEmplacement>, ArrayList<InfosModelHoraire>>(lieu.toInfos(), emplacements, horaires));
		}
		return lieux;
	}
	public ArrayList<InfosModelPropriete> getListeProprietes () {
		ArrayList<InfosModelPropriete> proprietes = new ArrayList<InfosModelPropriete>();
		for (ModelPropriete propriete : concours.getProprietes()) {
			proprietes.add(propriete.toInfos());
		}
		return proprietes;
	}
	public ArrayList<Pair<InfosModelCategorie, ArrayList<InfosModelPoule>>> getListeCategoriesPoules () {
		ArrayList<Pair<InfosModelCategorie, ArrayList<InfosModelPoule>>> categories = new ArrayList<Pair<InfosModelCategorie, ArrayList<InfosModelPoule>>>();
		for (ModelCategorie categorie : this.concours.getCategories()) {
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
		for(ModelCategorie categorie : this.concours.getCategories()) {
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
		for(ModelCategorie categorie : this.concours.getCategories()) {
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
	public ArrayList<String> getListeParticipants(String nomCategorie,String nomPoule) {
		ArrayList<String> participantes = new ArrayList<String>();
		for(ModelParticipant participant : this.concours.getCategorieByNom(nomCategorie).getPouleByNom(nomPoule).getParticipantsParticipants()) {
			participantes.add(participant.getNom());
		}
		return participantes;
	}
	public ArrayList<String> getListeParticipants(String nomCategorie) {
		ArrayList<String> participantes = new ArrayList<String>();
		for(ModelParticipant participant : this.concours.getCategorieByNom(nomCategorie).getParticipantsParticipants()) {
			participantes.add(participant.getNom());
		}
		return participantes;
	}
	public ArrayList<InfosModelCategorie> getListeCategories() {
		ArrayList<InfosModelCategorie> categories = new ArrayList<InfosModelCategorie>();
		for(ModelCategorie categorie : this.concours.getCategories()) {
			categories.add(categorie.toInfos());
		}
		return categories;
	}
	public ArrayList<InfosModelPoule> getListePoules(String nomCategorie) {
		ArrayList<InfosModelPoule> poules = new ArrayList<InfosModelPoule>();
		for(ModelPoule poule : this.concours.getCategorieByNom(nomCategorie).getPoules()) {
			poules.add(poule.toInfos());
		}
		return poules;
	}
	public ArrayList<InfosModelCompPhasesQualifsAbstract> getListeComparateurs () {
		ArrayList<InfosModelCompPhasesQualifsAbstract> comparateurs = new ArrayList<InfosModelCompPhasesQualifsAbstract>();
		for (ModelCompPhasesQualifsAbstract comparateur : concours.getCompsPhasesQualifs()) {
			comparateurs.add(comparateur.toInfos());
		}
		return comparateurs;
	}
	
	// Récupérer les validateurs de listes
	public IListValidator<InfosModelObjectif> getObjectifsValidator () {
		return new ModelObjectif.ValidatorForConcours();
	}
	public IListValidator<InfosModelCompPhasesQualifsAbstract> getComparateursValidator () {
		return new ModelCompPhasesQualifsAbstract.ValidatorForConcours();
	}
	public IListValidator<Triple<InfosModelExportation, InfosModelChemin, InfosModelTheme>> getExportationsValidator () {
		return new ModelExportation.ValidatorForConcours();
	}
	public IListValidator<Pair<InfosModelDiffusion, InfosModelTheme>> getDiffusionsValidator () {
		return new ModelDiffusion.ValidatorForConcours();
	}
	public IListValidator<InfosModelPrix> getPrixValidator () {
		return new ModelPrix.ValidatorForConcours();
	}
	public IListValidator<Triple<InfosModelLieu, TrackableList<InfosModelEmplacement>, TrackableList<InfosModelHoraire>>> getLieuxValidator () {
		return new ModelLieu.ValidatorForConcours();
	}
	public IListValidator<InfosModelEmplacement> getEmplacementsValidator () {
		return new ModelEmplacement.ValidatorForLieu();
	}
	public IListValidator<InfosModelHoraire> getHorairesValidator () {
		return new ModelHoraire.ValidatorForLieu();
	}
	public IListValidator<InfosModelPropriete> getProprietesValidator () {
		return new ModelPropriete.ValidatorForConcours();
	}
	public IListValidator<InfosModelCategorie> getCategoriesValidator() {
		return new ModelCategorie.ValidatorForConcours();
	}
	public IListValidator<Pair<InfosModelPoule, ArrayList<String>>> getPoulesValidator() {
		return new ModelPoule.ValidatorForCategorie();
	}
	
	// Récupérer les TreeModel
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
	public TreeModel getTreeModelPhasesQualifs () {
		if (this.tm_qualifs == null) {
			this.tm_qualifs = new TreeModelPhasesQualifs();
		}
		return this.tm_qualifs;
	}
	
	// Récupérer des TableModel
	public IClosableTableModel getTableModelParticipants () {
		return new TableModelParticipants(this.concours);
	}
	public IClosableTableModel getTableModelParticipants (String nomCategorie) {
		return new TableModelParticipants(this.concours.getCategorieByNom(nomCategorie));
	}
	public IClosableTableModel getTableModelParticipants (String nomCategorie, String nomPoule) {
		return new TableModelParticipants(this.concours.getCategorieByNom(nomCategorie).getPouleByNom(nomPoule));
	}
	public IClosableTableModel getTableModelMatchsPhasesQualifs () {
		return new TableModelPhasesQualifsMatchs(this.concours);
	}
	public IClosableTableModel getTableModelMatchsPhasesQualifs (String nomCategorie) {
		return new TableModelPhasesQualifsMatchs(this.concours.getCategorieByNom(nomCategorie));
	}
	public IClosableTableModel getTableModelMatchsPhasesQualifs (String nomCategorie, String nomPoule) {
		return new TableModelPhasesQualifsMatchs(this.concours.getCategorieByNom(nomCategorie).getPouleByNom(nomPoule));
	}
	public IClosableTableModel getTableModelMatchsPhasesQualifs (String nomCategorie, String nomPoule, int numeroPhase) {
		return new TableModelPhasesQualifsMatchs(this.concours.getCategorieByNom(nomCategorie).getPouleByNom(nomPoule).getPhasesQualificatives().get(numeroPhase));
	}
	
	// Récupérer des GrapheModel
	public IGraphModel<InfosModelCategorie,InfosModelParticipant> getGraphModelPhasesElimsGrandeFinale(String nomCategorie) {
		return new GraphModelPhasesElimsGrandeFinale(this.concours.getCategorieByNom(nomCategorie));
	}
	public IGraphModel<InfosModelCategorie,InfosModelParticipant> getGraphModelPhasesElimsPetiteFinale(String nomCategorie) {
		return new GraphModelPhasesElimsPetiteFinale(this.concours.getCategorieByNom(nomCategorie));
	}
	
	// Modifier les catégories et les poules
	public void updateCategories(TrackableList<InfosModelCategorie> categories) throws ContestOrgModelException {
		// Démarrer l'action de modification
		this.history.start("Modification des catégories");
		
		// Modifier les catégories
		this.concours.updateCategories(categories);
		
		// Fermer l'action de modification
		this.history.close();
	}
	public void updatePoules(ArrayList<Pair<String,TrackableList<Pair<InfosModelPoule, ArrayList<String>>>>> categoriesPoules) throws ContestOrgModelException {
		// Démarrer l'action de modification
		this.history.start("Modification des poules");
		
		// Modifier les poules des catégories
		for(Pair<String,TrackableList<Pair<InfosModelPoule, ArrayList<String>>>> categoriePoules : categoriesPoules) {
			// Modifier les poules de la catégorie
			this.concours.getCategorieByNom(categoriePoules.getFirst()).updatePoules(categoriePoules.getSecond());
		}
		
		// Fermer l'action de modification
		this.history.close();
	}
	
	// Ajouter/Modifier/Supprimer un participant
	public void addParticipant (Quintuple<String, String, InfosModelParticipant, TrackableList<Pair<String, InfosModelProprieteParticipant>>, TrackableList<String>> infos) throws ContestOrgModelException {
		// Démarrer l'action de création
		this.history.start("Ajout du participant \"" + infos.getThird().getNom() + "\"");
		
		// Ajouter le participant
		new ModelParticipant.Updater(this.concours).create(infos);
		
		// Fermer l'action de création
		this.history.close();
	}
	public void updateParticipant (String nomParticipant, Quintuple<String, String, InfosModelParticipant, TrackableList<Pair<String, InfosModelProprieteParticipant>>, TrackableList<String>> infos) throws ContestOrgModelException {
		// Récupérer le participant
		ModelParticipant participant = this.concours.getParticipantByNom(nomParticipant);
		
		// Démarrer l'action de modification
		this.history.start("Modification du participant \"" + participant.getNom() + "\"");
		
		// Modifier le participant
		new ModelParticipant.Updater(this.concours).update(participant, infos);
		
		// Fermer l'action de modification
		this.history.close();
	}
	public void updateParticipant (String nomParticipant, InfosModelParticipant.Statut statut) {
		// Récupérer le participant
		ModelParticipant participant = this.concours.getParticipantByNom(nomParticipant);
		
		// Démarrer l'action de modification
		this.history.start("Changement du statut du participant \"" + participant.getNom() + "\"");
		
		// Modifier le participant
		participant.setStatut(statut);
		
		// Fermer l'action de modification
		this.history.close();
	}
	public void updateParticipants(ArrayList<String> nomParticipants, InfosModelParticipant.Statut statut) {
		// Démarrer l'action de modification
		this.history.start("Changement du statut de plusieurs participants");
		
		// Pour chaque participant
		for(String nomParticipant : nomParticipants) {
			// Récupérer le participant et modifier son statut
			this.concours.getParticipantByNom(nomParticipant).setStatut(statut);
		}
		
		// Fermer l'action de modification
		this.history.close();
	}
	public void removeParticipant (String nomParticipant) throws ContestOrgModelException {
		// Récupérer le participant
		ModelParticipant participant = this.concours.getParticipantByNom(nomParticipant);
		
		// Démarrer l'action de suppression
		this.history.start("Suppression du participant \"" + participant.getNom() + "\"");
		
		// Chercher le participant et le supprimer
		participant.delete();
		
		// Fermer l'action de suppression
		this.history.close();
	}
	public void removeParticipants (ArrayList<String> nomParticipants) throws ContestOrgModelException {
		// Démarrer l'action de suppression
		this.history.start("Suppression de plusieurs participants");
		
		// Pour chaque participant
		for(String nomParticipant : nomParticipants) {
			// Récupérer le participant et le supprimer
			this.concours.getParticipantByNom(nomParticipant).delete();
		}
		
		// Fermer l'action de suppression
		this.history.close();
	}
	
	// Ajouter/Modifier/Supprimer une phase qualificative
	@SuppressWarnings("unchecked")
	public IGeneration<Configuration<String>> genererPhaseQualifAvance (String nomCategorie, String nomPoule, ArrayList<String> nomParticipants) {
		// Récupérer les participants
		ArrayList<ModelParticipant> participants = new ArrayList<ModelParticipant>();
		for(String nomParticipant : nomParticipants) {
			participants.add(this.concours.getParticipantByNom(nomParticipant));
		}
		
		// Ajouter le participant fantome si le nombre de participants est impaire
		if (participants.size() % 2 == 1) {
			participants.add(null);
		}
		
		// Récupérer la génération
		IGeneration<Configuration<ModelParticipant>> generation = ModelPhaseQualificative.genererConfigurationAvance(participants.toArray(new ModelParticipant[participants.size()]), new CompGenerationPhasesQualifs(this.concours.getComparateurPhasesQualificatives()));
		
		// Retourner le résultat de la génération
		return this.transformGeneration(generation);
	}
	@SuppressWarnings("unchecked")
	public IGeneration<Configuration<String>> genererPhaseQualifBasique (String nomCategorie, String nomPoule, ArrayList<String> nomParticipants) {
		// Récupérer les participants
		ArrayList<ModelParticipant> participants = new ArrayList<ModelParticipant>();
		for(String nomParticipant : nomParticipants) {
			participants.add(this.concours.getParticipantByNom(nomParticipant));
		}
		
		// Ajouter le participant fantome si le nombre de participants est impaire
		if (participants.size() % 2 == 1) {
			participants.add(null);
		}

		// Récupérer la génération		
		IGeneration<Configuration<ModelParticipant>> generation = ModelPhaseQualificative.genererConfigurationBasique(participants.toArray(new ModelParticipant[participants.size()]), new CompGenerationPhasesQualifs(this.concours.getComparateurPhasesQualificatives()));
		
		// Retourner le résultat de la génération
		return this.transformGeneration(generation);
	}
	private IGeneration<Configuration<String>> transformGeneration(IGeneration<Configuration<ModelParticipant>> generation) {
		// Transformeur
		ITransformer<Configuration<ModelParticipant>, Configuration<String>> transformer = new ITransformer<Configuration<ModelParticipant>, Configuration<String>>() {	
			@Override
			public Configuration<String> transform (Configuration<ModelParticipant> configuration) {
				return configuration.transform(new ITransformer<ModelParticipant, String>() {
					@Override
					public String transform (ModelParticipant participant) {
						return participant == null ? null : participant.getNom();
					}
				});
			}
		};
		
		// Appliquer le décorateur et retourner la génération
		return new GenerationDecorator<Configuration<ModelParticipant>, Configuration<String>>(generation, transformer);
	}
	public void addPhaseQualif (String nomCategorie, String nomPoule, Triple<Configuration<String>,InfosModelPhaseQualificative,InfosModelMatchPhasesQualifs> infos) throws ContestOrgModelException {
		// Démarrer l'action d'ajout
		this.history.start("Ajout d'une phase qualificative dans la poule \"" + nomCategorie + " > " + nomPoule + "\"");
		
		// Récupérer la poule
		ModelPoule poule = this.concours.getCategorieByNom(nomCategorie).getPouleByNom(nomPoule);
		
		// Ajouter la phase qualificative
		poule.addPhaseQualificative(new ModelPhaseQualificative(poule, infos.getThird(), this.transformConfiguration(infos.getFirst()), infos.getSecond()));
		
		// Fermer l'action d'ajout
		this.history.close();
	}
	public void updatePhaseQualif (String nomCategorie, String nomPoule, int numeroPhase, Triple<Configuration<String>,InfosModelPhaseQualificative,InfosModelMatchPhasesQualifs> infos) throws ContestOrgModelException {
		// Démarrer l'action de modification
		this.history.start("Modification de la phase qualificative " + numeroPhase + " de la poule \"" + nomCategorie + " > " + nomPoule + "\"");
		
		// Récupérer la phase qualificative
		ModelPhaseQualificative phase = this.concours.getCategorieByNom(nomCategorie).getPouleByNom(nomPoule).getPhasesQualificatives().get(numeroPhase);
		
		// Modifier la phase qualificative
		phase.setInfos(infos.getSecond());
		phase.setConfiguration(infos.getThird(), this.transformConfiguration(infos.getFirst()));
		
		// Fermer l'action de modification
		this.history.close();
	}
	private Configuration<ModelParticipant> transformConfiguration(Configuration<String> configuration) {
		// Placer le concours en variable local finale
		final ModelConcours concours = this.concours;
		
		// Transformer la configuration et la retourner
		return configuration.transform(new ITransformer<String, ModelParticipant>() {
			@Override
			public ModelParticipant transform (String nomParticipant) {
				return nomParticipant == null ? null : concours.getParticipantByNom(nomParticipant);
			}
		});
	} 
	public void removePhaseQualif (String nomCategorie, String nomPoule, int numeroPhase) throws ContestOrgModelException {
		// Démarrer l'action de suppression
		this.history.start("Suppression de la phase qualificative " + numeroPhase + " de la poule \"" + nomCategorie + " > " + nomPoule + "\"");
		
		// Supprimer la phase qualificative
		this.concours.getCategorieByNom(nomCategorie).getPouleByNom(nomPoule).getPhasesQualificatives().get(numeroPhase).delete();
		
		// Fermer l'action de suppression
		this.history.close();
	}
	
	// Ajouter/Modifier/Supprimer un match de phase qualificative
	public void addMatchPhaseQualif (String nomCategorie, String nomPoule, int numeroPhase, Triple<Triple<String, TrackableList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>, Triple<String, TrackableList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>,InfosModelMatchPhasesQualifs> infos) throws ContestOrgModelException {
		// Démarrer l'action d'ajout
		this.history.start("Ajout d'un match dans la phase qualificative " + numeroPhase + " de la poule \"" + nomCategorie + " > " + nomPoule + "\"");
		
		// Récupérer la phase qualificative
		ModelPhaseQualificative phase = this.concours.getCategorieByNom(nomCategorie).getPouleByNom(nomPoule).getPhasesQualificatives().get(numeroPhase);
		
		// Créer le match de phase qualificative
		phase.addMatch(new ModelMatchPhasesQualifs.UpdaterForPhaseQualif(phase).create(infos));
		
		// Fermer l'action d'ajout
		this.history.close();
	}
	public void updateMatchPhaseQualif(String nomCategorie, String nomPoule, Integer numeroPhase, int numeroMatch, Triple<Triple<String, TrackableList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>, Triple<String, TrackableList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>,InfosModelMatchPhasesQualifs> infos) {
		// Démarrer l'action de modification
		this.history.start("Modification du match "+numeroMatch+" de la phase qualificative " + numeroPhase + " de la poule \"" + nomCategorie + " > " + nomPoule + "\"");
		
		// Récupérer le match
		ModelMatchPhasesQualifs match;
		if(nomCategorie == null) {
			match = this.concours.getMatchsPhasesQualifs().get(numeroMatch);
		} else if(nomPoule == null) {
			match = this.concours.getCategorieByNom(nomCategorie).getMatchsPhasesQualifs().get(numeroMatch);
		} else if(numeroPhase == null) {
			match = this.concours.getCategorieByNom(nomCategorie).getPouleByNom(nomPoule).getMatchsPhasesQualifs().get(numeroMatch);
		} else {
			match = this.concours.getCategorieByNom(nomCategorie).getPouleByNom(nomPoule).getPhasesQualificatives().get(numeroPhase).getMatchs().get(numeroMatch);
		}
		
		// Modifier le match
		new ModelMatchPhasesQualifs.UpdaterForPhaseQualif(match.getPhaseQualificative()).update(match, infos);
		
		// Fermer l'action de modification
		this.history.close();
	}
	public void removeMatchPhaseQualif(String nomCategorie, String nomPoule, Integer numeroPhase, int numeroMatch) throws ContestOrgModelException {
		// Démarrer l'action de suppression
		this.history.start("Suppression du match "+numeroMatch+" de la phase qualificative " + numeroPhase + " de la poule \"" + nomCategorie + " > " + nomPoule + "\"");
		
		// Supprimer le match
		if(nomCategorie == null) {
			this.concours.getMatchsPhasesQualifs().get(numeroMatch).delete();
		} else if(nomPoule == null) {
			this.concours.getCategorieByNom(nomCategorie).getMatchsPhasesQualifs().get(numeroMatch).delete();
		} else if(numeroPhase == null) {
			this.concours.getCategorieByNom(nomCategorie).getPouleByNom(nomPoule).getMatchsPhasesQualifs().get(numeroMatch).delete();
		} else {
			this.concours.getCategorieByNom(nomCategorie).getPouleByNom(nomPoule).getPhasesQualificatives().get(numeroPhase).getMatchs().get(numeroMatch).delete();
		}
		
		// Fermer l'action de suppression
		this.history.close();
	}
	
	// Générer/Modifier/Supprimer les phases éliminatoires
	public void genererPhasesElims(String nomCategorie, int nbPhases, InfosModelMatchPhasesElims infosMatchs, InfosModelPhaseEliminatoires infosPhaseEliminatoire) throws ContestOrgModelException {
		// Démarrer l'action de génération
		this.history.start("Génération des phases éliminatoires pour la catégorie \""+nomCategorie+"\"");
		
		// Générer les phases éliminatoires
		this.concours.getCategorieByNom(nomCategorie).genererPhasesEliminatoires(nbPhases, infosMatchs, infosPhaseEliminatoire);
		
		// Fermer l'action de génération
		this.history.close();
	}
	public void resetPhasesElims(String nomCategorie) throws ContestOrgModelException {
		// Démarrer l'action de reset
		this.history.start("Reset des phases éliminatoires pour la catégorie \""+nomCategorie+"\"");
		
		// Générer les phases éliminatoires
		this.concours.getCategorieByNom(nomCategorie).getPhasesEliminatoires().delete();
		
		// Fermer l'action de reset
		this.history.close();
	}
	public void setParticipantPhasesElims(String nomCategorie, int numeroCellule, String nomParticipant) throws ContestOrgModelException {
		// Démarrer l'action de modification
		this.history.start("Modification d'un participant dans les phase éliminatoires de la catégorie \""+nomCategorie+"\"");
		
		// Récupérer la phases éliminatoires
		ModelPhasesEliminatoires phases = this.concours.getCategorieByNom(nomCategorie).getPhasesEliminatoires();
		
		// Récupérer le match
		ModelMatchPhasesElims match = phases.getMatch((numeroCellule-(numeroCellule%2))/2);
		
		// Récupérer la participation
		ModelParticipation participation = numeroCellule%2 == 0 ? match.getParticipationA() : match.getParticipationB();
		
		// Récupérer le participant
		ModelParticipant participant = this.concours.getParticipantByNom(nomParticipant);
		
		// Retirer la participation de l'ancien participant
		if(participation.getParticipant() != null) {
			participation.getParticipant().removeParticipation(participation);
		}
		
		// Modifier le participant de la participation
		participation.setParticipant(participant);
		
		// Ajouter la participation au nouveau participant
		participant.addParticipation(participation);
		
		// Fermer l'action de modification
		this.history.close();
	}
	public void updateMatchPhasesElims(String nomCategorie, int numeroMatch, Triple<Pair<TrackableList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>, Pair<TrackableList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>, InfosModelMatchPhasesElims> infos) throws ContestOrgModelException {
		// Démarrer l'action de modification
		this.history.start("Modification d'un match dans les phase éliminatoires de la catégorie \""+nomCategorie+"\"");
		
		// Récupérer le match
		ModelMatchPhasesElims match = this.concours.getCategorieByNom(nomCategorie).getPhasesEliminatoires().getMatch(numeroMatch);
		
		// Modifier le match
		new ModelMatchPhasesElims.Updater().update(match, infos);
		
		// Fermer l'action de modification
		this.history.close();
	}
	public void updateMatchPetiteFinale(String nomCategorie, Triple<Pair<TrackableList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>, Pair<TrackableList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>, InfosModelMatchPhasesElims> infos) throws ContestOrgModelException {
		// Démarrer l'action de modification
		this.history.start("Modification de la petite finale dans les phase éliminatoires de la catégorie \""+nomCategorie+"\"");
		
		// Récupérer le match
		ModelMatchPhasesElims match = this.concours.getCategorieByNom(nomCategorie).getPhasesEliminatoires().getPetiteFinale();
		
		// Modifier le match
		new ModelMatchPhasesElims.Updater().update(match, infos);
		
		// Fermer l'action de modification
		this.history.close();
	}
}
