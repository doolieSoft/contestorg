package org.contestorg.models;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import org.contestorg.common.Quadruple;
import org.contestorg.common.TreeNodeAbstract;
import org.contestorg.events.Action;
import org.contestorg.events.Event;
import org.contestorg.events.EventAdd;
import org.contestorg.events.EventMove;
import org.contestorg.events.EventRemove;
import org.contestorg.events.EventUpdate;
import org.contestorg.interfaces.IEventListener;
import org.contestorg.interfaces.IHistoryListener;

/**
 * Modèle de données pour arbre de phases qualificatives
 */
public class TreeModelPhasesQualifs extends DefaultTreeModel implements IHistoryListener
{
	/** Numéro de version de la classe */
	private static final long serialVersionUID = 1;
	
	/**
	 * Constructeur
	 */
	protected TreeModelPhasesQualifs() {
		// Appeller le constructeur du parent
		super(new ConcoursTreeNode(FrontModel.get().getConcours()));
		
		// Ecouter l'historique du concours
		FrontModel.get().getHistory().addListener(this);
	}
	
	/**
	 * Rafraichir la structure de l'arbre
	 */
	private void refresh () {
		// Caster la node root
		ConcoursTreeNode root = (ConcoursTreeNode)this.getRoot();
		
		// FIXME Etre plus fin dans les fires tout en évitant de provoquer des exceptions
		
		// Fire des listeners
		for (Quadruple<TreeNode, TreeNode[], int[], TreeNode[]> event : root.getTreeNodesRemoved()) {
			//this.fireTreeNodesRemoved(event.getFirst(), event.getSecond(), event.getThird(), event.getFourth());
			this.reload(event.getFirst());
		}
		for (Quadruple<TreeNode, TreeNode[], int[], TreeNode[]> event : root.getTreeNodesInserted()) {
			//this.fireTreeNodesInserted(event.getFirst(), event.getSecond(), event.getThird(), event.getFourth());
			this.reload(event.getFirst());
		}
		for (Quadruple<TreeNode, TreeNode[], int[], TreeNode[]> event : root.getTreeNodesChanged()) {
			//this.fireTreeNodesChanged(event.getFirst(), event.getSecond(), event.getThird(), event.getFourth());
			this.reload(event.getFirst());
		}
		
		// Effacer les indicateurs
		root.clearIndicators();
	}
	
	// Implémentation de IHistoryListener
	@Override
	public void historyActionPushed (Action action) {
		this.refresh();
	}
	@Override
	public void historyActionPoped (Action action) {
		this.refresh();
	}
	@Override
	public void historyInit () {
		// Retenir le concours
		((ConcoursTreeNode)this.getRoot()).setConcours(FrontModel.get().getConcours());
		
		// Fire des listeners
		this.reload();
	}
	
	/**
	 * Classe node représentant le concours
	 */
	private static class ConcoursTreeNode extends TreeNodeAbstract<ModelConcours> implements IEventListener
	{
		
		/**
		 * Constructeur
		 * @param concours concours
		 */
		public ConcoursTreeNode(ModelConcours concours) {
			// Appeller le constructeur du parent
			super(concours);
			
			// Vérifier si le concours n'est pas nul
			if (this.object != null) {
				// Remplir la liste des nodes des catégories filles
				for (ModelCategorie categorie : this.object.getCategories()) {
					CategorieTreeNode node = new CategorieTreeNode(categorie);
					this.children.add(node);
					node.setParent(this);
				}
				
				// Ecouter le concours
				this.object.addListener(this);
			}
		}
		
		/**
		 * Définir le concours
		 * @param concours concours
		 */
		public void setConcours (ModelConcours concours) {
			// Retenir le concours
			this.object = concours;
			
			// S'assurer que la liste des catégories soit vide
			this.children.clear();
			
			// Vérifier si le concours n'est pas nul
			if (this.object != null) {
				// Remplir la liste des nodes des catégories filles
				for (ModelCategorie categorie : this.object.getCategories()) {
					CategorieTreeNode node = new CategorieTreeNode(categorie);
					this.children.add(node);
					node.setParent(this);
				}
				
				// Ecouter le concours
				this.object.addListener(this);
			}
			
			// Nettoyer les indicateurs de modifications
			this.clearIndicators();
		}
		
		/**
		 * @see TreeNode#getAllowsChildren()
		 */
		@Override
		public boolean getAllowsChildren () {
			return true;
		}
		
		/**
		 * @see TreeNodeAbstract#getObject()
		 */
		public Object getObject () {
			return this.object.getInfos();
		}
		
		// Implémentation de IEventListener
		@Override
		public void event (Event event) {
			if (event instanceof EventAdd) {
				// Caster l'évenement
				EventAdd eventAdd = (EventAdd)event;
				
				// Vérifier s'il s'agit bien d'une catégorie qui a été ajoutée
				if (eventAdd.getAssociate() instanceof ModelCategorie) {
					// Ajouter la node enfant
					this.addChild(eventAdd.getIndex(), new CategorieTreeNode((ModelCategorie)eventAdd.getAssociate()));
				}
			} else if (event instanceof EventRemove) {
				// Caster l'évenement
				EventRemove eventRemove = (EventRemove)event;
				
				// Vérifier s'il s'agit bien d'une catégorie qui a été supprimée
				if (eventRemove.getAssociate() instanceof ModelCategorie) {
					// Supprimer la node enfant
					this.removeChild(eventRemove.getIndex());
				}
			} else if (event instanceof EventMove) {
				// Caster l'évenement
				EventMove eventMove = (EventMove)event;
				
				// Vérifier s'il s'agit bien d'une categorie qui a été déplacée
				if (eventMove.getAssociate() instanceof ModelCategorie) {
					// Déplacer la node enfant
					this.moveChild(eventMove.getBefore(), eventMove.getAfter());
				}
			}
		}
		
	}
	
	/**
	 * Classe node représentant une catégorie
	 */
	private static class CategorieTreeNode extends TreeNodeAbstract<ModelCategorie> implements IEventListener
	{
		
		/**
		 * Constructeur
		 * @param categorie catégorie
		 */
		public CategorieTreeNode(ModelCategorie categorie) {
			// Appeller le constructeur du parent
			super(categorie);
			
			// Remplir la liste des nodes des poules filles
			for (ModelPoule poule : this.object.getPoules()) {
				PouleTreeNode node = new PouleTreeNode(poule);
				this.children.add(node);
				node.setParent(this);
			}
			
			// Ecouter la catégorie
			this.object.addListener(this);
		}
		
		/**
		 * @see TreeNode#getAllowsChildren()
		 */
		@Override
		public boolean getAllowsChildren () {
			return true;
		}
		
		/**
		 * @see TreeNodeAbstract#getObject()
		 */
		public Object getObject () {
			return this.object.getInfos();
		}
		
		// Implémentation de IEventListener
		@Override
		public void event (Event event) {
			if (event instanceof EventAdd) {
				// Caster l'évenement
				EventAdd eventAdd = (EventAdd)event;
				
				// Vérifier s'il s'agit bien d'une poule qui a été ajoutée
				if (eventAdd.getAssociate() instanceof ModelPoule) {
					// Ajouter la node enfant
					this.addChild(eventAdd.getIndex(), new PouleTreeNode((ModelPoule)eventAdd.getAssociate()));
				}
			} else if (event instanceof EventRemove) {
				// Caster l'évenement
				EventRemove eventRemove = (EventRemove)event;
				
				// Vérifier s'il s'agit bien d'une poule qui a été supprimée
				if (eventRemove.getAssociate() instanceof ModelPoule) {
					// Supprimer la node enfant
					this.removeChild(eventRemove.getIndex());
				}
			} else if (event instanceof EventMove) {
				// Caster l'évenement
				EventMove eventMove = (EventMove)event;
				
				// Vérifier s'il s'agit bien d'une poule qui a été déplacée
				if (eventMove.getAssociate() instanceof ModelPoule) {
					// Déplacer la node enfant
					this.moveChild(eventMove.getBefore(), eventMove.getAfter());
				}
			} else if(event instanceof EventUpdate) {
				// Signaler la modification de la node
				this.setChanged();
			}
		}
	}
	
	/**
	 * Classe node représentant une poule
	 */
	private static class PouleTreeNode extends TreeNodeAbstract<ModelPoule> implements IEventListener
	{
		
		/**
		 * Constructeur
		 * @param poule poule
		 */
		public PouleTreeNode(ModelPoule poule) {
			// Appeller le constructeur parent
			super(poule);
			
			// Remplir la liste des nodes des participants fils
			for (ModelPhaseQualificative phaseQualif : this.object.getPhasesQualificatives()) {
				PhaseQualifTreeNode node = new PhaseQualifTreeNode(phaseQualif);
				this.children.add(node);
				node.setParent(this);
			}
			
			// Ecouter la poule
			this.object.addListener(this);
		}
		
		/**
		 * @see TreeNode#getAllowsChildren()
		 */
		@Override
		public boolean getAllowsChildren () {
			return true;
		}
		
		/**
		 * @see TreeNodeAbstract#getObject()
		 */
		public Object getObject () {
			return this.object.getInfos();
		}
		
		// Implémentation de IEventListener
		@Override
		public void event (Event event) {
			if (event instanceof EventAdd) {
				// Caster l'évenement
				EventAdd eventAdd = (EventAdd)event;
				
				// Vérifier s'il s'agit bien d'une phase qualificative qui a été ajoutée
				if (eventAdd.getAssociate() instanceof ModelPhaseQualificative) {
					// Ajouter la node enfant 
					this.addChild(eventAdd.getIndex(), new PhaseQualifTreeNode((ModelPhaseQualificative)eventAdd.getAssociate()));
					
					// Signaler la modification des phases suivantes
					if(eventAdd.getIndex() != this.children.size()-1) {
						for(TreeNodeAbstract<?> node : this.children.subList(eventAdd.getIndex()+1, this.children.size()-1)) {
							node.setChanged();
						}
					}
				}
			} else if (event instanceof EventRemove) {
				// Caster l'évenement
				EventRemove eventRemove = (EventRemove)event;
				
				// Vérifier s'il s'agit bien d'une phase qualificative qui a été supprimée
				if (eventRemove.getAssociate() instanceof ModelPhaseQualificative) {
					// Supprimer la node enfant
					this.removeChild(eventRemove.getIndex());
					
					// Signaler la modification des phases suivantes
					if(eventRemove.getIndex() != this.children.size()) {
						for(TreeNodeAbstract<?> node : this.children.subList(eventRemove.getIndex(), this.children.size()-1)) {
							node.setChanged();
						}
					}
				}
			} else if (event instanceof EventMove) {
				// Caster l'évenement
				EventMove eventMove = (EventMove)event;
				
				// Vérifier s'il s'agit bien d'une phase qualificative qui a été déplacée
				if (eventMove.getAssociate() instanceof ModelPhaseQualificative) {
					// Déplacer la node enfant
					this.moveChild(eventMove.getBefore(), eventMove.getAfter());
				}
			} else if(event instanceof EventUpdate) {
				// Signaler la modification de la node
				this.setChanged();
			}
		}
	}
	
	/**
	 * Classe node représentant une phase qualificative
	 */
	private static class PhaseQualifTreeNode extends TreeNodeAbstract<ModelPhaseQualificative>
	{
		/**
		 * Constructeur
		 * @param phaseQualif phase qualificative
		 */
		public PhaseQualifTreeNode(ModelPhaseQualificative phaseQualif) {
			// Appeller le constructeur parent
			super(phaseQualif);
		}
		
		/**
		 * @see TreeNode#getAllowsChildren()
		 */
		@Override
		public boolean getAllowsChildren () {
			return false;
		}
		
		/**
		 * @see TreeNodeAbstract#getObject()
		 */
		public Object getObject () {
			return this.object.getInfos();
		}
	}
}
