package org.contestorg.comparators;

import java.util.Comparator;

import org.contestorg.models.ModelParticipant;


/**
 * Comparateur pour trier une liste de participants alphabétiquement
 */
public class CompParticipantNom implements Comparator<ModelParticipant>
{

	// Implémentation de compare
	public int compare (ModelParticipant participantA, ModelParticipant participantB) {
		return participantA.getNom().compareTo(participantB.getNom());
	}

}
