package org.contestorg.events;

/**
 * Evénement de suppression
 */
public class EventRemove extends Event
{

	/** Objet propriétaire */
	private Object object;
	
	/** Objet supprimé */
	private Object associate;
	
	/** Indice de l'objet supprimé */
	private int index;
	
	/** Rôle de l'objet supprimé */
	private Integer role;

	/**
	 * Constructeur
	 * @param object objet propriétaire
	 * @param associate objet supprimé
	 * @param index indice de l'objet supprimé
	 */
	public EventRemove(Object object, Object associate, int index) {
		this(object,associate,index,null);
	}
	
	/**
	 * Constructeur
	 * @param object objet propriétaire
	 * @param associate objet supprimé
	 * @param index indice de l'objet supprimé
	 * @param role rôle de l'objet supprimé
	 */
	public EventRemove(Object object, Object associate, int index, Integer role) {
		this.object = object;
		this.associate = associate;
		this.index = index;
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
	 * Récupérer l'objet supprimé
	 * @return objet supprimé
	 */
	public Object getAssociate () {
		return this.associate;
	}
	
	/**
	 * Récupérer l'indice de l'objet supprimé
	 * @return indice de l'objet supprimé
	 */
	public int getIndex() {
		return this.index;
	}
	
	/**
	 * Récupérer le rôle de l'objet supprimé
	 * @return rôle de l'objet supprimé
	 */
	public Integer getRole () {
		return this.role;
	}
}
