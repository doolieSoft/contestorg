package org.contestorg.views;

import java.awt.Window;
import java.util.ArrayList;

import javax.swing.ComboBoxModel;
import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.table.TableModel;

import org.contestorg.common.Pair;
import org.contestorg.controllers.ContestOrg;
import org.contestorg.infos.InfosModelConcours;
import org.contestorg.infos.InfosModelPoule;

/**
 * Modèle de données pour un tableau de poules
 */
public class TMPoules extends TMAbstract<Pair<InfosModelPoule, ArrayList<String>>> implements ComboBoxModel<String> {

	/**
	 * Constructeur
	 * @param w_parent fenêtre parent
	 */
	public TMPoules(Window w_parent) {
		// Appeller le constructeur du parent
		super(w_parent);
	}

	/**
	 * @see TMAbstract#getAddWindow()
	 */
	@Override
	public Window getAddWindow () {
		return new JDPouleCreer(this.w_parent, this);
	}
	
	/**
	 * @see TMAbstract#getUpdateWindow(Object)
	 */
	@Override
	public Window getUpdateWindow (Pair<InfosModelPoule, ArrayList<String>> infos) {
		return new JDPouleEditer(this.w_parent, this, infos);
	}
	
	/**
	 * @see TMAbstract#acceptDelete(Object)
	 */
	@Override
	public boolean acceptDelete (Pair<InfosModelPoule, ArrayList<String>> infos) {
		// Demander la confirmation à l'utilisateur
		if (ViewHelper.confirmation(
				this.w_parent,
				"En supprimant la poule \"" + infos.getFirst().getNom() + "\", "+(ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "toutes les équipes qu'elle contient seront réaffectées" : "tous les joueurs qu'elle contient seront réaffectés")+" au sein\nde la poule \""+ this.get(0).getFirst().getNom() +"\" et les matchs qu'elle contient seront supprimés. Désirez-vous continuer ?",
				true)) {
			return true;
		} else {
			return false;
		}
	}

	// Implémentation manquante de TableModel
	
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
		return "Nom";
	}
	
	/**
	 * @see TableModel#getValueAt(int, int)
	 */
	@Override
	public Object getValueAt (int row, int column) {
		return this.get(row).getFirst().getNom();
	}
	
	/**
	 * @see TableModel#isCellEditable(int, int)
	 */
	@Override
	public boolean isCellEditable (int row, int column) {
		return true;
	}
	
	/**
	 * @see TableModel#setValueAt(Object, int, int)
	 */
	@Override
	public void setValueAt (Object object, int row, int column) {
		this.update(row, new Pair<InfosModelPoule, ArrayList<String>>(new InfosModelPoule((String)object), this.get(row).getSecond()));
	}

	/**
	 * @see TMAbstract#remove(int)
	 */
	@Override
	public boolean remove (int row) {
		// Récupérer les participants de la poule
		ArrayList<String> participants = this.get(row).getSecond();
		
		// Appeller le remove du parent
		boolean result = super.remove(row);
		
		// Placer les participants dans la première poule
		if(result) {
			this.get(0).getSecond().addAll(participants);
		}
		
		// Retourner le résultat de l'opération
		return result;
	}
	
	/** Listeners */
	private ArrayList<ListDataListener> ldl_listeners = new ArrayList<ListDataListener>();
	
	/** Poule séléctionnée */
	private Object selected;
	
	// Implémentation de ComboBoxModel
	
	/**
	 * @see ListModel#addListDataListener(ListDataListener)
	 */
	@Override
	public void addListDataListener (ListDataListener listener) {
		this.ldl_listeners.add(listener);
	}
	
	/**
	 * @see ListModel#removeListDataListener(ListDataListener)
	 */
	@Override
	public void removeListDataListener (ListDataListener listener) {
		this.ldl_listeners.remove(listener);
	}
	
	/**
	 * @see ListModel#getElementAt(int)
	 */
	@Override
	public String getElementAt (int index) {
		return this.getModifies().get(index+1).getFirst().getNom();
	}
	
	/**
	 * @see ListModel#getSize()
	 */
	@Override
	public int getSize () {
		return this.size()-1;
	}
	
	/**
	 * @see ComboBoxModel#getSelectedItem()
	 */
	@Override
	public Object getSelectedItem () {
		return this.selected == null && this.size() > 1 ? this.selected = this.get(1).getFirst().getNom() : this.selected;
	}
	
	/**
	 * @see ComboBoxModel#setSelectedItem(Object)
	 */
	@Override
	public void setSelectedItem (Object selected) {
		this.selected = selected;
	}
	
	// Signalements
	
	/**
	 * @see TMAbstract#fireRowInserted(int, Object)
	 */
	@Override
	protected void fireRowInserted (int row, Pair<InfosModelPoule, ArrayList<String>> inserted) {
		// Fire des listeners de ComboBoxModel
		for(ListDataListener listener : this.ldl_listeners) {
			listener.intervalAdded(new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, Math.max(0, row-1), Math.max(0, row-1)));
		}
		
		// Appeller le fireRowInserted du parent
		super.fireRowInserted(row, inserted);
	}
	
	/**
	 * @see TMAbstract#fireRowUpdated(int, Object, Object)
	 */
	protected void fireRowUpdated (int row, Pair<InfosModelPoule, ArrayList<String>> before, Pair<InfosModelPoule, ArrayList<String>> after) {
		if(row != 0) {
			// Modifier l'élément séléctionné si nécéssaire
			if(before.getFirst().getNom().equals(this.selected)) {
				this.selected = after.getFirst().getNom();
			}
			
			// Fire des listeners de ComboBoxModel
			for(ListDataListener listener : this.ldl_listeners) {
				listener.contentsChanged(new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, row-1, row-1));
			}
		}
		
		// Appeller le fireRowUpdated du parent
		super.fireRowUpdated(row, before, after);
	}
	
	/**
	 * @see TMAbstract#fireRowDeleted(int, Object)
	 */
	protected void fireRowDeleted (int row, Pair<InfosModelPoule, ArrayList<String>> deleted) {
		// Modifier l'élément séléctionné si nécéssaire
		if(this.size() == 0 || row != 0 && deleted.getFirst().getNom().equals(this.selected) || row == 0 && this.get(0).getFirst().getNom().equals(this.selected)) {
			this.selected = null;
		}

		// Fire des listeners de ComboBoxModel
		if(this.size() != 0) {
			for(ListDataListener listener : this.ldl_listeners) {
				listener.intervalRemoved(new ListDataEvent(this, ListDataEvent.INTERVAL_REMOVED, Math.max(0, row-1), Math.max(0, row-1)));
			}
		}
		
		// Appeller le fireRowDeleted du parent
		super.fireRowDeleted(row, deleted);
	}
	
}
