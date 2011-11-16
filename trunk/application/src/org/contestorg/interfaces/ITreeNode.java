package org.contestorg.interfaces;

import javax.swing.tree.TreeNode;

public interface ITreeNode extends TreeNode
{
	// Récupérer l'objet associée à la node
	public Object getObject();
	
	// Récupérer le niveau de la node
	public int getLevel();
	
	// Récupérer le parent
	public ITreeNode getParent();
	
	// Récupérer le chemin jusqu'à la node parent
	public TreeNode[] getPath();
	
}
