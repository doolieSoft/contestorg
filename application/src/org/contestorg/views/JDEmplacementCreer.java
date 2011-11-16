package org.contestorg.views;


import java.awt.Window;

import org.contestorg.infos.InfosModelEmplacement;
import org.contestorg.interfaces.ICollector;

@SuppressWarnings("serial")
public class JDEmplacementCreer extends JDEmplacementAbstract
{

	public JDEmplacementCreer(Window w_parent, ICollector<InfosModelEmplacement> collector) {
		// Appeller le constructeur du parent
		super(w_parent, "Ajouter un emplacement", collector);
	}

}
