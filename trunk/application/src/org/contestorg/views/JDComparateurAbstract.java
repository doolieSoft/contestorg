package org.contestorg.views;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Window;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.contestorg.infos.InfosModelCompPhasesQualifsAbstract;
import org.contestorg.infos.InfosModelCompPhasesQualifsObjectif;
import org.contestorg.infos.InfosModelCompPhasesQualifsPoints;
import org.contestorg.infos.InfosModelCompPhasesQualifsVictoires;
import org.contestorg.infos.InfosModelObjectif;
import org.contestorg.interfaces.ICollector;

/**
 * Boîte de dialogue de création/édition d'un comparateur en vue de générer les phases qualificatives
 */
@SuppressWarnings("serial")
public abstract class JDComparateurAbstract extends JDPattern implements ItemListener
{
	/** Collecteur des informations du comparateur */
	private ICollector<InfosModelCompPhasesQualifsAbstract> collector;
	
	/** Liste des objectifs */
	private ArrayList<InfosModelObjectif> objectifs;
	
	/** Panel des informations */
	private JPanel jp_informations;

	// Labels
	
	/** Label pour un comparateur de points */
	protected final static String LABEL_COMPARATEUR_POINTS = "Nombre de points";
	
	/** Label pour un comparateur de victoires */
	protected final static String LABEL_COMPARATEUR_VICTOIRES = "Nombre de victoires";
	
	/** Label pour un comparateur de remportés */
	protected final static String LABEL_COMPARATEUR_OBJECTIF = "Nombre d'objectifs remportés";
	
	// Entrées
	
	/** Liste des types */
	protected JComboBox<String> jcb_types;
	
	/** Liste des objectifs */
	protected JComboBox<String> jcb_objectifs;

	/**
	 * Constructeur
	 * @param w_parent fenêtre parent
	 * @param titre titre de la boîte de dialogue
	 * @param collector collecteur des informations du comparateur
	 * @param objectifs liste des objectifs
	 */
	public JDComparateurAbstract(Window w_parent, String titre, ICollector<InfosModelCompPhasesQualifsAbstract> collector, ArrayList<InfosModelObjectif> objectifs) {
		// Appeller le constructeur du parent
		super(w_parent, titre);

		// Retenir le collector et les objectifs
		this.collector = collector;
		this.objectifs = objectifs;

		// Type de comparateur
		this.jp_contenu.add(ViewHelper.title("Type de critère", ViewHelper.H1));
		String[] types = { JDComparateurAbstract.LABEL_COMPARATEUR_POINTS, JDComparateurAbstract.LABEL_COMPARATEUR_VICTOIRES, JDComparateurAbstract.LABEL_COMPARATEUR_OBJECTIF };
		this.jcb_types = new JComboBox<String>(types);
		this.jp_contenu.add(this.jcb_types);

		// Informations du comparateur
		this.jp_contenu.add(ViewHelper.title("Informations du critère", ViewHelper.H1));
		this.jp_informations = new JPanel(new CardLayout());
		this.jp_contenu.add(this.jp_informations);
		
		// Comparateur de nombre de points
		JPanel jp_comparateur_points = new JPanel();
		jp_comparateur_points.setLayout(new BoxLayout(jp_comparateur_points, BoxLayout.Y_AXIS));
		
		jp_comparateur_points.add(new JLabel("Pas d'informations à spécifier."));
		
		JPanel jp_objectif_points_hauteur = new JPanel(new BorderLayout());
		jp_objectif_points_hauteur.add(jp_comparateur_points, BorderLayout.NORTH);
		this.jp_informations.add(jp_objectif_points_hauteur, JDComparateurAbstract.LABEL_COMPARATEUR_POINTS);
		
		// Comparateur de nombre de victoires
		JPanel jp_comparateur_victoires = new JPanel();
		jp_comparateur_victoires.setLayout(new BoxLayout(jp_comparateur_victoires, BoxLayout.Y_AXIS));
		
		jp_comparateur_victoires.add(new JLabel("Pas d'informations à spécifier."));
		
		JPanel jp_objectif_victoires_hauteur = new JPanel(new BorderLayout());
		jp_objectif_victoires_hauteur.add(jp_comparateur_victoires, BorderLayout.NORTH);
		this.jp_informations.add(jp_objectif_victoires_hauteur, JDComparateurAbstract.LABEL_COMPARATEUR_VICTOIRES);
		
		// Comparateur de nombre d'objectifs remportés
		JPanel jp_comparateur_objectif = new JPanel();
		jp_comparateur_objectif.setLayout(new BoxLayout(jp_comparateur_objectif, BoxLayout.Y_AXIS));
		
		this.jcb_objectifs = new JComboBox<String>();
		if(this.objectifs.size() > 0) {
			for(InfosModelObjectif objectif : this.objectifs) {
				this.jcb_objectifs.addItem(objectif.getNom());
			}
		} else {
			this.jcb_objectifs.setEnabled(false);
		}
		
		JLabel[] jls_comparateur_objectif = { new JLabel("Objectif : ") };
		JComponent[] jcs_comparateur_objectif = { this.jcb_objectifs };
		jp_comparateur_objectif.add(ViewHelper.inputs(jls_comparateur_objectif, jcs_comparateur_objectif));
		
		JPanel jp_objectif_objectif_hauteur = new JPanel(new BorderLayout());
		jp_objectif_objectif_hauteur.add(jp_comparateur_objectif, BorderLayout.NORTH);
		this.jp_informations.add(jp_objectif_objectif_hauteur, JDComparateurAbstract.LABEL_COMPARATEUR_OBJECTIF);

		// Ecouter la liste
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
		InfosModelCompPhasesQualifsAbstract infos = null;

		// Vérifier les données et créer les informations
		if(selectedType.equals(JDComparateurAbstract.LABEL_COMPARATEUR_POINTS)) {
			// Créer les informations
			infos = new InfosModelCompPhasesQualifsPoints();
		} else if(selectedType.equals(JDComparateurAbstract.LABEL_COMPARATEUR_VICTOIRES)) {
			// Créer les informations
			infos = new InfosModelCompPhasesQualifsVictoires();
		} else if(selectedType.equals(JDComparateurAbstract.LABEL_COMPARATEUR_OBJECTIF)) {
			// Créer les informations
			if(this.jcb_objectifs.getItemCount() > 0) {
				infos = new InfosModelCompPhasesQualifsObjectif(this.objectifs.get(this.jcb_objectifs.getSelectedIndex()));
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
		layout.show(this.jp_informations, (String)event.getItem());
	}

}
