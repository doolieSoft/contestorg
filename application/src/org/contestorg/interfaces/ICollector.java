package org.contestorg.interfaces;

/**
 * Interface à implémenter si une classe souhaite être considérée comme un collecteur d'objets
 * @param <T> classe des objets à collecter
 */
public interface ICollector<T>
{
	/**
	 * Collecter un objet
	 * @param objet objet
	 */
	public void collect (T objet);

	/**
	 * Annuler la collecte
	 */
	public void cancel ();
}
