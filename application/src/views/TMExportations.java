package views;

import infos.InfosModelChemin;
import infos.InfosModelCheminFTP;
import infos.InfosModelCheminLocal;
import infos.InfosModelExportation;
import infos.InfosModelTheme;

import java.awt.Window;

import common.Triple;

public class TMExportations extends TMAbstract<Triple<InfosModelExportation,InfosModelChemin,InfosModelTheme>>
{

	// Constructeurs
	public TMExportations(Window w_parent) {
		// Appeller le constructeur du parent
		super(w_parent);
	}

	// Méthodes à implementer
	public Window getAddWindow () {
		// Créer et retourner la fenetre de création
		return new JDExportationCreer(this.w_parent, this);
	}
	public Window getUpdateWindow (Triple<InfosModelExportation,InfosModelChemin,InfosModelTheme> infos) {
		// Créer et retourner la fenetre de création
		return new JDExportationEditer(this.w_parent, this, infos);
	}
	public boolean acceptDelete (Triple<InfosModelExportation,InfosModelChemin,InfosModelTheme> infos) {
		// Demander la confirmation à l'utilisateur
		if (ViewHelper.confirmation(this.w_parent, "Désirez-vous vraiment supprimer l'exportation \"" + infos.getFirst().getNom() + "\" ?")) {
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
		return 3;
	}
	@Override
	public String getColumnName (int column) {
		switch (column) {
			case 0:
				return "Nom";
			case 1:
				return "Thème";
			case 2:
				return "Chemin";
		}
		return null;
	}
	@Override
	public Object getValueAt (int row, int column) {
		// Retourner l'information demandée
		switch (column) {
			case 0:
				return this.get(row).getFirst().getNom();
			case 1:
				// Récupérer le nom du thème
				return this.get(row).getThird().getNom();
			case 2:
				// Récupérer les informations du chemin
				InfosModelChemin chemin = this.get(row).getSecond();

				// Retourner chemin
				if (chemin instanceof InfosModelCheminLocal) {
					return "Répertoire";
				} else if (chemin instanceof InfosModelCheminFTP) {
					return "Serveur FTP";
				}
				break;
		}
		return null;
	}
	@Override
	public boolean isCellEditable (int row, int column) {
		return column == 0;
	}
	@Override
	public void setValueAt (Object object, int row, int column) {
		this.update(row, new Triple<InfosModelExportation,InfosModelChemin,InfosModelTheme>(new InfosModelExportation((String)object, this.get(row).getFirst().isAuto()), this.get(row).getSecond(), this.get(row).getThird()));
	}

}
