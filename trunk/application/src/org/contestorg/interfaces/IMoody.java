package org.contestorg.interfaces;

/**
 * Interface à implémenter si une classe souhaite être considérée comme une classe d'objets lunatiques
 */
public interface IMoody
{
	/**
	 * Savoir si l'objet lunatique possède un état donné
	 * @param etat état
	 * @return état possédé ?
	 */
	public boolean is(int etat);
	
	// Ajouter/Supprimer des listeners
	public void addListener(IMoodyListener listener);
	public void removeListener(IMoodyListener listener);
}
