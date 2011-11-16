package org.contestorg.views;


import java.awt.Window;
import java.util.ArrayList;

import org.contestorg.infos.InfosModelCompPhasesQualifsAbstract;
import org.contestorg.infos.InfosModelObjectif;
import org.contestorg.interfaces.ICollector;

@SuppressWarnings("serial")
public class JDComparateurCreer extends JDComparateurAbstract
{

	// Constructeur
	public JDComparateurCreer(Window w_parent, ICollector<InfosModelCompPhasesQualifsAbstract> collector, ArrayList<InfosModelObjectif> objectifs) {
		// Appeller le constructeur du parent
		super(w_parent, "Ajouter un critère", collector, objectifs);
	}

}
