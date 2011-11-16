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
import org.contestorg.infos.InfosModelEquipe;
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
import org.contestorg.infos.InfosModelProprieteEquipe;
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
	private TreeModelEquipes tm_categories;
	private TreeModelEquipes tm_poules;
	private TreeModelEquipes tm_equipes;
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
	public int getNbEquipes() {
		return this.concours.getNbEquipes();
	}
	public boolean isEquipeExiste (String nomEquipe) {
		return this.concours.getEquipeByNom(nomEquipe) != null;
	}
	public int getNbVillesCommunes(Configuration<String> configuration) {
		int nbVillesCommunes = 0;
		for(Couple<String> couple : configuration.getCouples()) {
			if(couple.getEquipeA() != null && couple.getEquipeB() != null) {
				ModelEquipe equipeA = this.concours.getEquipeByNom(couple.getEquipeA());
				ModelEquipe equipeB = this.concours.getEquipeByNom(couple.getEquipeB());
				if(equipeA.getVille() != null && !equipeA.getVille().isEmpty() && equipeB.getVille() != null && !equipeB.getVille().isEmpty() && equipeA.getVille().equals(equipeB.getVille())) {
					nbVillesCommunes++;
				}
			}
		}
		return nbVillesCommunes;
	}
	public int getNbMatchDejaJoues(Configuration<String> configuration) {
		int nbMatchDejaJoues = 0;
		for(Couple<String> couple : configuration.getCouples()) {
			if(couple.getEquipeA() != null && couple.getEquipeB() != null) {
				ModelEquipe equipeA = this.concours.getEquipeByNom(couple.getEquipeA());
				ModelEquipe equipeB = this.concours.getEquipeByNom(couple.getEquipeB());
				nbMatchDejaJoues += equipeA.getNbRencontres(equipeB);
			}
		}
		return nbMatchDejaJoues;
	}
	public boolean isEquipeParticipantePhasesQualif(String nomCategorie, String nomPoule, int numeroPhase, String nomEquipe) {
		ModelPhaseQualificative phase = this.concours.getCategorieByNom(nomCategorie).getPouleByNom(nomPoule).getPhasesQualificatives().get(numeroPhase);
		for(ModelMatchPhasesQualifs match : phase.getMatchs()) {
			if(match.getParticipationA().getEquipe() != null && match.getParticipationA().getEquipe().getNom().equals(nomEquipe)) {
				return true;
			}
			if(match.getParticipationB().getEquipe() != null && match.getParticipationB().getEquipe().getNom().equals(nomEquipe)) {
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
	public int getRangEquipePhasesQualificatives(String nomEquipe) {
		return this.concours.getEquipeByNom(nomEquipe).getRangPhasesQualifs();
	}
	public int getRangEquipePhasesEliminatoires(String nomEquipe) {
		return this.concours.getEquipeByNom(nomEquipe).getRangPhasesElims();
	}
	public int getNbPhasesElimsPossibles(String nomCategorie) {
		// Récupérer le nombre d'équipe participantes
		int nbEquipesParticipantes = this.concours.getCategorieByNom(nomCategorie).getEquipesParticipantes().size();
		
		// Compter le nombre de phases possibles
		int nbPhases = 0;
		for(int i=nbEquipesParticipantes;i>1;i=i/2) {
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
	public int getNbRencontres(String nomEquipeA, String nomEquipeB) {
		return nomEquipeA != null ? this.concours.getEquipeByNom(nomEquipeA).getNbRencontres(this.concours.getEquipeByNom(nomEquipeB)) : (nomEquipeB == null ? 0 : this.concours.getEquipeByNom(nomEquipeB).getNbRencontres(this.concours.getEquipeByNom(nomEquipeA)));
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
			return new Triple<InfosModelExportation, InfosModelChemin, InfosModelTheme>(concours.getPublication().toInformation(), concours.getPublication().getChemin().toInformation(), concours.getPublication().getTheme().toInformation());
		}
		return null;
	}
	public Quintuple<String, String, InfosModelEquipe, ArrayList<Pair<String, InfosModelProprieteEquipe>>, ArrayList<String>> getInfosEquipe (String nomEquipe) {
		// Récupérer l'équipe
		ModelEquipe equipe = this.concours.getEquipeByNom(nomEquipe);
		
		// Récupérer la liste des propriétés
		ArrayList<Pair<String, InfosModelProprieteEquipe>> proprietes = new ArrayList<Pair<String, InfosModelProprieteEquipe>>();
		for (ModelProprietePossedee propriete : equipe.getProprietesEquipe()) {
			proprietes.add(new Pair<String, InfosModelProprieteEquipe>(propriete.getPropriete().getNom(), propriete.toInformation()));
		}
		
		// Récupérer la liste des prix
		ArrayList<String> prixs = new ArrayList<String>();
		for (ModelPrix prix : equipe.getPrix()) {
			prixs.add(prix.getNom());
		}
		
		// Retourner les informations de l'équipe
		return new Quintuple<String, String, InfosModelEquipe, ArrayList<Pair<String, InfosModelProprieteEquipe>>, ArrayList<String>>(equipe.getPoule().getCategorie().getNom(), equipe.getPoule().getNom(), equipe.toInformation(), proprietes, prixs);
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
		return new Triple<Triple<String,ArrayList<Pair<String,InfosModelParticipationObjectif>>,InfosModelParticipation>, Triple<String,ArrayList<Pair<String,InfosModelParticipationObjectif>>,InfosModelParticipation>, InfosModelMatchPhasesQualifs>(infos.getFirst(), infos.getSecond(), match.toInformation());
	}
	public Triple<Triple<String, ArrayList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>, Triple<String, ArrayList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>,InfosModelMatchPhasesElims> getInfosMatchPhaseElims(String nomCategorie, int numeroMatch) {
		// Récupérer le match
		ModelMatchPhasesElims match = this.concours.getCategorieByNom(nomCategorie).getPhasesEliminatoires().getMatch(numeroMatch);
		
		// Récupérer les informations du match
		Pair<Triple<String, ArrayList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>, Triple<String, ArrayList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>> infos = this.getInfosMatch(match);
		
		// Retourner les informations du match
		return new Triple<Triple<String,ArrayList<Pair<String,InfosModelParticipationObjectif>>,InfosModelParticipation>, Triple<String,ArrayList<Pair<String,InfosModelParticipationObjectif>>,InfosModelParticipation>, InfosModelMatchPhasesElims>(infos.getFirst(), infos.getSecond(), match.toInformation());
	}
	public Triple<Triple<String, ArrayList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>, Triple<String, ArrayList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>,InfosModelMatchPhasesElims> getInfosMatchPetiteFinale(String nomCategorie) {
		// Récupérer le match
		ModelMatchPhasesElims match = this.concours.getCategorieByNom(nomCategorie).getPhasesEliminatoires().getPetiteFinale();
		
		// Récupérer les informations du match
		Pair<Triple<String, ArrayList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>, Triple<String, ArrayList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>> infos = this.getInfosMatch(match);
		
		// Retourner les informations du match
		return new Triple<Triple<String,ArrayList<Pair<String,InfosModelParticipationObjectif>>,InfosModelParticipation>, Triple<String,ArrayList<Pair<String,InfosModelParticipationObjectif>>,InfosModelParticipation>, InfosModelMatchPhasesElims>(infos.getFirst(), infos.getSecond(), match.toInformation());
	}
	private Pair<Triple<String, ArrayList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>, Triple<String, ArrayList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>> getInfosMatch(ModelMatchAbstract match) {
		// Récupérer les informations de participation de l'équipe A
		String nomEquipeA = match.getParticipationA() == null || match.getParticipationA().getEquipe() == null ? null : match.getParticipationA().getEquipe().getNom();
		ArrayList<Pair<String, InfosModelParticipationObjectif>> objectifsRemportesA = new ArrayList<Pair<String,InfosModelParticipationObjectif>>();
		for(ModelObjectifRemporte objectifRemporte : match.getParticipationA().getObjectifsRemportes()) {
			objectifsRemportesA.add(new Pair<String, InfosModelParticipationObjectif>(objectifRemporte.getObjectif().getNom(), objectifRemporte.toInformation()));
		}
		Triple<String, ArrayList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation> infosParticipationA = new Triple<String, ArrayList<Pair<String,InfosModelParticipationObjectif>>, InfosModelParticipation>(nomEquipeA, objectifsRemportesA, match.getParticipationA().toInformation());
		
		// Récupérer les informations de participation de l'équipe B
		String nomEquipeB = match.getParticipationB() == null || match.getParticipationB().getEquipe() == null ? null : match.getParticipationB().getEquipe().getNom();
		ArrayList<Pair<String, InfosModelParticipationObjectif>> objectifsRemportesB = new ArrayList<Pair<String,InfosModelParticipationObjectif>>();
		for(ModelObjectifRemporte objectifRemporte : match.getParticipationB().getObjectifsRemportes()) {
			objectifsRemportesB.add(new Pair<String, InfosModelParticipationObjectif>(objectifRemporte.getObjectif().getNom(), objectifRemporte.toInformation()));
		}
		Triple<String, ArrayList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation> infosParticipationB = new Triple<String, ArrayList<Pair<String,InfosModelParticipationObjectif>>, InfosModelParticipation>(nomEquipeB, objectifsRemportesB, match.getParticipationB().toInformation());
		
		// Assembler les informations demandées et les retourner
		return new Pair<Triple<String,ArrayList<Pair<String,InfosModelParticipationObjectif>>,InfosModelParticipation>, Triple<String,ArrayList<Pair<String,InfosModelParticipationObjectif>>,InfosModelParticipation>>(infosParticipationA, infosParticipationB);
	}
	public Triple<Configuration<String>,InfosModelPhaseQualificative,InfosModelMatchPhasesQualifs> getInfosPhaseQualif(String nomCategorie,String nomPoule,int numeroPhase) {
		// Récupérer la phase qualificative
		ModelPhaseQualificative phase = this.concours.getCategorieByNom(nomCategorie).getPouleByNom(nomPoule).getPhasesQualificatives().get(numeroPhase);

		// Construire la configuration
		Configuration<String> configuration = new Configuration<String>(phase.getMatchs().size());
		for(ModelMatchPhasesQualifs match : phase.getMatchs()) {
			ModelEquipe equipeA = match.getParticipationA().getEquipe();
			ModelEquipe equipeB = match.getParticipationB().getEquipe();
			configuration.addCouple(new Couple<String>(equipeA == null ? null : equipeA.getNom(), equipeB == null ? null : equipeB.getNom()));
		}
		
		// Récupérer les informations de la phase qualificative
		InfosModelPhaseQualificative infosPhase = phase.toInformation();
		
		// Récupérer les informations des matchs de la phase qualificative
		InfosModelMatchPhasesQualifs infosMatchs = new InfosModelMatchPhasesQualifs(null,null);
		
		// Assembler les informations demandées et les retourner
		return new Triple<Configuration<String>, InfosModelPhaseQualificative, InfosModelMatchPhasesQualifs>(configuration, infosPhase, infosMatchs);
	}
	
	// Récupérer des données liste
	public ArrayList<InfosModelObjectif> getListeObjectifs () {
		ArrayList<InfosModelObjectif> objectifs = new ArrayList<InfosModelObjectif>();
		for (ModelObjectif objectif : concours.getObjectifs()) {
			objectifs.add(objectif.toInformation());
		}
		return objectifs;
	}
	public ArrayList<Triple<InfosModelExportation, InfosModelChemin, InfosModelTheme>> getListeExportations () {
		ArrayList<Triple<InfosModelExportation, InfosModelChemin, InfosModelTheme>> exportations = new ArrayList<Triple<InfosModelExportation, InfosModelChemin, InfosModelTheme>>();
		for (ModelExportation exportation : concours.getExportations()) {
			exportations.add(new Triple<InfosModelExportation, InfosModelChemin, InfosModelTheme>(exportation.toInformation(), exportation.getChemin().toInformation(), exportation.getTheme().toInformation()));
		}
		return exportations;
	}
	public ArrayList<Pair<InfosModelDiffusion, InfosModelTheme>> getListeDiffusions () {
		ArrayList<Pair<InfosModelDiffusion, InfosModelTheme>> diffusions = new ArrayList<Pair<InfosModelDiffusion, InfosModelTheme>>();
		for (ModelDiffusion diffusion : concours.getDiffusions()) {
			diffusions.add(new Pair<InfosModelDiffusion, InfosModelTheme>(diffusion.toInformation(), diffusion.getTheme().toInformation()));
		}
		return diffusions;
	}
	public ArrayList<InfosModelPrix> getListePrix () {
		ArrayList<InfosModelPrix> prixs = new ArrayList<InfosModelPrix>();
		for (ModelPrix prix : concours.getPrix()) {
			prixs.add(prix.toInformation());
		}
		return prixs;
	}
	public ArrayList<Triple<InfosModelLieu, ArrayList<InfosModelEmplacement>, ArrayList<InfosModelHoraire>>> getListeLieux () {
		ArrayList<Triple<InfosModelLieu, ArrayList<InfosModelEmplacement>, ArrayList<InfosModelHoraire>>> lieux = new ArrayList<Triple<InfosModelLieu, ArrayList<InfosModelEmplacement>, ArrayList<InfosModelHoraire>>>();
		for (ModelLieu lieu : concours.getLieux()) {
			ArrayList<InfosModelEmplacement> emplacements = new ArrayList<InfosModelEmplacement>();
			for (ModelEmplacement emplacement : lieu.getEmplacements()) {
				emplacements.add(emplacement.toInformation());
			}
			ArrayList<InfosModelHoraire> horaires = new ArrayList<InfosModelHoraire>();
			for (ModelHoraire horaire : lieu.getHoraires()) {
				horaires.add(horaire.toInformation());
			}
			lieux.add(new Triple<InfosModelLieu, ArrayList<InfosModelEmplacement>, ArrayList<InfosModelHoraire>>(lieu.toInformation(), emplacements, horaires));
		}
		return lieux;
	}
	public ArrayList<InfosModelPropriete> getListeProprietes () {
		ArrayList<InfosModelPropriete> proprietes = new ArrayList<InfosModelPropriete>();
		for (ModelPropriete propriete : concours.getProprietes()) {
			proprietes.add(propriete.toInformation());
		}
		return proprietes;
	}
	public ArrayList<Pair<InfosModelCategorie, ArrayList<InfosModelPoule>>> getListeCategoriesPoules () {
		ArrayList<Pair<InfosModelCategorie, ArrayList<InfosModelPoule>>> categories = new ArrayList<Pair<InfosModelCategorie, ArrayList<InfosModelPoule>>>();
		for (ModelCategorie categorie : this.concours.getCategories()) {
			ArrayList<InfosModelPoule> poules = new ArrayList<InfosModelPoule>();
			for (ModelPoule poule : categorie.getPoules()) {
				poules.add(poule.toInformation());
			}
			categories.add(new Pair<InfosModelCategorie, ArrayList<InfosModelPoule>>(categorie.toInformation(), poules));
		}
		return categories;
	}
	public ArrayList<Pair<InfosModelCategorie, ArrayList<Pair<InfosModelPoule,ArrayList<InfosModelEquipe>>>>> getListeCategoriesPoulesEquipes() {
		ArrayList<Pair<InfosModelCategorie, ArrayList<Pair<InfosModelPoule,ArrayList<InfosModelEquipe>>>>> categories = new ArrayList<Pair<InfosModelCategorie,ArrayList<Pair<InfosModelPoule,ArrayList<InfosModelEquipe>>>>>();
		for(ModelCategorie categorie : this.concours.getCategories()) {
			ArrayList<Pair<InfosModelPoule,ArrayList<InfosModelEquipe>>> poules = new ArrayList<Pair<InfosModelPoule,ArrayList<InfosModelEquipe>>>();
			for(ModelPoule poule : categorie.getPoules()) {
				ArrayList<InfosModelEquipe> equipes = new ArrayList<InfosModelEquipe>();
				for(ModelEquipe equipe : poule.getEquipes()) {
					equipes.add(equipe.toInformation());
				}
				poules.add(new Pair<InfosModelPoule, ArrayList<InfosModelEquipe>>(poule.toInformation(), equipes));
			}
			categories.add(new Pair<InfosModelCategorie, ArrayList<Pair<InfosModelPoule,ArrayList<InfosModelEquipe>>>>(categorie.toInformation(), poules));
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
					phases.add(phase.toInformation());
				}
				poules.add(new Pair<InfosModelPoule, ArrayList<InfosModelPhaseQualificative>>(poule.toInformation(), phases));
			}
			categories.add(new Pair<InfosModelCategorie, ArrayList<Pair<InfosModelPoule,ArrayList<InfosModelPhaseQualificative>>>>(categorie.toInformation(), poules));
		}
		return categories;
	}
	public ArrayList<String> getListeEquipesParticipantes(String nomCategorie,String nomPoule) {
		ArrayList<String> participantes = new ArrayList<String>();
		for(ModelEquipe equipe : this.concours.getCategorieByNom(nomCategorie).getPouleByNom(nomPoule).getEquipesParticipantes()) {
			participantes.add(equipe.getNom());
		}
		return participantes;
	}
	public ArrayList<String> getListeEquipesParticipantes(String nomCategorie) {
		ArrayList<String> participantes = new ArrayList<String>();
		for(ModelEquipe equipe : this.concours.getCategorieByNom(nomCategorie).getEquipesParticipantes()) {
			participantes.add(equipe.getNom());
		}
		return participantes;
	}
	public ArrayList<InfosModelCategorie> getListeCategories() {
		ArrayList<InfosModelCategorie> categories = new ArrayList<InfosModelCategorie>();
		for(ModelCategorie categorie : this.concours.getCategories()) {
			categories.add(categorie.toInformation());
		}
		return categories;
	}
	public ArrayList<InfosModelPoule> getListePoules(String nomCategorie) {
		ArrayList<InfosModelPoule> poules = new ArrayList<InfosModelPoule>();
		for(ModelPoule poule : this.concours.getCategorieByNom(nomCategorie).getPoules()) {
			poules.add(poule.toInformation());
		}
		return poules;
	}
	public ArrayList<InfosModelCompPhasesQualifsAbstract> getListeComparateurs () {
		ArrayList<InfosModelCompPhasesQualifsAbstract> comparateurs = new ArrayList<InfosModelCompPhasesQualifsAbstract>();
		for (ModelCompPhasesQualifsAbstract comparateur : concours.getCompsPhasesQualifs()) {
			comparateurs.add(comparateur.toInformation());
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
			this.tm_categories = new TreeModelEquipes(false, false);
		}
		return this.tm_categories;
	}
	public TreeModel getTreeModelPoules () {
		if (this.tm_poules == null) {
			this.tm_poules = new TreeModelEquipes(true, false);
		}
		return this.tm_poules;
	}
	public TreeModel getTreeModelEquipes () {
		if (this.tm_equipes == null) {
			this.tm_equipes = new TreeModelEquipes(true, true);
		}
		return this.tm_equipes;
	}
	public TreeModel getTreeModelPhasesQualifs () {
		if (this.tm_qualifs == null) {
			this.tm_qualifs = new TreeModelPhasesQualifs();
		}
		return this.tm_qualifs;
	}
	
	// Récupérer des TableModel
	public IClosableTableModel getTableModelEquipes () {
		return new TableModelEquipes(this.concours);
	}
	public IClosableTableModel getTableModelEquipes (String nomCategorie) {
		return new TableModelEquipes(this.concours.getCategorieByNom(nomCategorie));
	}
	public IClosableTableModel getTableModelEquipes (String nomCategorie, String nomPoule) {
		return new TableModelEquipes(this.concours.getCategorieByNom(nomCategorie).getPouleByNom(nomPoule));
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
	public IGraphModel<InfosModelCategorie,InfosModelEquipe> getGraphModelPhasesElimsGrandeFinale(String nomCategorie) {
		return new GraphModelPhasesElimsGrandeFinale(this.concours.getCategorieByNom(nomCategorie));
	}
	public IGraphModel<InfosModelCategorie,InfosModelEquipe> getGraphModelPhasesElimsPetiteFinale(String nomCategorie) {
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
	
	// Ajouter/Modifier/Supprimer une équipe
	public void addEquipe (Quintuple<String, String, InfosModelEquipe, TrackableList<Pair<String, InfosModelProprieteEquipe>>, TrackableList<String>> infos) throws ContestOrgModelException {
		// Démarrer l'action de création
		this.history.start("Ajout de l'équipe \"" + infos.getThird().getNom() + "\"");
		
		// Ajouter l'équipe
		new ModelEquipe.Updater(this.concours).create(infos);
		
		// Fermer l'action de création
		this.history.close();
	}
	public void updateEquipe (String nomEquipe, Quintuple<String, String, InfosModelEquipe, TrackableList<Pair<String, InfosModelProprieteEquipe>>, TrackableList<String>> infos) throws ContestOrgModelException {
		// Récupérer l'équipe
		ModelEquipe equipe = this.concours.getEquipeByNom(nomEquipe);
		
		// Démarrer l'action de modification
		this.history.start("Modification de l'équipe \"" + equipe.getNom() + "\"");
		
		// Modifier l'équipe
		new ModelEquipe.Updater(this.concours).update(equipe, infos);
		
		// Fermer l'action de modification
		this.history.close();
	}
	public void updateEquipe (String nomEquipe, InfosModelEquipe.Statut statut) {
		// Récupérer l'équipe
		ModelEquipe equipe = this.concours.getEquipeByNom(nomEquipe);
		
		// Démarrer l'action de modification
		this.history.start("Changement du statut de l'équipe \"" + equipe.getNom() + "\"");
		
		// Modifier l'équipe
		equipe.setStatut(statut);
		
		// Fermer l'action de modification
		this.history.close();
	}
	public void updateEquipes(ArrayList<String> nomEquipes, InfosModelEquipe.Statut statut) {
		// Démarrer l'action de modification
		this.history.start("Changement du statut de plusieurs équipes");
		
		// Pour chaque équipe
		for(String nomEquipe : nomEquipes) {
			// Récupérer l'équipe et modifier son statut
			this.concours.getEquipeByNom(nomEquipe).setStatut(statut);
		}
		
		// Fermer l'action de modification
		this.history.close();
	}
	public void removeEquipe (String nomEquipe) throws ContestOrgModelException {
		// Récupérer l'équipe
		ModelEquipe equipe = this.concours.getEquipeByNom(nomEquipe);
		
		// Démarrer l'action de suppression
		this.history.start("Suppression de l'équipe \"" + equipe.getNom() + "\"");
		
		// Chercher l'équipe et la supprimer
		equipe.delete();
		
		// Fermer l'action de suppression
		this.history.close();
	}
	public void removeEquipes (ArrayList<String> nomEquipes) throws ContestOrgModelException {
		// Démarrer l'action de suppression
		this.history.start("Suppression de plusieurs équipes");
		
		// Pour chaque équipe
		for(String nomEquipe : nomEquipes) {
			// Récupérer l'équipe et la supprimer
			this.concours.getEquipeByNom(nomEquipe).delete();
		}
		
		// Fermer l'action de suppression
		this.history.close();
	}
	
	// Ajouter/Modifier/Supprimer une phase qualificative
	@SuppressWarnings("unchecked")
	public IGeneration<Configuration<String>> genererPhaseQualifAvance (String nomCategorie, String nomPoule, ArrayList<String> participantes) {
		// Récupérer les équipes participantes
		ArrayList<ModelEquipe> equipes = new ArrayList<ModelEquipe>();
		for(String participante : participantes) {
			equipes.add(this.concours.getEquipeByNom(participante));
		}
		
		// Ajouter l'équipe fantome si le nombre d'équipe est impaire
		if (equipes.size() % 2 == 1) {
			equipes.add(null);
		}
		
		// Récupérer la génération
		IGeneration<Configuration<ModelEquipe>> generation = ModelPhaseQualificative.genererConfigurationAvance(equipes.toArray(new ModelEquipe[equipes.size()]), new CompGenerationPhasesQualifs(this.concours.getComparateurPhasesQualificatives()));
		
		// Transformer et retourner la génération
		return this.transformGeneration(generation);
	}
	@SuppressWarnings("unchecked")
	public IGeneration<Configuration<String>> genererPhaseQualifBasique (String nomCategorie, String nomPoule, ArrayList<String> participantes) {
		// Récupérer les équipes participantes
		ArrayList<ModelEquipe> equipes = new ArrayList<ModelEquipe>();
		for(String participante : participantes) {
			equipes.add(this.concours.getEquipeByNom(participante));
		}
		
		// Ajouter l'équipe fantome si le nombre d'équipe est impaire
		if (equipes.size() % 2 == 1) {
			equipes.add(null);
		}

		// Récupérer la génération		
		IGeneration<Configuration<ModelEquipe>> generation = ModelPhaseQualificative.genererConfigurationBasique(equipes.toArray(new ModelEquipe[equipes.size()]), new CompGenerationPhasesQualifs(this.concours.getComparateurPhasesQualificatives()));
		
		// Récupérer et retourner la génération
		return this.transformGeneration(generation);
	}
	private IGeneration<Configuration<String>> transformGeneration(IGeneration<Configuration<ModelEquipe>> generation) {
		// Transformeur
		ITransformer<Configuration<ModelEquipe>, Configuration<String>> transformer = new ITransformer<Configuration<ModelEquipe>, Configuration<String>>() {	
			@Override
			public Configuration<String> transform (Configuration<ModelEquipe> configuration) {
				return configuration.transform(new ITransformer<ModelEquipe, String>() {
					@Override
					public String transform (ModelEquipe equipe) {
						return equipe == null ? null : equipe.getNom();
					}
				});
			}
		};
		
		// Appliquer le décorateur et retourner la génération
		return new GenerationDecorator<Configuration<ModelEquipe>, Configuration<String>>(generation, transformer);
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
	private Configuration<ModelEquipe> transformConfiguration(Configuration<String> configuration) {
		// Placer le concours en variable local finale
		final ModelConcours concours = this.concours;
		
		// Transformer la configuration et la retourner
		return configuration.transform(new ITransformer<String, ModelEquipe>() {
			@Override
			public ModelEquipe transform (String nomEquipe) {
				return nomEquipe == null ? null : concours.getEquipeByNom(nomEquipe);
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
	public void setEquipePhasesElims(String nomCategorie, int numeroCellule, String nomEquipe) throws ContestOrgModelException {
		// Démarrer l'action de modification
		this.history.start("Modification d'une équipe dans les phase éliminatoires de la catégorie \""+nomCategorie+"\"");
		
		// Récupérer la phases éliminatoires
		ModelPhasesEliminatoires phases = this.concours.getCategorieByNom(nomCategorie).getPhasesEliminatoires();
		
		// Récupérer le match
		ModelMatchPhasesElims match = phases.getMatch((numeroCellule-(numeroCellule%2))/2);
		
		// Récupérer la participation
		ModelParticipation participation = numeroCellule%2 == 0 ? match.getParticipationA() : match.getParticipationB();
		
		// Récupérer l'équipe
		ModelEquipe equipe = this.concours.getEquipeByNom(nomEquipe);
		
		// Retirer la participation de l'ancienne équipe
		if(participation.getEquipe() != null) {
			participation.getEquipe().removeParticipation(participation);
		}
		
		// Modifier l'équipe de la participation
		participation.setEquipe(equipe);
		
		// Ajouter la participation à la nouvelle équipe
		equipe.addParticipation(participation);
		
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
