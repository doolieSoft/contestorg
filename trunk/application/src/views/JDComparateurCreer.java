package views;

import infos.InfosModelCompPhasesQualifsAbstract;
import infos.InfosModelObjectif;
import interfaces.ICollector;

import java.awt.Window;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class JDComparateurCreer extends JDComparateurAbstract
{

	// Constructeur
	public JDComparateurCreer(Window w_parent, ICollector<InfosModelCompPhasesQualifsAbstract> collector, ArrayList<InfosModelObjectif> objectifs) {
		// Appeller le constructeur du parent
		super(w_parent, "Ajouter un critère", collector, objectifs);
	}

}
