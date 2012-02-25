package org.contestorg.views;

import java.awt.Window;
import java.util.ArrayList;

import javax.swing.table.TableModel;

import org.contestorg.infos.InfosModelCompPhasesQualifsAbstract;
import org.contestorg.infos.InfosModelCompPhasesQualifsObjectif;
import org.contestorg.infos.InfosModelCompPhasesQualifsPoints;
import org.contestorg.infos.InfosModelCompPhasesQualifsVictoires;
import org.contestorg.infos.InfosModelObjectif;
import org.contestorg.interfaces.IFournisseur;

/**
 * Modèle de données pour un tableau de comparateurs
 */
public class TMComparateurs extends TMAbstract<InfosModelCompPhasesQualifsAbstract>
{
	
	/** Fournisseur des objectifs */
	IFournisseur<ArrayList<InfosModelObjectif>> fournisseur;

	/**
	 * Constructeur
	 * @param w_parent fenêtre parent
	 * @param fournisseur fournisseur des objectifs
	 */
	public TMComparateurs(Window w_parent, IFournisseur<ArrayList<InfosModelObjectif>> fournisseur) {
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
		return new JDComparateurCreer(this.w_parent, this, fournisseur.get());
	}
	
	/**
	 * @see TMAbstract#getUpdateWindow(Object)
	 */
	@Override
	public Window getUpdateWindow (InfosModelCompPhasesQualifsAbstract infos) {
		return new JDComparateurEditer(this.w_parent, this, fournisseur.get(), infos);
	}
	
	/**
	 * @see TMAbstract#acceptDelete(Object)
	 */
	@Override
	public boolean acceptDelete (InfosModelCompPhasesQualifsAbstract infos) {
		// Demander la confirmation à l'utilisateur
		if (ViewHelper.confirmation(this.w_parent, "Désirez-vous vraiment supprimer le comparateur \""+this.getNom(infos)+"\" ?")) {
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
	 * Récupérer le nom d'un comparateur
	 * @param infos informations du comparateur
	 * @return nom du comparateur
	 */
	private String getNom(InfosModelCompPhasesQualifsAbstract infos) {
		if(infos instanceof InfosModelCompPhasesQualifsObjectif) {
			return "Nombre d'objectifs remportés ("+((InfosModelCompPhasesQualifsObjectif)infos).getObjectif().getNom()+")";
		}
		if(infos instanceof InfosModelCompPhasesQualifsPoints) {
				return "Nombre de points";
		}
		if(infos instanceof InfosModelCompPhasesQualifsVictoires) {
				return "Nombre de victoires";
		}
		return null;
	}

}
