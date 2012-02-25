package org.contestorg.events;

/**
 * Evénement de déplacement
 */
public class EventMove extends Event
{

	/** Objet propriétaire */
	private Object object;
	
	/** Objet déplacé */
	private Object associate;
	
	/** Indice avant le déplacement */
	private int before;
	
	/** Indice après le déplacement */
	private int after;
	
	/**
	 * Constructeur
	 * @param object objet propriétaire
	 * @param associate objet déplacé
	 * @param before indice avant le déplacement
	 * @param after indice après le déplacement
	 */
	public EventMove(Object object, Object associate, int before, int after) {
		this.object = object;
		this.associate = associate;
		this.before = before;
		this.after = after;
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
	 * Récupérer l'objet déplacé
	 * @return objet déplacé
	 */
	public Object getAssociate () {
		return this.associate;
	}
	
	/**
	 * Indice avant le déplacement
	 * @return indice avant le déplacement
	 */
	public int getBefore () {
		return this.before;
	}
	
	/**
	 * Indice après le déplacement
	 * @return indice après le déplacement
	 */
	public int getAfter () {
		return this.after;
	}

}
