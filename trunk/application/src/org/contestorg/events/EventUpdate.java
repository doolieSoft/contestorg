package org.contestorg.events;

/**
 * Evénement de modification
 */
public class EventUpdate extends Event
{

	/** Objet modifié */
	private Object object;

	/**
	 * Constructeur
	 * @param object objet modifié
	 */
	public EventUpdate(Object object) {
		this.object = object;
	}

	// Getters
	
	/**
	 * Récupérer l'objet modifié
	 * @return objet modifié
	 */
	public Object getObject () {
		return this.object;
	}
}
