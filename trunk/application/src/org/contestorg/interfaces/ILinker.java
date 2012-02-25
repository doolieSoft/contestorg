package org.contestorg.interfaces;

/**
 * Interface à implémenter si une classe souhaite gérer les liens entre des objets d'une classe avec un objet d'une autre
 * @param <L> classe à des objets à lier
 */
public interface ILinker<L>
{	
	/**
	 * Lier un objet
	 * @param objet objet à lier
	 */
	public void link(L objet);
	
	/**
	 * Délier un objet
	 * @param objet objet à délier
	 */
	public void unlink(L objet);
}
