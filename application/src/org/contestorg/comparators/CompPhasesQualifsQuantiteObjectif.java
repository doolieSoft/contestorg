package org.contestorg.comparators;

import org.contestorg.models.ModelObjectif;
import org.contestorg.models.ModelParticipant;

/**
 * Comparateur qui prend en compte la quantité remportée d'un objectif aux phases qualificatives
 */
public class CompPhasesQualifsQuantiteObjectif extends CompPhasesQualifs
{

	/** Objectif */
	private ModelObjectif objectif;

	/**
	 * Constructeur
	 * @param sens sens
	 * @param phaseQualifMax numéro de phase qualificative à ne pas dépasser (-1 pour ignorer le numéro de phase)
	 * @param objectif objectif qui permet de comparer deux participants aux phases qualificatives
	 */
	public CompPhasesQualifsQuantiteObjectif(int sens,int phaseQualifMax,ModelObjectif objectif) {
		this(null, sens, phaseQualifMax, objectif);
	}
	
	/**
	 * Constructeur
	 * @param comparateurSupp comparateur supplémentaire
	 * @param sens sens
	 * @param phaseQualifMax numéro de phase qualificative à ne pas dépasser (-1 pour ignorer le numéro de phase)
	 * @param objectif objectif qui permet de comparer deux participants aux phases qualificatives
	 */
	public CompPhasesQualifsQuantiteObjectif(CompPhasesQualifs comparateurSupp, int sens, int phaseQualifMax, ModelObjectif objectif) {
		super(comparateurSupp, sens, phaseQualifMax);
		this.objectif = objectif;
	}

	/**
	 * @see CompPhasesQualifs#getValue(ModelParticipant, ModelParticipant, int)
	 */
	@Override
	protected double getValue (ModelParticipant participant, ModelParticipant adversaire, int phaseQualifMax) {
		return participant == null ? 0 : participant.getQuantiteObjectifRemportee(this.objectif,false,true,phaseQualifMax);
	}

	/**
	 * @see CompPhasesQualifs#getMaxParticipants()
	 */
	@Override
	protected int getMaxParticipants () {
		return -1;
	}

}
