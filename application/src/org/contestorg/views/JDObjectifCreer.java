package org.contestorg.views;


import java.awt.Window;

import org.contestorg.infos.InfosModelObjectif;
import org.contestorg.interfaces.ICollector;

@SuppressWarnings("serial")
public class JDObjectifCreer extends JDObjectifAbstract
{
	// Constructeur
	public JDObjectifCreer(Window w_parent, ICollector<InfosModelObjectif> collector) {
		// Appeller le constructeur du parent
		super(w_parent, "Ajouter un objectif", collector);
	}
}
