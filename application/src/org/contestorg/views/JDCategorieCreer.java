package org.contestorg.views;

import java.awt.Window;

import org.contestorg.infos.InfosModelCategorie;
import org.contestorg.interfaces.ICollector;

/**
 * Boîte de dialogue de création d'une catégorie
 */
@SuppressWarnings("serial")
public class JDCategorieCreer extends JDCategorieAbstract
{

	/**
	 * Constructeur
	 * @param w_parent fenêtre parent
	 * @param collector collecteur des informations de la catégorie
	 */
	public JDCategorieCreer(Window w_parent, ICollector<InfosModelCategorie> collector) {
		// Appeller le constructeur parent
		super(w_parent, "Ajouter une catégorie", collector);
	}
	
}
