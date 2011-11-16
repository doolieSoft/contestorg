package org.contestorg.comparators;


import java.util.Comparator;

import org.contestorg.infos.Couple;
import org.contestorg.models.ModelEquipe;



/**
 * Comparateur qui permet de retourner :
 * - la meilleure équipe en fonction de leur rang aux phases de qualifications
 * - le couple qui a une différence minimal de niveaux entre ses deux équipes
 */
@SuppressWarnings("rawtypes")
abstract public class CompPhasesQualifs implements Comparator
{

	// Comparateur supplémentaire
	private CompPhasesQualifs comparateurSupp;
	
	// Numéro de phase qualificative à ne pas dépasser (-1 pour ignorer le numéro de phase)
	protected int phaseQualifMax;

	// Constructeur
	public CompPhasesQualifs(CompPhasesQualifs comparateurSupp, int phaseQualifMax) {
		this.comparateurSupp = comparateurSupp;
		this.phaseQualifMax = phaseQualifMax;
	}

	// Fabrique
	public static CompPhasesQualifs fabrique (CompPhasesQualifs... comparateurs) {
		for (int i = 1; i < comparateurs.length; i++) {
			comparateurs[i - 1].comparateurSupp = comparateurs[i];
		}
		return comparateurs[0];
	}

	// Implémentations de compare
	@SuppressWarnings("unchecked")
	public final int compare (Object objectA, Object objectB) {
		if (objectA == null || objectB == null) {
			return this.compareNullObjects(objectA, objectB);
		} else if (objectA instanceof ModelEquipe && objectB instanceof ModelEquipe) {
			return this.compare((ModelEquipe)objectA, (ModelEquipe)objectB);
		} else if (objectA instanceof Couple && objectB instanceof Couple) {
			return this.compare((Couple)objectA, (Couple)objectB);
		}
		return 0;
	}
	public final int compare (ModelEquipe equipeA, ModelEquipe equipeB) {
		// Vérifier si l'une des deux équipes n'est pas null
		if (equipeA == null || equipeB == null) {
			return this.compareNullObjects(equipeA, equipeB);
		}

		// Vérifier si les deux équipes appartiennent à la même poule
		if (!equipeA.getPoule().equals(equipeB.getPoule())) {
			// Comparer les équipes selon leur rang
			int rangA = equipeA.getRangPhasesQualifs();
			int rangB = equipeB.getRangPhasesQualifs();
			if (rangA != rangB) {
				return rangA > rangB ? -1 : 1;
			}
		}

		// Comparer les deux équipes
		double valueA = this.getValue(equipeA);
		double valueB = this.getValue(equipeB);
		int result = this.getSens() * Double.compare(valueA, valueB);

		// Utiliser le comparateur de niveau inférieur si le résultat est nul
		return this.comparateurSupp != null && result == 0 ? this.comparateurSupp.compare(equipeA, equipeB) : result;
	}
	public final int compare (Couple<ModelEquipe> coupleA, Couple<ModelEquipe> coupleB) {
		// Vérifier si l'un des deux couples n'est pas null
		if (coupleA == null || coupleB == null) {
			return this.compareNullObjects(coupleA, coupleB);
		}

		// Comparer l'écart entre les deux couples
		double differenceA = this.getDifference(coupleA.getEquipeA(), coupleA.getEquipeB());
		double differenceB = this.getDifference(coupleB.getEquipeA(), coupleB.getEquipeB());
		int result = Double.compare(differenceB, differenceA);

		// Utiliser le comparateur de niveau inférieur si le résultat est nul
		return this.comparateurSupp != null && result == 0 ? this.comparateurSupp.compare(coupleA, coupleB) : result;
	}

	// Méthode que doivent implémenter les classes filles pour indiquer la valeur comparée
	protected abstract double getValue (ModelEquipe equipe);

	// Méthode que doivent implémenter les classes filles pour indiquer le sens (1 pour croissant, -1 pour décroissant)
	protected abstract int getSens ();

	// Comparaison null
	private int compareNullObjects (Object objectA, Object objectB) {
		if (objectA == null && objectB == null) {
			return 0;
		}
		return objectB == null ? 1 : -1;
	}

	// Différence entre deux équipes
	private double getDifference (ModelEquipe equipeA, ModelEquipe equipeB) {
		return Math.abs((equipeA == null ? 0 : this.getValue(equipeA)) - (equipeB == null ? 0 : this.getValue(equipeB)));
	}

}
