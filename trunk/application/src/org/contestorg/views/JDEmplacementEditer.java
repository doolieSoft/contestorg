package org.contestorg.views;

import java.awt.Window;

import org.contestorg.infos.InfosModelEmplacement;
import org.contestorg.interfaces.ICollector;

/**
 * Boîte de dialogue d'édition d'un emplacement
 */
@SuppressWarnings("serial")
public class JDEmplacementEditer extends JDEmplacementAbstract
{

	/**
	 * Constructeur
	 * @param w_parent fenêtre parent
	 * @param collector collecteur des informations de l'emplacement
	 * @param infos informations de l'emplacement
	 */
	public JDEmplacementEditer(Window w_parent, ICollector<InfosModelEmplacement> collector, InfosModelEmplacement infos) {
		// Appeller le constructeur du parent
		super(w_parent, "Editer un emplacement", collector);
		
		// Remplir les entrées avec les données de l'emplacement
		this.jtf_nom.setText(infos.getNom());
		this.jta_description.setText(infos.getDescription());
	}

}
