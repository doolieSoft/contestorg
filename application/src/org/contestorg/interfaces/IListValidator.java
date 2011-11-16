package org.contestorg.interfaces;

import org.contestorg.common.TrackableList;

/**
 * Interface à implémenter si une classe souhaite un valideur des opérations effectuées sur une list
 * @param <T> classe des instances contenus par la liste
 */
public interface IListValidator<T>
{
	// Valider un ajout
	public String validateAdd(T infos,TrackableList<T> list);
	
	// Valider une modification
	public String validateUpdate(int row, T infos, TrackableList<T> list);
	
	// Valider une suppression
	public String validateDelete(int row, TrackableList<T> list);
	
	// Valider un déplacement
	public String validateMove(int row, int movement, TrackableList<T> list);
	
}
