package views;

import java.awt.Window;

import controlers.ContestOrg;

@SuppressWarnings("serial")
public class JDConcoursCreer extends JDConcoursAbstract
{

	// Constructeur
	public JDConcoursCreer(Window w_parent) {
		// Appeler le constructeur du parent
		super(w_parent, "Nouveau concours");
	}

	// Implémentation de l'action sur valider
	@Override
	public void ok () {
		// Vérifier les validité des données
		if (this.check()) {
			// Demander la creation de concours
			ContestOrg.get().procedureConcoursNouveau(
				this.getInfosModelConcours(), this.jp_points.getObjectifs(), this.jp_points.getComparacteurs(),
				this.jp_exportations.getExportations(), this.jp_exportations.getPublication(), this.jp_exportations.getDiffusions(),
				this.jp_prix.getPrix(), this.jp_lieux.getLieux(), this.jp_proprietes.getProprietes()
			);
		}
	}

	// Quitter la fenêtre
	@Override
	public void quit () {
		// Demander l'annulation de la procédure de création de concours
		ContestOrg.get().procedureConcoursNouveauAnnuler();
	}

}
