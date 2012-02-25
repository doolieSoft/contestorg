package org.contestorg.out;

import java.util.ArrayList;

import org.contestorg.events.Event;
import org.contestorg.interfaces.IEventListener;
import org.contestorg.models.ModelConcours;

/**
 * Persistance abstraite
 */
public abstract class PersistanceAbstract implements IEventListener
{
	/** Evénements */
	protected ArrayList<Event> events = new ArrayList<Event>();

	/**
	 * IEventListener.event()
	 */
	@Override
	public void event (Event event) {
		this.events.add(event);
	}

	/**
	 * Charger un concours
	 * @return concours
	 */
	public abstract ModelConcours load ();

	/**
	 * Enregistrer un concours
	 * @return opération réussie ?
	 */
	public abstract boolean save ();
}
