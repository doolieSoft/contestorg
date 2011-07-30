package views;

import infos.InfosModelCompPhasesQualifsAbstract;
import infos.InfosModelCompPhasesQualifsObjectif;
import infos.InfosModelCompPhasesQualifsPoints;
import infos.InfosModelCompPhasesQualifsVictoires;
import infos.InfosModelObjectif;
import interfaces.IFournisseur;

import java.awt.Window;
import java.util.ArrayList;

public class TMComparateurs extends TMAbstract<InfosModelCompPhasesQualifsAbstract>
{
	
	// Fournisseur des objectifs
	IFournisseur<ArrayList<InfosModelObjectif>> fournisseur;

	// Constructeur
	public TMComparateurs(Window w_parent, IFournisseur<ArrayList<InfosModelObjectif>> fournisseur) {
		// Appeller le constructeur du parent
		super(w_parent);
		
		// Retenir le fournisseur
		this.fournisseur = fournisseur;
	}
	
	// Méthodes à implémenter
	@Override
	public Window getAddWindow () {
		return new JDComparateurCreer(this.w_parent, this, fournisseur.get());
	}
	@Override
	public Window getUpdateWindow (InfosModelCompPhasesQualifsAbstract infos) {
		return new JDComparateurEditer(this.w_parent, this, fournisseur.get(), infos);
	}
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
	@Override
	public Class<?> getColumnClass (int column) {
		return String.class;
	}
	@Override
	public int getColumnCount () {
		return 1;
	}
	@Override
	public String getColumnName (int column) {
		return "Critère";
	}
	@Override
	public Object getValueAt (int row, int column) {
		// Retourner l'information demandée
		return this.getNom(this.get(row));
	}
	@Override
	public boolean isCellEditable (int row, int column) {
		return false;
	}
	@Override
	public void setValueAt (Object object, int row, int column) {
		
	}
	
	// Nom d'un comparateur
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
