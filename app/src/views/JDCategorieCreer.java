package views;

import infos.InfosModelCategorie;
import interfaces.ICollector;

import java.awt.Window;

@SuppressWarnings("serial")
public class JDCategorieCreer extends JDCategorieAbstract
{

	// Constructeur
	public JDCategorieCreer(Window w_parent, ICollector<InfosModelCategorie> collector) {
		// Appeller le constructeur parent
		super(w_parent, "Ajouter une catégorie", collector);
	}
	
}
