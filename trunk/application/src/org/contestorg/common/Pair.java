package org.contestorg.common;

public class Pair<L,M>
{
	// Attributs
	private L first;
	private M second;
	
	// Constructeur
	public Pair(L first, M second) {
		this.first = first;
		this.second = second;
	}
	
	// Getters
	public L getFirst() {
		return this.first;
	}
	public M getSecond() {
		return this.second;
	}
}
