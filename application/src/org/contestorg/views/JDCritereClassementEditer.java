package org.contestorg.views;

import java.awt.Window;
import java.util.ArrayList;

import org.contestorg.infos.InfosModelCritereClassementAbstract;
import org.contestorg.infos.InfosModelCritereClassementGoalAverage;
import org.contestorg.infos.InfosModelCritereClassementNbDefaites;
import org.contestorg.infos.InfosModelCritereClassementNbEgalites;
import org.contestorg.infos.InfosModelCritereClassementNbPoints;
import org.contestorg.infos.InfosModelCritereClassementNbVictoires;
import org.contestorg.infos.InfosModelCritereClassementQuantiteObjectif;
import org.contestorg.infos.InfosModelCritereClassementRencontresDirectes;
import org.contestorg.infos.InfosModelObjectif;
import org.contestorg.interfaces.ICollector;

/**
 * Boîte de dialogue d'édition d'un critère de classement
 */
@SuppressWarnings("serial")
public class JDCritereClassementEditer extends JDCritereClassementAbstract
{

	/**
	 * Constructeur
	 * @param w_parent fenêtre parent
	 * @param collector collecteur des informations du critère de classement
	 * @param objectifs liste des objectifs
	 * @param infos informations du critère de classement
	 */
	public JDCritereClassementEditer(Window w_parent, ICollector<InfosModelCritereClassementAbstract> collector, ArrayList<InfosModelObjectif> objectifs, InfosModelCritereClassementAbstract infos) {
		// Appeller le constructeur du parent
		super(w_parent, "Editer un critère", collector, objectifs);
		
		// Remplir les champs avec les données du critère de classement
		if(infos instanceof InfosModelCritereClassementNbPoints) {
			this.jcb_types.setSelectedItem(JDCritereClassementAbstract.LABEL_CRITERE_CLASSEMENT_NB_POINTS);
		} else if(infos instanceof InfosModelCritereClassementNbVictoires) {
			this.jcb_types.setSelectedItem(JDCritereClassementAbstract.LABEL_CRITERE_CLASSEMENT_NB_VICTOIRES);
		} else if(infos instanceof InfosModelCritereClassementNbEgalites) {
			this.jcb_types.setSelectedItem(JDCritereClassementAbstract.LABEL_CRITERE_CLASSEMENT_NB_EGALITES);
		} else if(infos instanceof InfosModelCritereClassementNbDefaites) {
			this.jcb_types.setSelectedItem(JDCritereClassementAbstract.LABEL_CRITERE_CLASSEMENT_NB_DEFAITES);
		} else if(infos instanceof InfosModelCritereClassementRencontresDirectes) {
			this.jcb_types.setSelectedItem(JDCritereClassementAbstract.LABEL_CRITERE_CLASSEMENT_RENCONTRE_DIRECTES);
		} else if(infos instanceof InfosModelCritereClassementQuantiteObjectif) {
			this.jcb_types.setSelectedItem(JDCritereClassementAbstract.LABEL_CRITERE_CLASSEMENT_QUANTITE_OBJECTIF);
			String nom = ((InfosModelCritereClassementQuantiteObjectif)infos).getObjectif().getNom();
			for(int index=0;index<objectifs.size();index++) {
				if(objectifs.get(index).getNom().equals(nom)) {
					this.jcb_quantiteobjectif_objectifs.setSelectedIndex(index);
					break;
				}
			}
		} else if(infos instanceof InfosModelCritereClassementGoalAverage) {
			this.jcb_types.setSelectedItem(JDCritereClassementAbstract.LABEL_CRITERE_CLASSEMENT_GOALAVERAGE);
			switch(((InfosModelCritereClassementGoalAverage)infos).getType()) {
				case InfosModelCritereClassementGoalAverage.TYPE_GENERAL:
					this.jrb_goalaverage_type_general.setSelected(true);
					break;
				case InfosModelCritereClassementGoalAverage.TYPE_PARTICULIER:
					this.jrb_goalaverage_type_particulier.setSelected(true);
					break;
			}
			switch(((InfosModelCritereClassementGoalAverage)infos).getMethode()) {
				case InfosModelCritereClassementGoalAverage.METHODE_DIFFERENCE:
					this.jrb_goalaverage_methode_difference.setSelected(true);
					break;
				case InfosModelCritereClassementGoalAverage.METHODE_DIVISION:
					this.jrb_goalaverage_methode_division.setSelected(true);
					break;
			}
			switch(((InfosModelCritereClassementGoalAverage)infos).getDonnee()) {
				case InfosModelCritereClassementGoalAverage.DONNEE_POINTS:
					this.jrb_goalaverage_donnee_points.setSelected(true);
					break;
				case InfosModelCritereClassementGoalAverage.DONNEE_RESULTAT:
					this.jrb_goalaverage_donnee_resultat.setSelected(true);
					break;
				case InfosModelCritereClassementGoalAverage.DONNEE_QUANTITE_OBJECTIF:
					String nom = ((InfosModelCritereClassementGoalAverage)infos).getObjectif().getNom();
					this.jrb_goalaverage_donnee_quantiteobjectif.setSelected(true);
					for(int index=0;index<objectifs.size();index++) {
						if(objectifs.get(index).getNom().equals(nom)) {
							this.jcb_goalaverage_objectifs.setSelectedIndex(index);
							break;
						}
					}
					break;
			}
		}
		this.jcb_isInverse.setSelected(infos.isInverse());
	}

}
