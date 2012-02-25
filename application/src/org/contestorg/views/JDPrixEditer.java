package org.contestorg.views;

import java.awt.Window;

import org.contestorg.infos.InfosModelPrix;
import org.contestorg.interfaces.ICollector;

/**
 * Boîte de dialogue d'édition d'un prix
 */
@SuppressWarnings("serial")
public class JDPrixEditer extends JDPrixAbstract
{

	/**
	 * Constructeur
	 * @param w_parent fenêtre parent
	 * @param collector collecteur des informations du prix
	 * @param infos information du prix
	 */
	public JDPrixEditer(Window w_parent, ICollector<InfosModelPrix> collector, InfosModelPrix infos) {
		// Appeller le constructeur du parent
		super(w_parent, "Editer un prix", collector);
		
		// Remplir les champs avec les données du prix
		this.jtf_nom.setText(infos.getNom());
	}

}
