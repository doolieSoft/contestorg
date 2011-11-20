package org.contestorg.models;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.contestorg.comparators.CompPhasesElims;
import org.contestorg.infos.InfosModelMatchPhasesElims;
import org.contestorg.infos.InfosModelParticipation;
import org.contestorg.infos.InfosModelPhaseEliminatoires;


public class ModelPhasesEliminatoires extends ModelAbstract
{
	
	// Roles
	public static final int FINALE = 1;
	public static final int PETITE_FINALE = 2;
	
	// Attributs objets
	private ModelCategorie categorie;
	private ModelMatchPhasesElims grandeFinale;
	private ModelMatchPhasesElims petiteFinale;
	
	// Constructeur
	public ModelPhasesEliminatoires(ModelCategorie categorie, InfosModelPhaseEliminatoires infos) {
		// Retenir la categorie
		this.categorie = categorie;
		
		// Enregistrer les informations
		this.setInfos(infos);
	}
	protected ModelPhasesEliminatoires(ModelCategorie categorie, ModelPhasesEliminatoires phasesEliminatoires) {
		// Appeller le constructeur principal
		this(categorie, phasesEliminatoires.toInfos());
		
		// Récupérer l'id
		this.setId(phasesEliminatoires.getId());
	}
	
	// Getters
	public ModelCategorie getCategorie () {
		return this.categorie;
	}
	public ModelMatchPhasesElims getGrandeFinale () {
		return this.grandeFinale;
	}
	public ModelMatchPhasesElims getPetiteFinale () {
		return this.petiteFinale;
	}
	public ArrayList<ModelParticipant> getClassement () {
		if (this.grandeFinale != null) {
			// Récupérer, trier et retourner la liste des participants
			ArrayList<ModelParticipant> participants = this.grandeFinale.getParticipants();
			Collections.sort(participants, new CompPhasesElims());
			return participants;
		}
		return new ArrayList<ModelParticipant>();
	}
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
	public int getNbMatchs() {
		// Calculer et retourner le nombre de matchs pour la première phase
		return new Double(Math.pow(2, this.getNbPhases()-1)).intValue();
	}
	public int getNbParticipants() {
		// Calculer et retourner le nombre de participants qui ont participer
		return new Double(Math.pow(2, this.getNbPhases())).intValue();
	}
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
	protected void setInfos (InfosModelPhaseEliminatoires infos) {
		// Appeller le setInfos du parent
		super.setInfos(infos);
		
		// Fire update
		this.fireUpdate();
	}
	public void setGrandeFinale (ModelMatchPhasesElims grandeFinale) {
		// Retenir la finale
		this.grandeFinale = grandeFinale;
		
		// Fire update
		this.fireUpdate();
	}
	public void setPetiteFinale (ModelMatchPhasesElims petiteFinale) {
		// Retenir la finale
		this.petiteFinale = petiteFinale;
		
		// Fire update
		this.fireUpdate();
	}
	
	// Actions
	@SuppressWarnings("unchecked")
	protected static ModelPhasesEliminatoires genererPhasesEliminatoires (ModelCategorie categorie, int nbPhases, InfosModelMatchPhasesElims infosMatchs, InfosModelPhaseEliminatoires infosPhaseEliminatoire) throws ContestOrgModelException {
		// Trier les participants d'après leur score aux phases qualificatives
		List<ModelParticipant> participants = categorie.getParticipantsParticipants();
		Collections.sort(participants, categorie.getConcours().getComparateurPhasesQualificatives());
		Collections.reverse(participants);
		
		// Garder les participant qui vont participer aux phases finales
		int nbParticipants = (int)Math.pow(2, nbPhases);
		participants = participants.subList(0, nbParticipants);
		
		// FIXME Eviter les rencontres entre participants venant de même poule
		
		// Tirer les couples de participants de la première phase
		/*
		 * Explication de l'algorythme :
		 * - on ajoute le premiere participant
		 * - a chaque tour on ajoute des participant :
		 *    - de maniere a doubler le nombre de participants
		 *    - les plus "mauvais" participants se placent sous les "meilleurs"
		 */
		ArrayList<Integer> numeros = new ArrayList<Integer>();
		numeros.add(0);
		for (int i = 2; i < nbParticipants * 2; i = i * 2) { // i : Nombre de participants restants dans la phase
			for (int j = 0; j < i; j = j + 2) { // j : Curseur sur les bons participants
				numeros.add(j + 1, i - numeros.get(j) - 1); // Ajouter le participant de rang inférieur sous le participant de rang supérieur
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
			for (int j=0; j<nbParticipantsPhase; j+=2) { // j : Curseur sur le premièr participant de chaque match
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
	
	// Clone
	protected ModelPhasesEliminatoires clone (ModelCategorie categorie) {
		return new ModelPhasesEliminatoires(categorie, this);
	}
	
	// ToInformation
	public InfosModelPhaseEliminatoires toInfos () {
		InfosModelPhaseEliminatoires infos = new InfosModelPhaseEliminatoires();
		infos.setId(this.getId());
		return infos;
	}
	
	// Remove
	protected void delete (ArrayList<ModelAbstract> removers) throws ContestOrgModelException {
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
