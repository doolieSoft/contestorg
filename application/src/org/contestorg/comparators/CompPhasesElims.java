package org.contestorg.comparators;

import java.util.Comparator;

import org.contestorg.models.ModelParticipant;


/**
 * Comparateur retournant le meilleur participant en fonction de leur rang aux phases éliminatoires
 */
public class CompPhasesElims implements Comparator<ModelParticipant>
{

	// Implémentation de compare
	public int compare (ModelParticipant participantA, ModelParticipant participantB) {
		int rangA = participantA.getRangPhasesElims();
		int rangB = participantB.getRangPhasesElims();
		if (rangA < 1 || rangB < 1 || rangA == rangB) {
			return 0;
		}
		return rangA > rangB ? 1 : -1;
	}

}
