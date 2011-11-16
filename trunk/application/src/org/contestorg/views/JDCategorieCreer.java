package org.contestorg.views;


import java.awt.Window;

import org.contestorg.infos.InfosModelCategorie;
import org.contestorg.interfaces.ICollector;

@SuppressWarnings("serial")
public class JDCategorieCreer extends JDCategorieAbstract
{

	// Constructeur
	public JDCategorieCreer(Window w_parent, ICollector<InfosModelCategorie> collector) {
		// Appeller le constructeur parent
		super(w_parent, "Ajouter une catégorie", collector);
	}
	
}
