package views;

import infos.InfosModelPoule;
import interfaces.ICollector;

import java.awt.Window;
import java.util.ArrayList;

import common.Pair;

@SuppressWarnings("serial")
public class JDPouleEditer extends JDPouleAbstract
{

	// Constructeur
	public JDPouleEditer(Window w_parent, ICollector<Pair<InfosModelPoule, ArrayList<String>>> collector, Pair<InfosModelPoule, ArrayList<String>> infos) {
		// Appeller le constucteur du parent
		super(w_parent, "Editer une poule", collector);
		
		// Remplir les champs avec les informations de la poule
		this.jtf_nom.setText(infos.getFirst().getNom());
	}
	
}
