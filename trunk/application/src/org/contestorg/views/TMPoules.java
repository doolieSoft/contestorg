package org.contestorg.views;


import java.awt.Window;
import java.util.ArrayList;

import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import org.contestorg.common.Pair;
import org.contestorg.controlers.ContestOrg;
import org.contestorg.infos.InfosModelConcours;
import org.contestorg.infos.InfosModelPoule;


public class TMPoules extends TMAbstract<Pair<InfosModelPoule, ArrayList<String>>> implements ComboBoxModel {

	// Constructeur
	public TMPoules(Window w_parent) {
		// Appeller le constructeur du parent
		super(w_parent);
	}

	// Méthodes à implementer
	@Override
	public Window getAddWindow () {
		return new JDPouleCreer(this.w_parent, this);
	}
	@Override
	public Window getUpdateWindow (Pair<InfosModelPoule, ArrayList<String>> infos) {
		return new JDPouleEditer(this.w_parent, this, infos);
	}
	@Override
	public boolean acceptDelete (Pair<InfosModelPoule, ArrayList<String>> infos) {
		// Demander la confirmation à l'utilisateur
		if (ViewHelper.confirmation(
				this.w_parent,
				"En supprimant la poule \"" + infos.getFirst().getNom() + "\", "+(ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "toutes les équipes" : "tous les joueurs")+" qu'elle contient seront\n" +
				"réaffectées au sein de la poule \""+ this.get(0).getFirst().getNom() +"\". Par contre, si des matchs ont été\n" +
			    "crées dans la poule \"" + infos.getFirst().getNom() + "\", ils seront supprimés. Désirez-vous continuer ?",
				true)) {
			return true;
		} else {
			return false;
		}
	}

	// Implémentation manquante de TableModel
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
		return "Nom";
	}
	@Override
	public Object getValueAt (int row, int column) {
		return this.get(row).getFirst().getNom();
	}
	@Override
	public boolean isCellEditable (int row, int column) {
		return true;
	}
	@Override
	public void setValueAt (Object object, int row, int column) {
		this.update(row, new Pair<InfosModelPoule, ArrayList<String>>(new InfosModelPoule((String)object), this.get(row).getSecond()));
	}

	// Surcharge de remove
	public boolean remove (int row) {
		// Récupérer les équipes de la poule
		ArrayList<String> equipes = this.get(row).getSecond();
		
		// Appeller le remove du parent
		boolean result = super.remove(row);
		
		// Placer les équipes dans la première poule
		if(result) {
			this.get(0).getSecond().addAll(equipes);
		}
		
		// Retourner le résultat de l'opération
		return result;
	}
	
	// Listeners de ComboBoxModel
	private ArrayList<ListDataListener> ldl_listeners = new ArrayList<ListDataListener>();
	
	// Item séléctionné
	private Object selected;
	
	// Implémentation de ComboBoxModel
	@Override
	public void addListDataListener (ListDataListener listener) {
		this.ldl_listeners.add(listener);
	}
	@Override
	public void removeListDataListener (ListDataListener listener) {
		this.ldl_listeners.remove(listener);
	}
	@Override
	public Object getElementAt (int index) {
		return this.getModifies().get(index+1).getFirst().getNom();
	}
	@Override
	public int getSize () {
		return this.size()-1;
	}
	@Override
	public Object getSelectedItem () {
		return this.selected == null && this.size() > 1 ? this.selected = this.get(1).getFirst().getNom() : this.selected;
	}
	@Override
	public void setSelectedItem (Object selected) {
		this.selected = selected;
	}
	
	// Fires des listeners de ComboBoxModel
	protected void fireRowInserted (int row, Pair<InfosModelPoule, ArrayList<String>> inserted) {
		// Fire des listeners de ComboBoxModel
		for(ListDataListener listener : this.ldl_listeners) {
			listener.intervalAdded(new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, Math.max(0, row-1), Math.max(0, row-1)));
		}
		
		// Appeller le fireRowInserted du parent
		super.fireRowInserted(row, inserted);
	}
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
