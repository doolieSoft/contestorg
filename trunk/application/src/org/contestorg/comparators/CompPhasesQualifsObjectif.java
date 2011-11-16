package org.contestorg.comparators;

import org.contestorg.models.ModelEquipe;
import org.contestorg.models.ModelObjectif;

/**
 * Comparateur qui, à partir d'un objectif donné, retourne l'équipe qui en a eu le plus aux phases qualificatives
 */
public class CompPhasesQualifsObjectif extends CompPhasesQualifs
{

	// Objectif
	private ModelObjectif objectif;

	// Constructeurs
	public CompPhasesQualifsObjectif(int phaseQualifMax,ModelObjectif objectif) {
		this(null,phaseQualifMax, objectif);
	}
	public CompPhasesQualifsObjectif(CompPhasesQualifs comparateurSupp, int phaseQualifMax, ModelObjectif objectif) {
		super(comparateurSupp,phaseQualifMax);
		this.objectif = objectif;
	}

	// Implémentation de getValue
	@Override
	protected double getValue (ModelEquipe equipe) {
		return equipe == null ? 0 : equipe.getQuantiteObjectif(objectif,this.phaseQualifMax);
	}

	// Implémentation de sens
	@Override
	protected int getSens () {
		return 1;
	}

}
