package comparators;

import infos.Configuration;
import infos.Couple;

import java.util.ArrayList;
import java.util.Comparator;

import models.ModelEquipe;


/**
 * Comparateur qui permet de retourner :
 * - le meilleur couple en fonction des affinités des équipes qui les composent
 * - la meilleure configuration de couples en fonction des affinités des équipes des couples qui les composent
 * Attention : à des fins d'optimisation, aucune donnée ne doit changée tout le long de son utilisation
 */
@SuppressWarnings("rawtypes")
public class CompGenerationPhasesQualifs implements Comparator
{

	// Comparateur intermediaire
	private CompPhasesQualifs comparateur;

	// Couples comparés
	private ArrayList<Couple<ModelEquipe>> couplesA = new ArrayList<Couple<ModelEquipe>>();
	private ArrayList<Couple<ModelEquipe>> couplesB = new ArrayList<Couple<ModelEquipe>>();
	private ArrayList<Integer> results = new ArrayList<Integer>();

	// Constructeur
	public CompGenerationPhasesQualifs(CompPhasesQualifs comparateur) {
		this.comparateur = comparateur;
	}

	// Implémentations de compare
	@SuppressWarnings("unchecked")
	public int compare (Object objectA, Object objectB) {
		if (objectA == null || objectB == null) {
			return this.compareNullObjects(objectA, objectB);
		} else if (objectA instanceof Couple && objectB instanceof Couple) {
			return this.compare((Couple)objectA, (Couple)objectB);
		} else if (objectA instanceof Configuration && objectB instanceof Configuration) {
			return this.compare((Configuration)objectA, (Configuration)objectB);
		}
		return 0;
	}
	public int compare (Couple<ModelEquipe> coupleA, Couple<ModelEquipe> coupleB) {
		// Vérifier de si le couple n'a pas déjà été comparé
		int nbResults = this.results.size();
		for (int i = 0; i < nbResults; i++) {
			if (coupleA.equals(this.couplesA.get(i)) && coupleB.equals(this.couplesB.get(i))) {
				return this.results.get(i);
			}
			if (coupleB.equals(this.couplesA.get(i)) && coupleA.equals(this.couplesB.get(i))) {
				return -this.results.get(i);
			}
		}

		// Initialiser le résultat
		int result;

		// Vérifier si l'un des deux couples n'est pas null
		if (coupleA == null || coupleB == null) {
			result = this.compareNullObjects(coupleA, coupleB);
		} else {
			// Comparer par rapport au nombre de rencontres des équipes des deux couples
			int nbRencontresA = coupleA.getEquipeA() != null ? coupleA.getEquipeA().getNbRencontres(coupleA.getEquipeB()) : coupleA.getEquipeB().getNbRencontres(coupleA.getEquipeA());
			int nbRencontresB = coupleB.getEquipeA() != null ? coupleB.getEquipeA().getNbRencontres(coupleB.getEquipeB()) : coupleB.getEquipeB().getNbRencontres(coupleB.getEquipeA());
			if (nbRencontresA != nbRencontresB) {
				result = nbRencontresA < nbRencontresB ? 1 : -1;
			} else {
				// Comparer à l'aide du comparateur donné
				result = this.comparateur.compare(coupleA, coupleB);
				if (result == 0) {
					// Comparer par rapport aux villes des équipes des deux couples
					String coupleAvilleA = coupleA.getEquipeA() == null ? null : coupleA.getEquipeA().getVille();
					String coupleAvilleB = coupleA.getEquipeB() == null ? null : coupleA.getEquipeB().getVille();
					String coupleBvilleA = coupleB.getEquipeA() == null ? null : coupleB.getEquipeA().getVille();
					String coupleBvilleB = coupleB.getEquipeB() == null ? null : coupleB.getEquipeB().getVille();
					boolean sameA = coupleAvilleA == null || coupleAvilleB == null || !coupleAvilleA.equals(coupleAvilleB) ? false : true;
					boolean sameB = coupleBvilleA == null || coupleBvilleB == null || !coupleBvilleA.equals(coupleBvilleB) ? false : true;
					if (sameA == sameB) {
						result = 0;
					} else {
						result = !sameA ? 1 : -1;
					}
				}
			}
		}

		// Conserver le résultat pour ne pas avoir à le recalculer
		this.couplesA.add(coupleA);
		this.couplesB.add(coupleB);
		this.results.add(result);

		// Retourner le resultat
		return result;
	}
	public int compare (Configuration<ModelEquipe> configurationA, Configuration<ModelEquipe> configurationB) {
		// Vérifier si l'une des deux configurations n'est pas null
		if (configurationA == null || configurationB == null) {
			this.compareNullObjects(configurationA, configurationB);
		}

		// Calculer la différence entre les deux configurations
		Couple<ModelEquipe>[] couplesA = configurationA.getCouples();
		Couple<ModelEquipe>[] couplesB = configurationB.getCouples();

		int nbMatchsDejaJouesA = 0;
		int nbVillesCommunesA = 0;
		int differenceSommeNiveauA = 0;
		int differenceMaxNiveauA = 0;
		int nbMatchsNiveauA = 0;
		
		for(Couple<ModelEquipe> couple : couplesA) {
			nbMatchsDejaJouesA += couple.getEquipeA() != null ? couple.getEquipeA().getNbRencontres(couple.getEquipeB()) : couple.getEquipeB().getNbRencontres(couple.getEquipeA());
			if(couple.getEquipeA() != null && couple.getEquipeB() != null) {
				differenceSommeNiveauA += Math.abs(couple.getEquipeA().getRangPhasesQualifs()-couple.getEquipeB().getRangPhasesQualifs());
				differenceMaxNiveauA = Math.max(differenceMaxNiveauA,Math.abs(couple.getEquipeA().getRangPhasesQualifs()-couple.getEquipeB().getRangPhasesQualifs()));
				nbMatchsNiveauA++;
				if(couple.getEquipeA().getVille() != null && couple.getEquipeB().getVille() != null && couple.getEquipeA().getVille().equals(couple.getEquipeB().getVille())) {
					nbVillesCommunesA++;
				}
			}
		}
		
		int differenceMoyenneNiveauA = differenceSommeNiveauA/nbMatchsNiveauA;

		int nbMatchsDejaJouesB = 0;
		int differenceSommeNiveauB = 0;
		int differenceMaxNiveauB = 0;
		int nbMatchsNiveauB = 0;
		int nbVillesCommunesB = 0;
		
		for(Couple<ModelEquipe> couple : couplesB) {
			nbMatchsDejaJouesB += couple.getEquipeA() != null ? couple.getEquipeA().getNbRencontres(couple.getEquipeB()) : couple.getEquipeB().getNbRencontres(couple.getEquipeA());
			if(couple.getEquipeA() != null && couple.getEquipeB() != null) {
				differenceSommeNiveauB += Math.abs(couple.getEquipeA().getRangPhasesQualifs()-couple.getEquipeB().getRangPhasesQualifs());
				differenceMaxNiveauB = Math.max(differenceMaxNiveauA,Math.abs(couple.getEquipeA().getRangPhasesQualifs()-couple.getEquipeB().getRangPhasesQualifs()));
				nbMatchsNiveauB++;
				if(couple.getEquipeA().getVille() != null && couple.getEquipeB().getVille() != null && couple.getEquipeA().getVille().equals(couple.getEquipeB().getVille())) {
					nbVillesCommunesB++;
				}
			}
		}
		
		int differenceMoyenneNiveauB = differenceSommeNiveauB/nbMatchsNiveauB;
		
		// Retourner le résultat de la comparaison
		if(nbMatchsDejaJouesA > nbMatchsDejaJouesB) {
			return -1;
		} else if(nbMatchsDejaJouesA < nbMatchsDejaJouesB) {
			return 1;
		}
		if(differenceMaxNiveauA > differenceMaxNiveauB) {
			return -1;
		} else if(differenceMaxNiveauA < differenceMaxNiveauB) {
			return 1;
		}
		if(differenceMoyenneNiveauA > differenceMoyenneNiveauB) {
			return -1;
		} else if(differenceMoyenneNiveauA < differenceMoyenneNiveauB) {
			return 1;
		}
		if(nbVillesCommunesA > nbVillesCommunesB) {
			return -1;
		} else if(nbVillesCommunesA < nbVillesCommunesB) {
			return 1;
		}
		return 0;
	}

	// Comparaison null
	private int compareNullObjects (Object objectA, Object objectB) {
		if (objectA == null && objectB == null) {
			return 0;
		}
		return objectB == null ? 1 : -1;
	}

}
