package org.contestorg.views;

import java.awt.Window;

import org.contestorg.infos.InfosModelPrix;
import org.contestorg.interfaces.ICollector;

/**
 * Boîte de dialogue de création d'un prix 
 */
@SuppressWarnings("serial")
public class JDPrixCreer extends JDPrixAbstract
{

	/**
	 * Constructeur
	 * @param w_parent fenêtre parent
	 * @param collector collecteur des informations du prix
	 */
	public JDPrixCreer(Window w_parent, ICollector<InfosModelPrix> collector) {
		// Appeller le constructeur du parent
		super(w_parent, "Ajouter un prix", collector);
	}

}
