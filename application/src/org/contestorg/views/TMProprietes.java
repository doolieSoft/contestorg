package org.contestorg.views;

import java.awt.Window;

import javax.swing.table.TableModel;

import org.contestorg.infos.InfosModelPropriete;

/**
 * Modèle de données pour un tableau de propriétés
 */
public class TMProprietes extends TMAbstract<InfosModelPropriete>
{

	/**
	 * Constructeur
	 * @param w_parent fenêtre parent
	 */
	public TMProprietes(final Window w_parent) {
		// Appeller le constructeur du parent
		super(w_parent);
	}
	
	/**
	 * @see TMAbstract#getAddWindow()
	 */
	@Override
	public Window getAddWindow () {
		return new JDProprieteCreer(this.w_parent, this);
	}
	
	/**
	 * @see TMAbstract#getUpdateWindow(Object)
	 */
	@Override
	public Window getUpdateWindow (InfosModelPropriete infos) {
		return new JDProprieteEditer(this.w_parent, this, infos);
	}
	
	/**
	 * @see TMAbstract#acceptDelete(Object)
	 */
	@Override
	public boolean acceptDelete (InfosModelPropriete infos) {
		// Demander la confirmation à l'utilisateur
		if (ViewHelper.confirmation(this.w_parent, "Désirez-vous vraiment supprimer la propriété \"" + infos.getNom() + "\" ?")) {
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
		return 2;
	}
	
	/**
	 * @see TableModel#getColumnName(int)
	 */
	@Override
	public String getColumnName (int column) {
		switch(column) {
			case 0: return "Nom";
			case 1: return "Type";
		}
		return null;
	}
	
	/**
	 * @see TableModel#getValueAt(int, int)
	 */
	@Override
	public Object getValueAt (int row, int column) {
		switch(column) {
			case 0: return this.get(row).getNom();
			case 1:
				switch(this.get(row).getType()) {
					case InfosModelPropriete.TYPE_INT: return "Nombre entier";
					case InfosModelPropriete.TYPE_FLOAT: return "Nombre décimal";
					case InfosModelPropriete.TYPE_STRING: return "Texte";
				}
		}
		return null;
	}
	
	/**
	 * @see TableModel#isCellEditable(int, int)
	 */
	@Override
	public boolean isCellEditable (int row, int column) {
		return column == 0;
	}
	
	/**
	 * @see TableModel#setValueAt(Object, int, int)
	 */
	@Override
	public void setValueAt (Object object, int row, int column) {
		this.update(row, new InfosModelPropriete((String)object, this.get(row).getType(), this.get(row).isObligatoire()));
	}

}
