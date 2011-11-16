package org.contestorg.views;


import java.awt.Window;

import org.contestorg.common.TrackableList;
import org.contestorg.common.Triple;
import org.contestorg.infos.InfosModelEmplacement;
import org.contestorg.infos.InfosModelHoraire;
import org.contestorg.infos.InfosModelLieu;


public class TMLieux extends TMAbstract<Triple<InfosModelLieu,TrackableList<InfosModelEmplacement>,TrackableList<InfosModelHoraire>>>
{

	// Constructeur
	public TMLieux(Window w_parent) {
		// Appeller le constructeur du parent
		super(w_parent);
	}
	
	// Méthodes à implémenter
	@Override
	public Window getAddWindow () {
		return new JDLieuCreer(this.w_parent, this);
	}
	@Override
	public Window getUpdateWindow (Triple<InfosModelLieu,TrackableList<InfosModelEmplacement>,TrackableList<InfosModelHoraire>> infos) {
		return new JDLieuEditer(this.w_parent, this, infos);
	}
	@Override
	public boolean acceptDelete (Triple<InfosModelLieu,TrackableList<InfosModelEmplacement>,TrackableList<InfosModelHoraire>> infos) {
		// Demander la confirmation à l'utilisateur
		if (ViewHelper.confirmation(this.w_parent, "Désirez-vous vraiment supprimer le lieu \"" + infos.getFirst().getNom() + "\" ?")) {
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
			case 0 : return "Nom";
			case 1 : return "Emplacements";
		}
		return null;
	}
	@Override
	public Object getValueAt (int row, int column) {
		// Retourner la donnée demandée
		switch(column) {
			case 0 : return this.get(row).getFirst().getNom();
			case 1 :
				StringBuilder emplacements = new StringBuilder();
				for(InfosModelEmplacement emplacement : this.get(row).getSecond()) {
					if(emplacements.length() != 0) {
						emplacements.append(", ");
					}
					emplacements.append(emplacement.getNom());
				}
				return emplacements.toString();
		}
		return null;
	}
	@Override
	public boolean isCellEditable (int row, int column) {
		return column == 0;
	}
	@Override
	public void setValueAt (Object object, int row, int column) {
		// Mettre à jour le nom du lieu
		this.update(row, new Triple<InfosModelLieu,TrackableList<InfosModelEmplacement>,TrackableList<InfosModelHoraire>>(
				new InfosModelLieu(
					(String)object,
					this.get(row).getFirst().getLieu(),
					this.get(row).getFirst().getTelephone(),
					this.get(row).getFirst().getEmail(),
					this.get(row).getFirst().getDescription()),
				this.get(row).getSecond(),
				this.get(row).getThird()
			)
		);
	}

}
