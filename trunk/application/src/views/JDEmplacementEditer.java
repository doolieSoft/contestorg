package views;

import infos.InfosModelEmplacement;
import interfaces.ICollector;

import java.awt.Window;

@SuppressWarnings("serial")
public class JDEmplacementEditer extends JDEmplacementAbstract
{

	// Constructeur
	public JDEmplacementEditer(Window w_parent, ICollector<InfosModelEmplacement> collector, InfosModelEmplacement infos) {
		// Appeller le constructeur du parent
		super(w_parent, "Editer un emplacement", collector);
		
		// Remplir les entrées avec les données de l'emplacement
		this.jtf_nom.setText(infos.getNom());
		this.jta_description.setText(infos.getDescription());
	}

}
