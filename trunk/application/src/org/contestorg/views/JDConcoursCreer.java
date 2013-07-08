package org.contestorg.views;

import java.awt.Window;

import org.contestorg.controllers.ContestOrg;

/**
 * Boîte de dialogue de création de concours
 */
@SuppressWarnings("serial")
public class JDConcoursCreer extends JDConcoursAbstract
{

	/**
	 * Constructeur
	 * @param w_parent fenêtre parent
	 */
	public JDConcoursCreer(Window w_parent) {
		// Appeler le constructeur du parent
		super(w_parent, "Nouveau concours");
	}

	/**
	 * @see JDPattern#ok()
	 */
	@Override
	public void ok () {
		// Vérifier les validité des données
		if (this.check()) {
			// Demander la creation de concours
			ContestOrg.get().concoursCreer(
				this.getInfosModelConcours(), this.jp_points.getObjectifs(), this.jp_points.getComparacteurs(),
				this.jp_exportations.getExportations(), this.jp_exportations.getPublication(), this.jp_exportations.getDiffusions(),
				this.jp_prix.getPrix(), this.jp_lieux.getLieux(), this.jp_proprietes.getProprietes()
			);
			
			// Fermer la boîte de dialogue
			this.setVisible(false);
		}
	}

	/**
	 * @see JDPattern#quit()
	 */
	@Override
	public void quit () {
		// Fermer la boîte de dialogue
		this.setVisible(false);
	}

}
