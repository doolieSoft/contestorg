package views;

import infos.InfosModelChemin;
import infos.InfosModelExportation;
import infos.InfosModelTheme;
import interfaces.ICollector;

import java.awt.Window;

import common.Triple;

@SuppressWarnings("serial")
public class JDExportationCreer extends JDExportationAbstract
{

	// Constructeur
	public JDExportationCreer(Window w_parent, ICollector<Triple<InfosModelExportation,InfosModelChemin,InfosModelTheme>> collector) {
		// Appeller le constructeur du parent
		super(w_parent, "Ajouter une exportation", collector);
	}

}
