package org.contestorg.interfaces;

/**
 * Interface à implémenter si une classe souhaite être tenue au courant des opérations d'une liste suivable
 * @param <T> classe des objets contenus par la liste suivable
 */
public interface ITrackableListListener<T>
{
	/**
	 * Recevoir une notification d'ajout
	 * @param row indice d'ajout
	 * @param added objet ajouté
	 */
	public void addEvent(int row, T added);
	
	/**
	 * Recevoir une notification de modification
	 * @param row indice de l'objet modifié
	 * @param before objet avant la modification
	 * @param after objet après la modification
	 */
	public void updateEvent(int row, T before, T after);
	
	/**
	 * Recevoir une notification de suppression
	 * @param row indice de suppression
	 * @param deleted objet supprimé
	 */
	public void removeEvent(int row, T deleted);
}
