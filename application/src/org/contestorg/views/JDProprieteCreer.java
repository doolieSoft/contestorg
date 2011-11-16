package org.contestorg.views;


import java.awt.Window;

import org.contestorg.infos.InfosModelPropriete;
import org.contestorg.interfaces.ICollector;

@SuppressWarnings("serial")
public class JDProprieteCreer extends JDProprieteAbstract
{

	// Constructeur
	public JDProprieteCreer(Window w_parent, ICollector<InfosModelPropriete> collector) {
		// Appeller le constructeur du parent
		super(w_parent, "Ajouter une propriété", collector);
	}

}
