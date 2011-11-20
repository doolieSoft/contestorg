package org.contestorg.comparators;

import org.contestorg.models.ModelParticipant;

/**
 * Comparateur qui retourne le participant ayant eu le plus de points aux phases qualificatives
 */
public class CompPhasesQualifsPoints extends CompPhasesQualifs
{

	// Constructeurs
	public CompPhasesQualifsPoints(int phaseQualifMax) {
		this(null,phaseQualifMax);
	}
	public CompPhasesQualifsPoints(CompPhasesQualifs comparateurSupp,int phaseQualifMax) {
		super(comparateurSupp,phaseQualifMax);
	}

	// Implémentation de getValue
	@Override
	protected double getValue (ModelParticipant participant) {
		return participant == null ? 0 : participant.getPoints(this.phaseQualifMax);
	}

	// Implémentation de sens
	@Override
	protected int getSens () {
		return 1;
	}

}
