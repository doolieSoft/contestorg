package org.contestorg.interfaces;

/**
 * Interface à implémenter si une classe souhaite être tenu au courant des opérations sur un modèle de données pour graphe pyramidal
 */
public interface IGraphModelListener
{
	/**
	 * Recevoir une notification de reconstruction du graphe pyramidal
	 */
	public void reloadGraphe();
	
}
