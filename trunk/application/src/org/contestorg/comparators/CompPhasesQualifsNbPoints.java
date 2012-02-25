package org.contestorg.comparators;

import org.contestorg.models.ModelParticipant;

/**
 * Comparateur qui retourne le participant qui en a eu le plus de points aux phases qualificatives
 */
public class CompPhasesQualifsNbPoints extends CompPhasesQualifs
{

	/**
	 * Constructeur
	 * @param phaseQualifMax numéro de phase qualificative à ne pas dépasser (-1 pour ignorer le numéro de phase)
	 */
	public CompPhasesQualifsNbPoints(int phaseQualifMax) {
		this(null,phaseQualifMax);
	}
	
	/**
	 * Constructeur
	 * @param comparateurSupp comparateur supplémentaire
	 * @param phaseQualifMax numéro de phase qualificative à ne pas dépasser (-1 pour ignorer le numéro de phase)
	 */
	public CompPhasesQualifsNbPoints(CompPhasesQualifs comparateurSupp,int phaseQualifMax) {
		super(comparateurSupp,phaseQualifMax);
	}

	// Implémentation de getValue()
	@Override
	protected double getValue (ModelParticipant participant) {
		return participant == null ? 0 : participant.getPoints(this.phaseQualifMax);
	}

	// Implémentation de getSens()
	@Override
	protected int getSens () {
		return 1;
	}

}
