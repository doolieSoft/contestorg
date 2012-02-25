package org.contestorg.infos;

import org.contestorg.interfaces.ITransformer;


/**
 * Ensemble de couples
 * @param <T> classe des objets des couples
 */
public class Configuration<T>
{

	/** Liste de couples */
	private Couple<T>[] couples;
	
	/** Nombre de couples */
	private int size;

	/**
	 * Constructeur
	 * @param capacity nombre de couples maximal
	 */
	@SuppressWarnings("unchecked")
	public Configuration(int capacity) {
		this(new Couple[capacity], 0);
	}
	
	/**
	 * Constructeur
	 * @param couples liste de couples d'initialisation
	 */
	public Configuration(Couple<T>[] couples) {
		this(couples, couples.length);
	}
	
	/**
	 * Constructeur
	 * @param participants liste de participants d'initialisation
	 */
	@SuppressWarnings("unchecked")
	public Configuration(T[] participants) {
		Couple<T>[] couples = new Couple[participants.length / 2];
		for (int i = 0; i < participants.length; i = i + 2) {
			couples[i / 2] = new Couple<T>(participants[i], participants[i + 1]);
		}
		this.couples = couples;
		this.size = couples.length;
	}
	
	/**
	 * Constructeur
	 * @param couples liste de couples d'initialisation
	 * @param capacity nombre de couples maximal
	 */
	public Configuration(Couple<T>[] couples, int capacity) {
		this.couples = couples;
		this.size = capacity;
	}
	
	/**
	 * Récupérer la liste des couples
	 * @return liste des couples
	 */
	public Couple<T>[] getCouples () {
		return this.couples;
	}
	
	/**
	 * Récupérer le nombre de couples
	 * @return nombre de couples
	 */
	public int getSize () {
		return this.size;
	}
	
	/**
	 * Vérifier si la configuration est complète
	 * @return vérifier si la configuration est complète
	 */
	public boolean isComplet () {
		return this.size == this.couples.length;
	}
	
	/**
	 * Ajouter un couple
	 * @param couple couple à ajouter
	 */
	public void addCouple (Couple<T> couple) {
		this.couples[this.size++] = couple;
	}

	/**
	 * ToString
	 */
	public String toString () {
		// Retourner le string représentative de la configuration
		StringBuilder string = new StringBuilder();
		for (Couple<T> couple : this.couples) {
			if (string.length() != 0) {
				string.append("\n");
			}
			string.append(couple.toString());
		}
		return string.toString();
	}

	/**
	 * Appliquer une transformation à la configuration
	 * @param transformer transformation
	 * @return configuration transformée
	 */
	@SuppressWarnings("unchecked")
	public <L> Configuration<L> transform(ITransformer<T,L> transformer) {
		Couple<L>[] couples = new Couple[this.couples.length];
		for(int i=0;i<this.size;i++) {
			couples[i] = this.couples[i].transform(transformer);
		}
		return new Configuration<L>(couples,this.size);
	}
	
}
