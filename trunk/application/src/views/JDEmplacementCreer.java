package views;

import infos.InfosModelEmplacement;
import interfaces.ICollector;

import java.awt.Window;

@SuppressWarnings("serial")
public class JDEmplacementCreer extends JDEmplacementAbstract
{

	public JDEmplacementCreer(Window w_parent, ICollector<InfosModelEmplacement> collector) {
		// Appeller le constructeur du parent
		super(w_parent, "Ajouter un emplacement", collector);
	}

}
