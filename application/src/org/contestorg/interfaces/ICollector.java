package org.contestorg.interfaces;

/**
 * Interface à implémenter si une classe souhaite etre considérée comme un collecteur de T
 * @param <T> classe des instances à collecter
 */
public interface ICollector<T>
{
	// Acceptation d'un T
	public void accept (T object);

	// Annulation
	public void cancel ();
}
