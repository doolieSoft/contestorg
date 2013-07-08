package org.contestorg.comparators;

import org.contestorg.models.ModelParticipant;

/**
 * Comparateur qui prend en compte le résultat des rencontres directes entre les participants
 */
public class CompPhasesQualifsRencontresDirectes extends CompPhasesQualifs
{

	/**
	 * Constructeur
	 * @param sens sens
	 * @param phaseQualifMax numéro de phase qualificative à ne pas dépasser (-1 pour ignorer le numéro de phase)
	 */
	public CompPhasesQualifsRencontresDirectes(int sens,int phaseQualifMax) {
		this(null, sens, phaseQualifMax);
	}
	
	/**
	 * Constructeur
	 * @param comparateurSupp comparateur supplémentaire
	 * @param sens sens
	 * @param phaseQualifMax numéro de phase qualificative à ne pas dépasser (-1 pour ignorer le numéro de phase)
	 */
	public CompPhasesQualifsRencontresDirectes(CompPhasesQualifs comparateurSupp, int sens, int phaseQualifMax) {
		super(comparateurSupp, sens, phaseQualifMax);
	}

	/**
	 * @see CompPhasesQualifs#getValue(ModelParticipant, ModelParticipant, int)
	 */
	@Override
	protected double getValue (ModelParticipant participant, ModelParticipant adversaire, int phaseQualifMax) {
		if(participant == null) {
			return 0;
		}
		return participant.getNbVictoiresContreAdversaire(adversaire,false,true,phaseQualifMax);
	}

	/**
	 * @see CompPhasesQualifs#getMaxParticipants()
	 */
	@Override
	protected int getMaxParticipants () {
		return 2;
	}

}
