package org.contestorg.interfaces;

/**
 * Interface à implémenter si une classe souhaite etre tenue au courant des opérations d'une liste
 * @param <T> classe des instances contenus par la liste
 */
public interface IListListener<T>
{
	// Recoit une notification d'ajout
	public void addEvent(int row, T added);
	
	// Recoit une notification de modification
	public void updateEvent(int row, T before, T after);
	
	// Recoit une notification de suppression
	public void removeEvent(int row, T deleted);
}
