package org.contestorg.views;


import java.awt.Window;

import org.contestorg.infos.InfosModelCategorie;
import org.contestorg.interfaces.ICollector;

@SuppressWarnings("serial")
public class JDCategorieEditer extends JDCategorieAbstract
{

	// Constructeur
	public JDCategorieEditer(Window w_parent, ICollector<InfosModelCategorie> collector, InfosModelCategorie infos) {
		// Appeller le constructeur du parent
		super(w_parent, "Editer une catégorie", collector);
		
		// Remplir les champs avec les données de la catégorie
		this.jtf_nom.setText(infos.getNom());
	}
	
}
