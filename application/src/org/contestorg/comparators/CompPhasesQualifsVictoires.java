package org.contestorg.comparators;

import org.contestorg.models.ModelEquipe;

/**
 * Comparateur retournant l'équipe qui a le plus de victoires dans les phases qualificatives
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
	protected double getValue (ModelEquipe equipe) {
		return equipe == null ? 0 : (double)equipe.getNbVictoires(this.phaseQualifMax);
	}

	// Implémentation de sens
	@Override
	protected int getSens () {
		return 1;
	}

}
