package interfaces;

import events.Event;

/**
 * Interface à implémenter si une classe souhaite etre tenue au courant d'événements
 */
public interface IEventListener
{

	// Recoit un évenement
	public void event (Event event);

}
