package org.contestorg.interfaces;

/**
 * Interface à implémenter si une classe souhaite être considéré comme une classe d'objets changeables
 */
public interface IChangeable<T>
{
	/**
	 * Ajouter un listener
	 * @param listener listener à ajouter
	 */
	public void addListener(IChangeableListener<T> listener);
	
	/**
	 * Retirer un listener
	 * @param listener listener à retirer
	 */
	public void removeListener(IChangeableListener<T> listener);
}
