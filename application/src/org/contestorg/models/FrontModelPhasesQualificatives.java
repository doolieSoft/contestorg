package org.contestorg.models;

import java.util.ArrayList;

import javax.swing.tree.TreeModel;

import org.contestorg.common.GenerationDecorator;
import org.contestorg.common.Pair;
import org.contestorg.common.Quadruple;
import org.contestorg.common.TrackableList;
import org.contestorg.common.Triple;
import org.contestorg.comparators.CompGenerationPhasesQualifs;
import org.contestorg.infos.Configuration;
import org.contestorg.infos.Couple;
import org.contestorg.infos.InfosModelMatchPhasesQualifs;
import org.contestorg.infos.InfosModelObjectifRemporte;
import org.contestorg.infos.InfosModelParticipation;
import org.contestorg.infos.InfosModelPhaseQualificative;
import org.contestorg.interfaces.IClosableTableModel;
import org.contestorg.interfaces.IGeneration;
import org.contestorg.interfaces.ITransformer;

/**
 * Point d'entrée aux modèles concernant les phases qualificatives
 */
public class FrontModelPhasesQualificatives
{
	/** Point d'entrée principal aux modèles */
	private FrontModel frontModel;

	// TreeModels
	
	/** TreeModel des phases qualificatives */
	private TreeModelPhasesQualifs tm_qualifs;
	
	/**
	 * Constructeur
	 * @param frontModel point d'entrée principal aux modèles
	 */
	public FrontModelPhasesQualificatives(FrontModel frontModel) {
		this.frontModel = frontModel;
	}
	
	// Récupérer des données calculées
	
	/**
	 * Récupérer le rang d'un participant aux phases éliminatoires
	 * @param nomParticipant nom du participant
	 * @return rang du participant aux phases éliminatoires
	 */
	public int getRangParticipantPhasesEliminatoires(String nomParticipant) {
		return this.frontModel.getConcours().getParticipantByNom(nomParticipant).getRangPhasesElims();
	}
	
	/**
	 * Récupérer le nombre de rencontres entre deux participants
	 * @param nomParticipantA nom du premier participant
	 * @param nomParticipantB nom du deuxième participant
	 * @return nombre de rencontres entre deux participants
	 */
	public int getNbRencontres(String nomParticipantA, String nomParticipantB) {
		return nomParticipantA != null ? this.frontModel.getConcours().getParticipantByNom(nomParticipantA).getNbRencontres(this.frontModel.getConcours().getParticipantByNom(nomParticipantB)) : (nomParticipantB == null ? 0 : this.frontModel.getConcours().getParticipantByNom(nomParticipantB).getNbRencontres(this.frontModel.getConcours().getParticipantByNom(nomParticipantA)));
	}
	
	/**
	 * Récupérer le nombre de phases éliminatoires possibles d'une catégorie donnée
	 * @param nomCategorie nom de la catégorie
	 * @return nombre de phases éliminatoires possible pour la catégorie
	 */
	public int getNbPhasesElimsPossibles(String nomCategorie) {
		// Récupérer le nombre de participants
		int nbParticipants = this.frontModel.getConcours().getCategorieByNom(nomCategorie).getParticipantsParticipants().size();
		
		// Compter le nombre de phases possibles
		int nbPhases = 0;
		for(int i=nbParticipants;i>1;i=i/2) {
			nbPhases++;
		}
		
		// Retourner le nombre de phases possibles
		return nbPhases;
	}
	
	/**
	 * Récupérer le nombre de phases éliminatoires générées d'une catégorie donnée
	 * @param nomCategorie nom de la catégorie
	 * @return nombre de phases éliminatoires générées pour la catégorie
	 */
	public int getNbPhasesElims(String nomCategorie) {
		ModelCategorie categorie = this.frontModel.getConcours().getCategorieByNom(nomCategorie);
		if(categorie != null && categorie.getPhasesEliminatoires() != null) {
			return categorie.getPhasesEliminatoires().getNbPhases();
		} else {
			return 0;
		}
	}
	
	/**
	 * Récupérer le nombre de matchs déjà joués au sein d'une configuration
	 * @param configuration configuration
	 * @return nombre de matchs déjà joués au sein de la configuration
	 */
	public int getNbMatchDejaJoues(Configuration<String> configuration) {
		int nbMatchDejaJoues = 0;
		for(Couple<String> couple : configuration.getCouples()) {
			if(couple.getParticipantA() != null && couple.getParticipantB() != null) {
				ModelParticipant participantA = this.frontModel.getConcours().getParticipantByNom(couple.getParticipantA());
				ModelParticipant participantB = this.frontModel.getConcours().getParticipantByNom(couple.getParticipantB());
				nbMatchDejaJoues += participantA.getNbRencontres(participantB);
			}
		}
		return nbMatchDejaJoues;
	}
	
	/**
	 * Vérifier si un participant peut participer aux phases qualificatives
	 * @param nomCategorie nom de la catégorie
	 * @param nomPoule nom de la poule
	 * @param numeroPhase numéro de la phase qualificative
	 * @param nomParticipant nom du participant
	 * @return le participant peut participer aux phases qualificatives ?
	 */
	public boolean isParticipantPhasesQualif(String nomCategorie, String nomPoule, int numeroPhase, String nomParticipant) {
		ModelPhaseQualificative phase = this.frontModel.getConcours().getCategorieByNom(nomCategorie).getPouleByNom(nomPoule).getPhasesQualificatives().get(numeroPhase);
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
	
	/**
	 * Récupérer les données manquantes à partir d'un numéro de match
	 * @param nomCategorie nom de la catégorie si elle est connue
	 * @param nomPoule nom de la poule si elle est connue
	 * @param numeroPhase numero de la phase qualificative si elle est connue
	 * @param numeroMatch numéro de match
	 * @return données manquantes retrouvées à partir du numéro de match
	 */
	public Quadruple<String,String,Integer,Integer> getCategoriePoulePhaseQualif(String nomCategorie,String nomPoule,Integer numeroPhase,int numeroMatch) {
		// Récupérer les informations
		ModelCategorie categorie = null;
		ModelPoule poule = null;
		ModelPhaseQualificative phase = null;
		ModelMatchPhasesQualifs match = null;
		if(nomCategorie == null) {
			match = this.frontModel.getConcours().getMatchsPhasesQualifs().get(numeroMatch);
			phase = match.getPhaseQualificative();
			poule = phase.getPoule();
			categorie = poule.getCategorie();
		} else if(nomPoule == null) {
			categorie = this.frontModel.getConcours().getCategorieByNom(nomCategorie);
			match = categorie.getMatchsPhasesQualifs().get(numeroMatch);
			phase = match.getPhaseQualificative();
			poule = phase.getPoule();
		} else if(numeroPhase == null) {
			categorie = this.frontModel.getConcours().getCategorieByNom(nomCategorie);
			poule = categorie.getPouleByNom(nomPoule);
			match = poule.getMatchsPhasesQualifs().get(numeroMatch);
			phase = match.getPhaseQualificative();
		} else {
			return new Quadruple<String,String,Integer,Integer>(nomCategorie,nomPoule,numeroPhase,numeroMatch);
		}
		
		// Retourner les informations
		return new Quadruple<String,String,Integer,Integer>(categorie.getNom(),poule.getNom(),phase.getNumero(),match.getNumero());
	}
	
	/**
	 * Récupérer le rang d'un participant aux phases qualificatives
	 * @param nomParticipant nom du participant
	 * @return rang du participant aux phases qualificatives
	 */
	public int getRangPhasesQualifs(String nomParticipant) {
		return this.frontModel.getConcours().getParticipantByNom(nomParticipant).getRangPhasesQualifs();
	}
	
	// Récupérer des données uniques
	
	/**
	 * Récupérer les informations sur un match
	 * @param nomCategorie nom de la catégorie
	 * @param nomPoule nom de la poule
	 * @param numeroPhase numéro de la phase qualificative
	 * @param numeroMatch numéro du match
	 * @return informations sur le match
	 */
	public Triple<Triple<String, ArrayList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>, Triple<String, ArrayList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>,InfosModelMatchPhasesQualifs> getInfosMatch(String nomCategorie, String nomPoule, Integer numeroPhase, int numeroMatch) {
		// Récupérer le match
		ModelMatchPhasesQualifs match;
		if(nomCategorie == null) {
			match = this.frontModel.getConcours().getMatchsPhasesQualifs().get(numeroMatch);
		} else if(nomPoule == null) {
			match = this.frontModel.getConcours().getCategorieByNom(nomCategorie).getMatchsPhasesQualifs().get(numeroMatch);
		} else if(numeroPhase == null) {
			match = this.frontModel.getConcours().getCategorieByNom(nomCategorie).getPouleByNom(nomPoule).getMatchsPhasesQualifs().get(numeroMatch);
		} else {
			match = this.frontModel.getConcours().getCategorieByNom(nomCategorie).getPouleByNom(nomPoule).getPhasesQualificatives().get(numeroPhase).getMatchs().get(numeroMatch);
		}
		
		// Récupérer les informations du match
		Pair<Triple<String, ArrayList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>, Triple<String, ArrayList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>> infos = this.getInfosMatch(match);
		
		// Retourner les informations du match
		return new Triple<Triple<String,ArrayList<Pair<String,InfosModelObjectifRemporte>>,InfosModelParticipation>, Triple<String,ArrayList<Pair<String,InfosModelObjectifRemporte>>,InfosModelParticipation>, InfosModelMatchPhasesQualifs>(infos.getFirst(), infos.getSecond(), match.getInfos());
	}
	
	/**
	 * Récupérer les informations sur un match
	 * @param match match
	 * @return informations sur le match
	 */
	private Pair<Triple<String, ArrayList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>, Triple<String, ArrayList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>> getInfosMatch(ModelMatchAbstract match) {
		// Récupérer les informations de participation du participant A
		String nomParticipantA = match.getParticipationA() == null || match.getParticipationA().getParticipant() == null ? null : match.getParticipationA().getParticipant().getNom();
		ArrayList<Pair<String, InfosModelObjectifRemporte>> objectifsRemportesA = new ArrayList<Pair<String,InfosModelObjectifRemporte>>();
		for(ModelObjectifRemporte objectifRemporte : match.getParticipationA().getObjectifsRemportes()) {
			objectifsRemportesA.add(new Pair<String, InfosModelObjectifRemporte>(objectifRemporte.getObjectif().getNom(), objectifRemporte.getInfos()));
		}
		Triple<String, ArrayList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation> infosParticipationA = new Triple<String, ArrayList<Pair<String,InfosModelObjectifRemporte>>, InfosModelParticipation>(nomParticipantA, objectifsRemportesA, match.getParticipationA().getInfos());
		
		// Récupérer les informations de participation du participant B
		String nomParticipantB = match.getParticipationB() == null || match.getParticipationB().getParticipant() == null ? null : match.getParticipationB().getParticipant().getNom();
		ArrayList<Pair<String, InfosModelObjectifRemporte>> objectifsRemportesB = new ArrayList<Pair<String,InfosModelObjectifRemporte>>();
		for(ModelObjectifRemporte objectifRemporte : match.getParticipationB().getObjectifsRemportes()) {
			objectifsRemportesB.add(new Pair<String, InfosModelObjectifRemporte>(objectifRemporte.getObjectif().getNom(), objectifRemporte.getInfos()));
		}
		Triple<String, ArrayList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation> infosParticipationB = new Triple<String, ArrayList<Pair<String,InfosModelObjectifRemporte>>, InfosModelParticipation>(nomParticipantB, objectifsRemportesB, match.getParticipationB().getInfos());
		
		// Assembler les informations demandées et les retourner
		return new Pair<Triple<String,ArrayList<Pair<String,InfosModelObjectifRemporte>>,InfosModelParticipation>, Triple<String,ArrayList<Pair<String,InfosModelObjectifRemporte>>,InfosModelParticipation>>(infosParticipationA, infosParticipationB);
	}
	
	/**
	 * Récupérer des informations sur une phase qualificative
	 * @param nomCategorie nom de la catégorie
	 * @param nomPoule nom de la poule
	 * @param numeroPhase numéro de la phase qualificative
	 * @return informations sur la phase qualificative
	 */
	public Triple<Configuration<String>,InfosModelPhaseQualificative,InfosModelMatchPhasesQualifs> getInfosPhaseQualif(String nomCategorie,String nomPoule,int numeroPhase) {
		// Récupérer la phase qualificative
		ModelPhaseQualificative phase = this.frontModel.getConcours().getCategorieByNom(nomCategorie).getPouleByNom(nomPoule).getPhasesQualificatives().get(numeroPhase);

		// Construire la configuration
		Configuration<String> configuration = new Configuration<String>(phase.getMatchs().size());
		for(ModelMatchPhasesQualifs match : phase.getMatchs()) {
			ModelParticipant participantA = match.getParticipationA().getParticipant();
			ModelParticipant participantB = match.getParticipationB().getParticipant();
			configuration.addCouple(new Couple<String>(participantA == null ? null : participantA.getNom(), participantB == null ? null : participantB.getNom()));
		}
		
		// Récupérer les informations de la phase qualificative
		InfosModelPhaseQualificative infosPhase = phase.getInfos();
		
		// Récupérer les informations des matchs de la phase qualificative
		InfosModelMatchPhasesQualifs infosMatchs = new InfosModelMatchPhasesQualifs(null,null);
		
		// Assembler les informations demandées et les retourner
		return new Triple<Configuration<String>, InfosModelPhaseQualificative, InfosModelMatchPhasesQualifs>(configuration, infosPhase, infosMatchs);
	}
	
	// Ajouter/Modifier/Supprimer une phase qualificative
	
	/**
	 * Générer une phase qualificative en mode avancé
	 * @param nomCategorie nom de la catégorie
	 * @param nomPoule nom de la poule
	 * @param participantes liste des participants
	 * @return génération de phase qualificative en mode avancé
	 */
	@SuppressWarnings("unchecked")
	public IGeneration<Configuration<String>> getGenerationAvance (String nomCategorie, String nomPoule, ArrayList<String> nomParticipants) {
		// Récupérer les participants
		ArrayList<ModelParticipant> participants = new ArrayList<ModelParticipant>();
		for(String nomParticipant : nomParticipants) {
			participants.add(this.frontModel.getConcours().getParticipantByNom(nomParticipant));
		}
		
		// Ajouter le participant fantome si le nombre de participants est impaire
		if (participants.size() % 2 == 1) {
			participants.add(null);
		}
		
		// Récupérer la génération
		IGeneration<Configuration<ModelParticipant>> generation = ModelPhaseQualificative.genererConfigurationAvance(participants.toArray(new ModelParticipant[participants.size()]), new CompGenerationPhasesQualifs(this.frontModel.getConcours().getComparateurPhasesQualificatives()));
		
		// Retourner le résultat de la génération
		return this.transformGeneration(generation);
	}
	
	/**
	 * Générer une phase qualificative en mode basique
	 * @param nomCategorie nom de la catégorie
	 * @param nomPoule nom de la poule
	 * @param participantes liste des participants
	 * @return génération de phase qualificative en mode basique
	 */
	@SuppressWarnings("unchecked")
	public IGeneration<Configuration<String>> getGenerationBasique (String nomCategorie, String nomPoule, ArrayList<String> nomParticipants) {
		// Récupérer les participants
		ArrayList<ModelParticipant> participants = new ArrayList<ModelParticipant>();
		for(String nomParticipant : nomParticipants) {
			participants.add(this.frontModel.getConcours().getParticipantByNom(nomParticipant));
		}
		
		// Ajouter le participant fantome si le nombre de participants est impaire
		if (participants.size() % 2 == 1) {
			participants.add(null);
		}

		// Récupérer la génération		
		IGeneration<Configuration<ModelParticipant>> generation = ModelPhaseQualificative.genererConfigurationBasique(participants.toArray(new ModelParticipant[participants.size()]), new CompGenerationPhasesQualifs(this.frontModel.getConcours().getComparateurPhasesQualificatives()));
		
		// Retourner le résultat de la génération
		return this.transformGeneration(generation);
	}
	
	/**
	 * Transformer une génération de configuration de participants en génération de configuration de noms de participants
	 * @param generation génération
	 * @return génération de configuration de noms de participants
	 */
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
	
	/**
	 * Ajouter une phase qualificative
	 * @param nomCategorie nom de la catégorie
	 * @param nomPoule nom de la poule
	 * @param infos informations de la phase qualificative
	 * @throws ContestOrgModelException
	 */
	public void addPhaseQualif (String nomCategorie, String nomPoule, Triple<Configuration<String>,InfosModelPhaseQualificative,InfosModelMatchPhasesQualifs> infos) throws ContestOrgModelException {
		// Démarrer l'action d'ajout
		this.frontModel.getHistory().start("Ajout d'une phase qualificative dans la poule \"" + nomCategorie + " > " + nomPoule + "\"");
		
		// Récupérer la poule
		ModelPoule poule = this.frontModel.getConcours().getCategorieByNom(nomCategorie).getPouleByNom(nomPoule);
		
		// Ajouter la phase qualificative
		poule.addPhaseQualificative(new ModelPhaseQualificative(poule, infos.getThird(), this.transformConfiguration(infos.getFirst()), infos.getSecond()));
		
		// Fermer l'action d'ajout
		this.frontModel.getHistory().close();
	}
	
	/**
	 * Modifier une phase qualificative
	 * @param nomCategorie nom de la catégorie
	 * @param nomPoule nom de la poule
	 * @param numeroPhase numéro de la phase qualificative
	 * @param infos nouvelles informations de la phase qualificative
	 * @throws ContestOrgModelException
	 */
	public void updatePhaseQualif (String nomCategorie, String nomPoule, int numeroPhase, Triple<Configuration<String>,InfosModelPhaseQualificative,InfosModelMatchPhasesQualifs> infos) throws ContestOrgModelException {
		// Démarrer l'action de modification
		this.frontModel.getHistory().start("Modification de la phase qualificative " + numeroPhase + " de la poule \"" + nomCategorie + " > " + nomPoule + "\"");
		
		// Récupérer la phase qualificative
		ModelPhaseQualificative phase = this.frontModel.getConcours().getCategorieByNom(nomCategorie).getPouleByNom(nomPoule).getPhasesQualificatives().get(numeroPhase);
		
		// Modifier la phase qualificative
		phase.setInfos(infos.getSecond());
		phase.setConfiguration(infos.getThird(), this.transformConfiguration(infos.getFirst()));
		
		// Fermer l'action de modification
		this.frontModel.getHistory().close();
	}
	
	/**
	 * Transformer une configuration de noms de participants en configuration de participants
	 * @param configuration configuration de noms de participants
	 * @return configuration de participants
	 */
	private Configuration<ModelParticipant> transformConfiguration(Configuration<String> configuration) {
		// Placer le concours en variable local finale
		final ModelConcours concours = this.frontModel.getConcours();
		
		// Transformer la configuration et la retourner
		return configuration.transform(new ITransformer<String, ModelParticipant>() {
			@Override
			public ModelParticipant transform (String nomParticipant) {
				return nomParticipant == null ? null : concours.getParticipantByNom(nomParticipant);
			}
		});
	}
	
	/**
	 * Supprimer une phase qualificative
	 * @param nomCategorie nom de la catégorie
	 * @param nomPoule nom de la poule
	 * @param numeroPhase numéro de la phase qualificative
	 * @throws ContestOrgModelException
	 */
	public void removePhaseQualif (String nomCategorie, String nomPoule, int numeroPhase) throws ContestOrgModelException {
		// Démarrer l'action de suppression
		this.frontModel.getHistory().start("Suppression de la phase qualificative " + numeroPhase + " de la poule \"" + nomCategorie + " > " + nomPoule + "\"");
		
		// Supprimer la phase qualificative
		this.frontModel.getConcours().getCategorieByNom(nomCategorie).getPouleByNom(nomPoule).getPhasesQualificatives().get(numeroPhase).delete();
		
		// Fermer l'action de suppression
		this.frontModel.getHistory().close();
	}
	
	// Ajouter/Modifier/Supprimer un match de phase qualificative
	
	/**
	 * Ajouter un match
	 * @param nomCategorie nom de la catégorie
	 * @param nomPoule nom de la poule
	 * @param numeroPhase numéro de la phase qualificative
	 * @param infos informations du match
	 * @throws ContestOrgModelException
	 */
	public void addMatchPhaseQualif (String nomCategorie, String nomPoule, int numeroPhase, Triple<Triple<String, TrackableList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>, Triple<String, TrackableList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>,InfosModelMatchPhasesQualifs> infos) throws ContestOrgModelException {
		// Démarrer l'action d'ajout
		this.frontModel.getHistory().start("Ajout d'un match dans la phase qualificative " + numeroPhase + " de la poule \"" + nomCategorie + " > " + nomPoule + "\"");
		
		// Récupérer la phase qualificative
		ModelPhaseQualificative phase = this.frontModel.getConcours().getCategorieByNom(nomCategorie).getPouleByNom(nomPoule).getPhasesQualificatives().get(numeroPhase);
		
		// Créer le match de phase qualificative
		phase.addMatch(new ModelMatchPhasesQualifs.UpdaterForPhaseQualif(phase).create(infos));
		
		// Fermer l'action d'ajout
		this.frontModel.getHistory().close();
	}
	
	/**
	 * Modifier un match
	 * @param nomCategorie nom de la catégorie
	 * @param nomPoule nom de la poule
	 * @param numeroPhase numéro de la phase qualificative
	 * @param numeroMatch numéro du match
	 * @param infos nouvelles informations du match
	 */
	public void updateMatch(String nomCategorie, String nomPoule, Integer numeroPhase, int numeroMatch, Triple<Triple<String, TrackableList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>, Triple<String, TrackableList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>,InfosModelMatchPhasesQualifs> infos) {
		// Démarrer l'action de modification
		this.frontModel.getHistory().start("Modification du match "+numeroMatch+" de la phase qualificative " + numeroPhase + " de la poule \"" + nomCategorie + " > " + nomPoule + "\"");
		
		// Récupérer le match
		ModelMatchPhasesQualifs match;
		if(nomCategorie == null) {
			match = this.frontModel.getConcours().getMatchsPhasesQualifs().get(numeroMatch);
		} else if(nomPoule == null) {
			match = this.frontModel.getConcours().getCategorieByNom(nomCategorie).getMatchsPhasesQualifs().get(numeroMatch);
		} else if(numeroPhase == null) {
			match = this.frontModel.getConcours().getCategorieByNom(nomCategorie).getPouleByNom(nomPoule).getMatchsPhasesQualifs().get(numeroMatch);
		} else {
			match = this.frontModel.getConcours().getCategorieByNom(nomCategorie).getPouleByNom(nomPoule).getPhasesQualificatives().get(numeroPhase).getMatchs().get(numeroMatch);
		}
		
		// Modifier le match
		new ModelMatchPhasesQualifs.UpdaterForPhaseQualif(match.getPhaseQualificative()).update(match, infos);
		
		// Fermer l'action de modification
		this.frontModel.getHistory().close();
	}
	
	/**
	 * Supprimer un match
	 * @param nomCategorie nom de la catégorie
	 * @param nomPoule nom de la poule
	 * @param numeroPhase numéro de la phase qualificative
	 * @param numeroMatch numéro du match
	 * @throws ContestOrgModelException
	 */
	public void removeMatch(String nomCategorie, String nomPoule, Integer numeroPhase, int numeroMatch) throws ContestOrgModelException {
		// Démarrer l'action de suppression
		this.frontModel.getHistory().start("Suppression du match "+numeroMatch+" de la phase qualificative " + numeroPhase + " de la poule \"" + nomCategorie + " > " + nomPoule + "\"");
		
		// Supprimer le match
		if(nomCategorie == null) {
			this.frontModel.getConcours().getMatchsPhasesQualifs().get(numeroMatch).delete();
		} else if(nomPoule == null) {
			this.frontModel.getConcours().getCategorieByNom(nomCategorie).getMatchsPhasesQualifs().get(numeroMatch).delete();
		} else if(numeroPhase == null) {
			this.frontModel.getConcours().getCategorieByNom(nomCategorie).getPouleByNom(nomPoule).getMatchsPhasesQualifs().get(numeroMatch).delete();
		} else {
			this.frontModel.getConcours().getCategorieByNom(nomCategorie).getPouleByNom(nomPoule).getPhasesQualificatives().get(numeroPhase).getMatchs().get(numeroMatch).delete();
		}
		
		// Fermer l'action de suppression
		this.frontModel.getHistory().close();
	}

	// Récupérer des TreeModels
	
	/**
	 * Récupérer le TreeModel des phases qualificatives
	 * @return TreeModel des phases qualificatives
	 */
	public TreeModel getTreeModelPhasesQualifs () {
		if (this.tm_qualifs == null) {
			this.tm_qualifs = new TreeModelPhasesQualifs();
		}
		return this.tm_qualifs;
	}
	
	// Récupérer des TableModels
	
	/**
	 * Récupérer le TableModel des matchs des phases qualificatives
	 * @return TableModel des matchs des phases qualificatives
	 */
	public IClosableTableModel getTableModelMatchsPhasesQualifs () {
		return new TableModelPhasesQualifsMatchs(this.frontModel.getConcours());
	}
	
	/**
	 * Récupérer le TableModel des matchs des phases qualificatives d'une catégorie donnée
	 * @param nomCategorie nom de la catégorie
	 * @return TableModel des matchs des phases qualificatives de la catégorie
	 */
	public IClosableTableModel getTableModelMatchsPhasesQualifs (String nomCategorie) {
		return new TableModelPhasesQualifsMatchs(this.frontModel.getConcours().getCategorieByNom(nomCategorie));
	}
	
	/**
	 * Récupérer le TableModel des matchs des phases qualificatives d'une poule donnée d'une catégorie donnée
	 * @param nomCategorie nom de la catégorie
	 * @param nomPoule nom de la poule
	 * @return TableModel des matchs des phases qualificatives de la poule de la catégorie
	 */
	public IClosableTableModel getTableModelMatchsPhasesQualifs (String nomCategorie, String nomPoule) {
		return new TableModelPhasesQualifsMatchs(this.frontModel.getConcours().getCategorieByNom(nomCategorie).getPouleByNom(nomPoule));
	}
	
	/**
	 * Récupérer le TableModel des matchs d'une phase qualificative
	 * @param nomCategorie nom de la catégorie
	 * @param nomPoule nom de la poule
	 * @param numeroPhase numéro de la phase qualificative
	 * @return TableModel des matchs de la phase qualificative
	 */
	public IClosableTableModel getTableModelMatchsPhasesQualifs (String nomCategorie, String nomPoule, int numeroPhase) {
		return new TableModelPhasesQualifsMatchs(this.frontModel.getConcours().getCategorieByNom(nomCategorie).getPouleByNom(nomPoule).getPhasesQualificatives().get(numeroPhase));
	}
	
}
