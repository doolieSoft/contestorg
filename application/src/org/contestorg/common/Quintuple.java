package org.contestorg.common;

/**
 * Quintuplet
 * @param <L> classe du premier objet du quintuplet
 * @param <M> classe du deuxième objet du quintuplet
 * @param <N> classe du troisième objet du quintuplet
 * @param <O> classe du quatrième objet du quintuplet
 * @param <P> classe du cinquième objet du quintuplet
 */
public class Quintuple<L,M,N,O,P>
{
	/** Permière partie du quintuplet */
	private L first;
	
	/** Deuxième partie du quintuplet */
	private M second;
	
	/** Troisième partie du quintuplet */
	private N third;
	
	/** Quatrième partie du quintuplet */
	private O fourth;
	
	/** Cinquième partie du quintuplet */
	private P fifth;
	
	/**
	 * Constructeur
	 * @param first première partie du quintuplet
	 * @param second deuxième partie du quintuplet
	 * @param third troisième partie du quintuplet
	 * @param fourth quatrième partie du quintuplet
	 * @param fifth cinquième partie du quintuplet
	 */
	public Quintuple(L first, M second, N third, O fourth, P fifth) {
		this.first = first;
		this.second = second;
		this.third = third;
		this.fourth = fourth;
		this.fifth = fifth;
	}

	// Getters
	
	/**
	 * Récupérer la première partie du quintuplet
	 * @return première partie du quintuplet
	 */
	public L getFirst () {
		return this.first;
	}
	
	/**
	 * Récupérer la deuxième partie du quintuplet
	 * @return deuxième partie du quintuplet
	 */
	public M getSecond () {
		return this.second;
	}
	
	/**
	 * Récupérer la troisième partie du quintuplet
	 * @return troisième partie du quintuplet
	 */
	public N getThird () {
		return this.third;
	}
	
	/**
	 * Récupérer la quatrième partie du quintuplet
	 * @return quatrième partie du quintuplet
	 */
	public O getFourth () {
		return this.fourth;
	}
	
	/**
	 * Récupérer la cinquième partie du quintuplet
	 * @return cinquième partie du quintuplet
	 */
	public P getFifth () {
		return this.fifth;
	}
	
}
