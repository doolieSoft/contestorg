package org.contestorg.interfaces;

/**
 * Interface à implémenter si une classe souhaite être tenu au courant des opérations sur un modèle de cellule de graphe pyramidal
 */
public interface ICelluleModelListener
{
	/**
	 * Recevoir une notification de modification de la cellule
	 */
	public void updateCellule();
}
