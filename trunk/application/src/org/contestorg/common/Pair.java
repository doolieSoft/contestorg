package org.contestorg.common;

/**
 * Paire
 * @param <L> classe du premier objet de la paire
 * @param <M> classe du deuxième objet de la paire
 */
public class Pair<L,M>
{
	/** Première partie de la paire */
	private L first;
	
	/** Deuxième partie de la paire */
	private M second;
	
	/**
	 * Constructeur
	 * @param first première partie de la paire
	 * @param second deuxième partie de la paire
	 */
	public Pair(L first, M second) {
		this.first = first;
		this.second = second;
	}
	
	// Getters
	
	/**
	 * Récupérer la première partie de la paire 
	 * @return première partie de la paire
	 */
	public L getFirst() {
		return this.first;
	}
	
	/**
	 * Récupérer la deuxième partie de la paire
	 * @return deuxième partie de la paire
	 */
	public M getSecond() {
		return this.second;
	}
}
