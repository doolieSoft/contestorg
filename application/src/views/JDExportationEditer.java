package views;

import infos.InfosConnexionFTP;
import infos.InfosModelChemin;
import infos.InfosModelCheminFTP;
import infos.InfosModelCheminLocal;
import infos.InfosModelExportation;
import infos.InfosModelTheme;
import interfaces.ICollector;

import java.awt.Window;

import common.Triple;

@SuppressWarnings("serial")
public class JDExportationEditer extends JDExportationAbstract
{

	// Constructeur
	public JDExportationEditer(Window w_parent, ICollector<Triple<InfosModelExportation,InfosModelChemin,InfosModelTheme>> collector, Triple<InfosModelExportation,InfosModelChemin,InfosModelTheme> infos) {
		// Appeller le constructeur du parent
		super(w_parent, "Editer une exportation", collector);

		// Remplir les champs avec les données de l'exportation
		InfosModelExportation infosExportation = infos.getFirst();
		this.jtf_nom.setText(infosExportation.getNom());
		this.jrb_automatique_oui.setSelected(infosExportation.isAuto());
		this.jrb_automatique_non.setSelected(!infosExportation.isAuto());

		// Remplir les champs avec les données du thème
		this.jp_theme.setTheme(infos.getThird());

		// Remplir les champs avec les données du chemin
		InfosModelChemin chemin = infos.getSecond();
		if (chemin instanceof InfosModelCheminFTP) {
			// Séléctionner le bon item dans la liste
			this.jcb_chemins.setSelectedItem(JDExportationEditer.LABEL_CHEMIN_FTP);

			// Remplir les champs
			this.jtf_chemin_ftp_host.setText(((InfosModelCheminFTP)chemin).getInfosFTP().getHost());
			this.jtf_chemin_ftp_port.setText(String.valueOf(((InfosModelCheminFTP)chemin).getInfosFTP().getPort()));
			this.jtf_chemin_ftp_login.setText(((InfosModelCheminFTP)chemin).getInfosFTP().getUsername());
			this.jtf_chemin_ftp_password.setText(((InfosModelCheminFTP)chemin).getInfosFTP().getPassword());
			this.jtf_chemin_ftp_path.setText(((InfosModelCheminFTP)chemin).getInfosFTP().getPath());
			this.jrb_chemin_ftp_mode_actif.setSelected(((InfosModelCheminFTP)chemin).getInfosFTP().getMode() == InfosConnexionFTP.MODE_ACTIF);
			this.jrb_chemin_ftp_mode_passif.setSelected(((InfosModelCheminFTP)chemin).getInfosFTP().getMode() == InfosConnexionFTP.MODE_PASSIF);
		} else if (chemin instanceof InfosModelCheminLocal) {
			// Séléctionner le bon item dans la liste
			this.jcb_chemins.setSelectedItem(JDExportationEditer.LABEL_CHEMIN_LOCAL);

			// Remplir les champs
			this.jtf_chemin_local_chemin.setText(((InfosModelCheminLocal)chemin).getChemin());
		}
	}

}
