package org.contestorg.comparators;

import org.contestorg.models.ModelParticipant;
import org.contestorg.models.ModelObjectif;

/**
 * Comparateur qui, à partir d'un objectif donné, retourne le participant qui en a eu le plus aux phases qualificatives
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
	protected double getValue (ModelParticipant participant) {
		return participant == null ? 0 : participant.getQuantiteObjectif(objectif,this.phaseQualifMax);
	}

	// Implémentation de sens
	@Override
	protected int getSens () {
		return 1;
	}

}
