package views;

import infos.InfosModelCompPhasesQualifsAbstract;
import infos.InfosModelCompPhasesQualifsObjectif;
import infos.InfosModelCompPhasesQualifsPoints;
import infos.InfosModelCompPhasesQualifsVictoires;
import infos.InfosModelObjectif;
import interfaces.ICollector;

import java.awt.Window;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class JDComparateurEditer extends JDComparateurAbstract
{

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
