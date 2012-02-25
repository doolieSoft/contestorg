package org.contestorg.interfaces;

import org.contestorg.events.Event;

/**
 * Interface à implémenter si une classe souhaite être tenue au courant d'événements
 */
public interface IEventListener
{

	/**
	 * Recevoir un évenement
	 * @param event évenement
	 */
	public void event (Event event);

}
