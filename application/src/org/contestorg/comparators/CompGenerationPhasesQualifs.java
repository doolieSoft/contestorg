package org.contestorg.comparators;


import java.util.ArrayList;
import java.util.Comparator;

import org.contestorg.infos.InfosConfiguration;
import org.contestorg.infos.InfosConfigurationCouple;
import org.contestorg.models.ModelParticipant;



/**
 * Comparateur qui permet de retourner :
 * - le meilleur couple en fonction des affinités des participants qui les composent
 * - la meilleure configuration de couples en fonction des affinités des participants des couples qui les composent
 * Attention : à des fins d'optimisation, aucune donnée ne doit changer tout le long de son utilisation
 */
@SuppressWarnings("rawtypes")
public class CompGenerationPhasesQualifs implements Comparator
{

	/** Comparateur intermediaire */
	private CompPhasesQualifs comparateur;

	/** Couples A déjà comparés (pour optimisation) */
	private ArrayList<InfosConfigurationCouple<ModelParticipant>> couplesA = new ArrayList<InfosConfigurationCouple<ModelParticipant>>();
	
	/** Couples B déjà comparés (pour optimisation) */
	private ArrayList<InfosConfigurationCouple<ModelParticipant>> couplesB = new ArrayList<InfosConfigurationCouple<ModelParticipant>>();
	
	/** Résultats des comparaisons entre couples A et B (pour optimisation) */
	private ArrayList<Integer> results = new ArrayList<Integer>();

	/**
	 * Constructeur
	 * @param comparateur comparateur de couples pour les phases qualificatives
	 */
	public CompGenerationPhasesQualifs(CompPhasesQualifs comparateur) {
		this.comparateur = comparateur;
	}

	// Implémentations de compare
	@SuppressWarnings("unchecked")
	public int compare (Object objectA, Object objectB) {
		if (objectA == null || objectB == null) {
			return objectA == null && objectB == null ? 0 : (objectB == null ? 1 : -1);
		} else if (objectA instanceof InfosConfigurationCouple && objectB instanceof InfosConfigurationCouple) {
			return this.compare((InfosConfigurationCouple)objectA, (InfosConfigurationCouple)objectB);
		} else if (objectA instanceof InfosConfiguration && objectB instanceof InfosConfiguration) {
			return this.compare((InfosConfiguration)objectA, (InfosConfiguration)objectB);
		}
		return 0;
	}
	public int compare (InfosConfigurationCouple<ModelParticipant> coupleA, InfosConfigurationCouple<ModelParticipant> coupleB) {
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
			return coupleA == null && coupleB == null ? 0 : (coupleB == null ? 1 : -1);
		} else {
			// Comparer par rapport au nombre de rencontres des participants des deux couples
			int nbRencontresA = coupleA.getParticipantA() != null ? coupleA.getParticipantA().getNbRencontres(coupleA.getParticipantB(),false,true) : coupleA.getParticipantB().getNbRencontres(coupleA.getParticipantA(),false,true);
			int nbRencontresB = coupleB.getParticipantA() != null ? coupleB.getParticipantA().getNbRencontres(coupleB.getParticipantB(),false,true) : coupleB.getParticipantB().getNbRencontres(coupleB.getParticipantA(),false,true);
			if (nbRencontresA != nbRencontresB) {
				result = nbRencontresA < nbRencontresB ? 1 : -1;
			} else {
				// Comparer à l'aide du comparateur donné
				result = this.comparateur.compare(coupleA, coupleB);
				if (result == 0) {
					// Comparer par rapport aux villes des participants des deux couples
					String coupleAvilleA = coupleA.getParticipantA() == null ? null : coupleA.getParticipantA().getVille();
					String coupleAvilleB = coupleA.getParticipantB() == null ? null : coupleA.getParticipantB().getVille();
					String coupleBvilleA = coupleB.getParticipantA() == null ? null : coupleB.getParticipantA().getVille();
					String coupleBvilleB = coupleB.getParticipantB() == null ? null : coupleB.getParticipantB().getVille();
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
	public int compare (InfosConfiguration<ModelParticipant> configurationA, InfosConfiguration<ModelParticipant> configurationB) {
		// Vérifier si l'une des deux configurations n'est pas null
		if (configurationA == null || configurationB == null) {
			return configurationA == null && configurationB == null ? 0 : (configurationB == null ? 1 : -1);
		}

		// Calculer la différence entre les deux configurations
		InfosConfigurationCouple<ModelParticipant>[] couplesA = configurationA.getCouples();
		InfosConfigurationCouple<ModelParticipant>[] couplesB = configurationB.getCouples();

		int nbMatchsDejaJouesA = 0;
		int nbVillesCommunesA = 0;
		int differenceSommeNiveauA = 0;
		int differenceMaxNiveauA = 0;
		int nbMatchsNiveauA = 0;
		
		for(InfosConfigurationCouple<ModelParticipant> couple : couplesA) {
			nbMatchsDejaJouesA += couple.getParticipantA() != null ? couple.getParticipantA().getNbRencontres(couple.getParticipantB(),false,true) : couple.getParticipantB().getNbRencontres(couple.getParticipantA(),false,true);
			if(couple.getParticipantA() != null && couple.getParticipantB() != null) {
				differenceSommeNiveauA += Math.abs(couple.getParticipantA().getRangPhasesQualifs()-couple.getParticipantB().getRangPhasesQualifs());
				differenceMaxNiveauA = Math.max(differenceMaxNiveauA,Math.abs(couple.getParticipantA().getRangPhasesQualifs()-couple.getParticipantB().getRangPhasesQualifs()));
				nbMatchsNiveauA++;
				if(couple.getParticipantA().getVille() != null && couple.getParticipantB().getVille() != null && couple.getParticipantA().getVille().equals(couple.getParticipantB().getVille())) {
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
		
		for(InfosConfigurationCouple<ModelParticipant> couple : couplesB) {
			nbMatchsDejaJouesB += couple.getParticipantA() != null ? couple.getParticipantA().getNbRencontres(couple.getParticipantB(),false,true) : couple.getParticipantB().getNbRencontres(couple.getParticipantA(),false,true);
			if(couple.getParticipantA() != null && couple.getParticipantB() != null) {
				differenceSommeNiveauB += Math.abs(couple.getParticipantA().getRangPhasesQualifs()-couple.getParticipantB().getRangPhasesQualifs());
				differenceMaxNiveauB = Math.max(differenceMaxNiveauA,Math.abs(couple.getParticipantA().getRangPhasesQualifs()-couple.getParticipantB().getRangPhasesQualifs()));
				nbMatchsNiveauB++;
				if(couple.getParticipantA().getVille() != null && couple.getParticipantB().getVille() != null && couple.getParticipantA().getVille().equals(couple.getParticipantB().getVille())) {
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

}
