package org.contestorg.views;


import java.awt.Window;

import org.contestorg.common.Pair;
import org.contestorg.infos.InfosModelDiffusion;
import org.contestorg.infos.InfosModelTheme;
import org.contestorg.interfaces.ICollector;


@SuppressWarnings("serial")
public class JDDiffusionCreer extends JDDiffusionAbstract
{

	// Constructeur
	public JDDiffusionCreer(Window w_parent, ICollector<Pair<InfosModelDiffusion,InfosModelTheme>> collector) {
		// Appeller le constructeur du parent
		super(w_parent, "Ajouter une diffusion", collector);
	}

}
