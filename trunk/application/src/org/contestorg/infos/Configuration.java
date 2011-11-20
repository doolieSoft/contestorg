package org.contestorg.infos;

import org.contestorg.interfaces.ITransformer;


/**
 * Cette classe permet de regrouper des couples compatibles de participants en vue d'une possible phase qualificative
 */
public class Configuration<T>
{

	// Liste de couples
	private Couple<T>[] couples;
	private int size;

	// Constructeur
	@SuppressWarnings("unchecked")
	public Configuration(int capacity) {
		this(new Couple[capacity], 0);
	}
	public Configuration(Couple<T>[] couples) {
		this(couples, couples.length);
	}
	@SuppressWarnings("unchecked")
	public Configuration(T[] participants) {
		Couple<T>[] couples = new Couple[participants.length / 2];
		for (int i = 0; i < participants.length; i = i + 2) {
			couples[i / 2] = new Couple<T>(participants[i], participants[i + 1]);
		}
		this.couples = couples;
		this.size = couples.length;
	}
	public Configuration(Couple<T>[] couples, int size) {
		this.couples = couples;
		this.size = size;
	}

	// Getters
	public Couple<T>[] getCouples () {
		return this.couples;
	}
	public int getSize () {
		return this.size;
	}
	public boolean isComplet () {
		return this.size == this.couples.length;
	}

	// Adders
	public void addCouple (Couple<T> couple) {
		this.couples[this.size++] = couple;
	}

	// ToString
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

	// Transform
	@SuppressWarnings("unchecked")
	public <L> Configuration<L> transform(ITransformer<T,L> transformer) {
		Couple<L>[] couples = new Couple[this.couples.length];
		for(int i=0;i<this.size;i++) {
			couples[i] = this.couples[i].transform(transformer);
		}
		return new Configuration<L>(couples,this.size);
	}
	
}
