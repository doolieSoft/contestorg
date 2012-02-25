package org.contestorg.views;

import java.awt.Window;

import org.contestorg.infos.InfosModelObjectif;
import org.contestorg.interfaces.ICollector;

/**
 * Boîte de dialogue de création d'un objectif
 */
@SuppressWarnings("serial")
public class JDObjectifCreer extends JDObjectifAbstract
{
	/**
	 * Constructeur
	 * @param w_parent fenêtre parent
	 * @param collector collecteur des informations de l'objectif
	 */
	public JDObjectifCreer(Window w_parent, ICollector<InfosModelObjectif> collector) {
		// Appeller le constructeur du parent
		super(w_parent, "Ajouter un objectif", collector);
	}
}
