package org.contestorg.views;


import java.awt.Window;

import org.contestorg.infos.InfosModelEmplacement;

public class TMEmplacements extends TMAbstract<InfosModelEmplacement>
{

	// Constructeur
	public TMEmplacements(Window w_parent) {
		// Appeller le constructeur du parent
		super(w_parent);
	}
	
	// Méthodes à implémentation
	@Override
	public Window getAddWindow () {
		return new JDEmplacementCreer(this.w_parent, this);
	}
	@Override
	public Window getUpdateWindow (InfosModelEmplacement infos) {
		return new JDEmplacementEditer(this.w_parent, this, infos);
	}
	@Override
	public boolean acceptDelete (InfosModelEmplacement infos) {
		// Demander la confirmation à l'utilisateur
		if (ViewHelper.confirmation(this.w_parent, "Désirez-vous vraiment supprimer l'emplacement \"" + infos.getNom() + "\" ?")) {
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
		this.update(row, new InfosModelEmplacement((String)object, this.get(row).getDescription()));
	}

}
