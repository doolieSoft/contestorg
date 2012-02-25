package org.contestorg.comparators;

import org.contestorg.models.ModelObjectif;
import org.contestorg.models.ModelParticipant;

/**
 * Comparateur qui, à partir d'un objectif donné, retourne le participant qui en a eu le plus aux phases qualificatives
 */
public class CompPhasesQualifsNbObjectifs extends CompPhasesQualifs
{

	/** Objectif */
	private ModelObjectif objectif;

	/**
	 * Constructeur
	 * @param phaseQualifMax numéro de phase qualificative à ne pas dépasser (-1 pour ignorer le numéro de phase)
	 * @param objectif objectif qui permet de comparer deux participants aux phases qualificatives
	 */
	public CompPhasesQualifsNbObjectifs(int phaseQualifMax,ModelObjectif objectif) {
		this(null, phaseQualifMax, objectif);
	}
	
	/**
	 * Constructeur
	 * @param comparateurSupp comparateur supplémentaire
	 * @param phaseQualifMax numéro de phase qualificative à ne pas dépasser (-1 pour ignorer le numéro de phase)
	 * @param objectif objectif qui permet de comparer deux participants aux phases qualificatives
	 */
	public CompPhasesQualifsNbObjectifs(CompPhasesQualifs comparateurSupp, int phaseQualifMax, ModelObjectif objectif) {
		super(comparateurSupp, phaseQualifMax);
		this.objectif = objectif;
	}

	// Implémentation de getValue()
	@Override
	protected double getValue (ModelParticipant participant) {
		return participant == null ? 0 : participant.getQuantiteObjectif(this.objectif,this.phaseQualifMax);
	}

	// Implémentation de getSens()
	@Override
	protected int getSens () {
		return 1;
	}

}
