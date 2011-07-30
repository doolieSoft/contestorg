package views;

import infos.InfosModelPrix;
import interfaces.ICollector;

import java.awt.Window;

@SuppressWarnings("serial")
public class JDPrixEditer extends JDPrixAbstract
{

	// Constructeur
	public JDPrixEditer(Window w_parent, ICollector<InfosModelPrix> collector, InfosModelPrix infos) {
		// Appeller le constructeur du parent
		super(w_parent, "Editer un prix", collector);
		
		// Remplir les champs avec les données du prix
		this.jtf_nom.setText(infos.getNom());
	}

}
