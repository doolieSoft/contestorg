package org.contestorg.events;

/**
 * Evénement d'effacement
 */
public class EventClear extends Event
{
	
	/** Objet propriétaire */
	private Object object;
	
	/** Classe des objets supprimés */
	private Class<?> associateClass;
	
	/** Rôle des objets supprimés */
	private Integer role;

	/**
	 * Constructeur
	 * @param object object propriétaire
	 * @param associateClass classe des objets supprimés
	 */
	public EventClear(Object object, Class<?> associateClass) {
		this(object, associateClass, null);
	}
	
	/**
	 * Constructeur
	 * @param object object propriétaire
	 * @param associateClass classe des objets supprimés
	 * @param role rôle des objets supprimés
	 */
	public EventClear(Object object, Class<?> associateClass, Integer role) {
		this.object = object;
		this.associateClass = associateClass;
		this.role = role;
	}

	// Getters
	
	/**
	 * Récupérer l'objet propriétaire
	 * @return objet propriétaire
	 */
	public Object getObject () {
		return this.object;
	}
	
	/**
	 * Récupérer la classe des objets supprimés
	 * @return classe des objets supprimés
	 */
	public Class<?> getAssociateClass () {
		return this.associateClass;
	}
	
	/**
	 * Récupérer le rôle des objets supprimés
	 * @return rôle des objets supprimés
	 */
	public Integer getRole () {
		return this.role;
	}
}
