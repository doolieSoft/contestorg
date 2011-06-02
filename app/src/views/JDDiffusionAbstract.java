package views;

import infos.InfosModelDiffusion;
import infos.InfosModelTheme;
import interfaces.ICollector;
import interfaces.IOperation;

import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import common.Pair;

import controlers.ContestOrg;

@SuppressWarnings("serial")
public abstract class JDDiffusionAbstract extends JDPattern
{
	// Collector d'informations sur la diffusion
	private ICollector<Pair<InfosModelDiffusion,InfosModelTheme>> collector;
	
	// Panel
	protected JPTheme jp_theme;

	// Entrées
	protected JTextField jtf_nom = new JTextField();
	protected JTextField jtf_port = new JTextField("80");

	// Bouton tester
	private JButton jb_tester = new JButton("Tester", new ImageIcon("img/farm/16x16/control_play_blue.png"));

	// Constructeur
	public JDDiffusionAbstract(Window w_parent, String titre, ICollector<Pair<InfosModelDiffusion,InfosModelTheme>> collector) {
		// Appeller le constructeur du parent
		super(w_parent, titre);

		// Retenir le collector
		this.collector = collector;

		// Information de la diffusion
		this.jp_contenu.add(ViewHelper.title("Informations de la diffusion", ViewHelper.H1));
		JLabel[] jls = { new JLabel("Nom : "), new JLabel("Port : ") };
		JComponent[] jcs = { this.jtf_nom, this.jtf_port };
		this.jp_contenu.add(ViewHelper.inputs(jls, jcs));
		
		// Bouton
		JPanel jp_button_tester = new JPanel(new FlowLayout(FlowLayout.LEFT));
		jp_button_tester.add(this.jb_tester);
		this.jp_contenu.add(jp_button_tester);
		this.jb_tester.addActionListener(this);
		
		// Informations sur le thème
		this.jp_contenu.add(ViewHelper.title("Thème", ViewHelper.H1));
		this.jp_theme = new JPTheme(this,ContestOrg.get().getCtrlOut().getThemesDiffusion());
		this.jp_contenu.add(this.jp_theme);
		if(this.jp_theme.isError()) {
			this.jb_valider.setEnabled(false);
		}

		// Pack
		this.pack();
	}
	
	// Récupérer le port
	public Integer getPort() {
		// Mettre 80 au port si pas de port spécifié
		if (this.jtf_port.getText().trim().isEmpty()) {
			this.jtf_port.setText(String.valueOf(80));
		}

		try {
			// Retourner les informations
			return Integer.parseInt(this.jtf_port.getText().trim());
		} catch (NumberFormatException e) {
			// Erreur
			ViewHelper.derror(this, "Le port doit être un nombre entier.");
			return null;
		}
	}

	// Récupérer les informations de la diffusion
	public InfosModelDiffusion getInfosModelDiffusion () {
		// Récupérer les données
		Integer port = this.getPort();
		String nom = this.jtf_nom.getText().trim();
		
		// Vérifier les données
		boolean erreur = false;
		if(nom.isEmpty()) {
			// Erreur
			ViewHelper.derror(this, "Le nom de la diffusion n'est pas précisé.");
			erreur = true;
		}
		if(port == null) {
			// Erreur
			erreur = true;
		}

		// Retourner les informations
		if(!erreur) {
			return new InfosModelDiffusion(nom,Integer.parseInt(this.jtf_port.getText().trim()));
		}
		return null;
	}

	// Implémentation de ok
	@Override
	protected void ok () {
		// Récupérer les informations de la diffusion
		InfosModelDiffusion diffusion = this.getInfosModelDiffusion();
		
		// Récupérer les informations du thème
		InfosModelTheme theme = this.jp_theme.getTheme();

		// Envoyer les informations au collector
		if (diffusion != null && theme != null) {
			this.collector.accept(new Pair<InfosModelDiffusion,InfosModelTheme>(diffusion,theme));
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
		if (event.getSource() == this.jb_tester) {
			// Demander au controleur principal un test
			Integer port = this.getPort();
			if (port != null) {
				IOperation tester = ContestOrg.get().getCtrlOut().getTesterDiffusionOperation(port);	// Créer le testeur
				JDOperation jd_test = new JDOperation(this, "Test d'une diffusion",tester);				// Créer la fenetre de test
				tester.operationStart();																// Demarrer le test
				jd_test.setVisible(true);																// Afficher la fenetre de test
				
			}
		} else {
			// Appeller l'implémentation du parent
			super.actionPerformed(event);
		}
	}

}
