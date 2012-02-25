package org.contestorg.views;

import java.awt.Window;

import javax.swing.table.TableModel;

import org.contestorg.infos.InfosModelEmplacement;

/**
 * Modèle de données pour un tableau d'emplacements
 */
public class TMEmplacements extends TMAbstract<InfosModelEmplacement>
{

	/**
	 * Constructeur
	 * @param w_parent fenêtre parent
	 */
	public TMEmplacements(Window w_parent) {
		// Appeller le constructeur du parent
		super(w_parent);
	}
	
	/**
	 * @see TMAbstract#getAddWindow()
	 */
	@Override
	public Window getAddWindow () {
		return new JDEmplacementCreer(this.w_parent, this);
	}
	
	/**
	 * @see TMAbstract#getUpdateWindow(Object)
	 */
	@Override
	public Window getUpdateWindow (InfosModelEmplacement infos) {
		return new JDEmplacementEditer(this.w_parent, this, infos);
	}
	
	/**
	 * @see TMAbstract#acceptDelete(Object)
	 */
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
		return this.get(row).getNom();
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
		this.update(row, new InfosModelEmplacement((String)object, this.get(row).getDescription()));
	}

}
