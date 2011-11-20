package org.contestorg.views;


import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Window;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.contestorg.infos.InfosModelObjectif;
import org.contestorg.infos.InfosModelObjectifNul;
import org.contestorg.infos.InfosModelObjectifPoints;
import org.contestorg.infos.InfosModelObjectifPourcentage;
import org.contestorg.interfaces.ICollector;

@SuppressWarnings("serial")
public abstract class JDObjectifAbstract extends JDPattern implements ItemListener
{

	// Collector d'informations sur l'objectif
	protected ICollector<InfosModelObjectif> collector;

	// Panel des informations
	private JPanel jp_informations;

	protected final static String LABEL_OBJECTIF_POINTS = "Points";
	protected final static String LABEL_OBJECTIF_POURCENTAGE = "Pourcentage";
	protected final static String LABEL_OBJECTIF_NUL = "Nul";

	// Entrées
	protected JComboBox jcb_types;

	protected JTextField jtf_objectif_points_nom = new JTextField();
	protected JTextField jtf_objectif_points_points = new JTextField();
	protected JTextField jtf_objectif_points_borneParticipation = new JTextField();

	protected JTextField jtf_objectif_pourcentage_nom = new JTextField();
	protected JTextField jtf_objectif_pourcentage_pourcentage = new JTextField();
	protected JTextField jtf_objectif_pourcentage_borneParticipation = new JTextField();
	protected JTextField jtf_objectif_pourcentage_borneAugmentation = new JTextField();

	protected JTextField jtf_objectif_nul_nom = new JTextField();

	// Constructeur
	public JDObjectifAbstract(Window w_parent, String titre, ICollector<InfosModelObjectif> collector) {
		// Appeler le constructeur du parent
		super(w_parent, titre);

		// Retenir le collector
		this.collector = collector;

		// Type d'objectif
		this.jp_contenu.add(ViewHelper.title("Type d'objectif", ViewHelper.H1));
		String[] types = { JDObjectifAbstract.LABEL_OBJECTIF_POINTS, JDObjectifAbstract.LABEL_OBJECTIF_POURCENTAGE, JDObjectifAbstract.LABEL_OBJECTIF_NUL };
		this.jcb_types = new JComboBox(types);
		this.jp_contenu.add(this.jcb_types);

		// Informations de l'objectif
		this.jp_contenu.add(ViewHelper.title("Informations de l'objectif", ViewHelper.H1));
		this.jp_informations = new JPanel(new CardLayout());
		this.jp_contenu.add(this.jp_informations);

		// Objectif de points
		JPanel jp_objectif_points = new JPanel();
		jp_objectif_points.setLayout(new BoxLayout(jp_objectif_points, BoxLayout.Y_AXIS));
		JLabel[] jls_objectif_points = { new JLabel("Nom : "), new JLabel("Points : "), new JLabel("Borne de participation : ") };
		JComponent[] jcs_objectif_points = { this.jtf_objectif_points_nom, this.jtf_objectif_points_points, this.jtf_objectif_points_borneParticipation };
		jp_objectif_points.add(ViewHelper.inputs(jls_objectif_points, jcs_objectif_points));
		JPanel jp_objectif_points_hauteur = new JPanel(new BorderLayout());
		jp_objectif_points_hauteur.add(jp_objectif_points, BorderLayout.NORTH);
		this.jp_informations.add(jp_objectif_points_hauteur, JDObjectifAbstract.LABEL_OBJECTIF_POINTS);

		// Objectif de pourcentage
		JPanel jp_objectif_pourcentage = new JPanel();
		jp_objectif_pourcentage.setLayout(new BoxLayout(jp_objectif_pourcentage, BoxLayout.Y_AXIS));
		JLabel[] jls_objectif_pourcentage = { new JLabel("Nom : "), new JLabel("Pourcentage : "), new JLabel("Borne de participation : "), new JLabel("Borne d'augmentation : ") };
		JComponent[] jcs_objectif_pourcentage = { this.jtf_objectif_pourcentage_nom, this.jtf_objectif_pourcentage_pourcentage, this.jtf_objectif_pourcentage_borneParticipation, this.jtf_objectif_pourcentage_borneAugmentation };
		jp_objectif_pourcentage.add(ViewHelper.inputs(jls_objectif_pourcentage, jcs_objectif_pourcentage));
		JPanel jp_objectif_pourcentage_hauteur = new JPanel(new BorderLayout());
		jp_objectif_pourcentage_hauteur.add(jp_objectif_pourcentage, BorderLayout.NORTH);
		this.jp_informations.add(jp_objectif_pourcentage_hauteur, JDObjectifAbstract.LABEL_OBJECTIF_POURCENTAGE);

		// Objectif nul
		JPanel jp_objectif_nul = new JPanel();
		jp_objectif_nul.setLayout(new BoxLayout(jp_objectif_nul, BoxLayout.Y_AXIS));
		JLabel[] jls_objectif_nul = { new JLabel("Nom : ") };
		JComponent[] jcs_objectif_nul = { this.jtf_objectif_nul_nom };
		jp_objectif_nul.add(ViewHelper.inputs(jls_objectif_nul, jcs_objectif_nul));
		JPanel jp_objectif_nul_hauteur = new JPanel(new BorderLayout());
		jp_objectif_nul_hauteur.add(jp_objectif_nul, BorderLayout.NORTH);
		this.jp_informations.add(jp_objectif_nul_hauteur, JDObjectifAbstract.LABEL_OBJECTIF_NUL);

		// Bouton d'aide
		this.addButton(new JBHelperButton(this) {
			// Message d'aide
			@Override
			protected String message () {
				return "<font size=+1>Objectif à points :</font>\n" +
				       "Ajout d'un nombre de points au score du participant\n" +
				       "Informations :\n" +
				       "  - nom : nom de l'objectif\n" +
				       "  - points : nombre de points à créditer sur le score\n" +
				       "  - borne de score : borne que le score ne doit pas dépasser après crédit des points\n" +
				       "\n" +
				       "<font size=+1>Objectif à pourcentage :</font>\n" +
				       "Ajout d'un pourcentage au score du participant\n" +
				       "Informations :\n" +
				       "  - nom : nom de l'objectif\n" +
				       "  - pourcentage : pourcentage du score à ajouter au score\n" +
				       "  - borne de score : borne que le score ne doit pas dépasser après crédit des points\n" +
				       "  - borne d'augmentation : borne que l'augmentation du score ne doit pas dépasser\n" +
				       "\n" +
				       "<font size=+1>Objectif nul :</font>\n" +
				       "Aucun effet sur le score\n" +
				       "Informations :\n" +
				       "  - nom : nom de l'objectif";
			}
		});

		// Ecouter la liste
		this.jcb_types.addItemListener(this);

		// Pack
		this.pack();
	}

	// Implémentation de ok
	@Override
	protected void ok () {
		// Récupérer le type séléctionné
		String selectedType = (String)this.jcb_types.getSelectedItem();

		// Booléen d'erreur
		boolean erreur = false;

		// Initialiser les informations
		InfosModelObjectif infos = null;

		// Vérifier les données et créer les informations
		if (selectedType.equals(JDObjectifAbstract.LABEL_OBJECTIF_POINTS)) {
			// Récupérer les données
			String nom = this.jtf_objectif_points_nom.getText().trim();
			String points = this.jtf_objectif_points_points.getText().trim();
			String borneScore = this.jtf_objectif_points_borneParticipation.getText().trim();

			// Vérifier les données
			if (nom.isEmpty()) {
				ViewHelper.derror(this, "Le nom de l'objectif n'est pas précisé.");
				erreur = true;
			}
			if (points.isEmpty()) {
				ViewHelper.derror(this, "Le nombre de points de l'objectif n'est pas précisé.");
				erreur = true;
			}

			// Parser les données
			double pointsParse = 0;
			Double borneScoreParse = null;
			if (!points.isEmpty()) {
				try {
					pointsParse = Double.parseDouble(points);
				} catch (NumberFormatException e) {
					ViewHelper.derror(this, "Le nombre de points de l'objectif doit être un nombre décimale.");
					erreur = true;
				}
			}
			if (!borneScore.isEmpty()) {
				try {
					borneScoreParse = Double.parseDouble(borneScore);
				} catch (NumberFormatException e) {
					ViewHelper.derror(this, "La borne de score de l'objectif doit être un nombre décimale.");
					erreur = true;
				}
			}

			// Créer les informations
			if (!erreur) {
				infos = new InfosModelObjectifPoints(nom, pointsParse, borneScoreParse);
			}
		} else if (selectedType.equals(JDObjectifAbstract.LABEL_OBJECTIF_POURCENTAGE)) {
			// Récupérer les données
			String nom = this.jtf_objectif_pourcentage_nom.getText().trim();
			String points = this.jtf_objectif_pourcentage_pourcentage.getText().trim();
			String borneScore = this.jtf_objectif_pourcentage_borneParticipation.getText().trim();
			String borneAugmentation = this.jtf_objectif_pourcentage_borneAugmentation.getText().trim();

			// Vérifier les données
			if (nom.isEmpty()) {
				ViewHelper.derror(this, "Le nom de l'objectif n'est pas précisé.");
				erreur = true;
			}
			if (points.isEmpty()) {
				ViewHelper.derror(this, "Le nombre de points de l'objectif n'est pas précisé.");
				erreur = true;
			}

			// Parser les données
			double pointsParse = 0;
			Double borneScoreParse = null, borneAugmentationParse = null;
			if (!points.isEmpty()) {
				try {
					pointsParse = Double.parseDouble(points);
				} catch (NumberFormatException e) {
					ViewHelper.derror(this, "Le nombre de points de l'objectif doit être un nombre décimale.");
					erreur = true;
				}
			}
			if (!borneScore.isEmpty()) {
				try {
					borneScoreParse = Double.parseDouble(borneScore);
				} catch (NumberFormatException e) {
					ViewHelper.derror(this, "La borne de score de l'objectif doit être un nombre décimale.");
					erreur = true;
				}
			}
			if (!borneAugmentation.isEmpty()) {
				try {
					borneAugmentationParse = Double.parseDouble(borneAugmentation);
				} catch (NumberFormatException e) {
					ViewHelper.derror(this, "La borne d'augmentation de l'objectif doit être un nombre décimale.");
					erreur = true;
				}
			}

			// Créer les informations
			if (!erreur) {
				infos = new InfosModelObjectifPourcentage(nom, pointsParse, borneScoreParse, borneAugmentationParse);
			}
		} else if (selectedType.equals(JDObjectifAbstract.LABEL_OBJECTIF_NUL)) {
			// Récupérer les données
			String nom = this.jtf_objectif_nul_nom.getText().trim();

			// Vérifier les données
			if (nom.isEmpty()) {
				ViewHelper.derror(this, "Le nom de l'objectif n'est pas précisé.");
				erreur = true;
			}

			// Créer les informations
			if (!erreur) {
				infos = new InfosModelObjectifNul(nom);
			}
		}

		// Envoyer les informations au collector
		if (infos != null) {
			this.collector.accept(infos);
		}
	}

	// Implémentation de quit
	@Override
	protected void quit () {
		// Annuler
		this.collector.cancel();
	}

	// Implémentation de ItemListener
	@Override
	public void itemStateChanged (ItemEvent event) {
		CardLayout layout = (CardLayout)(this.jp_informations.getLayout());
		layout.show(this.jp_informations, (String)event.getItem());
	}

}
