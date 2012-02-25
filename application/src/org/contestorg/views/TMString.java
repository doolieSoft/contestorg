package org.contestorg.views;

import java.util.ArrayList;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/**
 * Modèle de données pour un tableau de chaînes de caractères
 */
public class TMString implements TableModel
{
	/** Entête */
	private String header;
	
	/** Liste des chaînes de caractères */
	private ArrayList<String> strings = new ArrayList<String>();
	
	/** Listeners */
	private ArrayList<TableModelListener> listeners = new ArrayList<TableModelListener>();
	
	/**
	 * Constructeur
	 * @param header entête
	 */
	public TMString(String header) {
		// Retenir le header
		this.header = header;
	}
	
	// Opérations sur la liste des chaînes de caractères
	
	/**
	 * Ajouter une chaîne de caractères
	 * @param string chaîne de caractères
	 */
	public void add(String string) {
		// Ajouter la string
		this.strings.add(string);
		
		// Fire
		this.fire(this.strings.size()-1, TableModelEvent.INSERT);
	}
	
	/**
	 * Récupérer une chaîne de caractères
	 * @param row indice de la chaîne de caractères
	 * @return chaîne de caractères
	 */
	public String get(int row) {
		// Récupérer et retourner la string demandée
		return this.strings.get(row);
	}
	
	/**
	 * Supprimer une chaîne de caractères
	 * @param row indice de la chaîne de caractères
	 * @return chaîne de caractères
	 */
	public String remove(int row) {
		// Rétirer la string
		String string = this.strings.remove(row);
		
		// Fire
		this.fire(row, TableModelEvent.DELETE);
		
		// Retourner la string supprimée
		return string;
	}
	
	/**
	 * Supprimer toutes les chaînes de caractères
	 */
	public void clear() {
		// Récupérer le nombre de strings
		int nbStrings = this.strings.size();
		
		// Retirer toutes les strings
		this.strings.clear();
		
		// Fires
		while(nbStrings > 0) {
			this.fire((nbStrings--)-1, TableModelEvent.DELETE);
		}
	}
	
	/**
	 * Remplir avec une liste de chaînes de caractères
	 * @param strings liste de chaînes de caractères
	 */
	public void fill(ArrayList<String> strings) {
		// Vider la liste des strings
		this.clear();
		
		// Ajouter les nouveaux strings
		for(String string : strings) {
			this.add(string);
		}
	}
	
	/**
	 * Lier à une liste de chaînes de caractères
	 * @param strings liste de chaînes de caractères
	 */
	public void link(ArrayList<String> strings) {
		// Fires de suppression
		this.strings = new ArrayList<String>(this.strings);
		this.clear();
		
		// Se lier avec la nouvelle liste
		this.strings = strings;
		
		// Fires d'ajout
		for(int i=0;i<this.strings.size();i++) {
			this.fire(i, TableModelEvent.INSERT);
		}
	}
	
	// Implémentation de TableModel
	
	/**
	 * @see TableModel#addTableModelListener(TableModelListener)
	 */
	@Override
	public void addTableModelListener (TableModelListener listener) {
		this.listeners.add(listener);
	}
	
	/**
	 * @see TableModel#removeTableModelListener(TableModelListener)
	 */
	@Override
	public void removeTableModelListener (TableModelListener listener) {
		this.listeners.remove(listener);
	}
	
	/**
	 * @see TableModel#getColumnClass(int)
	 */
	@Override
	public Class<?> getColumnClass (int column) {
		return String.class;
	}
	
	/**
	 * @see TableModel#getColumnCount()
	 */
	@Override
	public int getColumnCount () {
		return 1;
	}
	
	/**
	 * @see TableModel#getColumnName(int)
	 */
	@Override
	public String getColumnName (int column) {
		return this.header;
	}
	
	/**
	 * @see TableModel#getRowCount()
	 */
	@Override
	public int getRowCount () {
		return this.strings.size();
	}
	
	/**
	 * @see TableModel#getValueAt(int, int)
	 */
	@Override
	public Object getValueAt (int row, int column) {
		return this.strings.get(row);
	}
	
	/**
	 * @see TableModel#isCellEditable(int, int)
	 */
	@Override
	public boolean isCellEditable (int row, int column) {
		return false;
	}
	
	/**
	 * @see TableModel#setValueAt(Object, int, int)
	 */
	@Override
	public void setValueAt (Object object, int row, int column) {
	}
	
	/**
	 * Signaler un événement
	 * @param row indice de la chaîne de caractères
	 * @param type type de l'événement
	 */
	protected void fire (int row, int type) {
		TableModelEvent event = new TableModelEvent(this, row, row, TableModelEvent.ALL_COLUMNS, type);
		for (TableModelListener listener : this.listeners) {
			listener.tableChanged(event);
		}
	}
}
