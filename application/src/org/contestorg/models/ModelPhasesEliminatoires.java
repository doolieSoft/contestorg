package org.contestorg.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.contestorg.common.ContestOrgErrorException;
import org.contestorg.comparators.CompPhasesElims;
import org.contestorg.infos.InfosModelMatchPhasesElims;
import org.contestorg.infos.InfosModelParticipation;
import org.contestorg.infos.InfosModelPhasesEliminatoires;

/**
 * Phases éliminatoires
 */
public class ModelPhasesEliminatoires extends ModelAbstract
{
	
	// Roles
	
	/** Finale */
	public static final int FINALE = 1;
	
	/** Petite finale */
	public static final int PETITE_FINALE = 2;
	
	// Attributs objets
	
	/** Catégorie */
	private ModelCategorie categorie;
	
	/** Grande finale */
	private ModelMatchPhasesElims grandeFinale;
	
	/** Petite finale */
	private ModelMatchPhasesElims petiteFinale;
	
	// Constructeurs
	
	/**
	 * Constructeur
	 * @param categorie catégorie
	 * @param infos informations des phases éliminatoires
	 */
	public ModelPhasesEliminatoires(ModelCategorie categorie, InfosModelPhasesEliminatoires infos) {
		// Retenir la categorie
		this.categorie = categorie;
		
		// Enregistrer les informations
		this.setInfos(infos);
	}
	
	/**
	 * Constructeur par copie
	 * @param categorie catégorie
	 * @param phasesEliminatoires phases éliminatoires
	 */
	protected ModelPhasesEliminatoires(ModelCategorie categorie, ModelPhasesEliminatoires phasesEliminatoires) {
		// Appeller le constructeur principal
		this(categorie, phasesEliminatoires.getInfos());
		
		// Récupérer l'id
		this.setId(phasesEliminatoires.getId());
	}
	
	// Getters
	
	/**
	 * Récupérer la catégorie
	 * @return catégorie
	 */
	public ModelCategorie getCategorie () {
		return this.categorie;
	}
	
	/**
	 * Récupérer la grande finale
	 * @return grande finale
	 */
	public ModelMatchPhasesElims getGrandeFinale () {
		return this.grandeFinale;
	}
	
	/**
	 * Récupérer la petite finale
	 * @return petite finale
	 */
	public ModelMatchPhasesElims getPetiteFinale () {
		return this.petiteFinale;
	}
	
	/**
	 * Récupérer le classement des participants
	 * @return classement des participants
	 */
	public ArrayList<ModelParticipant> getClassement () {
		if (this.grandeFinale != null) {
			// Récupérer, trier et retourner la liste des participants
			ArrayList<ModelParticipant> participants = this.grandeFinale.getParticipants();
			Collections.sort(participants, new CompPhasesElims());
			return participants;
		}
		return new ArrayList<ModelParticipant>();
	}
	
	/**
	 * Récupérer le rang d'un participant
	 * @param participant participant
	 * @return rang du participant
	 */
	public int getRang (ModelParticipant participant) {
		// Est ce que le participant a participé à la petite finale ?
		if (this.petiteFinale != null && this.petiteFinale.getParticipationA() != null && this.petiteFinale.getParticipationB() != null) {
			if (this.petiteFinale.getParticipationA().getParticipant().equals(participant)) {
				return this.petiteFinale.getParticipationA().getResultat() == InfosModelParticipation.RESULTAT_VICTOIRE ? 3 : 4;
			}
			if (this.petiteFinale.getParticipationB().getParticipant().equals(participant)) {
				return this.petiteFinale.getParticipationB().getResultat() == InfosModelParticipation.RESULTAT_VICTOIRE ? 3 : 4;
			}
		}
		
		// Rechercher le rang du participant dans les phases éliminatoires
		if (this.grandeFinale != null) {
			return this.grandeFinale.getRang(participant);
		}
		
		// Retourner -1 si les phases éliminatoires ne sont pas encore générées
		return -1;
	}
	
	/**
	 * Récupérer le nombre de phases éliminatoires
	 * @return récupérer le nombre de phases éliminatoires
	 */
	public int getNbPhases () {		
		// Initialiser le nombre de phases
		int nbPhases = 0;
		
		// Compter le nombre de phases
		ModelMatchPhasesElims match = this.grandeFinale;
		while (match != null) {
			// Incrémenter le nombre de phases
			nbPhases++;
			
			// Passer au match précédant
			match = match.getMatchPrecedantA();
		}
		
		// Retourner le nombre de phases
		return nbPhases;
	}
	
	/**
	 * Récupérer le nombre de matchs de la première phase éliminatoire
	 * @return nombre de matchs de la première phase éliminatoire
	 */
	public int getNbMatchs() {
		// Calculer et retourner le nombre de matchs pour la première phase
		return new Double(Math.pow(2, this.getNbPhases()-1)).intValue();
	}
	
	/**
	 * Récupérer le nombre de participants
	 * @return nombre de participants
	 */
	public int getNbParticipants() {
		// Calculer et retourner le nombre de participants qui ont participer
		return new Double(Math.pow(2, this.getNbPhases())).intValue();
	}
	
	/**
	 * Récupérer la liste des matchs d'une phase éliminatoire
	 * @param numPhase numéro de la phase éliminatoire
	 * @return liste des matchs d'une phase éliminatoire
	 */
	public ArrayList<ModelMatchPhasesElims> getMatchs (int numPhase) {
		// Trouver le nombre de phases à traverser depuis la finale
		int nbPhases = this.getNbPhases() - numPhase - 1;
		
		// Initialiser la liste des matchs
		ArrayList<ModelMatchPhasesElims> matchs = new ArrayList<ModelMatchPhasesElims>((int)Math.pow(2, nbPhases));
		
		// Récupérer la liste des matchs
		this.getMatchs(nbPhases, this.grandeFinale, matchs);
		
		// Retourner la liste des matchs
		return matchs;
	}
	
	/**
	 * Récupérer la liste des matchs d'une phase éliminatoire
	 * @param nbPhases nombre de phases éliminatoires restantes à traverser
	 * @param match match de la phase courante
	 * @param matchs liste des matchs de la phase éliminatoire visée
	 */
	private void getMatchs (int nbPhases, ModelMatchPhasesElims match, ArrayList<ModelMatchPhasesElims> matchs) {
		// Vérifier si le nombre de phases à traverser est à 0
		if (nbPhases == 0) {
			// Ajouter le match à la liste des matchs
			matchs.add(match);
		} else {
			// Recursivité sur les deux matchs précédants
			this.getMatchs(nbPhases - 1, match.getMatchPrecedantA(), matchs);
			this.getMatchs(nbPhases - 1, match.getMatchPrecedantB(), matchs);
		}
	}
	
	/**
	 * Récupérer un match d'après son numéro
	 * @param numeroMatch numéro du match
	 * @return match trouvé
	 */
	public ModelMatchPhasesElims getMatch(int numeroMatch) {
		// Récupérer le nombre de phases éliminatoires
		int nbPhases = this.getNbPhases();
		
		// Récupérer le nombre de matchs pour la première phase
		int nbMatchs = this.getNbMatchs();
		
		// Chercher le match dans chacune des phases
		for(int numPhase=0;numPhase<nbPhases;numPhase++,nbMatchs=nbMatchs/2) {
			// Si le match recherché se trouve dans la phase
			if(numeroMatch < nbMatchs) {
				// Retourner le match
				return this.getMatchs(numPhase).get(numeroMatch);
			}
			
			// Décrémenter le numero du match recherché
			numeroMatch -= nbMatchs;
		}
		
		// Retourner null si le match n'a pas été trouvé
		return null;
	}
	
	// Setters
	
	/**
	 * Définir les informations des phases éliminatoires
	 * @param infos informations des phases éliminatoires
	 */
	protected void setInfos (InfosModelPhasesEliminatoires infos) {
		// Appeller le setInfos du parent
		super.setInfos(infos);
		
		// Fire update
		this.fireUpdate();
	}
	
	/**
	 * Définir la grande finale
	 * @param grandeFinale grande finale
	 */
	public void setGrandeFinale (ModelMatchPhasesElims grandeFinale) {
		// Retenir la finale
		this.grandeFinale = grandeFinale;
		
		// Fire update
		this.fireUpdate();
	}
	
	/**
	 * Définir la petite finale
	 * @param petiteFinale petite finale
	 */
	public void setPetiteFinale (ModelMatchPhasesElims petiteFinale) {
		// Retenir la finale
		this.petiteFinale = petiteFinale;
		
		// Fire update
		this.fireUpdate();
	}
	
	// Actions
	
	/**
	 * Générer les phases éliminatoires
	 * @param categorie catégorie
	 * @param nbPhases nombre de phases éliminatoires à générer
	 * @param infosMatchs informations des matchs
	 * @param infosPhaseEliminatoire informations des phases éliminatoires
	 * @return phases éliminatoires
	 * @throws ContestOrgErrorException
	 */
	@SuppressWarnings("unchecked")
	protected static ModelPhasesEliminatoires genererPhasesEliminatoires (ModelCategorie categorie, int nbPhases, InfosModelMatchPhasesElims infosMatchs, InfosModelPhasesEliminatoires infosPhaseEliminatoire) throws ContestOrgErrorException {
		// Récupérer la liste des participants pour chaque poule triée d'après leur rang aux phases qualificatives
		int nbParticipants = 0;
		List<List<ModelParticipant>> participantsPoules = new ArrayList<List<ModelParticipant>>();
		for(ModelPoule poule : categorie.getPoules()) {
			List<ModelParticipant> participantsPoule = poule.getParticipantsParticipants();
			Collections.sort(participantsPoule, categorie.getConcours().getComparateurPhasesQualificatives()); 
			participantsPoules.add(participantsPoule);
			nbParticipants += participantsPoule.size();
		}
		
		// Rassembler tous les participants au sein d'une même liste en alternant sur chacune des poules
		List<ModelParticipant> participants = new ArrayList<ModelParticipant>();
		while(nbParticipants != 0) {
			for(List<ModelParticipant> participantsPoule : participantsPoules) {
				if(nbParticipants != 0 && participantsPoule.size() != 0) {
					participants.add(participantsPoule.remove(0));
					nbParticipants--;
				}
			}
		}
		
		// Inverser la liste pour avoir les meilleurs participants en premier
		Collections.reverse(participants);
		
		// Garder les participants qualifiés aux phases éliminatoires
		int nbParticipantsQualifies = (int)Math.pow(2, nbPhases);
		participants = participants.subList(0, nbParticipantsQualifies);
		
		// Tirer les couples de participants de la première phase éliminatoire
		/*
		 * Explication de l'algorythme :
		 * - on ajoute le premier participant
		 * - à chaque tour on ajoute des participants :
		 *    - de maniere à doubler le nombre de participants
		 *    - les plus "mauvais" participants se placent sous les "meilleurs"
		 */
		ArrayList<Integer> numeros = new ArrayList<Integer>();
		numeros.add(0);
		for (int i = 2; i < nbParticipantsQualifies * 2; i = i * 2) { // i : Nombre de participants restants dans la phase
			for (int j = 0; j < i; j = j + 2) { // j : Curseur sur les "bons" participants
				numeros.add(j + 1, i - numeros.get(j) - 1); // Ajouter le "mauvais" participant sous le "bon" participant
			}
		}
		
		// Créer la phase éliminatoire
		ModelPhasesEliminatoires phasesEliminatoires = new ModelPhasesEliminatoires(categorie, infosPhaseEliminatoire);
		
		// Créer les matchs
		ModelMatchPhasesElims match = null;
		ModelMatchPhasesElims matchPrecedantA = null;
		ModelMatchPhasesElims matchPrecedantB = null;
		ArrayList<ModelMatchPhasesElims> matchs = new ArrayList<ModelMatchPhasesElims>();
		for (int i=0;i<nbPhases; i++) { // i : Numero de phase
			int nbParticipantsPhase = (int)Math.pow(2, nbPhases-i); // Nombre de participants dans la phase
			for (int j=0; j<nbParticipantsPhase; j+=2) { // j : Curseur sur le premier participant de chaque match
				// Matchs précédants
				matchPrecedantA = null;
				matchPrecedantB = null;
				if (i != 0) {
					int index = matchs.size()+(j/2)-nbParticipantsPhase;
					matchPrecedantA = (ModelMatchPhasesElims)matchs.get(index+0);
					matchPrecedantB = (ModelMatchPhasesElims)matchs.get(index+1);
				}
				
				// Créer le match
				match = new ModelMatchPhasesElims(phasesEliminatoires, matchPrecedantA, matchPrecedantB, infosMatchs);
				
				// Participations
				if (i == 0) {
					// Récupérer les participants A et B
					ModelParticipant participantA = participants.get(numeros.get(j+0));
					ModelParticipant participantB = participants.get(numeros.get(j+1));
					
					// Créer les participations A et B
					ModelParticipation participationA = new ModelParticipation(participantA, match, InfosModelParticipation.defaut());
					ModelParticipation participationB = new ModelParticipation(participantB, match, InfosModelParticipation.defaut());
					
					// Ajouter les participations A et B au match
					match.setParticipationA(participationA);
					match.setParticipationB(participationB);
					
					// Ajouter les participations A et B aux participants A et B
					participantA.addParticipation(participationA);
					participantB.addParticipation(participationB);
				}
				
				// Match suivant
				if (i != 0) {
					matchPrecedantA.setMatchSuivant(match);
					matchPrecedantB.setMatchSuivant(match);
				}
				
				// Ajouter le match
				matchs.add(match);
			}
		}
		
		// Définir la finale
		phasesEliminatoires.setGrandeFinale(match);
		
		// Créer et définir la petite finale
		phasesEliminatoires.setPetiteFinale(new ModelMatchPhasesElims(phasesEliminatoires, matchPrecedantA, matchPrecedantB, infosMatchs));
		
		// Retourner la phase éliminatoire
		return phasesEliminatoires;
	}
	
	/**
	 * Vérifier si des participants sont ex-aequo à la qualification aux phases éliminatoires
	 * @param categorie catégorie
	 * @param nbPhases nombre de phases éliminatoires à générer
	 * @return participants ex-aequo à la qualification au phases éliminatoires
	 */
	@SuppressWarnings("unchecked")
	protected static Map<ModelPoule,List<ModelParticipant>> verifierExAequo(ModelCategorie categorie, int nbPhases) {
		// Initialiser la liste des participants ex-aeqo à la qualification aux phases éliminatoires
		Map<ModelPoule,List<ModelParticipant>> participantsExAequo = new HashMap<ModelPoule,List<ModelParticipant>>();

		// Récupérer la liste des poules
		List<ModelPoule> poules = categorie.getPoules();
		
		// Calculer le nombre de participants qualifiés aux phases éliminatoires pour chaque poule
		int nbPoules = poules.size();
		List<Integer> nbParticipantsPoules = new ArrayList<Integer>();
		for(int i=0;i<nbPoules;i++) {
			nbParticipantsPoules.add(0);
		}
		int nbParticipants = (int)Math.pow(2, nbPhases);
		while(nbParticipants != 0) {
			for(int i=0;i<nbPoules && nbParticipants!=0;i++) {
				if(poules.get(i).getParticipants().size() > nbParticipantsPoules.get(i)) {
					nbParticipantsPoules.set(i,nbParticipantsPoules.get(i)+1);
					nbParticipants--;
				}
			}
		}
		
		// Vérifier si des participants sont ex-aequo à la qualification aux phases éliminatoires
		for(int i=0;i<nbPoules;i++) {
			// Récupérer la liste des participants de la poule
			List<ModelParticipant> participants = poules.get(i).getParticipantsParticipants();
			
			// Trier d'après leur rang aux phases qualificatives
			Collections.sort(participants, categorie.getConcours().getComparateurPhasesQualificatives());
			
			// Inverser la liste pour avoir les meilleurs participants en premier
			Collections.reverse(participants);
			
			// Récupérer le nombre de participants
			int nbParticipantsPoule = participants.size();
			
			// Récupérer le nombre de participants qualifiés aux phases éliminatoires
			int nbParticipantsQualifiesPoule = nbParticipantsPoules.get(i);

			// Vérifier si le dernier participant qualifié aux phases éliminatoires n'est pas ex-aequo avec celui qui suit
			if(nbParticipantsQualifiesPoule != 0 && nbParticipantsPoule > nbParticipantsQualifiesPoule) {
				if(participants.get(nbParticipantsQualifiesPoule-1).getRangPhasesQualifs() == participants.get(nbParticipantsQualifiesPoule).getRangPhasesQualifs()) {
					// Construire la liste des participants ex-aequo à la qualification aux phases éliminatoires
					List<ModelParticipant> participantsExAequoPoule = new ArrayList<ModelParticipant>();
					for(int j=nbParticipantsQualifiesPoule-1;j>0 && participants.get(j-1).getRangPhasesQualifs() == participants.get(j).getRangPhasesQualifs();j--) {
						participantsExAequoPoule.add(participants.get(j-1));
					}
					participantsExAequoPoule.add(participants.get(nbParticipantsQualifiesPoule-1));
					for(int j=nbParticipantsQualifiesPoule;j<nbParticipantsPoule && participants.get(j-1).getRangPhasesQualifs() == participants.get(j).getRangPhasesQualifs();j++) {
						participantsExAequoPoule.add(participants.get(j));
					}
					participantsExAequo.put(poules.get(i),participantsExAequoPoule);
				}
			}
		}
		
		// Retourner la liste des participants ex-aeqo à la qualification aux phases éliminatoires
		return participantsExAequo;
	}
	
	/**
	 * Cloner les phases éliminatoires
	 * @param categorie catégorie
	 * @return phases éliminatoires
	 */
	protected ModelPhasesEliminatoires clone (ModelCategorie categorie) {
		return new ModelPhasesEliminatoires(categorie, this);
	}
	
	/**
	 * @see ModelAbstract#getInfos()
	 */
	public InfosModelPhasesEliminatoires getInfos () {
		InfosModelPhasesEliminatoires infos = new InfosModelPhasesEliminatoires();
		infos.setId(this.getId());
		return infos;
	}
	
	/**
	 * @see ModelAbstract#delete(ArrayList)
	 */
	protected void delete (ArrayList<ModelAbstract> removers) throws ContestOrgErrorException {
		if (!removers.contains(this)) {
			// Ajouter les phases éliminatoires aux removers
			removers.add(this);
			
			// Retirer les phases éliminatoires de la catégorie
			if (this.categorie != null) {
				if (!removers.contains(this.categorie)) {
					this.categorie.setPhasesEliminatoires(null);
				}
				this.categorie = null;
				this.fireClear(ModelCategorie.class);
			}
			
			// Supprimer la finale
			if (this.grandeFinale != null) {
				if (!removers.contains(this.grandeFinale)) {
					this.grandeFinale.delete(removers);
				}
				this.grandeFinale = null;
				this.fireClear(ModelMatchPhasesElims.class, FINALE);
			}
			
			// Supprimer la petite finale
			if (this.petiteFinale != null) {
				if (!removers.contains(this.petiteFinale)) {
					this.petiteFinale.delete(removers);
				}
				this.petiteFinale = null;
				this.fireClear(ModelMatchPhasesElims.class, PETITE_FINALE);
			}
			
			// Fire delete
			this.fireDelete();
		}
	}
	
}
