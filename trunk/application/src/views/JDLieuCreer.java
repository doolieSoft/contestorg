package views;

import infos.InfosModelEmplacement;
import infos.InfosModelHoraire;
import infos.InfosModelLieu;
import interfaces.ICollector;

import java.awt.Window;

import common.TrackableList;
import common.Triple;


@SuppressWarnings("serial")
public class JDLieuCreer extends JDLieuAbstract
{

	// Constructeur
	public JDLieuCreer(Window w_parent, ICollector<Triple<InfosModelLieu,TrackableList<InfosModelEmplacement>,TrackableList<InfosModelHoraire>>> collector) {
		// Appeller le constructeur du parent
		super(w_parent, "Ajouter un lieu", collector);
	}

}
