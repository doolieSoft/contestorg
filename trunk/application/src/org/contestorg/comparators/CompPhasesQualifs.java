package org.contestorg.comparators;


import java.util.Comparator;

import org.contestorg.infos.Couple;
import org.contestorg.models.ModelParticipant;



/**
 * Comparateur qui permet de retourner :
 * - le meilleur participant en fonction de leur rang aux phases de qualifications
 * - le couple qui a une différence minimal de niveaux entre ses deux participants
 */
@SuppressWarnings("rawtypes")
abstract public class CompPhasesQualifs implements Comparator
{

	/** Comparateur supplémentaire */
	private CompPhasesQualifs comparateurSupp;
	
	/** Numéro de phase qualificative à ne pas dépasser (-1 pour ignorer le numéro de phase) */
	protected int phaseQualifMax;

	/**
	 * Constructeur
	 * @param comparateurSupp comparateur supplémentaire
	 * @param phaseQualifMax numéro de phase qualificative à ne pas dépasser (-1 pour ignorer le numéro de phase)
	 */
	public CompPhasesQualifs(CompPhasesQualifs comparateurSupp, int phaseQualifMax) {
		this.comparateurSupp = comparateurSupp;
		this.phaseQualifMax = phaseQualifMax;
	}

	/**
	 * Fabriquer une comparateur
	 * @param comparateurs liste des comparateurs
	 * @return comparateur fabriqué
	 */
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
		} else if (objectA instanceof ModelParticipant && objectB instanceof ModelParticipant) {
			return this.compare((ModelParticipant)objectA, (ModelParticipant)objectB);
		} else if (objectA instanceof Couple && objectB instanceof Couple) {
			return this.compare((Couple)objectA, (Couple)objectB);
		}
		return 0;
	}
	public final int compare (ModelParticipant participantA, ModelParticipant participantB) {
		// Vérifier si l'un des deux participants n'est pas null
		if (participantA == null || participantB == null) {
			return this.compareNullObjects(participantA, participantB);
		}

		// Vérifier si les deux participants appartiennent à la même poule
		if (!participantA.getPoule().equals(participantB.getPoule())) {
			// Comparer les participants selon leur rang
			int rangA = participantA.getRangPhasesQualifs();
			int rangB = participantB.getRangPhasesQualifs();
			if (rangA != rangB) {
				return rangA > rangB ? -1 : 1;
			}
		}

		// Comparer les deux participants
		double valueA = this.getValue(participantA);
		double valueB = this.getValue(participantB);
		int result = this.getSens() * Double.compare(valueA, valueB);

		// Utiliser le comparateur de niveau inférieur si le résultat est nul
		return this.comparateurSupp != null && result == 0 ? this.comparateurSupp.compare(participantA, participantB) : result;
	}
	public final int compare (Couple<ModelParticipant> coupleA, Couple<ModelParticipant> coupleB) {
		// Vérifier si l'un des deux couples n'est pas null
		if (coupleA == null || coupleB == null) {
			return this.compareNullObjects(coupleA, coupleB);
		}

		// Comparer l'écart entre les deux couples
		double differenceA = this.getDifference(coupleA.getParticipantA(), coupleA.getParticipantB());
		double differenceB = this.getDifference(coupleB.getParticipantA(), coupleB.getParticipantB());
		int result = Double.compare(differenceB, differenceA);

		// Utiliser le comparateur de niveau inférieur si le résultat est nul
		return this.comparateurSupp != null && result == 0 ? this.comparateurSupp.compare(coupleA, coupleB) : result;
	}

	/**
	 * Méthode que doivent implémenter les classes filles pour indiquer la valeur comparée
	 * @param participant participant dont on souhaite récupérer la valeur à comparer
	 * @return valeur du participant à comparer
	 */
	protected abstract double getValue (ModelParticipant participant);

	/**
	 * Méthode que doivent implémenter les classes filles pour indiquer le sens de comparaison
	 * @return sens de comparaison (1 pour croissant, -1 pour décroissant)
	 */
	protected abstract int getSens ();

	/**
	 * Comparaison dans le cas ou l'un des deux objets est null
	 * @param objectA objet A
	 * @param objectB objet B
	 * @return résultat
	 */
	private int compareNullObjects (Object objectA, Object objectB) {
		if (objectA == null && objectB == null) {
			return 0;
		}
		return objectB == null ? 1 : -1;
	}

	/**
	 * Calculer la différence entre la valeur de deux participants
	 * @param participantA participant A
	 * @param participantB participant B
	 * @return différence entre la valeur des deux participants
	 */
	private double getDifference (ModelParticipant participantA, ModelParticipant participantB) {
		return Math.abs((participantA == null ? 0 : this.getValue(participantA)) - (participantB == null ? 0 : this.getValue(participantB)));
	}

}
