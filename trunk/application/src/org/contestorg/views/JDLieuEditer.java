package org.contestorg.views;


import java.awt.Window;

import org.contestorg.common.TrackableList;
import org.contestorg.common.Triple;
import org.contestorg.infos.InfosModelEmplacement;
import org.contestorg.infos.InfosModelHoraire;
import org.contestorg.infos.InfosModelLieu;
import org.contestorg.interfaces.ICollector;



@SuppressWarnings("serial")
public class JDLieuEditer extends JDLieuAbstract
{

	// Constructeur
	public JDLieuEditer(Window w_parent, ICollector<Triple<InfosModelLieu,TrackableList<InfosModelEmplacement>,TrackableList<InfosModelHoraire>>> collector, Triple<InfosModelLieu,TrackableList<InfosModelEmplacement>,TrackableList<InfosModelHoraire>> infos) {
		// Appeller le constructeur du parent
		super(w_parent, "Editer un lieu", collector);
		
		// Remplir les champs avec les données du lieu
		this.jtf_lieu_nom.setText(infos.getFirst().getNom());
		this.jtf_lieu_lieu.setText(infos.getFirst().getLieu());
		this.jtf_lieu_telephone.setText(infos.getFirst().getTelephone());
		this.jtf_lieu_email.setText(infos.getFirst().getEmail());
		this.jta_lieu_description.setText(infos.getFirst().getDescription());
		
		// Remplir les TableModel des horaires et des emplacements
		this.tm_horaires.fill(infos.getThird());
		this.tm_emplacements.fill(infos.getSecond());
	}

}
