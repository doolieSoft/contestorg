package org.contestorg.views;

import java.awt.Window;
import java.util.ArrayList;

import org.contestorg.common.Pair;
import org.contestorg.infos.InfosModelPoule;
import org.contestorg.interfaces.ICollector;

/**
 * Boîte de dialogue de création de poule
 */
@SuppressWarnings("serial")
public class JDPouleCreer extends JDPouleAbstract
{

	/**
	 * Constructeur
	 * @param w_parent fenêtre parent
	 * @param collector collecteur des informations de la poule
	 */
	public JDPouleCreer(Window w_parent, ICollector<Pair<InfosModelPoule, ArrayList<String>>> collector) {
		// Appeller le constructeur du parent
		super(w_parent, "Ajouter une poule", collector);
	}
	
}
