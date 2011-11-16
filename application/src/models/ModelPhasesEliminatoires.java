package models;

import infos.InfosModelMatchPhasesElims;
import infos.InfosModelParticipation;
import infos.InfosModelPhaseEliminatoires;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import comparators.CompPhasesElims;

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
		this(categorie, phasesEliminatoires.toInformation());
		
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
	public ArrayList<ModelEquipe> getClassement () {
		if (this.grandeFinale != null) {
			// Récupérer, trier et retourner la liste des équipes
			ArrayList<ModelEquipe> equipes = this.grandeFinale.getEquipes();
			Collections.sort(equipes, new CompPhasesElims());
			return equipes;
		}
		return new ArrayList<ModelEquipe>();
	}
	public int getRang (ModelEquipe equipe) {
		// Est ce que l'équipe a participé à la petite finale ?
		if (this.petiteFinale != null && this.petiteFinale.getParticipationA() != null && this.petiteFinale.getParticipationB() != null) {
			if (this.petiteFinale.getParticipationA().getEquipe().equals(equipe)) {
				return this.petiteFinale.getParticipationA().getResultat() == InfosModelParticipation.RESULTAT_VICTOIRE ? 3 : 4;
			}
			if (this.petiteFinale.getParticipationB().getEquipe().equals(equipe)) {
				return this.petiteFinale.getParticipationB().getResultat() == InfosModelParticipation.RESULTAT_VICTOIRE ? 3 : 4;
			}
		}
		
		// Rechercher le rang de l'équipe dans les phases éliminatoires
		if (this.grandeFinale != null) {
			return this.grandeFinale.getRang(equipe);
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
	public int getNbEquipes() {
		// Calculer et retourner le nombre d'équipes participantes
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
		// Trier les équipes d'après leur score aux phases qualificatives
		List<ModelEquipe> equipes = categorie.getEquipesParticipantes();
		Collections.sort(equipes, categorie.getConcours().getComparateurPhasesQualificatives());
		Collections.reverse(equipes);
		
		// Garder les équipes participantes aux phases finales
		int nbEquipes = (int)Math.pow(2, nbPhases);
		equipes = equipes.subList(0, nbEquipes);
		
		// FIXME Eviter les rencontres entre équipes venant de même poule
		
		// Tirer les couples d'équipes de la première phase
		/*
		 * Explication de l'algorythme :
		 * - on ajoute la premiere equipe
		 * - a chaque tour on ajoute des equipes :
		 * - de maniere a doubler le nombre d'equipes
		 * - les plus mauvaises equipes se placent sous les meilleures
		 */
		ArrayList<Integer> numeros = new ArrayList<Integer>();
		numeros.add(0);
		for (int i = 2; i < nbEquipes * 2; i = i * 2) { // i : Nombre d'équipes restantes dans la phase
			for (int j = 0; j < i; j = j + 2) { // j : Curseur sur les bonnes équipes
				numeros.add(j + 1, i - numeros.get(j) - 1); // Ajouter l'équipe de niveau inférieur sous l'équipe de niveau supérieur
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
			int nbEquipesPhase = (int)Math.pow(2, nbPhases-i); // Nombre d'équipes dans la phase
			for (int j=0; j<nbEquipesPhase; j+=2) { // j : Curseur sur la première équipe de chaque match
				// Matchs précédants
				matchPrecedantA = null;
				matchPrecedantB = null;
				if (i != 0) {
					int index = matchs.size()+(j/2)-nbEquipesPhase;
					matchPrecedantA = (ModelMatchPhasesElims)matchs.get(index+0);
					matchPrecedantB = (ModelMatchPhasesElims)matchs.get(index+1);
				}
				
				// Créer le match
				match = new ModelMatchPhasesElims(phasesEliminatoires, matchPrecedantA, matchPrecedantB, infosMatchs);
				
				// Participations
				if (i == 0) {
					// Récupérer les équipes A et B
					ModelEquipe equipeA = equipes.get(numeros.get(j+0));
					ModelEquipe equipeB = equipes.get(numeros.get(j+1));
					
					// Créer les participations A et B
					ModelParticipation participationA = new ModelParticipation(equipeA, match, InfosModelParticipation.defaut());
					ModelParticipation participationB = new ModelParticipation(equipeB, match, InfosModelParticipation.defaut());
					
					// Ajouter les participations A et B au match
					match.setParticipationA(participationA);
					match.setParticipationB(participationB);
					
					// Ajouter les participations A et B aux équipes A et B
					equipeA.addParticipation(participationA);
					equipeB.addParticipation(participationB);
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
	public InfosModelPhaseEliminatoires toInformation () {
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
