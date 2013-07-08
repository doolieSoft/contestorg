package org.contestorg.views;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Window;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.contestorg.infos.InfosModelCritereClassementAbstract;
import org.contestorg.infos.InfosModelCritereClassementGoalAverage;
import org.contestorg.infos.InfosModelCritereClassementNbDefaites;
import org.contestorg.infos.InfosModelCritereClassementNbEgalites;
import org.contestorg.infos.InfosModelCritereClassementNbForfaits;
import org.contestorg.infos.InfosModelCritereClassementNbPoints;
import org.contestorg.infos.InfosModelCritereClassementNbVictoires;
import org.contestorg.infos.InfosModelCritereClassementQuantiteObjectif;
import org.contestorg.infos.InfosModelCritereClassementRencontresDirectes;
import org.contestorg.infos.InfosModelObjectif;
import org.contestorg.interfaces.ICollector;

/**
 * Boîte de dialogue de création/édition d'un critère de classement
 */
@SuppressWarnings("serial")
public abstract class JDCritereClassementAbstract extends JDPattern implements ItemListener
{
	/** Collecteur des informations du critère de classement */
	private ICollector<InfosModelCritereClassementAbstract> collector;
	
	/** Liste des objectifs */
	private ArrayList<InfosModelObjectif> objectifs;
	
	/** Panel des informations */
	private JPanel jp_informations;

	// Labels des critères de classement
	
	/** Label pour un critère de classement de nombre de points */
	protected final static String LABEL_CRITERE_CLASSEMENT_NB_POINTS = "Nombre de points";
	
	/** Label pour un critère de classement de nombre de victoires */
	protected final static String LABEL_CRITERE_CLASSEMENT_NB_VICTOIRES = "Nombre de victoires";
	
	/** Label pour un critère de classement de nombre d'égalités */
	protected final static String LABEL_CRITERE_CLASSEMENT_NB_EGALITES = "Nombre d'égalités";
	
	/** Label pour un critère de classement de nombre de défaites */
	protected final static String LABEL_CRITERE_CLASSEMENT_NB_DEFAITES = "Nombre de défaites";
	
	/** Label pour un critère de classement de nombre de forfaits */
	protected final static String LABEL_CRITERE_CLASSEMENT_NB_FORFAITS = "Nombre de forfaits";
	
	/** Label pour un critère de classement des rencontres directes */
	protected final static String LABEL_CRITERE_CLASSEMENT_RENCONTRE_DIRECTES = "Résultat des rencontres directes";
	
	/** Label pour un critère de classement de quantité remportée d'un objectif */
	protected final static String LABEL_CRITERE_CLASSEMENT_QUANTITE_OBJECTIF = "Quantité remportée d'un objectif";
	
	/** Label pour un critère de classement de goal-average */
	protected final static String LABEL_CRITERE_CLASSEMENT_GOALAVERAGE = "Goal-Average";
	
	// Entrées
	
	/** Liste des types de critères de classement */
	protected JComboBox<String> jcb_types;
	
	/** Est-ce que le critère est inversé ? */
	protected JCheckBox jcb_isInverse;
	
	// Entrées pour le critère "quantité remportée d'un objectif"
	
	/** Liste des objectifs */
	protected JComboBox<String> jcb_quantiteobjectif_objectifs;
	
	// Entrées pour le critère "goal-average"

	/** Bouton radio pour séléctionner le type "général" */
	protected JRadioButton jrb_goalaverage_type_general;
	/** Bouton radio pour séléctionner le type "particulier" */
	protected JRadioButton jrb_goalaverage_type_particulier;
	/** Bouton radio pour séléctionner la méthode "différence" */
	protected JRadioButton jrb_goalaverage_methode_difference;
	/** Bouton radio pour séléctionner la méthode "division" */
	protected JRadioButton jrb_goalaverage_methode_division;
	/** Bouton radio pour séléctionner la donnée "nombre de points" */
	protected JRadioButton jrb_goalaverage_donnee_points;
	/** Bouton radio pour séléctionner la donnée "résultat (victoires/défaites)" */
	protected JRadioButton jrb_goalaverage_donnee_resultat;
	/** Bouton radio pour séléctionner la donnée "quantité remportée d'un objectif" */
	protected JRadioButton jrb_goalaverage_donnee_quantiteobjectif;
	/** Liste des objectifs */
	protected JComboBox<String> jcb_goalaverage_objectifs;
	
	/**
	 * Constructeur
	 * @param w_parent fenêtre parent
	 * @param titre titre de la boîte de dialogue
	 * @param collector collecteur des informations du critère de classement
	 * @param objectifs liste des objectifs
	 */
	public JDCritereClassementAbstract(Window w_parent, String titre, ICollector<InfosModelCritereClassementAbstract> collector, ArrayList<InfosModelObjectif> objectifs) {
		// Appeller le constructeur du parent
		super(w_parent, titre);

		// Retenir le collector et les objectifs
		this.collector = collector;
		this.objectifs = objectifs;

		// Type de critère de classement
		this.jp_contenu.add(ViewHelper.title("Type de critère", ViewHelper.H1));
		String[] types = {
			JDCritereClassementAbstract.LABEL_CRITERE_CLASSEMENT_NB_POINTS,
			JDCritereClassementAbstract.LABEL_CRITERE_CLASSEMENT_NB_VICTOIRES,
			JDCritereClassementAbstract.LABEL_CRITERE_CLASSEMENT_NB_EGALITES,
			JDCritereClassementAbstract.LABEL_CRITERE_CLASSEMENT_NB_DEFAITES,
			JDCritereClassementAbstract.LABEL_CRITERE_CLASSEMENT_NB_FORFAITS,
			JDCritereClassementAbstract.LABEL_CRITERE_CLASSEMENT_RENCONTRE_DIRECTES,
			JDCritereClassementAbstract.LABEL_CRITERE_CLASSEMENT_QUANTITE_OBJECTIF,
			JDCritereClassementAbstract.LABEL_CRITERE_CLASSEMENT_GOALAVERAGE,
		};
		this.jcb_types = new JComboBox<String>(types);
		this.jp_contenu.add(this.jcb_types);

		// Informations du critère de classement
		this.jp_contenu.add(ViewHelper.title("Informations du critère", ViewHelper.H1));
		this.jcb_isInverse = new JCheckBox("Critère inversé ?");
		this.jp_contenu.add(ViewHelper.left(this.jcb_isInverse));
		this.jp_contenu.add(Box.createVerticalStrut(5));
		this.jp_informations = new JPanel(new CardLayout());
		this.jp_contenu.add(this.jp_informations);
		
		// Nombre de points
		JPanel jp_nbpoints = new JPanel();
		jp_nbpoints.setLayout(new BoxLayout(jp_nbpoints, BoxLayout.Y_AXIS));
		
		//jp_critere_classement_nb_points.add(*);
		
		JPanel jp_nbpoints_hauteur = new JPanel(new BorderLayout());
		jp_nbpoints_hauteur.add(jp_nbpoints, BorderLayout.NORTH);
		this.jp_informations.add(jp_nbpoints_hauteur, JDCritereClassementAbstract.LABEL_CRITERE_CLASSEMENT_NB_POINTS);
		
		// Nombre de victoires
		JPanel jp_nbvictoires = new JPanel();
		jp_nbvictoires.setLayout(new BoxLayout(jp_nbvictoires, BoxLayout.Y_AXIS));
		
		//jp_critere_classement_nb_victoires.add(*);
		
		JPanel jp_nbvictoires_hauteur = new JPanel(new BorderLayout());
		jp_nbvictoires_hauteur.add(jp_nbvictoires, BorderLayout.NORTH);
		this.jp_informations.add(jp_nbvictoires_hauteur, JDCritereClassementAbstract.LABEL_CRITERE_CLASSEMENT_NB_VICTOIRES);
		
		// Nombre d'égalités
		JPanel jp_nbegalites = new JPanel();
		jp_nbegalites.setLayout(new BoxLayout(jp_nbegalites, BoxLayout.Y_AXIS));
		
		//jp_critere_classement_nb_egalites.add(*);
		
		JPanel jp_nbegalites_hauteur = new JPanel(new BorderLayout());
		jp_nbegalites_hauteur.add(jp_nbegalites, BorderLayout.NORTH);
		this.jp_informations.add(jp_nbegalites_hauteur, JDCritereClassementAbstract.LABEL_CRITERE_CLASSEMENT_NB_EGALITES);
		
		// Nombre de défaites
		JPanel jp_nbdefaites = new JPanel();
		jp_nbdefaites.setLayout(new BoxLayout(jp_nbdefaites, BoxLayout.Y_AXIS));
		
		//jp_critere_classement_nb_defaites.add(*);
		
		JPanel jp_nbdefaites_hauteur = new JPanel(new BorderLayout());
		jp_nbdefaites_hauteur.add(jp_nbdefaites, BorderLayout.NORTH);
		this.jp_informations.add(jp_nbdefaites_hauteur, JDCritereClassementAbstract.LABEL_CRITERE_CLASSEMENT_NB_DEFAITES);
		
		// Nombre de forfaits
		JPanel jp_nbforfaits = new JPanel();
		jp_nbforfaits.setLayout(new BoxLayout(jp_nbforfaits, BoxLayout.Y_AXIS));
		
		//jp_critere_classement_nb_defaites.add(*);
		
		JPanel jp_nbforfaits_hauteur = new JPanel(new BorderLayout());
		jp_nbforfaits_hauteur.add(jp_nbforfaits, BorderLayout.NORTH);
		this.jp_informations.add(jp_nbforfaits_hauteur, JDCritereClassementAbstract.LABEL_CRITERE_CLASSEMENT_NB_DEFAITES);
		
		// Rencontres directes
		JPanel jp_rencontres_directes = new JPanel();
		jp_rencontres_directes.setLayout(new BoxLayout(jp_rencontres_directes, BoxLayout.Y_AXIS));
		
		//jp_critere_classement_nb_defaites.add(*);
		
		JPanel jp_rencontres_directes_hauteur = new JPanel(new BorderLayout());
		jp_rencontres_directes_hauteur.add(jp_rencontres_directes, BorderLayout.NORTH);
		this.jp_informations.add(jp_rencontres_directes_hauteur, JDCritereClassementAbstract.LABEL_CRITERE_CLASSEMENT_RENCONTRE_DIRECTES);
		
		// Quantité remportée d'un objectif
		JPanel jp_quantiteobjectif = new JPanel();
		jp_quantiteobjectif.setLayout(new BoxLayout(jp_quantiteobjectif, BoxLayout.Y_AXIS));
		
		this.jcb_quantiteobjectif_objectifs = new JComboBox<String>();
		if(this.objectifs.size() > 0) {
			for(InfosModelObjectif objectif : this.objectifs) {
				this.jcb_quantiteobjectif_objectifs.addItem(objectif.getNom());
			}
		} else {
			this.jcb_quantiteobjectif_objectifs.setEnabled(false);
		}
		
		JLabel[] jls_quantiteobjectif = { new JLabel("Objectif : ") };
		JComponent[] jcs_quantiteobjectif = { this.jcb_quantiteobjectif_objectifs };
		jp_quantiteobjectif.add(ViewHelper.inputs(jls_quantiteobjectif, jcs_quantiteobjectif));
		
		JPanel jp_quantiteobjectif_hauteur = new JPanel(new BorderLayout());
		jp_quantiteobjectif_hauteur.add(jp_quantiteobjectif, BorderLayout.NORTH);
		this.jp_informations.add(jp_quantiteobjectif_hauteur, JDCritereClassementAbstract.LABEL_CRITERE_CLASSEMENT_QUANTITE_OBJECTIF);
		
		// Goal-average
		JPanel jp_goalaverage = new JPanel();
		jp_goalaverage.setLayout(new BoxLayout(jp_goalaverage, BoxLayout.Y_AXIS));
		
		ButtonGroup bg_goalaverage_type = new ButtonGroup();
		this.jrb_goalaverage_type_general = new JRadioButton("général");
		bg_goalaverage_type.add(this.jrb_goalaverage_type_general);
		this.jrb_goalaverage_type_particulier = new JRadioButton("particulier");
		bg_goalaverage_type.add(this.jrb_goalaverage_type_particulier);

		JPanel jp_goalaverage_type = new JPanel();
		jp_goalaverage_type.setLayout(new BoxLayout(jp_goalaverage_type, BoxLayout.Y_AXIS));
		jp_goalaverage_type.add(this.jrb_goalaverage_type_general);
		jp_goalaverage_type.add(this.jrb_goalaverage_type_particulier);
		
		ButtonGroup bg_goalaverage_methode = new ButtonGroup();
		this.jrb_goalaverage_methode_difference = new JRadioButton("par différence");
		bg_goalaverage_methode.add(this.jrb_goalaverage_methode_difference);
		this.jrb_goalaverage_methode_division = new JRadioButton("par division");
		bg_goalaverage_methode.add(this.jrb_goalaverage_methode_division);

		JPanel jp_goalaverage_methode = new JPanel();
		jp_goalaverage_methode.setLayout(new BoxLayout(jp_goalaverage_methode, BoxLayout.Y_AXIS));
		jp_goalaverage_methode.add(this.jrb_goalaverage_methode_difference);
		jp_goalaverage_methode.add(this.jrb_goalaverage_methode_division);

		ButtonGroup bg_goalaverage_donnee = new ButtonGroup();
		this.jrb_goalaverage_donnee_points = new JRadioButton("nombre de points");
		bg_goalaverage_donnee.add(this.jrb_goalaverage_donnee_points);
		this.jrb_goalaverage_donnee_resultat = new JRadioButton("nombre de victoires");
		bg_goalaverage_donnee.add(this.jrb_goalaverage_donnee_resultat);
		this.jrb_goalaverage_donnee_quantiteobjectif = new JRadioButton("quantité remportée d'un objectif :");
		bg_goalaverage_donnee.add(this.jrb_goalaverage_donnee_quantiteobjectif);
		
		this.jrb_goalaverage_type_general.setSelected(true);
		this.jrb_goalaverage_methode_difference.setSelected(true);
		this.jrb_goalaverage_donnee_points.setSelected(true);
		
		JPanel jp_goalaverage_donnee = new JPanel();
		jp_goalaverage_donnee.setLayout(new BoxLayout(jp_goalaverage_donnee, BoxLayout.Y_AXIS));
		jp_goalaverage_donnee.add(this.jrb_goalaverage_donnee_points);
		jp_goalaverage_donnee.add(this.jrb_goalaverage_donnee_resultat);
		jp_goalaverage_donnee.add(this.jrb_goalaverage_donnee_quantiteobjectif);
		
		this.jcb_goalaverage_objectifs = new JComboBox<String>();
		this.jcb_goalaverage_objectifs.setEnabled(false);
		if(this.objectifs.size() > 0) {
			for(InfosModelObjectif objectif : this.objectifs) {
				this.jcb_goalaverage_objectifs.addItem(objectif.getNom());
			}
		} else {
			this.jcb_goalaverage_objectifs.setEnabled(false);
		}

		this.jrb_goalaverage_donnee_quantiteobjectif.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged (ChangeEvent e) {
				jcb_goalaverage_objectifs.setEnabled(jrb_goalaverage_donnee_quantiteobjectif.isSelected());
			}
		});
		
		JLabel[] jls_goalaverage = { new JLabel("Type : "),new JLabel("Méthode : "),new JLabel("Donnée : "),new JLabel("") };
		JComponent[] jcs_goalaverage = { jp_goalaverage_type,jp_goalaverage_methode,jp_goalaverage_donnee,this.jcb_goalaverage_objectifs };
		jp_goalaverage.add(ViewHelper.inputs(jls_goalaverage, jcs_goalaverage));
		
		JPanel jp_goalaverage_hauteur = new JPanel(new BorderLayout());
		jp_goalaverage_hauteur.add(jp_goalaverage, BorderLayout.NORTH);
		this.jp_informations.add(jp_goalaverage_hauteur, JDCritereClassementAbstract.LABEL_CRITERE_CLASSEMENT_GOALAVERAGE);
		
		// Bouton d'aide
		this.addButton(new JBHelperButton(this) {
			// Message d'aide
			@Override
			protected String getMessage () {
				return "<font size=+1>Nombre de points :</font>\n" +
					   "Les meilleurs participants sont ceux qui ont le plus grand nombre de points.\n" +
					   "\n" +
					   "<font size=+1>Nombre de victoires :</font>\n" +
				       "Les meilleurs participants sont ceux qui ont le plus grand nombre de victoires.\n"+
					   "\n" +
					   "<font size=+1>Nombre d'égalités :</font>\n" +
				       "Les meilleurs participants sont ceux qui ont le plus grand nombre d'égalités.\n"+
					   "\n" +
					   "<font size=+1>Nombre de défaites :</font>\n" +
				       "Les meilleurs participants sont ceux qui ont le plus grand nombre de défaites.\n"+
					   "Remarque : il est conseillé d'inverser ce critère.\n" +
					   "\n" +
					   "<font size=+1>Nombre de forfaits :</font>\n" +
				       "Les meilleurs participants sont ceux qui ont le plus grand nombre de forfaits.\n"+
					   "Remarque : il est conseillé d'inverser ce critère.\n" +
					   "\n" +
					   "<font size=+1>Résultat des rencontres directes :</font>\n" +
				       "Départage entre deux ex-aequos sur le résultat des matchs disputés par les deux ex-aequos.\n"+
					   "\n" +
					   "<font size=+1>Quantité remportée d'un objectif :</font>\n" +
				       "Les meilleurs participants sont ceux qui ont la plus grande quantité remportée d'un objectif.\n"+
					   "\n" +
					   "<font size=+1>Goal-Average :</font>\n" +
				       "Les meilleurs participants sont ceux qui ont le plus grand goal-average.\n" +
				       "Informations :\n" +
				       "- type : général ou particulier (départage deux ex-aequos et seulement deux)\n" +
				       "- méthode : méthode utilisée pour le calcul du goal-average\n" +
				       "- donnée : donnée utilisée pour le calcul du goal-average\n" +
				       "\n" +
				       "<i>Pour plus d'information, rendez-vous dans l'aide.</i>";
			}
		});

		// Ecouter la liste des types
		this.jcb_types.addItemListener(this);
		
		// Pack
		this.pack();
	}

	/**
	 * @see JDPattern#ok()
	 */
	@Override
	protected void ok () {
		// Récupérer le type séléctionné
		String selectedType = (String)this.jcb_types.getSelectedItem();
		
		// Initialiser les informations
		InfosModelCritereClassementAbstract infos = null;

		// Vérifier les données et créer les informations
		if(selectedType.equals(JDCritereClassementAbstract.LABEL_CRITERE_CLASSEMENT_NB_POINTS)) {
			// Créer les informations
			infos = new InfosModelCritereClassementNbPoints(this.jcb_isInverse.isSelected());
			
			// Demander confirmer si nécessaire
			if(this.jcb_isInverse.isSelected()) {
				if(!ViewHelper.confirmation(this, "Si le critère \""+selectedType+"\" est inversé, les meilleurs participants seront ceux qui ont le plus petit nombre de points. Désirez-vous continuer ?", true)) {
					infos = null;
				}
			}
		} else if(selectedType.equals(JDCritereClassementAbstract.LABEL_CRITERE_CLASSEMENT_NB_VICTOIRES)) {
			// Créer les informations
			infos = new InfosModelCritereClassementNbVictoires(this.jcb_isInverse.isSelected());
			
			// Demander confirmer si nécessaire
			if(this.jcb_isInverse.isSelected()) {
				if(!ViewHelper.confirmation(this, "Si le critère \""+selectedType+"\" est inversé, les meilleurs participants seront ceux qui ont le plus petit nombre de victoires. Désirez-vous continuer ?", true)) {
					infos = null;
				}
			}
		} else if(selectedType.equals(JDCritereClassementAbstract.LABEL_CRITERE_CLASSEMENT_NB_EGALITES)) {
			// Créer les informations
			infos = new InfosModelCritereClassementNbEgalites(this.jcb_isInverse.isSelected());
		} else if(selectedType.equals(JDCritereClassementAbstract.LABEL_CRITERE_CLASSEMENT_NB_DEFAITES)) {
			// Créer les informations
			infos = new InfosModelCritereClassementNbDefaites(this.jcb_isInverse.isSelected());
			
			// Demander confirmer si nécessaire
			if(!this.jcb_isInverse.isSelected()) {
				if(!ViewHelper.confirmation(this, "Si le critère \""+selectedType+"\" n'est pas inversé, les meilleurs participants seront ceux qui ont le plus grand nombre de défaites. Désirez-vous continuer ?", true)) {
					infos = null;
				}
			}
		} else if(selectedType.equals(JDCritereClassementAbstract.LABEL_CRITERE_CLASSEMENT_NB_FORFAITS)) {
			// Créer les informations
			infos = new InfosModelCritereClassementNbForfaits(this.jcb_isInverse.isSelected());
			
			// Demander confirmer si nécessaire
			if(!this.jcb_isInverse.isSelected()) {
				if(!ViewHelper.confirmation(this, "Si le critère \""+selectedType+"\" n'est pas inversé, les meilleurs participants seront ceux qui ont le plus grand nombre de forfaits. Désirez-vous continuer ?", true)) {
					infos = null;
				}
			}
		} else if(selectedType.equals(JDCritereClassementAbstract.LABEL_CRITERE_CLASSEMENT_RENCONTRE_DIRECTES)) {
			// Créer les informations
			infos = new InfosModelCritereClassementRencontresDirectes(this.jcb_isInverse.isSelected());
			
			// Demander confirmer si nécessaire
			if(this.jcb_isInverse.isSelected()) {
				if(!ViewHelper.confirmation(this, "Si le critère \""+selectedType+"\" est inversé, cela donnera avantage au participant qui a le moins de victoires lors d'un départage entre deux ex-aequos. Désirez-vous continuer ?", true)) {
					infos = null;
				}
			}
		} else if(selectedType.equals(JDCritereClassementAbstract.LABEL_CRITERE_CLASSEMENT_QUANTITE_OBJECTIF)) {
			// Créer les informations
			if(this.jcb_quantiteobjectif_objectifs.getItemCount() > 0) {
				infos = new InfosModelCritereClassementQuantiteObjectif(this.jcb_isInverse.isSelected(),this.objectifs.get(this.jcb_quantiteobjectif_objectifs.getSelectedIndex()));
			} else {
				// Message d'erreur
				ViewHelper.derror(this, "Il faut d'abord créer au moins un objectif avant de créer ce type de critère.");
			}
		} else if(selectedType.equals(JDCritereClassementAbstract.LABEL_CRITERE_CLASSEMENT_GOALAVERAGE)) {
			// Vérifier s'il est possible de créer le critère de classement
			if(!this.jrb_goalaverage_donnee_quantiteobjectif.isSelected() || this.jcb_goalaverage_objectifs.getItemCount() > 0) {
				// Créer les informations
				int type;
				if(this.jrb_goalaverage_type_general.isSelected()) {
					type = InfosModelCritereClassementGoalAverage.TYPE_GENERAL;
				} else {
					type = InfosModelCritereClassementGoalAverage.TYPE_PARTICULIER;
				}
				int methode;
				if(this.jrb_goalaverage_methode_difference.isSelected()) {
					methode = InfosModelCritereClassementGoalAverage.METHODE_DIFFERENCE;
				} else {
					methode = InfosModelCritereClassementGoalAverage.METHODE_DIVISION;
				}
				int donnee;
				InfosModelObjectif objectif = null;
				if(this.jrb_goalaverage_donnee_points.isSelected()) {
					donnee = InfosModelCritereClassementGoalAverage.DONNEE_POINTS;
				} else if(this.jrb_goalaverage_donnee_resultat.isSelected()) {
					donnee = InfosModelCritereClassementGoalAverage.DONNEE_RESULTAT;
				} else {
					donnee = InfosModelCritereClassementGoalAverage.DONNEE_QUANTITE_OBJECTIF;
					objectif = this.objectifs.get(this.jcb_goalaverage_objectifs.getSelectedIndex());
				}
				infos = new InfosModelCritereClassementGoalAverage(this.jcb_isInverse.isSelected(),type,methode,donnee,objectif);
				
				// Demander confirmer si nécessaire
				if(this.jcb_isInverse.isSelected()) {
					if(!ViewHelper.confirmation(this, "Si le critère \""+selectedType+"\" est inversé, les meilleurs participants seront ceux qui ont le plus petit goal-average. Désirez-vous continuer ?", true)) {
						infos = null;
					}
				}
			} else {
				// Message d'erreur
				ViewHelper.derror(this, "Il faut d'abord créer au moins un objectif avant de créer ce type de critère.");
			}
		}

		// Envoyer les informations au collector
		if (infos != null) {
			this.collector.collect(infos);
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
	 * @see ItemListener#itemStateChanged(ItemEvent)
	 */
	@Override
	public void itemStateChanged (ItemEvent event) {
		CardLayout layout = (CardLayout)(this.jp_informations.getLayout());
		String type = (String)event.getItem();
		layout.show(this.jp_informations, type);
		this.jcb_isInverse.setSelected(type.equals(JDCritereClassementAbstract.LABEL_CRITERE_CLASSEMENT_NB_DEFAITES) || type.equals(JDCritereClassementAbstract.LABEL_CRITERE_CLASSEMENT_NB_FORFAITS));
	}

}
