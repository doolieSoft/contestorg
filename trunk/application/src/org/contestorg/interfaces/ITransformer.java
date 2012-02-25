package org.contestorg.interfaces;

/**
 * Interface à implémenter si une classe souhaite être considérée comme un transformateur d'objet
 * @param <L> classe de l'objet source 
 * @param <M> classe de l'objet cible
 */
public interface ITransformer<L,M>
{
	/**
	 * Transformer l'objet de classe L en objet de classe M
	 * @param objet objet source
	 * @return objet cible
	 */
	public M transform(L objet);
}
