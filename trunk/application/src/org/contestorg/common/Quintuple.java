package org.contestorg.common;

public class Quintuple<L,M,N,O,P>
{
	// Attributs
	private L first;
	private M second;
	private N third;
	private O fourth;
	private P fifth;
	
	// Constructeur
	public Quintuple(L first, M second, N third, O fourth, P fifth) {
		this.first = first;
		this.second = second;
		this.third = third;
		this.fourth = fourth;
		this.fifth = fifth;
	}

	// Getters
	public L getFirst () {
		return this.first;
	}
	public M getSecond () {
		return this.second;
	}
	public N getThird () {
		return this.third;
	}
	public O getFourth () {
		return this.fourth;
	}
	public P getFifth () {
		return this.fifth;
	}
	
}
