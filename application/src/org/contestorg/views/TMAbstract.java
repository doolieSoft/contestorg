package org.contestorg.views;


import java.awt.Window;
import java.util.ArrayList;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import org.contestorg.common.TrackableList;
import org.contestorg.interfaces.ICollector;


public abstract class TMAbstract<T> extends TrackableList<T> implements TableModel, ICollector<T>
{
	// Parent
	protected Window w_parent;

	// fenêtres de création et d'édition
	protected Window w_add_update;
	private Integer row;

	// Liste de TableModelListener
	private ArrayList<TableModelListener> tm_listeners = new ArrayList<TableModelListener>();

	// Constructeurs
	public TMAbstract(Window w_parent) {
		// Retenir le parent
		this.w_parent = w_parent;
	}

	// Méthodes à implémenter
	public abstract Window getAddWindow (); // Afficher la fenêtre d'ajout d'un T
	public abstract Window getUpdateWindow (T infos); // Afficher la fenêtre de mise à jour d'un T
	public abstract boolean acceptDelete (T infos); // Demande de suppression d'un T

	// Lanceurs de fenêtres
	public void launchAddWindow () {
		// Récupérer la fenêtre et l'afficher
		this.row = null;
		this.w_add_update = this.getAddWindow();
		this.w_add_update.setVisible(true);
	}
	public void launchUpdateWindow (int row) {
		// Récupérer la fenêtre et l'afficher
		this.row = row;
		this.w_add_update = this.getUpdateWindow(this.get(row));
		this.w_add_update.setVisible(true);
	}
	public void launchDeleteWindow (int row) {
		// Demander à l'utilisateur s'il veut vraiment supprimer ce T
		if (this.acceptDelete(this.get(row))) {
			this.remove(row);
		}
	}

	// Implémentation de ICollector
	@Override
	public void accept (T infos) {
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
	@Override
	public void cancel () {
		// Masquer la fenêtre
		this.w_add_update.setVisible(false);
		this.w_add_update = null;
		this.row = null;
	}

	// Surcharge des opérations sur la liste pour afficher les erreurs eventuelles
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
	
	// Implémentation de TableModel
	@Override
	public void addTableModelListener (TableModelListener listener) {
		this.tm_listeners.add(listener);
	}
	@Override
	public void removeTableModelListener (TableModelListener listener) {
		this.tm_listeners.remove(listener);
	}
	@Override
	public int getRowCount () {
		return this.size();
	}
	
	// Fires des listeners de TableModel
	protected void fire (int row, int type) {
		TableModelEvent event = new TableModelEvent(this, row, row, TableModelEvent.ALL_COLUMNS, type);
		for (TableModelListener listener : this.tm_listeners) {
			listener.tableChanged(event);
		}
	}
	protected void fireRowInserted (int row, T inserted) {
		// Fire des listeners de TableModel
		this.fire(row, TableModelEvent.INSERT);
		
		// Appeller le fireRowInserted du parent
		super.fireRowInserted(row, inserted);
	}
	protected void fireRowUpdated (int row, T before, T after) {
		// Fire des listeners de TableModel
		this.fire(row, TableModelEvent.UPDATE);
		
		// Appeller le fireRowUpdated du parent
		super.fireRowUpdated(row, before, after);
	}
	protected void fireRowDeleted (int row, T deleted) {
		// Fire des listeners de TableModel
		this.fire(row, TableModelEvent.DELETE);
		
		// Appeller le fireRowDeleted du parent
		super.fireRowDeleted(row, deleted);
	}
	
}
