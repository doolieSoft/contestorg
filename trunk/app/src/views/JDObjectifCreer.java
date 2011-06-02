package views;

import infos.InfosModelObjectif;
import interfaces.ICollector;

import java.awt.Window;

@SuppressWarnings("serial")
public class JDObjectifCreer extends JDObjectifAbstract
{
	// Constructeur
	public JDObjectifCreer(Window w_parent, ICollector<InfosModelObjectif> collector) {
		// Appeller le constructeur du parent
		super(w_parent, "Ajouter un objectif", collector);
	}
}
