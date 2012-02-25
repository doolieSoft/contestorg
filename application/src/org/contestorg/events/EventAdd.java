package org.contestorg.events;

/**
 * Evénement d'ajout
 */
public class EventAdd extends Event
{

	/** Objet propriétaire  */
	private Object object;
	
	/** Objet ajouté */
	private Object associate;
	
	/** Indice d'ajout */
	private int index;
	
	/** Rôle de l'objet ajouté */
	private Integer role;

	/**
	 * Constructeur
	 * @param object objet propriétaire
	 * @param associate objet ajouté
	 * @param index indice d'ajout
	 */
	public EventAdd(Object object, Object associate, int index) {
		this(object, associate, index, null);
	}
	
	/**
	 * Constructeur
	 * @param object objet propriétaire
	 * @param associate objet ajouté
	 * @param index indice d'ajout
	 * @param role rôle de l'objet ajouté
	 */
	public EventAdd(Object object, Object associate, int index, Integer role) {
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
	 * Récupérer l'objet ajouté
	 * @return object ajouté
	 */
	public Object getAssociate () {
		return this.associate;
	}
	
	/**
	 * Récupérer l'indice d'ajout
	 * @return indice d'ajout 
	 */
	public int getIndex () {
		return this.index;
	}
	
	/**
	 * Récupérer le rôle de l'objet ajouté
	 * @return rôle de l'objet ajouté
	 */
	public Integer getRole () {
		return this.role;
	}
}
