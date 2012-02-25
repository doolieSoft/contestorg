package org.contestorg.interfaces;

import org.contestorg.common.TrackableList;

/**
 * Interface à implémenter si une classe souhaite un valideur des opérations effectuées sur une liste suivable
 * @param <T> classe des objets contenus par la liste suivable
 */
public interface ITrackableListValidator<T>
{
	/**
	 * Valider un ajout
	 * @param objet objet à valider
	 * @param liste liste suivable
	 * @return message d'erreur (null si pas d'erreur)
	 */
	public String validateAdd(T objet,TrackableList<T> liste);
	
	/**
	 * Valider une modification
	 * @param row indice de l'objet à modifier
	 * @param objet nouvel objet
	 * @param liste liste suivable
	 * @return message d'erreur (null si pas d'erreur)
	 */
	public String validateUpdate(int row, T objet, TrackableList<T> liste);
	
	/**
	 * Valider une suppression
	 * @param row indice de suppression
	 * @param liste liste suivable
	 * @return message d'erreur (null si pas d'erreur)
	 */
	public String validateDelete(int row, TrackableList<T> liste);
	
	/**
	 * Valider un déplacement
	 * @param row indice de l'objet à déplacer
	 * @param movement deplacement
	 * @param liste liste suivable
	 * @return message d'erreur (null si pas d'erreur)
	 */
	public String validateMove(int row, int movement, TrackableList<T> liste);
	
}
