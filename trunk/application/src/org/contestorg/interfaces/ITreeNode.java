package org.contestorg.interfaces;

import javax.swing.tree.TreeNode;

/**
 * Interface à implémenter si une classe souhaite être considérée comme une cellule de graphe pyramidal
 */
public interface ITreeNode extends TreeNode
{
	/**
	 * Récupérer l'objet associée à la cellule
	 * @return objet associée à la cellule
	 */
	public Object getObject();
	
	/**
	 * Récupérer le niveau de la cellule
	 * @return niveau de la cellule
	 */
	public int getLevel();
	
	/**
	 * Récupérer la cellule parent
	 * @return cellule parent
	 */
	public ITreeNode getParent();
	
	/**
	 * Récupérer le chemin jusqu'à la cellule parent
	 * @return chemin jusqu'à la cellule parent
	 */
	public ITreeNode[] getPath();
	
}
