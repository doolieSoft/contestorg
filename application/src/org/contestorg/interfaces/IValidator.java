package org.contestorg.interfaces;

/**
 * Interface à implémenter si une classe souhaite devenir un valideur de T 
 * @param <T> classe d'objet que la classe pourra valider
 */
public interface IValidator<T>
{

	/**
	 * Méthode de validation que doivent posséder les classes implémentant cette interface
	 * @param object l'objet à valider
	 * @return true si l'objet est valide
	 */
	public boolean validate (T object);
}
