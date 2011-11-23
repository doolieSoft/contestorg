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
import org.contestorg.infos.InfosModelParticipation;
import org.contestorg.infos.InfosModelParticipationObjectif;
import org.contestorg.infos.InfosModelPhaseQualificative;
import org.contestorg.interfaces.IClosableTableModel;
import org.contestorg.interfaces.IGeneration;
import org.contestorg.interfaces.ITransformer;

public class FrontModelPhasesQualificatives
{
	// Point d'entrée principal aux modèles
	private FrontModel frontModel;

	// TreeModels
	private TreeModelPhasesQualifs tm_qualifs;
	
	// Constructeur
	public FrontModelPhasesQualificatives(FrontModel frontModel) {
		this.frontModel = frontModel;
	}
	
	// Récupérer des données calculées
	public int getRangParticipantPhasesEliminatoires(String nomParticipant) {
		return this.frontModel.getConcours().getParticipantByNom(nomParticipant).getRangPhasesElims();
	}
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
	public int getNbPhasesElims(String nomCategorie) {
		ModelCategorie categorie = this.frontModel.getConcours().getCategorieByNom(nomCategorie);
		if(categorie != null && categorie.getPhasesEliminatoires() != null) {
			return categorie.getPhasesEliminatoires().getNbPhases();
		} else {
			return 0;
		}
	}
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
	public int getRangParticipantPhasesQualificatives(String nomParticipant) {
		return this.frontModel.getConcours().getParticipantByNom(nomParticipant).getRangPhasesQualifs();
	}
	
	// Récupérer des données uniques
	public Triple<Triple<String, ArrayList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>, Triple<String, ArrayList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>,InfosModelMatchPhasesQualifs> getInfosMatchPhaseQualif(String nomCategorie, String nomPoule, Integer numeroPhase, int numeroMatch) {
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
		Pair<Triple<String, ArrayList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>, Triple<String, ArrayList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>> infos = this.getInfosMatch(match);
		
		// Retourner les informations du match
		return new Triple<Triple<String,ArrayList<Pair<String,InfosModelParticipationObjectif>>,InfosModelParticipation>, Triple<String,ArrayList<Pair<String,InfosModelParticipationObjectif>>,InfosModelParticipation>, InfosModelMatchPhasesQualifs>(infos.getFirst(), infos.getSecond(), match.toInfos());
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
		ModelPhaseQualificative phase = this.frontModel.getConcours().getCategorieByNom(nomCategorie).getPouleByNom(nomPoule).getPhasesQualificatives().get(numeroPhase);

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
	
	// Ajouter/Modifier/Supprimer une phase qualificative
	@SuppressWarnings("unchecked")
	public IGeneration<Configuration<String>> genererPhaseQualifAvance (String nomCategorie, String nomPoule, ArrayList<String> nomParticipants) {
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
	@SuppressWarnings("unchecked")
	public IGeneration<Configuration<String>> genererPhaseQualifBasique (String nomCategorie, String nomPoule, ArrayList<String> nomParticipants) {
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
		this.frontModel.getHistory().start("Ajout d'une phase qualificative dans la poule \"" + nomCategorie + " > " + nomPoule + "\"");
		
		// Récupérer la poule
		ModelPoule poule = this.frontModel.getConcours().getCategorieByNom(nomCategorie).getPouleByNom(nomPoule);
		
		// Ajouter la phase qualificative
		poule.addPhaseQualificative(new ModelPhaseQualificative(poule, infos.getThird(), this.transformConfiguration(infos.getFirst()), infos.getSecond()));
		
		// Fermer l'action d'ajout
		this.frontModel.getHistory().close();
	}
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
	public void removePhaseQualif (String nomCategorie, String nomPoule, int numeroPhase) throws ContestOrgModelException {
		// Démarrer l'action de suppression
		this.frontModel.getHistory().start("Suppression de la phase qualificative " + numeroPhase + " de la poule \"" + nomCategorie + " > " + nomPoule + "\"");
		
		// Supprimer la phase qualificative
		this.frontModel.getConcours().getCategorieByNom(nomCategorie).getPouleByNom(nomPoule).getPhasesQualificatives().get(numeroPhase).delete();
		
		// Fermer l'action de suppression
		this.frontModel.getHistory().close();
	}
	
	// Ajouter/Modifier/Supprimer un match de phase qualificative
	public void addMatchPhaseQualif (String nomCategorie, String nomPoule, int numeroPhase, Triple<Triple<String, TrackableList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>, Triple<String, TrackableList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>,InfosModelMatchPhasesQualifs> infos) throws ContestOrgModelException {
		// Démarrer l'action d'ajout
		this.frontModel.getHistory().start("Ajout d'un match dans la phase qualificative " + numeroPhase + " de la poule \"" + nomCategorie + " > " + nomPoule + "\"");
		
		// Récupérer la phase qualificative
		ModelPhaseQualificative phase = this.frontModel.getConcours().getCategorieByNom(nomCategorie).getPouleByNom(nomPoule).getPhasesQualificatives().get(numeroPhase);
		
		// Créer le match de phase qualificative
		phase.addMatch(new ModelMatchPhasesQualifs.UpdaterForPhaseQualif(phase).create(infos));
		
		// Fermer l'action d'ajout
		this.frontModel.getHistory().close();
	}
	public void updateMatchPhaseQualif(String nomCategorie, String nomPoule, Integer numeroPhase, int numeroMatch, Triple<Triple<String, TrackableList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>, Triple<String, TrackableList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>,InfosModelMatchPhasesQualifs> infos) {
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
	public void removeMatchPhaseQualif(String nomCategorie, String nomPoule, Integer numeroPhase, int numeroMatch) throws ContestOrgModelException {
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
	public TreeModel getTreeModelPhasesQualifs () {
		if (this.tm_qualifs == null) {
			this.tm_qualifs = new TreeModelPhasesQualifs();
		}
		return this.tm_qualifs;
	}
	
	// Récupérer des TableModels
	public IClosableTableModel getTableModelMatchsPhasesQualifs () {
		return new TableModelPhasesQualifsMatchs(this.frontModel.getConcours());
	}
	public IClosableTableModel getTableModelMatchsPhasesQualifs (String nomCategorie) {
		return new TableModelPhasesQualifsMatchs(this.frontModel.getConcours().getCategorieByNom(nomCategorie));
	}
	public IClosableTableModel getTableModelMatchsPhasesQualifs (String nomCategorie, String nomPoule) {
		return new TableModelPhasesQualifsMatchs(this.frontModel.getConcours().getCategorieByNom(nomCategorie).getPouleByNom(nomPoule));
	}
	public IClosableTableModel getTableModelMatchsPhasesQualifs (String nomCategorie, String nomPoule, int numeroPhase) {
		return new TableModelPhasesQualifsMatchs(this.frontModel.getConcours().getCategorieByNom(nomCategorie).getPouleByNom(nomPoule).getPhasesQualificatives().get(numeroPhase));
	}
	
}
