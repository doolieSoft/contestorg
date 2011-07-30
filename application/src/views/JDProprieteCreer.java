package views;

import infos.InfosModelPropriete;
import interfaces.ICollector;

import java.awt.Window;

@SuppressWarnings("serial")
public class JDProprieteCreer extends JDProprieteAbstract
{

	// Constructeur
	public JDProprieteCreer(Window w_parent, ICollector<InfosModelPropriete> collector) {
		// Appeller le constructeur du parent
		super(w_parent, "Ajouter une propriété", collector);
	}

}
