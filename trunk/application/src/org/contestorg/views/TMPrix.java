package org.contestorg.views;


import java.awt.Window;

import org.contestorg.infos.InfosModelPrix;

public class TMPrix extends TMAbstract<InfosModelPrix>
{

	// Constructeur
	public TMPrix(Window w_parent) {
		// Appeller le constructeur du parent
		super(w_parent);
	}
	
	// Méthodes à implémenter
	@Override
	public Window getAddWindow () {
		return new JDPrixCreer(this.w_parent, this);
	}
	@Override
	public Window getUpdateWindow (InfosModelPrix infos) {
		return new JDPrixEditer(this.w_parent, this, infos);
	}
	@Override
	public boolean acceptDelete (InfosModelPrix infos) {
		// Demander la confirmation à l'utilisateur
		if (ViewHelper.confirmation(this.w_parent, "Désirez-vous vraiment supprimer le prix \"" + infos.getNom() + "\" ?")) {
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
		return this.get(row).getNom();
	}
	@Override
	public boolean isCellEditable (int row, int column) {
		return true;
	}
	@Override
	public void setValueAt (Object object, int row, int column) {
		this.update(row, new InfosModelPrix((String)object));
	}

}
