package org.contestorg.views;


import java.awt.Window;

import org.contestorg.infos.InfosModelPrix;
import org.contestorg.interfaces.ICollector;

@SuppressWarnings("serial")
public class JDPrixCreer extends JDPrixAbstract
{

	// Constructeur
	public JDPrixCreer(Window w_parent, ICollector<InfosModelPrix> collector) {
		// Appeller le constructeur du parent
		super(w_parent, "Ajouter un prix", collector);
	}

}
