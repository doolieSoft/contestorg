package common;

public class Quadruple<L,M,N,O>
{
	// Attributs
	private L first;
	private M second;
	private N third;
	private O fourth;
	
	// Constructeur
	public Quadruple(L first, M second, N third, O fourth) {
		this.first = first;
		this.second = second;
		this.third = third;
		this.fourth = fourth;
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
}
