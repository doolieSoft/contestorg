package views;

import infos.InfosModelPoule;
import interfaces.ICollector;

import java.awt.Window;
import java.util.ArrayList;

import common.Pair;

@SuppressWarnings("serial")
public class JDPouleCreer extends JDPouleAbstract
{

	// Constructeur
	public JDPouleCreer(Window w_parent, ICollector<Pair<InfosModelPoule, ArrayList<String>>> collector) {
		// Appeller le constructeur du parent
		super(w_parent, "Ajouter une poule", collector);
	}
	
}
