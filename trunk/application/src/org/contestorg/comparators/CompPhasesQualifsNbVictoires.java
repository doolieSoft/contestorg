package org.contestorg.comparators;

import org.contestorg.models.ModelParticipant;

/**
 * Comparateur qui retourne le participant qui a eu le plus de victoires aux phases qualificatives
 */
public class CompPhasesQualifsNbVictoires extends CompPhasesQualifs
{

	/**
	 * Constructeur
	 * @param phaseQualifMax numéro de phase qualificative à ne pas dépasser (-1 pour ignorer le numéro de phase)
	 */
	public CompPhasesQualifsNbVictoires(int phaseQualifMax) {
		super(null,phaseQualifMax);
	}
	
	/**
	 * Constructeur
	 * @param comparateurSupp comparateur supplémentaire
	 * @param phaseQualifMax numéro de phase qualificative à ne pas dépasser (-1 pour ignorer le numéro de phase)
	 */
	public CompPhasesQualifsNbVictoires(CompPhasesQualifs comparateurSupp, int phaseQualifMax) {
		super(comparateurSupp,phaseQualifMax);
	}

	// Implémentation de getValue()
	@Override
	protected double getValue (ModelParticipant participant) {
		return participant == null ? 0 : (double)participant.getNbVictoires(this.phaseQualifMax);
	}

	// Implémentation de getSens()
	@Override
	protected int getSens () {
		return 1;
	}

}
