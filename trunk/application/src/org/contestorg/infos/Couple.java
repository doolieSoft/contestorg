package org.contestorg.infos;

import org.contestorg.interfaces.ITransformer;


/**
 * Cette classe permet d'associer deux participants en vue de tester leurs affinitées pour une possible rencontre
 */
public class Couple<T>
{

	// Participants
	private T participantA;
	private T participantB;

	// Constructeur
	public Couple(T participantA, T participantB) {
		this.participantA = participantA;
		this.participantB = participantB;
	}

	// Getters
	public T getParticipantA () {
		return this.participantA;
	}
	public T getParticipantB () {
		return this.participantB;
	}
	public boolean isCompatible (Couple<T> couple) {
		return (this.participantA != null && !this.participantA.equals(couple.participantA) && !this.participantA.equals(couple.participantB) || this.participantA == null && couple.participantA != null && couple.participantB != null) && (this.participantB != null && !this.participantB.equals(couple.participantA) && !this.participantB.equals(couple.participantB) || this.participantB == null && couple.participantA != null && couple.participantB != null);
	}
	public boolean isCompatible (Configuration<T> configuration) {
		Couple<T>[] couples = configuration.getCouples();
		int size = configuration.getSize();
		for (int i = 0; i < size; i++) {
			if (!this.isCompatible(couples[i])) {
				return false;
			}
		}
		return true;
	}

	// Generer des couples
	@SuppressWarnings("unchecked")
	public static <T> Couple<T>[] genererCouples (T[] participants) {
		// Initialiser les variables
		int nbParticipants = participants.length, i, j, n = 0;
		Couple<T>[] couples = new Couple[nbParticipants * (nbParticipants - 1) / 2];

		// Mélanger les participants
		T temp;
		int swap;
		for (i = 0; i < nbParticipants; i++) {
			swap = (int)(Math.random() * nbParticipants);
			temp = participants[swap];
			participants[swap] = participants[i];
			participants[i] = temp;
		}

		// Générer les couples
		for (i = 0; i < nbParticipants; i++) {
			for (j = i + 1; j < nbParticipants; j++) {
				couples[n++] = new Couple<T>(participants[i], participants[j]);
			}
		}

		// Retourner les couples
		return couples;
	}

	// Equals
	public boolean equals (Couple<T> couple) {
		return couple != null && (((this.participantA != null && this.participantA.equals(couple.participantA) || this.participantA == null && couple.participantA == null) && (this.participantB != null && this.participantB.equals(couple.participantB) || this.participantB == null && couple.participantB == null)) || ((this.participantB != null && this.participantB.equals(couple.participantA) || this.participantB == null && couple.participantA == null) && (this.participantA != null && this.participantA.equals(couple.participantB) || this.participantA == null && couple.participantB == null)));
	}

	// Transform
	public <L> Couple<L> transform(ITransformer<T,L> transformer) {
		return new Couple<L>(transformer.transform(this.participantA), transformer.transform(this.participantB));
	}
	
}
