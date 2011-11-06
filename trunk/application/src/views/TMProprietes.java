package views;

import infos.InfosModelPropriete;

import java.awt.Window;

public class TMProprietes extends TMAbstract<InfosModelPropriete>
{

	// Constructeur
	public TMProprietes(final Window w_parent) {
		// Appeller le constructeur du parent
		super(w_parent);
	}
	
	// Méthodes à implémenter
	@Override
	public Window getAddWindow () {
		return new JDProprieteCreer(this.w_parent, this);
	}
	@Override
	public Window getUpdateWindow (InfosModelPropriete infos) {
		return new JDProprieteEditer(this.w_parent, this, infos);
	}
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
	@Override
	public Class<?> getColumnClass (int column) {
		return String.class;
	}
	@Override
	public int getColumnCount () {
		return 2;
	}
	@Override
	public String getColumnName (int column) {
		switch(column) {
			case 0: return "Nom";
			case 1: return "Type";
		}
		return null;
	}
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
	@Override
	public boolean isCellEditable (int row, int column) {
		return column == 0;
	}
	@Override
	public void setValueAt (Object object, int row, int column) {
		this.update(row, new InfosModelPropriete((String)object, this.get(row).getType(), this.get(row).isObligatoire()));
	}

}
