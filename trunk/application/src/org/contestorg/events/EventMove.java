package org.contestorg.events;

public class EventMove extends Event
{

	// Attributs
	private Object object;
	private Object associate;
	private int before;
	private int after;
	
	// Constructeur
	public EventMove(Object object, Object associate, int before, int after) {
		this.object = object;
		this.associate = associate;
		this.before = before;
		this.after = after;
	}

	// Getters
	public Object getObject () {
		return this.object;
	}
	public Object getAssociate () {
		return this.associate;
	}
	public int getBefore () {
		return this.before;
	}
	public int getAfter () {
		return this.after;
	}

}
