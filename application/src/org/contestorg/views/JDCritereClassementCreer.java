package org.contestorg.views;

import java.awt.Window;
import java.util.ArrayList;

import org.contestorg.infos.InfosModelCritereClassementAbstract;
import org.contestorg.infos.InfosModelObjectif;
import org.contestorg.interfaces.ICollector;

/**
 * Boîte de dialogue de création d'un critère de classement
 */
@SuppressWarnings("serial")
public class JDCritereClassementCreer extends JDCritereClassementAbstract
{

	/**
	 * Constructeur
	 * @param w_parent fenêtre parent
	 * @param collector collecteur des informations du critère de classement
	 * @param objectifs liste des objectifs
	 */
	public JDCritereClassementCreer(Window w_parent, ICollector<InfosModelCritereClassementAbstract> collector, ArrayList<InfosModelObjectif> objectifs) {
		// Appeller le constructeur du parent
		super(w_parent, "Ajouter un critère", collector, objectifs);
		
		// Données par défaut
		this.jrb_goalaverage_type_general.setSelected(true);
		this.jrb_goalaverage_donnee_points.setSelected(true);
		this.jrb_goalaverage_methode_difference.setSelected(true);
	}

}
