package org.contestorg.common;

/**
 * Triplet
 * @param <L> classe du premier objet du triplet
 * @param <M> classe du deuxième objet du triplet
 * @param <N> classe du troisième objet du triplet
 */
public class Triple<L,M,N>
{
	/** Première partie du triplet */
	private L first;
	
	/** Deuxième partie du triplet */
	private M second;
	
	/** Troisième partie du triplet */
	private N third;

	/**
	 * Constructeur
	 * @param first première partie du triplet
	 * @param second deuxième partie du triplet
	 * @param third troisième partie du triplet
	 */
	public Triple(L first, M second, N third) {
		this.first = first;
		this.second = second;
		this.third = third;
	}
	
	// Getters
	
	/**
	 * Récupérer la première partie du triplet
	 * @return première partie du triplet
	 */
	public L getFirst () {
		return this.first;
	}
	
	/**
	 * Récupérer la deuxième partie du triplet
	 * @return deuxième partie du triplet
	 */
	public M getSecond () {
		return this.second;
	}
	
	/**
	 * Récupérer la troisième partie du triplet
	 * @return troisième partie du triplet
	 */
	public N getThird () {
		return this.third;
	}
}
