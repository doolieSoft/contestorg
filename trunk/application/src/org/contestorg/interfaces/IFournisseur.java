package org.contestorg.interfaces;

/**
 * Interface à implémenter si une classe souhaite être considérée comme un fournisseur d'objet
 * @param <T> classe de l'objet fournit
 */
public interface IFournisseur<T>
{
	/**
	 * Récupérer un objet
	 * @return objet
	 */
	public T get();
}
