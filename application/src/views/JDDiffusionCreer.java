package views;

import infos.InfosModelDiffusion;
import infos.InfosModelTheme;
import interfaces.ICollector;

import java.awt.Window;

import common.Pair;

@SuppressWarnings("serial")
public class JDDiffusionCreer extends JDDiffusionAbstract
{

	// Constructeur
	public JDDiffusionCreer(Window w_parent, ICollector<Pair<InfosModelDiffusion,InfosModelTheme>> collector) {
		// Appeller le constructeur du parent
		super(w_parent, "Ajouter une diffusion", collector);
	}

}
