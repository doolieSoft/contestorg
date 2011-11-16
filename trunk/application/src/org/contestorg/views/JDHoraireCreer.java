package org.contestorg.views;


import java.awt.Window;

import org.contestorg.infos.InfosModelHoraire;
import org.contestorg.interfaces.ICollector;

@SuppressWarnings("serial")
public class JDHoraireCreer extends JDHoraireAbstract
{

	// Constructeur
	public JDHoraireCreer(Window w_parent, ICollector<InfosModelHoraire> collector) {
		// Appeller le constructeur du parent
		super(w_parent, "Ajouter un horaire", collector);
	}

}
