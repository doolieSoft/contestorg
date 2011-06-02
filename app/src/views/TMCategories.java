package views;

import infos.InfosModelCategorie;
import infos.InfosModelConcours;

import java.awt.Window;

import controlers.ContestOrg;

public class TMCategories extends TMAbstract<InfosModelCategorie>
{

	// Constructeur
	public TMCategories(Window w_parent) {
		// Appeller le constructeur du parent
		super(w_parent);
	}
	
	// Méthodes à implémenter
	@Override
	public Window getAddWindow () {
		return new JDCategorieCreer(this.w_parent, this);
	}
	@Override
	public Window getUpdateWindow (InfosModelCategorie infos) {
		return new JDCategorieEditer(this.w_parent, this, infos);
	}
	/**
	 * @author Gaëlle
	 */
	@Override
	public boolean acceptDelete (InfosModelCategorie infos) {
		// Demander la confirmation à l'utilisateur
		if (ViewHelper.confirmation(this.w_parent, "En supprimant la catégorie \""+infos.getNom()+"\", vous supprimerez toutes les poules, "+(ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "équipes" : "joueurs")+" et matchs qu'elle contient. Désirez-vous continuer ?", true)) {
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
		return "Nom";
	}
	@Override
	public Object getValueAt (int row, int column) {
		return this.getModifies().get(row).getNom();
	}
	@Override
	public boolean isCellEditable (int row, int column) {
		return true;
	}
	@Override
	public void setValueAt (Object object, int row, int column) {
		this.update(row, new InfosModelCategorie((String)object));
	}
	
}
