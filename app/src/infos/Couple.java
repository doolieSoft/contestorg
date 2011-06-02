package infos;

import interfaces.ITransformer;


/**
 * Cette classe permet d'associer deux équipes en vue de tester leurs affinitées pour une possible rencontre
 */
public class Couple<T>
{

	// Equipes
	private T equipeA;
	private T equipeB;

	// Constructeur
	public Couple(T equipeA, T equipeB) {
		this.equipeA = equipeA;
		this.equipeB = equipeB;
	}

	// Getters
	public T getEquipeA () {
		return this.equipeA;
	}
	public T getEquipeB () {
		return this.equipeB;
	}
	public boolean isCompatible (Couple<T> couple) {
		return (this.equipeA != null && !this.equipeA.equals(couple.equipeA) && !this.equipeA.equals(couple.equipeB) || this.equipeA == null && couple.equipeA != null && couple.equipeB != null) && (this.equipeB != null && !this.equipeB.equals(couple.equipeA) && !this.equipeB.equals(couple.equipeB) || this.equipeB == null && couple.equipeA != null && couple.equipeB != null);
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
	public static <T> Couple<T>[] genererCouples (T[] equipes) {
		// Initialiser les variables
		int nbEquipes = equipes.length, i, j, n = 0;
		Couple<T>[] couples = new Couple[nbEquipes * (nbEquipes - 1) / 2];

		// Mélanger les équipes
		T temp;
		int swap;
		for (i = 0; i < nbEquipes; i++) {
			swap = (int)(Math.random() * nbEquipes);
			temp = equipes[swap];
			equipes[swap] = equipes[i];
			equipes[i] = temp;
		}

		// Générer les couples
		for (i = 0; i < nbEquipes; i++) {
			for (j = i + 1; j < nbEquipes; j++) {
				couples[n++] = new Couple<T>(equipes[i], equipes[j]);
			}
		}

		// Retourner les couples
		return couples;
	}

	// Equals
	public boolean equals (Couple<T> couple) {
		return couple != null && (((this.equipeA != null && this.equipeA.equals(couple.equipeA) || this.equipeA == null && couple.equipeA == null) && (this.equipeB != null && this.equipeB.equals(couple.equipeB) || this.equipeB == null && couple.equipeB == null)) || ((this.equipeB != null && this.equipeB.equals(couple.equipeA) || this.equipeB == null && couple.equipeA == null) && (this.equipeA != null && this.equipeA.equals(couple.equipeB) || this.equipeA == null && couple.equipeB == null)));
	}

	// Transform
	public <L> Couple<L> transform(ITransformer<T,L> transformer) {
		return new Couple<L>(transformer.transform(this.equipeA), transformer.transform(this.equipeB));
	}
	
}
