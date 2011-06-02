package views;

import infos.InfosModelPrix;
import interfaces.ICollector;

import java.awt.Window;

@SuppressWarnings("serial")
public class JDPrixCreer extends JDPrixAbstract
{

	// Constructeur
	public JDPrixCreer(Window w_parent, ICollector<InfosModelPrix> collector) {
		// Appeller le constructeur du parent
		super(w_parent, "Ajouter un prix", collector);
	}

}
