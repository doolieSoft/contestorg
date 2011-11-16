package common;

import interfaces.ITreeNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;

import javax.swing.tree.TreeNode;

public abstract class TreeNodeAbstract<T> implements ITreeNode
{
	// Indicateurs de modifications
	private boolean inserted = false;
	private boolean changed = false;
	private ArrayList<Quadruple<TreeNode, TreeNode[], int[], TreeNode[]>> removed = new ArrayList<Quadruple<TreeNode, TreeNode[], int[], TreeNode[]>>();
	
	// Parent
	private TreeNodeAbstract<?> parent;
	
	// Objet
	protected T object;
	
	// Enfants
	protected ArrayList<TreeNodeAbstract<?>> children = new ArrayList<TreeNodeAbstract<?>>();
	
	// Constructeur
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
	public TreeNode[] getPath() {
		// Initialiser le chemin
		TreeNode[] path = new TreeNode[this.getLevel()+1];
		
		// Remplir le chemin
		int i = 0;
		TreeNode parent = this;
		while(parent != null) {
			path[i++] = parent;
			parent = parent.getParent();
		}
		
		// Retourner le chemin
		return path;
	}
	
	// Définir le parent
	public void setParent(TreeNodeAbstract<?> parent) {
		// Retenir l'évenement
		this.inserted = true;
		
		// Retenir la node parent
		this.parent = parent;
	}
	
	// Ajouter/Déplacer/Supprimer des enfants
	public void addChild(int index, TreeNodeAbstract<?> child) {
		// Ajouter l'enfant dans la liste
		this.children.add(index, child);
		
		// Définir le parent de l'enfant
		child.setParent(this);
	}
	public void moveChild(int before, int after) {
		// Déplacer l'enfant dans la liste
		this.addChild(after,this.removeChild(before));
	}
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
	
	// Signaler la modification des données de la node
	public void hasChanged() {
		// Retenir l'évenement
		this.changed = true;
	}
	
	// Récupérer/Effacer les indicateurs de modifications
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
	public void clearIndicators () {
		// Remettre à zéro les indicateurs de la node courante
		this.inserted = false;
		this.changed = false;
		this.removed.clear();
		
		// Remettre à zéro les indicateurs des nodes enfants
		for(TreeNodeAbstract<?> child : this.children) {
			child.clearIndicators();
		}
	}
	
}
