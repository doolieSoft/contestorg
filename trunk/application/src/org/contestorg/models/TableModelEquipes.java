package org.contestorg.models;


import java.util.ArrayList;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import org.contestorg.events.Action;
import org.contestorg.infos.InfosModelEquipe;
import org.contestorg.interfaces.IClosableTableModel;
import org.contestorg.interfaces.IHistoryListener;


public class TableModelEquipes implements TableModel, IClosableTableModel, IHistoryListener
{
	// Concours, categorie et poule
	private ModelConcours concours;
	private ModelCategorie categorie;
	private ModelPoule poule;
	
	// Equipes
	private ArrayList<ModelEquipe> equipes = new ArrayList<ModelEquipe>();
	
	// Listeners
	private ArrayList<TableModelListener> listeners = new ArrayList<TableModelListener>();
	
	// Constructeur
	public TableModelEquipes(ModelConcours concours) {
		this(concours, null, null);
	}
	public TableModelEquipes(ModelCategorie categorie) {
		this(null, categorie, null);
	}
	public TableModelEquipes(ModelPoule poule) {
		this(null, null, poule);
	}
	private TableModelEquipes(ModelConcours concours, ModelCategorie categorie, ModelPoule poule) {
		// Retenir le concours, la categorie et la poule
		this.concours = concours;
		this.categorie = categorie;
		this.poule = poule;
		
		// Initialiser la liste des équipes
		if (this.concours != null) {
			this.equipes = this.concours.getEquipes();
		} else if (this.categorie != null) {
			this.equipes = this.categorie.getEquipes();
		} else if (this.poule != null) {
			this.equipes = this.poule.getEquipes();
		}
		
		// Ecouter l'historique du concours
		FrontModel.get().getHistory().addListener(this);
	}
	
	// Rafraichir la liste des équipes
	private void refresh () {
		// Retenir le nombre d'équipes dans l'ancienne liste
		int nbEquipesAnciennes = this.equipes.size();
		
		// Mettre à jour la liste des équipes
		if (this.concours != null) {
			this.equipes = this.concours.getEquipes();
		} else if (this.categorie != null) {
			this.equipes = this.categorie.getEquipes();
		} else if (this.poule != null) {
			this.equipes = this.poule.getEquipes();
		}
		
		// Retenir le nombre d'équipes dans la nouvelle liste
		int nbEquipesNouvelles = this.equipes.size();
		
		// Signaler les modifications
		if(nbEquipesAnciennes > 0 || nbEquipesNouvelles > 0) {
			// Min/Max
			int max = Math.max(nbEquipesAnciennes,nbEquipesNouvelles);
			int min = Math.min(nbEquipesAnciennes,nbEquipesNouvelles);
			
			// Updates
			if(nbEquipesAnciennes > 0 && nbEquipesNouvelles != 0) {
				this.fireRowUpdated(0, min-1);
			}
			
			// Inserts/Deletes
			if(nbEquipesAnciennes < nbEquipesNouvelles) {
				this.fireRowInserted(min, max-1);
			} else if(nbEquipesAnciennes > nbEquipesNouvelles) {
				this.fireRowDeleted(min, max-1);
			}
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
		switch (column + this.getDecallage()) {
			case 0:
				return String.class;
			case 1:
				return String.class;
			case 2:
				return String.class;
			case 3:
				return String.class;
			case 4:
				return Double.class;
			case 5:
				return Double.class;
			case 6:
				return Integer.class;
			case 7:
				return String.class;
			case 8:
				return InfosModelEquipe.Statut.class;
		}
		return null;
	}
	@Override
	public int getColumnCount () {
		return 9 - this.getDecallage();
	}
	@Override
	public String getColumnName (int column) {
		switch (column + this.getDecallage()) {
			case 0:
				return "Catégorie";
			case 1:
				return "Poule";
			case 2:
				return "Stand";
			case 3:
				return "Nom";
			case 4:
				return "Rang";
			case 5:
				return "Points";
			case 6:
				return "Victoires";
			case 7:
				return "Ville";
			case 8:
				return "Statut";
		}
		return null;
	}
	@Override
	public int getRowCount () {
		return this.equipes.size();
	}
	@Override
	public Object getValueAt (int index, int column) {
		switch (column + this.getDecallage()) {
			case 0:
				return this.equipes.get(index).getPoule().getCategorie().getNom();
			case 1:
				return this.equipes.get(index).getPoule().getNom();
			case 2:
				return this.equipes.get(index).getStand();
			case 3:
				return this.equipes.get(index).getNom();
			case 4:
				return this.equipes.get(index).getRangPhasesQualifs();
			case 5:
				return this.equipes.get(index).getPoints();
			case 6:
				return this.equipes.get(index).getNbVictoires();
			case 7:
				return this.equipes.get(index).getVille();
			case 8:
				return this.equipes.get(index).getStatut();
		}
		return null;
	}
	@Override
	public boolean isCellEditable (int row, int column) {
		return false;
	}
	@Override
	public void setValueAt (Object object, int row, int column) {
	}
	
	// Fires des listeners
	protected void fire (int first, int last, int type) {
		TableModelEvent event = new TableModelEvent(this, first, last, TableModelEvent.ALL_COLUMNS, type);
		for (TableModelListener listener : this.listeners) {
			listener.tableChanged(event);
		}
	}
	protected void fireRowInserted (int first, int last) {
		// Fire des listeners de TableModel
		this.fire(first, last, TableModelEvent.INSERT);
	}
	protected void fireRowUpdated (int first, int last) {
		// Fire des listeners de TableModel
		this.fire(first, last, TableModelEvent.UPDATE);
	}
	protected void fireRowDeleted (int first, int last) {
		// Fire des listeners de TableModel
		this.fire(first, last, TableModelEvent.DELETE);
	}
	
	// Décalaler le numéro de colonne en fonction de la catégorie et la poule
	private int getDecallage () {
		if (this.poule != null) {
			return 2;
		} else if (this.categorie != null) {
			return 1;
		} else {
			return 0;
		}
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
		this.concours = FrontModel.get().getConcours();
		this.categorie = null;
		this.poule = null;
		
		// Rafraichir la liste des équipes
		this.refresh();
	}
	
	// Implémentation de IClosableTableModel
	@Override
	public void close () {
		FrontModel.get().getHistory().removeListener(this);
	}
	
}
