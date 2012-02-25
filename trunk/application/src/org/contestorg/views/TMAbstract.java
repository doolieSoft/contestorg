package org.contestorg.views;

import java.awt.Window;
import java.util.ArrayList;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import org.contestorg.common.TrackableList;
import org.contestorg.interfaces.ICollector;

/**
 * Modèle de données pour tableaux
 * @param <T> classe des objets du modèle de données pour tableaux
 */
public abstract class TMAbstract<T> extends TrackableList<T> implements TableModel, ICollector<T>
{
	/** Fenêtre parent */
	protected Window w_parent;

	/** Fenêtre de création/édition */
	protected Window w_add_update;
	
	/** Indice de la donnée éditée */
	private Integer row;

	/** Listeners */
	private ArrayList<TableModelListener> tm_listeners = new ArrayList<TableModelListener>();

	/**
	 * Constructeur
	 * @param w_parent fenêtre parent
	 */
	public TMAbstract(Window w_parent) {
		// Retenir le parent
		this.w_parent = w_parent;
	}

	/**
	 * Afficher la fenêtre d'ajout d'un T
	 * @return fenêtre d'ajout du T
	 */
	public abstract Window getAddWindow ();
	
	/**
	 * Afficher la fenêtre d'édition d'un T
	 * @param infos informations du T à éditer
	 * @return fenêtre d'édition du T
	 */
	public abstract Window getUpdateWindow (T infos);
	
	/**
	 * Demander la suppression d'un T
	 * @param infos information du T à supprimer
	 * @return suppression acceptée ?
	 */
	public abstract boolean acceptDelete (T infos);

	/**
	 * Afficher une fenêtre d'ajout d'un T
	 */
	public void launchAddWindow () {
		// Récupérer la fenêtre et l'afficher
		this.row = null;
		this.w_add_update = this.getAddWindow();
		this.w_add_update.setVisible(true);
	}
	
	/**
	 * Afficher une fenêtre d'édition d'un T
	 * @param row indice du T à éditer
	 */
	public void launchUpdateWindow (int row) {
		// Récupérer la fenêtre et l'afficher
		this.row = row;
		this.w_add_update = this.getUpdateWindow(this.get(row));
		this.w_add_update.setVisible(true);
	}
	
	/**
	 * Afficher la fenêtre de suppression d'un T
	 * @param row indice du T à supprimer
	 */
	public void launchDeleteWindow (int row) {
		// Demander à l'utilisateur s'il veut vraiment supprimer ce T
		if (this.acceptDelete(this.get(row))) {
			this.remove(row);
		}
	}

	/**
	 * @see ICollector#collect(Object)
	 */
	@Override
	public void collect (T infos) {
		// Ajouter/editer le T
		boolean result = this.row == null ? this.add(infos) : this.update(this.row, infos);

		// Masquer la fenêtre
		if(result) {
			// Masquer la fenêtre
			this.w_add_update.setVisible(false);
			this.w_add_update = null;
			this.row = null;
		}
	}
	
	/**
	 * @see ICollector#cancel()
	 */
	@Override
	public void cancel () {
		// Masquer la fenêtre
		this.w_add_update.setVisible(false);
		this.w_add_update = null;
		this.row = null;
	}

	/**
	 * @see TrackableList#add(Object) 
	 */
	@Override
	public boolean add(T infos) {
		// Appeller le add du parent
		boolean result = super.add(infos);
		
		// Afficher les messages d'erreurs si nécéssaires
		if(!result) {
			// Message d'erreur
			for(String erreur : this.erreurs) {
				ViewHelper.derror(this.w_add_update == null ? this.w_parent : this.w_add_update, erreur);
			}
		}
		
		// Retourner le résultat de l'opération
		return result;
	}
	
	/**
	 * @see TrackableList#update(int, Object)
	 */
	@Override
	public boolean update(int row, T after) {
		// Appeller l'update du parent
		boolean result = super.update(row, after);
		
		// Afficher les messages d'erreurs si nécéssaires
		if(!result) {
			// Message d'erreur
			for(String erreur : this.erreurs) {
				ViewHelper.derror(this.w_add_update == null ? this.w_parent : this.w_add_update, erreur);
			}
		}
		
		// Retourner le résultat de l'opération
		return result;
	}
	
	/**
	 * @see TrackableList#remove(int)
	 */
	@Override
	public boolean remove(int row) {
		// Appeller le remove du parent
		boolean result = super.remove(row);
		
		// Afficher les messages d'erreurs si nécéssaires
		if(!result) {
			// Message d'erreur
			for(String erreur : this.erreurs) {
				ViewHelper.derror(this.w_parent, erreur);
			}
		}
		
		// Retourner le résultat de l'opération
		return result;
	}
	
	/**
	 * @see TrackableList#moveUp(int)
	 */
	@Override
	public boolean moveUp(int row) {
		// Appeller le moveDown du parent
		boolean result = super.moveUp(row);
		
		// Afficher les messages d'erreurs si nécéssaires
		if(!result) {
			// Message d'erreur
			for(String erreur : this.erreurs) {
				ViewHelper.derror(this.w_parent, erreur);
			}
		}
		
		// Retourner le résultat de l'opération
		return result;
	}
	
	/**
	 * @see TrackableList#moveDown(int)
	 */
	@Override
	public boolean moveDown(int row) {
		// Appeller le moveUp du parent
		boolean result = super.moveDown(row);
		
		// Afficher les messages d'erreurs si nécéssaires
		if(!result) {
			// Message d'erreur
			for(String erreur : this.erreurs) {
				ViewHelper.derror(this.w_parent, erreur);
			}
		}
		
		// Retourner le résultat de l'opération
		return result;
	}
	
	/**
	 * @see TableModel#addTableModelListener(TableModelListener) 
	 */
	@Override
	public void addTableModelListener (TableModelListener listener) {
		this.tm_listeners.add(listener);
	}
	
	/**
	 * @see TableModel#removeTableModelListener(TableModelListener)
	 */
	@Override
	public void removeTableModelListener (TableModelListener listener) {
		this.tm_listeners.remove(listener);
	}
	
	/**
	 * @see TableModel#getRowCount()
	 */
	@Override
	public int getRowCount () {
		return this.size();
	}
	
	/**
	 * Signaler un évenement
	 * @param row indice du T concerné
	 * @param type type de l'évenement
	 */
	protected void fire (int row, int type) {
		TableModelEvent event = new TableModelEvent(this, row, row, TableModelEvent.ALL_COLUMNS, type);
		for (TableModelListener listener : this.tm_listeners) {
			listener.tableChanged(event);
		}
	}
	
	/**
	 * @see TrackableList#fireRowInserted(int, Object)
	 */
	@Override
	protected void fireRowInserted (int row, T inserted) {
		// Fire des listeners de TableModel
		this.fire(row, TableModelEvent.INSERT);
		
		// Appeller le fireRowInserted du parent
		super.fireRowInserted(row, inserted);
	}
	
	/**
	 * @see TrackableList#fireRowUpdated(int, Object, Object)
	 */
	@Override
	protected void fireRowUpdated (int row, T before, T after) {
		// Fire des listeners de TableModel
		this.fire(row, TableModelEvent.UPDATE);
		
		// Appeller le fireRowUpdated du parent
		super.fireRowUpdated(row, before, after);
	}
	
	/**
	 * @see TrackableList#fireRowDeleted(int, Object)
	 */
	@Override
	protected void fireRowDeleted (int row, T deleted) {
		// Fire des listeners de TableModel
		this.fire(row, TableModelEvent.DELETE);
		
		// Appeller le fireRowDeleted du parent
		super.fireRowDeleted(row, deleted);
	}
	
}
