package out;

import interfaces.IEventListener;

import java.util.ArrayList;

import models.ModelConcours;
import events.Event;

public abstract class PersistanceAbstract implements IEventListener
{

	// Events
	protected ArrayList<Event> events = new ArrayList<Event>();

	// Implémentation de event
	@Override
	public void event (Event event) {
		this.events.add(event);
	}

	// Charger un concours
	public abstract ModelConcours load ();

	// Enregistrer un concours
	public abstract boolean save ();

}
