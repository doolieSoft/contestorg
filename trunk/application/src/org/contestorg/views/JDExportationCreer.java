package org.contestorg.views;


import java.awt.Window;

import org.contestorg.common.Triple;
import org.contestorg.infos.InfosModelChemin;
import org.contestorg.infos.InfosModelExportation;
import org.contestorg.infos.InfosModelTheme;
import org.contestorg.interfaces.ICollector;


@SuppressWarnings("serial")
public class JDExportationCreer extends JDExportationAbstract
{

	// Constructeur
	public JDExportationCreer(Window w_parent, ICollector<Triple<InfosModelExportation,InfosModelChemin,InfosModelTheme>> collector) {
		// Appeller le constructeur du parent
		super(w_parent, "Ajouter une exportation", collector);
	}

}
