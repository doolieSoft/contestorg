package org.contestorg.interfaces;

/**
 * Interface à implémenter si une classe souhaite devenir un valideur d'objets 
 * @param <T> classe des objets à valider
 */
public interface IValidator<T>
{

	/**
	 * Valider un objet
	 * @param objet objet à valider
	 * @return objet valide ?
	 */
	public boolean validate (T objet);
}
