package common;

public class Triple<L,M,N>
{
	// Attributs
	private L first;
	private M second;
	private N third;

	// Constructeur
	public Triple(L first, M second, N third) {
		this.first = first;
		this.second = second;
		this.third = third;
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
}
