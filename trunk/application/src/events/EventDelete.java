package events;

public class EventDelete extends Event
{

	// Attributs
	private Object objet;

	// Constructeur
	public EventDelete(Object objet) {
		this.objet = objet;
	}

	// Getters
	public Object getObject () {
		return this.objet;
	}
}
