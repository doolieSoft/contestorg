package org.contestorg.views;

import java.awt.Window;

import org.contestorg.infos.InfosModelCategorie;
import org.contestorg.interfaces.ICollector;

/**
 * Boîte de dialogue d'édition d'une catégorie
 */
@SuppressWarnings("serial")
public class JDCategorieEditer extends JDCategorieAbstract
{
	
	/**
	 * Constructeur
	 * @param w_parent fenêtre parent
	 * @param collector collecteur des informations de la catégorie
	 * @param infos informations de la catégorie
	 */
	public JDCategorieEditer(Window w_parent, ICollector<InfosModelCategorie> collector, InfosModelCategorie infos) {
		// Appeller le constructeur du parent
		super(w_parent, "Editer une catégorie", collector);
		
		// Remplir les champs avec les données de la catégorie
		this.jtf_nom.setText(infos.getNom());
	}
	
}
