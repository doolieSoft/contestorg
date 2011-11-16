package org.contestorg.events;

public class EventUpdate extends Event
{

	// Attributs
	private Object object;

	// Constructeur
	public EventUpdate(Object object) {
		this.object = object;
	}

	// Getters
	public Object getObject () {
		return this.object;
	}
}
