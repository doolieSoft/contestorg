package views;

import java.util.ArrayList;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

public class TMString implements TableModel
{
	// Header
	private String header;
	
	// Liste des strings
	private ArrayList<String> strings = new ArrayList<String>();
	
	// Listeners
	private ArrayList<TableModelListener> listeners = new ArrayList<TableModelListener>();
	
	// Constructeur
	public TMString(String header) {
		// Retenir le header
		this.header = header;
	}
	
	// Opérations sur la liste des strings
	public void add(String string) {
		// Ajouter la string
		this.strings.add(string);
		
		// Fire
		this.fire(this.strings.size()-1, TableModelEvent.INSERT);
	}
	public String get(int row) {
		// Récupérer et retourner la string demandée
		return this.strings.get(row);
	}
	public String remove(int row) {
		// Rétirer la string
		String string = this.strings.remove(row);
		
		// Fire
		this.fire(row, TableModelEvent.DELETE);
		
		// Retourner la string supprimée
		return string;
	}
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
	public void fill(ArrayList<String> strings) {
		// Vider la liste des strings
		this.clear();
		
		// Ajouter les nouveaux strings
		for(String string : strings) {
			this.add(string);
		}
	}
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
	@Override
	public void addTableModelListener (TableModelListener listener) {
		this.listeners.add(listener);
	}
	@Override
	public void removeTableModelListener (TableModelListener listener) {
		this.listeners.remove(listener);
	}
	@Override
	public Class<?> getColumnClass (int column) {
		return String.class;
	}
	@Override
	public int getColumnCount () {
		return 1;
	}
	@Override
	public String getColumnName (int column) {
		return this.header;
	}
	@Override
	public int getRowCount () {
		return this.strings.size();
	}
	@Override
	public Object getValueAt (int row, int column) {
		return this.strings.get(row);
	}
	@Override
	public boolean isCellEditable (int row, int column) {
		return false;
	}
	@Override
	public void setValueAt (Object object, int row, int column) {
	}
	
	// Fires des listeners
	protected void fire (int row, int type) {
		TableModelEvent event = new TableModelEvent(this, row, row, TableModelEvent.ALL_COLUMNS, type);
		for (TableModelListener listener : this.listeners) {
			listener.tableChanged(event);
		}
	}
}
