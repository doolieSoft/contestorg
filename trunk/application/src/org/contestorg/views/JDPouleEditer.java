package org.contestorg.views;

import java.awt.Window;
import java.util.ArrayList;

import org.contestorg.common.Pair;
import org.contestorg.infos.InfosModelPoule;
import org.contestorg.interfaces.ICollector;

/**
 * Boîte de dialogue d'édition d'une poule
 */
@SuppressWarnings("serial")
public class JDPouleEditer extends JDPouleAbstract
{

	/**
	 * Constructeur
	 * @param w_parent fenêtre parent
	 * @param collector collecteur des informations de la poule
	 * @param infos informations de la poule
	 */
	public JDPouleEditer(Window w_parent, ICollector<Pair<InfosModelPoule, ArrayList<String>>> collector, Pair<InfosModelPoule, ArrayList<String>> infos) {
		// Appeller le constucteur du parent
		super(w_parent, "Editer une poule", collector);
		
		// Remplir les champs avec les informations de la poule
		this.jtf_nom.setText(infos.getFirst().getNom());
	}
	
}
