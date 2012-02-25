package org.contestorg.views;

import java.awt.Window;

import org.contestorg.infos.InfosModelEmplacement;
import org.contestorg.interfaces.ICollector;

/**
 * Boîte de dialogue de création d'emplacement
 */
@SuppressWarnings("serial")
public class JDEmplacementCreer extends JDEmplacementAbstract
{

	/**
	 * Constructeur
	 * @param w_parent fenêtre parent
	 * @param collector collecteur des informations de l'emplacement
	 */
	public JDEmplacementCreer(Window w_parent, ICollector<InfosModelEmplacement> collector) {
		// Appeller le constructeur du parent
		super(w_parent, "Ajouter un emplacement", collector);
	}

}
