package org.contestorg.interfaces;

/**
 * Interface à implémenter si une classe souhaite etre considéré comme une classe d'instances changeables
 */
public interface IChangeable<T>
{
	// Ajouter/Retirer un listener
	public void addListener(IChangeableListener<T> listener);
	public void removeListener(IChangeableListener<T> listener);
}
