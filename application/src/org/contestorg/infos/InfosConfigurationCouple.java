package org.contestorg.infos;

import org.contestorg.interfaces.ITransformer;


/**
 * Couple
 * @param <T> classe des objets du couple
 */
public class InfosConfigurationCouple<T>
{

	/** Participant A */
	private T participantA;
	
	/** Participant B */
	private T participantB;

	/**
	 * Constructeur
	 * @param participantA participant A
	 * @param participantB participant B
	 */
	public InfosConfigurationCouple(T participantA, T participantB) {
		this.participantA = participantA;
		this.participantB = participantB;
	}
	
	/**
	 * Récupérer le participant A
	 * @return participant A
	 */
	public T getParticipantA () {
		return this.participantA;
	}
	
	/**
	 * Récupérer le participant B
	 * @return participant B
	 */
	public T getParticipantB () {
		return this.participantB;
	}
	
	/**
	 * Vérifier la compatibilité avec un autre couple
	 * @param couple couple
	 * @return compatibilité avec le couple ?
	 */
	public boolean isCompatible (InfosConfigurationCouple<T> couple) {
		return (this.participantA != null && !this.participantA.equals(couple.participantA) && !this.participantA.equals(couple.participantB) || this.participantA == null && couple.participantA != null && couple.participantB != null) && (this.participantB != null && !this.participantB.equals(couple.participantA) && !this.participantB.equals(couple.participantB) || this.participantB == null && couple.participantA != null && couple.participantB != null);
	}
	
	/**
	 * Vérifier la compatibilité avec les couples d'une configuration
	 * @param configuration configuration
	 * @return compatibilité avec la configuration ?
	 */
	public boolean isCompatible (InfosConfiguration<T> configuration) {
		InfosConfigurationCouple<T>[] couples = configuration.getCouples();
		int size = configuration.getSize();
		for (int i = 0; i < size; i++) {
			if (!this.isCompatible(couples[i])) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Générer une liste de couples
	 * @param participants liste des participants
	 * @return liste des couples
	 */
	@SuppressWarnings("unchecked")
	public static <T> InfosConfigurationCouple<T>[] genererCouples (T[] participants) {
		// Initialiser les variables
		int nbParticipants = participants.length, i, j, n = 0;
		InfosConfigurationCouple<T>[] couples = new InfosConfigurationCouple[nbParticipants * (nbParticipants - 1) / 2];

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
				couples[n++] = new InfosConfigurationCouple<T>(participants[i], participants[j]);
			}
		}

		// Retourner les couples
		return couples;
	}

	/**
	 * Vérifier si le couple est égal à un autre couple
	 * @param couple couple
	 * @return couples égaux ?
	 */
	public boolean equals (InfosConfigurationCouple<T> couple) {
		return couple != null && (((this.participantA != null && this.participantA.equals(couple.participantA) || this.participantA == null && couple.participantA == null) && (this.participantB != null && this.participantB.equals(couple.participantB) || this.participantB == null && couple.participantB == null)) || ((this.participantB != null && this.participantB.equals(couple.participantA) || this.participantB == null && couple.participantA == null) && (this.participantA != null && this.participantA.equals(couple.participantB) || this.participantA == null && couple.participantB == null)));
	}

	/**
	 * Appliquer une transformation au couple
	 * @param transformer transformation
	 * @return couple transformé
	 */
	public <L> InfosConfigurationCouple<L> transform(ITransformer<T,L> transformer) {
		return new InfosConfigurationCouple<L>(transformer.transform(this.participantA), transformer.transform(this.participantB));
	}
	
}
