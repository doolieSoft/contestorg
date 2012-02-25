package org.contestorg.common;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;

import javax.swing.tree.TreeNode;

import org.contestorg.interfaces.ITreeNode;

/**
 * Classe d'aide à l'implémentation de l'interface ITreeNode
 * @param <T> classe des objets de la node
 */
public abstract class TreeNodeAbstract<T> implements ITreeNode
{	
	/** Parent */
	private TreeNodeAbstract<?> parent;
	
	/** Objet */
	protected T object;
	
	/** Enfants */
	protected ArrayList<TreeNodeAbstract<?>> children = new ArrayList<TreeNodeAbstract<?>>();
	
	/** Enfants supprimés */
	private ArrayList<Quadruple<TreeNode, TreeNode[], int[], TreeNode[]>> removed = new ArrayList<Quadruple<TreeNode, TreeNode[], int[], TreeNode[]>>();
	
	/** La node a-t-elle un parent ? */
	private boolean inserted = false;
	
	/** La node a-t-elle été modifiée ? */
	private boolean changed = false;
	
	/**
	 * Constructeur
	 * @param object objet associé à la node
	 */
	public TreeNodeAbstract(T object) {
		// Retenir l'objet associé à la node
		this.object = object;
	}
		
	// Implémentation de TreeNode
	@Override
	public Enumeration<?> children () {
		return Collections.enumeration(this.children);
	}
	@Override
	public ITreeNode getChildAt (int index) {
		return this.children.get(index);
	}
	@Override
	public int getChildCount () {
		return this.children.size();
	}
	@Override
	public int getIndex (TreeNode node) {
		return this.children.indexOf(node);
	}
	@Override
	public ITreeNode getParent () {
		return this.parent;
	}
	@Override
	public boolean isLeaf () {
		return this.children.size() == 0;
	}
	
	// Implémentation de ITreeNode
	@Override
	public Object getObject() {
		return this.object;
	}
	@Override	
	public int getLevel() {
		return this.parent == null ? 0 : this.parent.getLevel()+1;
	}
	@Override
	public ITreeNode[] getPath() {
		// Initialiser le chemin
		ITreeNode[] path = new ITreeNode[this.getLevel()+1];
		
		// Remplir le chemin
		int i = 0;
		ITreeNode parent = this;
		while(parent != null) {
			path[i++] = parent;
			parent = parent.getParent();
		}
		
		// Retourner le chemin
		return path;
	}
	
	/**
	 * Définir le parent
	 * @param parent parent de la node
	 */
	public void setParent(TreeNodeAbstract<?> parent) {
		// Retenir l'évenement
		this.inserted = true;
		
		// Retenir la node parent
		this.parent = parent;
	}
	
	/**
	 * Ajouter un enfant
	 * @param index indice d'ajout 
	 * @param child enfant à ajouter
	 */
	public void addChild(int index, TreeNodeAbstract<?> child) {
		// Ajouter l'enfant dans la liste
		this.children.add(index, child);
		
		// Définir le parent de l'enfant
		child.setParent(this);
	}
	
	/**
	 * Déplacer un enfant
	 * @param before indice de l'enfant à déplacer
	 * @param after indice de destination 
	 */
	public void moveChild(int before, int after) {
		// Déplacer l'enfant dans la liste
		this.addChild(after,this.removeChild(before));
	}
	
	/**
	 * Supprimer un enfant
	 * @param index indice de l'enfant à supprimer
	 * @return enfant supprimé
	 */
	public TreeNodeAbstract<?> removeChild(int index) {
		// Retirer l'enfant de la liste
		TreeNodeAbstract<?> node = this.children.remove(index);

		// Retenir l'évenement
		int[] indexes = { index };
		TreeNode[] nodes = { node };
		this.removed.add(new Quadruple<TreeNode, TreeNode[], int[], TreeNode[]>(this, this.getPath(), indexes, nodes));
		
		// Retourner la node
		return node;
	}
	
	/**
	 * Signaler la modification des données de la node
	 */
	public void setChanged() {
		// Retenir l'évenement
		this.changed = true;
	}
	
	/**
	 * Récupérer la liste des nodes ajoutées
	 * @return liste des nodes ajoutées
	 */
	public ArrayList<Quadruple<TreeNode, TreeNode[], int[], TreeNode[]>> getTreeNodesInserted () {
		// Initialiser la liste des nodes
		ArrayList<Quadruple<TreeNode, TreeNode[], int[], TreeNode[]>> nodes = new ArrayList<Quadruple<TreeNode,TreeNode[],int[],TreeNode[]>>();

		// Ajouter la node courante si nécéssaire
		if(this.inserted) {
			int[] indexes = { this.parent.getIndex(this) };
			TreeNode[] children = { this };
			nodes.add(new Quadruple<TreeNode, TreeNode[], int[], TreeNode[]>(this.parent, this.parent.getPath(), indexes, children));
		}
		
		// Ajouter les nodes des enfants
		for(TreeNodeAbstract<?> enfant : this.children) {
			nodes.addAll(enfant.getTreeNodesInserted());
		}
		
		// Retourner les nodes
		return nodes;
	}
	
	/**
	 * Récupérer la liste des nodes modifiées
	 * @return liste des nodes modifiées
	 */
	public ArrayList<Quadruple<TreeNode, TreeNode[], int[], TreeNode[]>> getTreeNodesChanged () {
		// Initialiser la liste des nodes
		ArrayList<Quadruple<TreeNode, TreeNode[], int[], TreeNode[]>> nodes = new ArrayList<Quadruple<TreeNode,TreeNode[],int[],TreeNode[]>>();

		// Ajouter la node courante si nécéssaire
		if(this.changed) {
			int[] indexes = { this.parent.getIndex(this) };
			TreeNode[] children = { this };
			nodes.add(new Quadruple<TreeNode, TreeNode[], int[], TreeNode[]>(this.parent, this.parent.getPath(), indexes, children));
		}
		
		// Ajouter les nodes des enfants
		for(TreeNodeAbstract<?> enfant : this.children) {
			nodes.addAll(enfant.getTreeNodesChanged());
		}
		
		// Retourner les nodes
		return nodes;
	}
	
	/**
	 * Récupérer la liste des nodes supprimées
	 * @return liste des nodes supprimées
	 */
	public ArrayList<Quadruple<TreeNode, TreeNode[], int[], TreeNode[]>> getTreeNodesRemoved () {
		// Initialiser la liste des nodes
		ArrayList<Quadruple<TreeNode, TreeNode[], int[], TreeNode[]>> nodes = this.removed;

		// Ajouter les nodes des enfants
		for(TreeNodeAbstract<?> enfant : this.children) {
			nodes.addAll(enfant.getTreeNodesRemoved());
		}
		
		// Retourner les nodes
		return nodes;
	}
	
	/**
	 * Effacer les indicateurs
	 */
	public void clearIndicators () {
		// Effacer les indicateurs
		this.inserted = false;
		this.changed = false;
		this.removed.clear();
		
		// Effacer les indicateurs des enfants
		for(TreeNodeAbstract<?> child : this.children) {
			child.clearIndicators();
		}
	}
	
}
