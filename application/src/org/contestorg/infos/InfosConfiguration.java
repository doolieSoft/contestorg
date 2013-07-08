package org.contestorg.infos;

import org.contestorg.interfaces.ITransformer;


/**
 * Ensemble de couples
 * @param <T> classe des objets des couples
 */
public class InfosConfiguration<T>
{

	/** Liste de couples */
	private InfosConfigurationCouple<T>[] couples;
	
	/** Nombre de couples */
	private int size;

	/**
	 * Constructeur
	 * @param capacity nombre de couples maximal
	 */
	@SuppressWarnings("unchecked")
	public InfosConfiguration(int capacity) {
		this(new InfosConfigurationCouple[capacity], 0);
	}
	
	/**
	 * Constructeur
	 * @param couples liste de couples d'initialisation
	 */
	public InfosConfiguration(InfosConfigurationCouple<T>[] couples) {
		this(couples, couples.length);
	}
	
	/**
	 * Constructeur
	 * @param participants liste de participants d'initialisation
	 */
	@SuppressWarnings("unchecked")
	public InfosConfiguration(T[] participants) {
		InfosConfigurationCouple<T>[] couples = new InfosConfigurationCouple[participants.length / 2];
		for (int i = 0; i < participants.length; i = i + 2) {
			couples[i / 2] = new InfosConfigurationCouple<T>(participants[i], participants[i + 1]);
		}
		this.couples = couples;
		this.size = couples.length;
	}
	
	/**
	 * Constructeur
	 * @param couples liste de couples d'initialisation
	 * @param capacity nombre de couples maximal
	 */
	public InfosConfiguration(InfosConfigurationCouple<T>[] couples, int capacity) {
		this.couples = couples;
		this.size = capacity;
	}
	
	/**
	 * Récupérer la liste des couples
	 * @return liste des couples
	 */
	public InfosConfigurationCouple<T>[] getCouples () {
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
	public void addCouple (InfosConfigurationCouple<T> couple) {
		this.couples[this.size++] = couple;
	}

	/**
	 * ToString
	 */
	public String toString () {
		// Retourner le string représentative de la configuration
		StringBuilder string = new StringBuilder();
		for (InfosConfigurationCouple<T> couple : this.couples) {
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
	public <L> InfosConfiguration<L> transform(ITransformer<T,L> transformer) {
		InfosConfigurationCouple<L>[] couples = new InfosConfigurationCouple[this.couples.length];
		for(int i=0;i<this.size;i++) {
			couples[i] = this.couples[i].transform(transformer);
		}
		return new InfosConfiguration<L>(couples,this.size);
	}
	
}
