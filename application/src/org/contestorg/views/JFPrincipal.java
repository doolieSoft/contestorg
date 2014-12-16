package org.contestorg.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.net.URI;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;

import org.contestorg.common.Tools;
import org.contestorg.controllers.ContestOrg;
import org.contestorg.infos.InfosModelConcours;
import org.contestorg.interfaces.IMoody;
import org.contestorg.interfaces.IMoodyListener;
import org.contestorg.interfaces.IOperation;

/**
 * Fenêtre principale
 */
@SuppressWarnings("serial")
public class JFPrincipal extends JFrame implements ActionListener, WindowListener, IMoodyListener
{
	/** Titre de fenêtre */
	private String titre;
	
	/** Onglets */
	private JTabbedPane jtb_onglets;

	// Boutons
	
	/** Bouton "Nouveau" */
	private JButton jb_nouveau;
	/** Bouton "Ouvrir" */
	private JButton jb_ouvrir;

	/** Bouton "Sauvegarder" */
	private JButton jb_sauvegarder;
	/** Bouton "Sauvegarder sous" */
	private JButton jb_sauvegarder_sous;
	/** Bouton "Exporter" */
	private JButton jb_exporter;
	/** Bouton "Publier" */
	private JButton jb_publier;
	/** Bouton "Diffuser" */
	private JButton jb_diffuser;
	/** Bouton "Configurer" */
	private JButton jb_configurer;
	/** Bouton "Fermer" */
	private JButton jb_fermer;

	/** Bouton "Aide" */
	private JButton jb_aide;
	/** Bouton "Site web" */
	private JButton jb_web;
	/** Bouton "A propos" */
	private JButton jb_apropos;
	/** Bouton "Quitter" */
	private JButton jb_quitter;

	/**
	 * Constructeur
	 * @param titre titre de la fenêtre
	 */
	public JFPrincipal(String titre) {
		// Parametres de la fenêtre
		super(titre);
		this.setResizable(true); // Redimensionnable
		this.setPreferredSize(new Dimension(1000, 700)); // Dimensions
		this.setMinimumSize(new Dimension(1000, 650)); // Dimensions au minimum
		this.setLocation(50, 50); // Localisation
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE); // Ne rien faire si echap
		this.setIconImage(new ImageIcon("img/farm/32x32/sport.png").getImage()); // Icone de la fenêtre
		this.setExtendedState(JFrame.MAXIMIZED_BOTH); // Plein écran
		
		// Retenir le titre
		this.titre = titre;

		// Panel principal
		JPanel panel_principal = new JPanel();
		panel_principal.setLayout(new BoxLayout(panel_principal, BoxLayout.X_AXIS));
		this.add(panel_principal, BorderLayout.CENTER);

		// Panel de gauche
		JPanel jp_gauche = new JPanel();
		jp_gauche.setLayout(new BoxLayout(jp_gauche, BoxLayout.Y_AXIS));
		jp_gauche.setBackground(new Color(55, 133, 207));
		jp_gauche.setBorder(new LineBorder(new Color(55, 133, 207), 8));
		jp_gauche.setOpaque(true);
		jp_gauche.setMaximumSize(new Dimension(280, this.getMaximumSize().height));
		panel_principal.add(jp_gauche);

		this.jb_nouveau = new JButton("Nouveau", new ImageIcon("img/farm/32x32/add.png"));
		this.jb_ouvrir = new JButton("Ouvrir", new ImageIcon("img/farm/32x32/folder.png"));
		this.jb_sauvegarder = new JButton("Sauvegarder", new ImageIcon("img/farm/16x16/disk.png"));
		this.jb_sauvegarder_sous = new JButton("Sauvegarder sous");
		this.jb_exporter = new JButton("Exporter", new ImageIcon("img/farm/16x16/application_go.png"));
		this.jb_publier = new JButton("Publier", new ImageIcon("img/farm/16x16/world_go.png"));
		this.jb_diffuser = new JButton("Diffuser", new ImageIcon("img/farm/16x16/monitor_go.png"));
		this.jb_configurer = new JButton("Configurer", new ImageIcon("img/farm/16x16/cog.png"));
		this.jb_fermer = new JButton("Fermer", new ImageIcon("img/farm/16x16/cross.png"));
		this.jb_aide = new JButton("Aide", new ImageIcon("img/farm/16x16/help.png"));
		this.jb_web = new JButton("Site Web", new ImageIcon("img/farm/16x16/world.png"));
		this.jb_apropos = new JButton("A propos", new ImageIcon("img/farm/16x16/information.png"));
		this.jb_quitter = new JButton("Quitter", new ImageIcon("img/farm/16x16/door_in.png"));
		
		this.jb_nouveau.setToolTipText("Créer un nouveau concours");
		this.jb_ouvrir.setToolTipText("Ouvrir un concours existant");
		this.jb_sauvegarder.setToolTipText("Sauvegarder les modifications");
		this.jb_sauvegarder_sous.setToolTipText("Sauvegarder le tournoi dans un autre fichier");
		this.jb_exporter.setToolTipText("Lancer des exportations prédéfinies");
		this.jb_publier.setToolTipText("Lancer l'exportation choisie pour la publication");
		this.jb_diffuser.setToolTipText("Démarrer des diffusions d'informations sur le réseau");
		this.jb_configurer.setToolTipText("Configurer le concours");
		this.jb_fermer.setToolTipText("Fermer le concours sans sauvegarder les modifications");
		this.jb_aide.setToolTipText("Ouvrir l'aide");
		this.jb_web.setToolTipText("Se rendre sur le site web de ContestOrg");
		this.jb_apropos.setToolTipText("Afficher les informations de ContestOrg");
		this.jb_quitter.setToolTipText("Quitter le programme");

		this.jb_sauvegarder.setEnabled(false);
		this.jb_sauvegarder_sous.setEnabled(false);
		this.jb_exporter.setEnabled(false);
		this.jb_publier.setEnabled(false);
		this.jb_diffuser.setEnabled(false);
		this.jb_configurer.setEnabled(false);
		this.jb_fermer.setEnabled(false);

		this.addButton(jp_gauche, this.jb_nouveau, true);
		this.addButton(jp_gauche, this.jb_ouvrir, true);

		JSeparator js_sep1 = new JSeparator();
		js_sep1.setMaximumSize(new Dimension(280, 12));
		jp_gauche.add(js_sep1);
		jp_gauche.add(Box.createVerticalStrut(5));

		this.addButton(jp_gauche, this.jb_sauvegarder, true);
		this.addButton(jp_gauche, this.jb_sauvegarder_sous, false);
		this.addButton(jp_gauche, this.jb_exporter, true);
		this.addButton(jp_gauche, this.jb_publier, true);
		this.addButton(jp_gauche, this.jb_diffuser, true);
		this.addButton(jp_gauche, this.jb_configurer, true);
		this.addButton(jp_gauche, this.jb_fermer, true);

		JSeparator js_sep2 = new JSeparator();
		js_sep2.setMaximumSize(new Dimension(280, 12));
		jp_gauche.add(js_sep2);
		jp_gauche.add(Box.createVerticalStrut(5));

		this.addButton(jp_gauche, this.jb_aide, true);
		this.addButton(jp_gauche, this.jb_web, true);
		this.addButton(jp_gauche, this.jb_apropos, true);
		this.addButton(jp_gauche, this.jb_quitter, true);

		// Panel à onglets
		JPanel jp_participants = new JPPrincipalParticipants(this);
		JPanel jp_phasesQualificatives = new JPPrincipalPhasesQualificatives(this);
		JPanel jp_phasesFinales = new JPPrincipalPhasesEliminatoires(this);
		
		this.jtb_onglets = new JTabbedPane();
		this.jtb_onglets.addTab("Equipes", new ImageIcon("img/farm/32x32/group.png"), jp_participants, "Créer, éditer et supprimer des équipes");
		this.jtb_onglets.addTab("Phases qualificatives", new ImageIcon("img/farm/32x32/arrow_switch.png"), jp_phasesQualificatives, "Organiser les phases qualificatives");
		this.jtb_onglets.addTab("Phases éliminatoires", new ImageIcon("img/farm/32x32/arrow_join.png"), jp_phasesFinales, "Organiser les phases éliminatoires");
		panel_principal.add(this.jtb_onglets);

		// Evenements
		this.addWindowListener(this);
		this.jb_nouveau.addActionListener(this);
		this.jb_ouvrir.addActionListener(this);
		this.jb_sauvegarder.addActionListener(this);
		this.jb_sauvegarder_sous.addActionListener(this);
		this.jb_exporter.addActionListener(this);
		this.jb_publier.addActionListener(this);
		this.jb_diffuser.addActionListener(this);
		this.jb_configurer.addActionListener(this);
		this.jb_fermer.addActionListener(this);
		this.jb_aide.addActionListener(this);
		this.jb_web.addActionListener(this);
		this.jb_apropos.addActionListener(this);
		this.jb_quitter.addActionListener(this);

		// Pack
		// this.pack();
	}
	
	/**
	 * Ajouter un bouton au panneau
	 * @param panel panel du panneau
	 * @param button boutton
	 * @param resizeHeight redimensionner le bouton ?
	 */
	private void addButton (JPanel panel, JButton button, boolean resizeHeight) {
		button.setAlignmentX(Component.CENTER_ALIGNMENT);
		Dimension maxSize = button.getMaximumSize();
		maxSize.width = 280;
		if(resizeHeight) {
				maxSize.height = 40;
		}
		button.setMaximumSize(maxSize);
		panel.add(button);
		panel.add(Box.createVerticalStrut(5));
	}

	/**
	 * Quitter la fenêtre
	 */
	private void quit () {
		if (ViewHelper.confirmation(this, !ContestOrg.get().is(ContestOrg.STATE_SAVE) && ContestOrg.get().is(ContestOrg.STATE_EDIT) ?  "En quittant ContestOrg, vous perdrez toutes les modifications non sauvegardées. Désirez-vous continuer ?" : "Désirez vous vraiment quitter ContestOrg ?", !ContestOrg.get().is(ContestOrg.STATE_SAVE) && ContestOrg.get().is(ContestOrg.STATE_EDIT))) {
			// Quitter le programme
			ContestOrg.get().quitter();
		}
	}

	// Implémentation de WindowListener
	public void windowActivated (WindowEvent event) {
	}
	public void windowClosed (WindowEvent event) {
	}
	public void windowClosing (WindowEvent event) {
		this.quit();
	}
	public void windowDeactivated (WindowEvent event) {
	}
	public void windowDeiconified (WindowEvent event) {
	}
	public void windowIconified (WindowEvent event) {
	}
	public void windowOpened (WindowEvent event) {
	}

	/**
	 * @see ActionListener#actionPerformed(ActionEvent)
	 */
	public void actionPerformed (ActionEvent event) {
		if (event.getSource() == this.jb_nouveau) {
			if(!ContestOrg.get().is(ContestOrg.STATE_OPEN) || ContestOrg.get().is(ContestOrg.STATE_SAVE) || ViewHelper.confirmation(this, "En créant un nouveau concours, vous perdrez toutes les modifications non sauvegardées. Désirez-vous continuer ?", true)) {
				JDModeles dialog = new JDModeles(this);
				dialog.setVisible(true);
			}
		} else if (event.getSource() == this.jb_ouvrir) {
			if(!ContestOrg.get().is(ContestOrg.STATE_OPEN) || ContestOrg.get().is(ContestOrg.STATE_SAVE) || ViewHelper.confirmation(this, "En ouvrant un autre concours, vous perdrez toutes les modifications non sauvegardées. Désirez-vous continuer ?", true)) {
				ContestOrg.get().concoursOuvrir();
			}
		} else if (event.getSource() == this.jb_sauvegarder) {
			ContestOrg.get().concoursSauvegarder();
		} else if (event.getSource() == this.jb_sauvegarder_sous) {
			ContestOrg.get().concoursSauvegarderSous();
		} else if (event.getSource() == this.jb_exporter) {
			JDExportations dialog = new JDExportations(this);
			dialog.setVisible(true);
		} else if (event.getSource() == this.jb_publier) {
			if(ContestOrg.get().getCtrlOut().getPublication() != null) {
				IOperation operation = ContestOrg.get().getCtrlOut().getLancerPublicationOperation();	// Récupérer l'opération
				JDOperation jd_operation = new JDOperation(this,"Publication",operation,true,true);		// Créer la fenêtre associée à l'opération
				operation.operationStart();																// Démarrer l'opération
				jd_operation.setVisible(true);															// Afficher la fenêtre
			} else {
				ViewHelper.dwarning(this, "La publication n'a pas encore été définie. Rendez-vous dans \"Configurer > Exportations et diffusions\".");
			}
		} else if (event.getSource() == this.jb_diffuser) {
			JDDiffusions dialog = new JDDiffusions(this,ContestOrg.get().getCtrlOut().getListeDiffusions());
			dialog.setVisible(true);
		} else if (event.getSource() == this.jb_configurer) {			
			JDConcoursEditer dialog = new JDConcoursEditer(
				this,
				ContestOrg.get().getConcours(),
				ContestOrg.get().getListeObjectifs(),
				ContestOrg.get().getCtrlPhasesQualificatives().getListeCriteresClassement(),
				ContestOrg.get().getCtrlOut().getListeExportations(),
				ContestOrg.get().getCtrlOut().getPublicationIndex(),
				ContestOrg.get().getCtrlOut().getListeDiffusions(),
				ContestOrg.get().getCtrlParticipants().getListePrix(),
				ContestOrg.get().getListeLieuxEmplacements(),
				ContestOrg.get().getCtrlParticipants().getListeProprietes()
			);
			dialog.setVisible(true);
		} else if (event.getSource() == this.jb_fermer) {
			if(ContestOrg.get().is(ContestOrg.STATE_SAVE) || ViewHelper.confirmation(this, "En fermant ce concours, vous perdrez toutes les modifications non sauvegardées. Désirez-vous continuer ?", true)) {
				ContestOrg.get().concoursFermer();
			}
		} else if (event.getSource() == this.jb_aide) {
			try {
				Desktop.getDesktop().open(new File(Tools.isWindows() ? "doc/documentation.chm" : "doc/documentation.html"));
			} catch (Exception e) {
				ViewHelper.derror(this, "Erreur de l'ouverture de l'aide de ContestOrg.");
			}
		} else if (event.getSource() == this.jb_web) {
			try {
				Desktop.getDesktop().browse(new URI("http://www.cyrilperrin.fr/contestorg/"));
			} catch (Exception e) {
				ViewHelper.derror(this, "Erreur lors de l'ouverture du site web de ContestOrg.");
			}
		} else if (event.getSource() == this.jb_apropos) {
			ViewHelper.dinformation(
				this, "A propos",
				"<font size=+1>Informations générales</font>\n" +
				"Version : "+ContestOrg.VERSION+"\n" +
				"Site web : <a href=\"#\">http://www.cyrilperrin.fr/contestorg/</a>\n" +
				"\n" +
				"ContestOrg est un logiciel d’organisation de tournois. Celui-ci est\n" +
				"particulièrement bien adapté aux tournois sportifs (football, judo,\n" +
				"rugby…) mais peut également convenir pour d'autres types de\n" +
				"tournois (jeux vidéo, jeux de société, robotique...).");
		} else if (event.getSource() == this.jb_quitter) {
			this.quit();
		}
	}

	/**
	 * @see IMoodyListener#moodyChanged(IMoody)
	 */
	@Override
	public void moodyChanged (IMoody moody) {
		// Mettre à jour l'état de certains boutons
		this.jb_sauvegarder.setEnabled(moody.is(ContestOrg.STATE_EDIT) && !moody.is(ContestOrg.STATE_SAVE));
		this.jb_sauvegarder_sous.setEnabled(moody.is(ContestOrg.STATE_EDIT));
		this.setTitle(!moody.is(ContestOrg.STATE_OPEN) || moody.is(ContestOrg.STATE_SAVE) ? this.titre : this.titre+" *");
		this.jb_exporter.setEnabled(moody.is(ContestOrg.STATE_OPEN));
		this.jb_publier.setEnabled(moody.is(ContestOrg.STATE_OPEN));
		this.jb_diffuser.setEnabled(moody.is(ContestOrg.STATE_OPEN));
		this.jb_configurer.setEnabled(moody.is(ContestOrg.STATE_EDIT));
		this.jb_fermer.setEnabled(moody.is(ContestOrg.STATE_OPEN));
		
		// Changer le nom de l'onglet des participants
		if(moody.is(ContestOrg.STATE_OPEN)) {
			this.jtb_onglets.setTitleAt(0, ContestOrg.get().getCtrlParticipants().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "Equipes" : "Joueurs");
			this.jtb_onglets.setToolTipTextAt(0, ContestOrg.get().getCtrlParticipants().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "Créer, éditer et supprimer des équipes" : "Créer, éditer et supprimer des joueurs");
		}
	}
}