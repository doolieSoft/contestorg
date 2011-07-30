package views;

import infos.InfosModelCompPhasesQualifsAbstract;
import infos.InfosModelCompPhasesQualifsObjectif;
import infos.InfosModelCompPhasesQualifsPoints;
import infos.InfosModelCompPhasesQualifsVictoires;
import infos.InfosModelObjectif;
import interfaces.ICollector;

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

@SuppressWarnings("serial")
public abstract class JDComparateurAbstract extends JDPattern implements ItemListener
{
	// Collector d'informations sur la diffusion
	private ICollector<InfosModelCompPhasesQualifsAbstract> collector;
	
	// Objectifs
	private ArrayList<InfosModelObjectif> objectifs;
	
	// Panel des informations
	private JPanel jp_informations;

	protected final static String LABEL_COMPARATEUR_POINTS = "Nombre de points";
	protected final static String LABEL_COMPARATEUR_VICTOIRES = "Nombre de victoires";
	protected final static String LABEL_COMPARATEUR_OBJECTIF = "Nombre d'objectifs remportés";
	
	// Entrées
	protected JComboBox jcb_types;
	
	protected JComboBox jcb_objectifs;

	// Constructeur
	public JDComparateurAbstract(Window w_parent, String titre, ICollector<InfosModelCompPhasesQualifsAbstract> collector, ArrayList<InfosModelObjectif> objectifs) {
		// Appeller le constructeur du parent
		super(w_parent, titre);

		// Retenir le collector et les objectifs
		this.collector = collector;
		this.objectifs = objectifs;

		// Type de comparateur
		this.jp_contenu.add(ViewHelper.title("Type de critère", ViewHelper.H1));
		String[] types = { JDComparateurAbstract.LABEL_COMPARATEUR_POINTS, JDComparateurAbstract.LABEL_COMPARATEUR_VICTOIRES, JDComparateurAbstract.LABEL_COMPARATEUR_OBJECTIF };
		this.jcb_types = new JComboBox(types);
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
		
		this.jcb_objectifs = new JComboBox();
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

	// Implémentation de ok
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
