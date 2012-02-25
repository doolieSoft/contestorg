package org.contestorg.views;

import java.awt.Window;
import java.util.ArrayList;

import org.contestorg.infos.InfosModelCompPhasesQualifsAbstract;
import org.contestorg.infos.InfosModelObjectif;
import org.contestorg.interfaces.ICollector;

/**
 * Boîte de dialogue de création d'un comparateur en vue de générer les phases qualificatives
 */
@SuppressWarnings("serial")
public class JDComparateurCreer extends JDComparateurAbstract
{

	/**
	 * Constructeur
	 * @param w_parent fenêtre parent
	 * @param collector collecteur des informations du comparateur
	 * @param objectifs liste des objectifs
	 */
	public JDComparateurCreer(Window w_parent, ICollector<InfosModelCompPhasesQualifsAbstract> collector, ArrayList<InfosModelObjectif> objectifs) {
		// Appeller le constructeur du parent
		super(w_parent, "Ajouter un critère", collector, objectifs);
	}

}
