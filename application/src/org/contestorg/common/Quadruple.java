package org.contestorg.common;

/**
 * Quadruplet
 * @param <L> classe du premier objet du quadruplet
 * @param <M> classe du deuxième objet du quadruplet
 * @param <N> classe du troisième objet du quadruplet
 * @param <O> classe du quatrième objet du quadruplet
 */
public class Quadruple<L,M,N,O>
{
	/** Première partie du quadruplet */
	private L first;
	
	/** Deuxième partie du quadruplet */
	private M second;
	
	/** Troisième partie du quadruplet */
	private N third;
	
	/** Quatrième partie du quadruplet */
	private O fourth;
	
	/**
	 * Constructeur
	 * @param first première partie du quadruplet
	 * @param second deuxième partie du quadruplet
	 * @param third troisième partie du quadruplet
	 * @param fourth quatrième partie du quadruplet
	 */
	public Quadruple(L first, M second, N third, O fourth) {
		this.first = first;
		this.second = second;
		this.third = third;
		this.fourth = fourth;
	}

	// Getters
	
	/**
	 * Récupérer la première partie du quadruplet
	 * @return première partie du quadruplet
	 */
	public L getFirst () {
		return this.first;
	}
	
	/**
	 * Récupérer la deuxième partie du quadruplet
	 * @return deuxième partie du quadruplet
	 */
	public M getSecond () {
		return this.second;
	}
	
	/**
	 * Récupérer la troisième partie du quadruplet
	 * @return troisième partie du quadruplet
	 */
	public N getThird () {
		return this.third;
	}
	
	/**
	 * Récupérer la quatrième partie du quadruplet
	 * @return quatrième partie du quadruplet
	 */
	public O getFourth () {
		return this.fourth;
	}
}
