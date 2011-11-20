package org.contestorg.views;


import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.filechooser.FileSystemView;

import org.contestorg.common.Triple;
import org.contestorg.controlers.ContestOrg;
import org.contestorg.infos.InfosConnexionFTP;
import org.contestorg.infos.InfosModelChemin;
import org.contestorg.infos.InfosModelCheminFTP;
import org.contestorg.infos.InfosModelCheminLocal;
import org.contestorg.infos.InfosModelExportation;
import org.contestorg.infos.InfosModelTheme;
import org.contestorg.interfaces.ICollector;
import org.contestorg.interfaces.IOperation;



@SuppressWarnings("serial")
public abstract class JDExportationAbstract extends JDPattern implements ItemListener, ActionListener
{

	// Collector d'informations sur l'exportation
	protected ICollector<Triple<InfosModelExportation,InfosModelChemin,InfosModelTheme>> collector;
	
	// Labels des chemins
	protected final static String LABEL_CHEMIN_LOCAL = "Répertoire";
	protected final static String LABEL_CHEMIN_FTP = "Serveur FTP";

	// Panels
	private JPanel jp_chemins;
	protected JPTheme jp_theme;

	// Entrées
	protected JTextField jtf_nom = new JTextField();
	protected JRadioButton jrb_automatique_oui = new JRadioButton("Oui");
	protected JRadioButton jrb_automatique_non = new JRadioButton("Non", true);

	protected JComboBox jcb_chemins;

	protected JTextField jtf_chemin_local_chemin = new JTextField(10);

	protected JTextField jtf_chemin_ftp_host = new JTextField();
	protected JTextField jtf_chemin_ftp_port = new JTextField("21");
	protected JTextField jtf_chemin_ftp_login = new JTextField();
	protected JPasswordField jtf_chemin_ftp_password = new JPasswordField();
	protected JTextField jtf_chemin_ftp_path = new JTextField("/");
	protected JRadioButton jrb_chemin_ftp_mode_actif = new JRadioButton("Actif", true);
	protected JRadioButton jrb_chemin_ftp_mode_passif = new JRadioButton("Passif");
	
	// Boutons
	private JButton jb_chemin_local_chemin = new JButton("Séléctionner", new ImageIcon("img/farm/16x16/folder.png"));
	private JButton jb_chemin_ftp_tester = new JButton("Tester", new ImageIcon("img/farm/16x16/control_play_blue.png"));

	// Constructeur
	public JDExportationAbstract(Window w_parent, String titre, ICollector<Triple<InfosModelExportation,InfosModelChemin,InfosModelTheme>> collector) {
		// Appeler le constructeur du parent
		super(w_parent, titre);

		// Retenir le collector
		this.collector = collector;

		// Informations de l'exportation
		this.jp_contenu.add(ViewHelper.title("Informations de l'exportation", ViewHelper.H1));

		ButtonGroup bg_automatique = new ButtonGroup();
		bg_automatique.add(this.jrb_automatique_oui);
		bg_automatique.add(this.jrb_automatique_non);
		JPanel jp_automatique = new JPanel(new GridLayout(1, 2));
		jp_automatique.add(this.jrb_automatique_oui);
		jp_automatique.add(this.jrb_automatique_non);

		JLabel[] jls_exportation = { new JLabel("Nom : "), new JLabel("Automatique : ") };
		JComponent[] jcs_exportation = { this.jtf_nom, jp_automatique };
		this.jp_contenu.add(ViewHelper.inputs(jls_exportation, jcs_exportation));

		// Informations sur le thème
		this.jp_contenu.add(ViewHelper.title("Thème", ViewHelper.H1));
		this.jp_theme = new JPTheme(this,ContestOrg.get().getCtrlOut().getThemesExportation());
		this.jp_contenu.add(this.jp_theme);
		if(this.jp_theme.isError()) {
			this.jb_valider.setEnabled(false);
		} else {
			this.jtf_nom.setText(this.jp_theme.getTheme().getNom());
			this.jp_theme.addItemListener(this);
		}

		// Informations sur le chemin
		this.jp_contenu.add(ViewHelper.title("Chemin", ViewHelper.H1));
		String[] chemins = { JDExportationAbstract.LABEL_CHEMIN_LOCAL, JDExportationAbstract.LABEL_CHEMIN_FTP };
		this.jcb_chemins = new JComboBox(chemins);
		this.jp_contenu.add(this.jcb_chemins);
		this.jp_contenu.add(Box.createVerticalStrut(5));
		this.jp_chemins = new JPanel(new CardLayout());
		this.jp_contenu.add(this.jp_chemins);

		// Chemin local
		JPanel jp_chemin_local = new JPanel();
		jp_chemin_local.setLayout(new BoxLayout(jp_chemin_local, BoxLayout.Y_AXIS));
		JPanel jp_chemin_local_hauteur = new JPanel(new BorderLayout());
		jp_chemin_local_hauteur.add(jp_chemin_local, BorderLayout.NORTH);
		this.jp_chemins.add(jp_chemin_local_hauteur, JDExportationAbstract.LABEL_CHEMIN_LOCAL);

		JPanel jp_chemin_local_chemin = new JPanel(new GridBagLayout());

		GridBagConstraints contrainte_texfield = new GridBagConstraints();
		contrainte_texfield.gridx = 0;
		contrainte_texfield.weightx = 1;
		contrainte_texfield.fill = GridBagConstraints.HORIZONTAL;

		GridBagConstraints contrainte_button = new GridBagConstraints();
		contrainte_button.gridx = 1;
		contrainte_button.weightx = 0;
		contrainte_button.insets = new Insets(0, 2, 0, 0);
		contrainte_button.gridwidth = GridBagConstraints.REMAINDER;

		jp_chemin_local_chemin.add(this.jtf_chemin_local_chemin, contrainte_texfield);
		jp_chemin_local_chemin.add(this.jb_chemin_local_chemin, contrainte_button);

		JLabel[] jls_chemin_local = { new JLabel("Répertoire : ") };
		JComponent[] jcs_chemin_local = { jp_chemin_local_chemin };

		jp_chemin_local.add(ViewHelper.inputs(jls_chemin_local, jcs_chemin_local));

		// Chemin FTP
		JPanel jp_chemin_ftp = new JPanel();
		jp_chemin_ftp.setLayout(new BoxLayout(jp_chemin_ftp, BoxLayout.Y_AXIS));
		JPanel jp_chemin_ftp_hauteur = new JPanel(new BorderLayout());
		jp_chemin_ftp_hauteur.add(jp_chemin_ftp, BorderLayout.NORTH);
		this.jp_chemins.add(jp_chemin_ftp_hauteur, JDExportationAbstract.LABEL_CHEMIN_FTP);

		ButtonGroup bg_chemin_ftp_mode = new ButtonGroup();
		bg_chemin_ftp_mode.add(this.jrb_chemin_ftp_mode_actif);
		bg_chemin_ftp_mode.add(this.jrb_chemin_ftp_mode_passif);
		JPanel jp_chemin_ftp_mode = new JPanel(new GridLayout(1, 2));
		jp_chemin_ftp_mode.add(this.jrb_chemin_ftp_mode_actif);
		jp_chemin_ftp_mode.add(this.jrb_chemin_ftp_mode_passif);
		JLabel[] jls_chemin_ftp = { new JLabel("Hote : "), new JLabel("Port : "), new JLabel("Utilisateur : "), new JLabel("Mot de passe : "), new JLabel("Chemin : "), new JLabel("Mode : ") };
		JComponent[] jcs_chemin_ftp = { this.jtf_chemin_ftp_host, this.jtf_chemin_ftp_port, this.jtf_chemin_ftp_login, this.jtf_chemin_ftp_password, this.jtf_chemin_ftp_path, jp_chemin_ftp_mode };
		jp_chemin_ftp.add(ViewHelper.inputs(jls_chemin_ftp, jcs_chemin_ftp));

		JPanel jp_chemin_ftp_tester = new JPanel(new FlowLayout(FlowLayout.LEFT));
		jp_chemin_ftp_tester.add(this.jb_chemin_ftp_tester);
		jp_chemin_ftp.add(jp_chemin_ftp_tester);

		// Ecouter la liste et les boutons
		this.jcb_chemins.addItemListener(this);
		this.jb_chemin_local_chemin.addActionListener(this);
		this.jb_chemin_ftp_tester.addActionListener(this);

		// Pack
		this.pack();
	}

	// Récupérer les informatios de la connexion FTP
	private InfosConnexionFTP getInfosModelCheminFTP () {
		// Ajouter un slash à la fin du path si nécéssaire
		if (!this.jtf_chemin_ftp_path.getText().trim().endsWith("/")) {
			this.jtf_chemin_ftp_path.setText(this.jtf_chemin_ftp_path.getText().trim() + "/");
		}

		// Mettre 21 au port si pas de port spécifié
		if (this.jtf_chemin_ftp_port.getText().trim().isEmpty()) {
			this.jtf_chemin_ftp_port.setText(String.valueOf(21));
		}

		// Récupérer les données
		String host = this.jtf_chemin_ftp_host.getText().trim();
		String port = this.jtf_chemin_ftp_port.getText().trim();
		String login = this.jtf_chemin_ftp_login.getText().trim();
		String password = new String(jtf_chemin_ftp_password.getPassword());
		String path = this.jtf_chemin_ftp_path.getText().trim();
		int mode = this.jrb_chemin_ftp_mode_actif.isSelected() ? InfosConnexionFTP.MODE_ACTIF : InfosConnexionFTP.MODE_PASSIF;

		// Vérifier les données
		boolean erreur = false;
		if (host.isEmpty()) {
			ViewHelper.derror(this, "Erreur sur le chemin", "L'hote du chemin FTP n'est pas précisé.");
			erreur = true;
		}
		int portParse = 0;
		if (port.isEmpty()) {
			ViewHelper.derror(this, "Erreur sur le chemin", "Le port du chemin FTP n'est pas précisé.");
			erreur = true;
		} else {
			try {
				portParse = Integer.parseInt(port);
			} catch (NumberFormatException e) {
				ViewHelper.derror(this, "Erreur sur le chemin", "Le port du chemin FTP doit être un nombre entier.");
				erreur = true;
			}
		}
		if (login.isEmpty()) {
			ViewHelper.derror(this, "Erreur sur le chemin", "Le nom d'utilisateur du chemin FTP n'est pas précisé.");
			erreur = true;
		}
		if (password.isEmpty()) {
			ViewHelper.derror(this, "Erreur sur le chemin", "Le mot de passe du chemin FTP n'est pas précisé.");
			erreur = true;
		}
		// FIXME On pourrait vérifier si le path et le host sont valides mais ce n'est pas prioritaire


		// Créer et retourner les informations
		if (!erreur) {
			return new InfosConnexionFTP(host, portParse, login, password, path, mode);
		}
		return null;
	}

	// Récupérer les informations du chemin local
	private InfosModelCheminLocal getInfosModelCheminLocal () {
		// Ajouter un slash à la fin du chemin si nécéssaire
		if (!this.jtf_chemin_local_chemin.getText().trim().isEmpty() && !this.jtf_chemin_local_chemin.getText().trim().endsWith(File.separator)) {
			this.jtf_chemin_local_chemin.setText(this.jtf_chemin_local_chemin.getText().trim() + File.separator);
		}

		// Récupérer les données
		String chemin = this.jtf_chemin_local_chemin.getText().trim();

		// Vérifier les données
		if (chemin.isEmpty()) {
			ViewHelper.derror(this, "Erreur sur le chemin", "Le chemin du répertoire local n'est pas précisé.");
			return null;
		}
		File repertoire = new File(chemin);
		if (!repertoire.exists()) {
			ViewHelper.derror(this, "Erreur sur le chemin", "Le répertoire du chemin local n'existe pas.");
			return null;
		}
		if (!repertoire.isDirectory()) {
			ViewHelper.derror(this, "Erreur sur le chemin", "Le répertoire du chemin local n'est pas un répertoire.");
			return null;
		}

		// Créer et retourner les informations
		return new InfosModelCheminLocal(chemin);
	}

	// Récupérer les informations du chemin
	private InfosModelChemin getInfosModelChemin () {
		// Récupérer le thème et le chemin séléctionnés
		String selectedChemin = (String)this.jcb_chemins.getSelectedItem();

		// Vérifier les données et créer les informations du chemin
		if (selectedChemin.equals(JDExportationAbstract.LABEL_CHEMIN_LOCAL)) {
			// Récupérer et retourner les informations
			return this.getInfosModelCheminLocal();
		} else if (selectedChemin.equals(JDExportationAbstract.LABEL_CHEMIN_FTP)) {
			// Récupérer et retourner les informations
			InfosConnexionFTP connexion = this.getInfosModelCheminFTP();

			// Vérifier les informations
			if (connexion == null) {
				return null;
			}

			// Créer les informations
			return new InfosModelCheminFTP(connexion);
		}

		// Retourner les informations
		return null;
	}

	// Implémentation de ok
	@Override
	protected void ok () {
		// Booléen d'erreur
		boolean erreur = false;

		// Initialiser les informations
		InfosModelExportation exportation = null;
		InfosModelTheme theme = null;
		InfosModelChemin chemin = null;

		// Vérifier les données et créer les informations de l'exportation
		String nom = this.jtf_nom.getText().trim();
		if (nom.isEmpty()) {
			ViewHelper.derror(this, "Le nom de l'exportation n'a pas été précisé.");
			erreur = true;
		}
		if (!erreur) {
			exportation = new InfosModelExportation(nom, this.jrb_automatique_oui.isSelected());
		}

		// Vérifier les données et créer les informations de l'exportation
		theme = this.jp_theme.getInfosModelTheme();

		// Vérifier les données et créer les informations de l'exportation
		chemin = this.getInfosModelChemin();

		// Envoyer les informations au collector
		if (!erreur && theme != null && chemin != null) {
			this.collector.accept(new Triple<InfosModelExportation,InfosModelChemin,InfosModelTheme>(exportation, chemin, theme));
		}
	}

	// Implémentation de quit
	@Override
	protected void quit () {
		// Annuler
		this.collector.cancel();
	}

	// Implémentation de ActionListener
	public void actionPerformed (ActionEvent event) {
		if (event.getSource() == this.jb_chemin_local_chemin) {
			// Demander à l'utilisateur l'emplacement du repertoire
			String chemin = this.jtf_chemin_local_chemin.getText().trim();
			File defaut = chemin.isEmpty() ? FileSystemView.getFileSystemView().getHomeDirectory() : new File(chemin);
			chemin = ViewHelper.selectionner(this, "Définir le répertoire d'exportation", defaut, FileSystemView.getFileSystemView());
			if (chemin != null)
				this.jtf_chemin_local_chemin.setText(chemin);
		} else if (event.getSource() == this.jb_chemin_ftp_tester) {
			// Demander au controleur principal un test
			InfosConnexionFTP infos = this.getInfosModelCheminFTP();
			if (infos != null) {
				IOperation tester = ContestOrg.get().getCtrlOut().getTesterExportationFtpOperation(infos);	// Créer le testeur
				JDOperation jd_test = new JDOperation(this, "Test de connexion au serveur FTP",tester);		// Créer la fenêtre de test
				tester.operationStart();																	// Demarrer le test
				jd_test.setVisible(true);																	// Afficher la fenêtre de test
			}
		} else {
			// Appeller l'implémentation du parent
			super.actionPerformed(event);
		}
	}

	// Implémentation de ItemListener
	@Override
	public void itemStateChanged (ItemEvent event) {
		if(event.getSource() == this.jcb_chemins) {
			CardLayout layout = (CardLayout)(this.jp_chemins.getLayout());
			layout.show(this.jp_chemins, (String)event.getItem());
		} else {
			this.jtf_nom.setText(this.jp_theme.getTheme().getNom());
		}
	}

}
