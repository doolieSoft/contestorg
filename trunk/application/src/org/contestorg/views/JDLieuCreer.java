package org.contestorg.views;


import java.awt.Window;

import org.contestorg.common.TrackableList;
import org.contestorg.common.Triple;
import org.contestorg.infos.InfosModelEmplacement;
import org.contestorg.infos.InfosModelHoraire;
import org.contestorg.infos.InfosModelLieu;
import org.contestorg.interfaces.ICollector;



@SuppressWarnings("serial")
public class JDLieuCreer extends JDLieuAbstract
{

	// Constructeur
	public JDLieuCreer(Window w_parent, ICollector<Triple<InfosModelLieu,TrackableList<InfosModelEmplacement>,TrackableList<InfosModelHoraire>>> collector) {
		// Appeller le constructeur du parent
		super(w_parent, "Ajouter un lieu", collector);
	}

}
