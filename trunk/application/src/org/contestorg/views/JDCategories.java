package org.contestorg.views;

import java.awt.Window;

import org.contestorg.common.TrackableList;
import org.contestorg.controllers.ContestOrg;
import org.contestorg.infos.InfosModelCategorie;

/**
 * Boîte de dialogue d'édition des catégories
 */
@SuppressWarnings("serial")
public class JDCategories extends JDPattern
{
	/** Fenêtre parent */
	private Window w_parent;
	
	/** TableModel de la liste des catégories */
	private TMCategories tm_categories;
	
	/**
	 * Constructeur
	 * @param w_parent fenêtre parent
	 */
	public JDCategories(Window w_parent) {
		// Appeller le constructeur du parent
		super(w_parent, "Gestion des catégories");
		
		// Retenir la fenêtre parent
		this.w_parent = w_parent;
		
		// Liste des catégories
		this.jp_contenu.add(ViewHelper.title("Liste des catégories", ViewHelper.H1));
		this.tm_categories = new TMCategories(this);
		this.tm_categories.fill(ContestOrg.get().getCtrlParticipants().getListeCategories());
		this.tm_categories.addValidator(ContestOrg.get().getCategoriesValidator());
		this.jp_contenu.add(new JPTable<InfosModelCategorie>(this.w_parent, this.tm_categories, true, true, true, true, true, 10));
		
		// Pack
		this.pack();
	}

	/**
	 * @see JDPattern#ok()
	 */
	@Override
	protected void ok () {
		// Vérifier qu'il n'y a au moins une catégorie
		if(this.tm_categories.size() == 0) {
			// Erreur
			ViewHelper.derror(this, "Il doit y avoir au moins une catégorie.");
		} else {
			// Demander la modification des catégories
			ContestOrg.get().getCtrlParticipants().updateCategories(new TrackableList<InfosModelCategorie>(this.tm_categories));
			
			// Fermer la fenêtre
			this.quit();
		}
	}
	
	/**
	 * @see JDPattern#quit()
	 */
	@Override
	protected void quit () {
		// Masquer la fenêtre
		this.setVisible(false);
	}
	
}
