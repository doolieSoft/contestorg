package org.contestorg.comparators;

import org.contestorg.models.ModelParticipant;

/**
 * Comparateur retournant le participant qui a le plus de victoires dans les phases qualificatives
 */
public class CompPhasesQualifsVictoires extends CompPhasesQualifs
{

	// Constructeurs
	public CompPhasesQualifsVictoires(int phaseQualifMax) {
		super(null,phaseQualifMax);
	}
	public CompPhasesQualifsVictoires(CompPhasesQualifs comparateurSupp, int phaseQualifMax) {
		super(comparateurSupp,phaseQualifMax);
	}

	// Implémentation de getValue
	@Override
	protected double getValue (ModelParticipant participant) {
		return participant == null ? 0 : (double)participant.getNbVictoires(this.phaseQualifMax);
	}

	// Implémentation de sens
	@Override
	protected int getSens () {
		return 1;
	}

}
