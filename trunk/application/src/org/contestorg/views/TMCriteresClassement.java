package org.contestorg.views;

import java.awt.Window;
import java.util.ArrayList;

import javax.swing.table.TableModel;

import org.contestorg.infos.InfosModelCritereClassementAbstract;
import org.contestorg.infos.InfosModelCritereClassementGoalAverage;
import org.contestorg.infos.InfosModelCritereClassementNbDefaites;
import org.contestorg.infos.InfosModelCritereClassementNbEgalites;
import org.contestorg.infos.InfosModelCritereClassementNbForfaits;
import org.contestorg.infos.InfosModelCritereClassementNbPoints;
import org.contestorg.infos.InfosModelCritereClassementNbVictoires;
import org.contestorg.infos.InfosModelCritereClassementQuantiteObjectif;
import org.contestorg.infos.InfosModelCritereClassementRencontresDirectes;
import org.contestorg.infos.InfosModelObjectif;
import org.contestorg.interfaces.IFournisseur;

/**
 * Modèle de données pour un tableau de critères de classement
 */
public class TMCriteresClassement extends TMAbstract<InfosModelCritereClassementAbstract>
{
	
	/** Fournisseur des objectifs */
	IFournisseur<ArrayList<InfosModelObjectif>> fournisseur;

	/**
	 * Constructeur
	 * @param w_parent fenêtre parent
	 * @param fournisseur fournisseur d'objectifs
	 */
	public TMCriteresClassement(Window w_parent, IFournisseur<ArrayList<InfosModelObjectif>> fournisseur) {
		// Appeller le constructeur du parent
		super(w_parent);
		
		// Retenir le fournisseur
		this.fournisseur = fournisseur;
	}
	
	/**
	 * @see TMAbstract#getAddWindow()
	 */
	@Override
	public Window getAddWindow () {
		return new JDCritereClassementCreer(this.w_parent, this, fournisseur.get());
	}
	
	/**
	 * @see TMAbstract#getUpdateWindow(Object)
	 */
	@Override
	public Window getUpdateWindow (InfosModelCritereClassementAbstract infos) {
		return new JDCritereClassementEditer(this.w_parent, this, fournisseur.get(), infos);
	}
	
	/**
	 * @see TMAbstract#acceptDelete(Object)
	 */
	@Override
	public boolean acceptDelete (InfosModelCritereClassementAbstract infos) {
		// Demander la confirmation à l'utilisateur
		if (ViewHelper.confirmation(this.w_parent, "Désirez-vous vraiment supprimer le critère de classement \""+this.getNom(infos)+"\" ?")) {
			return true;
		} else {
			return false;
		}
	}

	// Implémentation manquante de TableModel
	
	/**
	 * @see TableModel#getColumnClass(int)
	 */
	@Override
	public Class<?> getColumnClass (int column) {
		return String.class;
	}
	
	/**
	 * @see TableModel#getColumnCount()
	 */
	@Override
	public int getColumnCount () {
		return 1;
	}
	
	/**
	 * @see TableModel#getColumnName(int)
	 */
	@Override
	public String getColumnName (int column) {
		return "Critère";
	}
	
	/**
	 * @see TableModel#getValueAt(int, int)
	 */
	@Override
	public Object getValueAt (int row, int column) {
		// Retourner l'information demandée
		return this.getNom(this.get(row));
	}
	
	/**
	 * @see TableModel#isCellEditable(int, int)
	 */
	@Override
	public boolean isCellEditable (int row, int column) {
		return false;
	}
	
	/**
	 * @see TableModel#setValueAt(Object, int, int)
	 */
	@Override
	public void setValueAt (Object object, int row, int column) {
		
	}
	
	/**
	 * Récupérer le nom d'un critère de classement
	 * @param infos informations du critère de classement
	 * @return nom du critère de classement
	 */
	private String getNom(InfosModelCritereClassementAbstract infos) {
		if(infos instanceof InfosModelCritereClassementNbPoints) {
				return "Nombre de points"+(infos.isInverse() ? " (critère inversé)" : "");
		}
		if(infos instanceof InfosModelCritereClassementNbVictoires) {
			return "Nombre de victoires"+(infos.isInverse() ? " (critère inversé)" : "");
		}
		if(infos instanceof InfosModelCritereClassementNbEgalites) {
				return "Nombre d'égalités"+(infos.isInverse() ? " (critère inversé)" : "");
		}
		if(infos instanceof InfosModelCritereClassementNbDefaites) {
			return "Nombre de défaites"+(infos.isInverse() ? " (critère inversé)" : "");
		}
		if(infos instanceof InfosModelCritereClassementNbForfaits) {
			return "Nombre de forfaits"+(infos.isInverse() ? " (critère inversé)" : "");
		}
		if(infos instanceof InfosModelCritereClassementRencontresDirectes) {
			return "Résultat des rencontres directes"+(infos.isInverse() ? " (critère inversé)" : "");
		}
		if(infos instanceof InfosModelCritereClassementQuantiteObjectif) {
			return "Quantité remportée de l'objectif \""+((InfosModelCritereClassementQuantiteObjectif)infos).getObjectif().getNom()+"\""+(infos.isInverse() ? " (critère inversé)" : "");
		}
		if(infos instanceof InfosModelCritereClassementGoalAverage) {
			StringBuilder details = new StringBuilder("Goal-average ");
			switch(((InfosModelCritereClassementGoalAverage)infos).getType()) {
				case InfosModelCritereClassementGoalAverage.TYPE_GENERAL:
					details.append("général ");
					break;
				case InfosModelCritereClassementGoalAverage.TYPE_PARTICULIER:
					details.append("particulier ");
					break;
			}
			if(infos.isInverse()) {
				details.append("inversé ");
			}
			switch(((InfosModelCritereClassementGoalAverage)infos).getMethode()) {
				case InfosModelCritereClassementGoalAverage.METHODE_DIFFERENCE:
					details.append("par différence ");
					break;
				case InfosModelCritereClassementGoalAverage.METHODE_DIVISION:
					details.append("par division ");
					break;
			}
			details.append("(");
			switch(((InfosModelCritereClassementGoalAverage)infos).getDonnee()) {
				case InfosModelCritereClassementGoalAverage.DONNEE_POINTS:
					details.append("nombre de points");
					break;
				case InfosModelCritereClassementGoalAverage.DONNEE_RESULTAT:
					details.append("nombre de victoires");
					break;
				case InfosModelCritereClassementGoalAverage.DONNEE_QUANTITE_OBJECTIF:
					details.append("quantité remportée de l'objectif \""+((InfosModelCritereClassementGoalAverage)infos).getObjectif().getNom()+"\"");
					break;
			}
			details.append(")");
			return details.toString();
		}
		return null;
	}
}
