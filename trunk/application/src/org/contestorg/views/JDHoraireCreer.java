package org.contestorg.views;

import java.awt.Window;

import org.contestorg.infos.InfosModelHoraire;
import org.contestorg.interfaces.ICollector;

/**
 * Boîte de dialogue de création d'un horaire
 */
@SuppressWarnings("serial")
public class JDHoraireCreer extends JDHoraireAbstract
{

	/**
	 * Constructeur
	 * @param w_parent fenêtre parent
	 * @param collector collecteur des informations de l'horaire
	 */
	public JDHoraireCreer(Window w_parent, ICollector<InfosModelHoraire> collector) {
		// Appeller le constructeur du parent
		super(w_parent, "Ajouter un horaire", collector);
	}

}
