package org.contestorg.events;

/**
 * Evénement de suppression
 */
public class EventDelete extends Event
{

	/** Objet supprimé */
	private Object objet;

	/**
	 * Constructeur
	 * @param objet objet supprimé
	 */
	public EventDelete(Object objet) {
		this.objet = objet;
	}

	// Getters
	
	/**
	 * Récupérer l'objet supprimé
	 * @return objet supprimé
	 */
	public Object getObject () {
		return this.objet;
	}
}
