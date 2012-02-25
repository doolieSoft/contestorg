package org.contestorg.views;

import java.awt.Window;

import org.contestorg.infos.InfosModelPropriete;
import org.contestorg.interfaces.ICollector;

/**
 * Boîte de dialogue de création de la propriété
 */
@SuppressWarnings("serial")
public class JDProprieteCreer extends JDProprieteAbstract
{

	/**
	 * Constructeur
	 * @param w_parent fenêtre parent
	 * @param collector collecteur des informations de la propriété
	 */
	public JDProprieteCreer(Window w_parent, ICollector<InfosModelPropriete> collector) {
		// Appeller le constructeur du parent
		super(w_parent, "Ajouter une propriété", collector);
	}

}
