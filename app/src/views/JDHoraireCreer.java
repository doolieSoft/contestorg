package views;

import infos.InfosModelHoraire;
import interfaces.ICollector;

import java.awt.Window;

@SuppressWarnings("serial")
public class JDHoraireCreer extends JDHoraireAbstract
{

	// Constructeur
	public JDHoraireCreer(Window w_parent, ICollector<InfosModelHoraire> collector) {
		// Appeller le constructeur du parent
		super(w_parent, "Ajouter un horaire", collector);
	}

}
