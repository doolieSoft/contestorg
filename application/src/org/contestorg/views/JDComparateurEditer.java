package org.contestorg.views;

import java.awt.Window;
import java.util.ArrayList;

import org.contestorg.infos.InfosModelCompPhasesQualifsAbstract;
import org.contestorg.infos.InfosModelCompPhasesQualifsObjectif;
import org.contestorg.infos.InfosModelCompPhasesQualifsPoints;
import org.contestorg.infos.InfosModelCompPhasesQualifsVictoires;
import org.contestorg.infos.InfosModelObjectif;
import org.contestorg.interfaces.ICollector;

/**
 * Boîte de dialogue d'édition d'un comparateur en vue de générer les phases qualificatives
 */
@SuppressWarnings("serial")
public class JDComparateurEditer extends JDComparateurAbstract
{

	/**
	 * Constructeur
	 * @param w_parent fenêtre parent
	 * @param collector collecteur des informations du comparateur
	 * @param objectifs liste des objectifs
	 * @param infos informations du comparateur
	 */
	public JDComparateurEditer(Window w_parent, ICollector<InfosModelCompPhasesQualifsAbstract> collector, ArrayList<InfosModelObjectif> objectifs, InfosModelCompPhasesQualifsAbstract infos) {
		// Appeller le constructeur du parent
		super(w_parent, "Editer un critère", collector, objectifs);
		
		// Remplir les champs avec les données du comparateur
		if(infos instanceof InfosModelCompPhasesQualifsPoints) {
			// Séléctionner le bon item dans la liste des types
			this.jcb_types.setSelectedItem(JDComparateurAbstract.LABEL_COMPARATEUR_POINTS);
		} else if(infos instanceof InfosModelCompPhasesQualifsVictoires) {
			// Séléctionner le bon item dans la liste des types
			this.jcb_types.setSelectedItem(JDComparateurAbstract.LABEL_COMPARATEUR_VICTOIRES);
		} else if(infos instanceof InfosModelCompPhasesQualifsObjectif) {
			// Séléctionner le bon item dans la liste des types
			this.jcb_types.setSelectedItem(JDComparateurAbstract.LABEL_COMPARATEUR_OBJECTIF);

			// Séléctionner le bon item dans la liste des objectifs
			this.jcb_objectifs.setSelectedIndex(objectifs.indexOf(((InfosModelCompPhasesQualifsObjectif)infos).getObjectif()));
		}
	}

}
