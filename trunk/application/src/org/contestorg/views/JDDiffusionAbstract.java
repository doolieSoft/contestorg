package org.contestorg.views;

import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.contestorg.common.Pair;
import org.contestorg.controllers.ContestOrg;
import org.contestorg.infos.InfosModelDiffusion;
import org.contestorg.infos.InfosModelTheme;
import org.contestorg.interfaces.ICollector;
import org.contestorg.interfaces.IOperation;

/**
 * Boîte de dialogue de création/édition de diffusion
 */
@SuppressWarnings("serial")
public abstract class JDDiffusionAbstract extends JDPattern implements ItemListener
{
	/** Collecteur des informations de la diffusion */
	private ICollector<Pair<InfosModelDiffusion,InfosModelTheme>> collector;
	
	/** Panel du thème */
	protected JPTheme jp_theme;

	/** Bouton tester */
	private JButton jb_tester = new JButton("Tester", new ImageIcon("img/farm/16x16/control_play_blue.png"));

	// Entrées
	
	/** Nom */
	protected JTextField jtf_nom = new JTextField();
	
	/** Port */
	protected JTextField jtf_port = new JTextField("80");

	/**
	 * Constructeur
	 * @param w_parent fenêtre parent
	 * @param titre titre de la boîte de dialogue
	 * @param collector collecteur des informations de la diffusion
	 */
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
		}else {
			this.jtf_nom.setText(this.jp_theme.getTheme().getNom());
			this.jp_theme.addItemListener(this);
		}

		// Pack
		this.pack();
	}
	
	/**
	 * Récupérer le port
	 * @return port
	 */
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

	/**
	 * Récupérer les informations de la diffusion
	 * @return informations de la diffusion
	 */
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

	/**
	 * @see JDPattern#ok()
	 */
	@Override
	protected void ok () {
		// Récupérer les informations de la diffusion
		InfosModelDiffusion diffusion = this.getInfosModelDiffusion();
		
		// Récupérer les informations du thème
		InfosModelTheme theme = this.jp_theme.getInfosModelTheme();

		// Envoyer les informations au collector
		if (diffusion != null && theme != null) {
			this.collector.collect(new Pair<InfosModelDiffusion,InfosModelTheme>(diffusion,theme));
		}
	}

	/**
	 * @see JDPattern#quit()
	 */
	@Override
	protected void quit () {
		// Annuler
		this.collector.cancel();
	}

	/**
	 * @see ActionListener#actionPerformed(ActionEvent)
	 */
	public void actionPerformed (ActionEvent event) {
		if (event.getSource() == this.jb_tester) {
			// Demander au controleur principal un test
			Integer port = this.getPort();
			if (port != null) {
				IOperation tester = ContestOrg.get().getCtrlOut().getTesterDiffusionOperation(port);	// Créer le testeur
				JDOperation jd_test = new JDOperation(this, "Test d'une diffusion",tester);				// Créer la fenêtre de test
				tester.operationStart();																// Demarrer le test
				jd_test.setVisible(true);																// Afficher la fenêtre de test
				
			}
		} else {
			// Appeller l'implémentation du parent
			super.actionPerformed(event);
		}
	}

	/**
	 * @see ItemListener#itemStateChanged(ItemEvent)
	 */
	@Override
	public void itemStateChanged (ItemEvent event) {
		this.jtf_nom.setText(this.jp_theme.getTheme().getNom());
	}

}
