package org.contestorg.views;

import java.awt.GridLayout;
import java.awt.Window;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.contestorg.common.TrackableList;
import org.contestorg.controllers.ContestOrg;
import org.contestorg.infos.InfosModelCritereClassementAbstract;
import org.contestorg.infos.InfosModelCritereClassementGoalAverage;
import org.contestorg.infos.InfosModelCritereClassementNbPoints;
import org.contestorg.infos.InfosModelCritereClassementNbVictoires;
import org.contestorg.infos.InfosModelCritereClassementQuantiteObjectif;
import org.contestorg.infos.InfosModelObjectif;
import org.contestorg.interfaces.IFournisseur;
import org.contestorg.interfaces.ITrackableListListener;

/**
 * Panel de configuration des points
 */
@SuppressWarnings("serial")
public class JPConfigurationPoints extends JPConfigurationAbstract implements ITrackableListListener<InfosModelObjectif>, ChangeListener
{
	/** TableModel de la liste des objectifs */
	private TMObjectifs tm_objectifs;

	/** TableModel de la liste des critères de classement */
	private TMCriteresClassement tm_criteresClassement;
	
	// Points en fonction des résultats
	
	/** Points de victoire */
	protected JTextField jtf_scores_resultats_pvictoire = new JTextField();
	
	/** Points d'égalité */
	protected JTextField jtf_scores_resultats_pegalite = new JTextField();
	
	/** Egalité activé */
	protected JRadioButton jrb_egalite_activee_oui = new JRadioButton("Oui", true);
	
	/** Egalité désactivée */
	protected JRadioButton jrb_egalite_activee_non = new JRadioButton("Non");
	
	/** Points de défaite */
	protected JTextField jtf_scores_resultats_pdefaite = new JTextField();
	
	/** Points de forfait */
	protected JTextField jtf_scores_resultats_pforfait = new JTextField();

	/**
	 * Constructeur
	 * @param w_parent fenêtre parent
	 */
	public JPConfigurationPoints(Window w_parent) {
		// Appeller le constructeur du parent
		super(w_parent);

		// En fonction du résultat
		this.jp_contenu.add(ViewHelper.title("En fonction du résultat", ViewHelper.H1));
		
		ButtonGroup bg_egalite_activee = new ButtonGroup();
		bg_egalite_activee.add(this.jrb_egalite_activee_oui);
		bg_egalite_activee.add(this.jrb_egalite_activee_non);
		JPanel jp_egalite_activee = new JPanel(new GridLayout(1, 2));
		jp_egalite_activee.add(this.jrb_egalite_activee_oui);
		jp_egalite_activee.add(this.jrb_egalite_activee_non);
		
		JLabel[] jls_scores_resultat = { new JLabel("Points de victoire : "), new JLabel("Points d'égalité : "), new JLabel("Egalité activée : "), new JLabel("Points de défaite : "), new JLabel("Points de forfait : ") };
		JComponent[] jcs_scores_resultat = { this.jtf_scores_resultats_pvictoire, this.jtf_scores_resultats_pegalite, jp_egalite_activee, this.jtf_scores_resultats_pdefaite, this.jtf_scores_resultats_pforfait };
		this.jp_contenu.add(ViewHelper.inputs(jls_scores_resultat, jcs_scores_resultat));
		this.jp_contenu.add(Box.createVerticalStrut(8));
		
		// Ecouter l'activation ou non de l'égalié
		this.jrb_egalite_activee_non.addChangeListener(this);
		this.jrb_egalite_activee_oui.addChangeListener(this);

		// Liste des objectifs
		this.jp_contenu.add(ViewHelper.title("Liste des objectifs", ViewHelper.H1));
		this.tm_objectifs = new TMObjectifs(this.w_parent);
		this.tm_objectifs.addValidator(ContestOrg.get().getObjectifsValidator());
		this.jp_contenu.add(new JPTable<InfosModelObjectif>(this.w_parent, this.tm_objectifs, true, true, true, true, true));

		// Ecouter la liste des objectifs
		this.tm_objectifs.addITrackableListListener(this);
		
		// Critères de classement
		this.jp_contenu.add(ViewHelper.title("Critères de classement", ViewHelper.H1));
		final TMObjectifs tm_objectifs = this.tm_objectifs;
		this.tm_criteresClassement = new TMCriteresClassement(this.w_parent,new IFournisseur<ArrayList<InfosModelObjectif>>() {
			// Fournir les objectifs
			@Override
			public ArrayList<InfosModelObjectif> get () {
				return tm_objectifs.getModifies();
			}
		});
		this.tm_criteresClassement.addValidator(ContestOrg.get().getCriteresClassementValidator());
		this.jp_contenu.add(new JPTable<InfosModelCritereClassementAbstract>(this.w_parent, this.tm_criteresClassement, true, true, true, true, true));
		
		// Placer les valeurs par défaut
		this.jtf_scores_resultats_pvictoire.setText("2.0");
		this.jtf_scores_resultats_pegalite.setText("1.0");
		this.jtf_scores_resultats_pdefaite.setText("0.0");
		this.jtf_scores_resultats_pforfait.setText("0.0");
		
		// Placer des critères de classement par défaut
		this.tm_criteresClassement.add(new InfosModelCritereClassementNbPoints(false));
		this.tm_criteresClassement.add(new InfosModelCritereClassementNbVictoires(false));
	}

	/**
	 * Récupérer les points de victoire
	 * @return points de victoire
	 */
	public float getPointsVictoire () {
		return Float.parseFloat(this.jtf_scores_resultats_pvictoire.getText().trim());
	}
	
	/**
	 * Récupérer les points d'égalité
	 * @return points d'égalité
	 */
	public float getPointsEgalite () {
		return Float.parseFloat(this.jtf_scores_resultats_pegalite.getText().trim());
	}
	
	/**
	 * Savoir si l'égalite est activée
	 * @return égalité activée ?
	 */
	public boolean isEgaliteActivee () {
		return this.jrb_egalite_activee_oui.isSelected();
	}
	
	/**
	 * Récupérer les points de défaite
	 * @return points de défaite
	 */
	public float getPointsDefaite () {
		return Float.parseFloat(this.jtf_scores_resultats_pdefaite.getText().trim());
	}
	
	/**
	 * Récupérer les points de forfait
	 * @return points de forfait
	 */
	public float getPointsForfait () {
		return Float.parseFloat(this.jtf_scores_resultats_pforfait.getText().trim());
	}
	
	/**
	 * Récupérer la liste des objectifs
	 * @return liste des objectifs
	 */
	public TrackableList<InfosModelObjectif> getObjectifs() {
		return new TrackableList<InfosModelObjectif>(this.tm_objectifs);
	}
	
	/**
	 * Récupérer la liste des critères de classement
	 * @return liste des critères de classement
	 */
	public TrackableList<InfosModelCritereClassementAbstract> getComparacteurs() {
		return new TrackableList<InfosModelCritereClassementAbstract>(this.tm_criteresClassement);
	}
	
	/**
	 * Définir les points de victoire
	 * @param points définir les points de victoire
	 */
	public void setPointsVictoire(double points) {
		this.jtf_scores_resultats_pvictoire.setText(String.valueOf(points));
	}
	
	/**
	 * Définir les points d'égalité
	 * @param points points d'égalité
	 */
	public void setPointsEgalite(double points) {
		this.jtf_scores_resultats_pegalite.setText(String.valueOf(points));
	}
	
	/**
	 * Définir si l'égalité est activée
	 * @param egaliteActivee égalité activée ?
	 */
	public void setIsEgaliteActivee(boolean egaliteActivee) {
		this.jrb_egalite_activee_oui.setSelected(egaliteActivee);
		this.jrb_egalite_activee_non.setSelected(!egaliteActivee);
	}
	
	/**
	 * Définir les points de défaite
	 * @param points points de défaite
	 */
	public void setPointsDefaite(double points) {
		this.jtf_scores_resultats_pdefaite.setText(String.valueOf(points));
	}
	
	/**
	 * Définir les points de forfait
	 * @param points points de forfait
	 */
	public void setPointsForfait(double points) {
		this.jtf_scores_resultats_pforfait.setText(String.valueOf(points));
	}
	
	/**
	 * Définir la liste des objectifs
	 * @param objectifs définir les liste des objectifs
	 */
	public void setObjectifs(ArrayList<InfosModelObjectif> objectifs) {
		this.tm_objectifs.fill(objectifs);
	}
	
	/**
	 * Définir la liste des critères de classement
	 * @param criteresClassement liste des critères de classement
	 */
	public void setCriteresClassement(ArrayList<InfosModelCritereClassementAbstract> criteresClassement) {
		this.tm_criteresClassement.fill(criteresClassement);
	}
	
	/**
	 * @see JPConfigurationAbstract#check()
	 */
	public boolean check () {
		// Booléen d'erreur
		boolean erreur = false;
		
		// Valider les points
		if (ViewHelper.empty(this.jtf_scores_resultats_pvictoire, this.jtf_scores_resultats_pegalite, this.jtf_scores_resultats_pdefaite)) {
			// Message d'erreur
			ViewHelper.derror(this.w_parent, "Points et classement", "Les points de victoire, d'égalité et de défaite n'ont pas tous été précisés.");
			erreur = true;
		} else {
			try {
				// Conversion de données
				Float.parseFloat(this.jtf_scores_resultats_pvictoire.getText().trim());
				Float.parseFloat(this.jtf_scores_resultats_pegalite.getText().trim());
				Float.parseFloat(this.jtf_scores_resultats_pdefaite.getText().trim());
				Float.parseFloat(this.jtf_scores_resultats_pforfait.getText().trim());
			} catch (NumberFormatException exception) {
				// Message d'erreur
				ViewHelper.derror(this.w_parent, "Points et classement", "Les points de victoire, d'égalité et de défaite doivent être des nombres décimales.");
				erreur = true;
			}
		}
		
		// Valider les classements
		if(this.tm_criteresClassement.getRowCount() == 0) {
			// Message d'erreur
			ViewHelper.derror(this, "Points et classement", "Les critères de classement doivent avoir au moins un critère.");
			erreur = true;
		}

		// Retourner true si les données sont correctes
		return !erreur;
	}
	
	// Implémentation de IListListener
	@Override
	public void addEvent (int row, InfosModelObjectif added) { }
	@Override
	public void updateEvent (int row, InfosModelObjectif before, InfosModelObjectif after) {
		// Modifier les critères de classement correspondants à l'objectif
		for(InfosModelCritereClassementAbstract critere : this.getCriteresClassementParObjectif(before)) {
			if(critere instanceof InfosModelCritereClassementQuantiteObjectif) {
				this.tm_criteresClassement.update(this.tm_criteresClassement.getRow(critere), new InfosModelCritereClassementQuantiteObjectif(critere.isInverse(),after));
			} else if(critere instanceof InfosModelCritereClassementGoalAverage) {
				this.tm_criteresClassement.update(this.tm_criteresClassement.getRow(critere), new InfosModelCritereClassementGoalAverage(critere.isInverse(),((InfosModelCritereClassementGoalAverage)critere).getType(),((InfosModelCritereClassementGoalAverage)critere).getMethode(),((InfosModelCritereClassementGoalAverage)critere).getDonnee(),after));
			}
		}
	}
	@Override
	public void removeEvent (int row, InfosModelObjectif deleted) {
		// Supprimer les critères de classement correspondants à l'objectif
		for(InfosModelCritereClassementAbstract critereClassement : this.getCriteresClassementParObjectif(deleted)) {
			this.tm_criteresClassement.remove(this.tm_criteresClassement.getRow(critereClassement));
		}
	}
	
	/**
	 * Chercher la liste des critères de classement pour un objectif donné
	 * @param objectif objectif
	 * @return liste des critères de classement de l'objectif
	 */
	private ArrayList<InfosModelCritereClassementAbstract> getCriteresClassementParObjectif(InfosModelObjectif objectif) {
		// Initialiser la liste des critères de classement correspondants
		ArrayList<InfosModelCritereClassementAbstract> criteresClassement = new ArrayList<InfosModelCritereClassementAbstract>();
		
		// Pour chaque critère de classement du TableModel
		for(InfosModelCritereClassementAbstract critereClassement : this.tm_criteresClassement) {
			// Vérifier s'il s'agit d'un critère de classement avec un objectif
			if(critereClassement instanceof InfosModelCritereClassementQuantiteObjectif) {
				// Vérifier si l'objectif supprimé correspond à l'objectif du critère de classement
				if(((InfosModelCritereClassementQuantiteObjectif)critereClassement).getObjectif().getNom().equals(objectif.getNom())) {
					// Ajouter le critère de classement dans la liste des critères de classement correspondants
					criteresClassement.add(critereClassement);
				}
			} else if(critereClassement instanceof InfosModelCritereClassementGoalAverage) {
				// Vérifier si l'objectif supprimé correspond à l'objectif du critère de classement
				if(((InfosModelCritereClassementGoalAverage)critereClassement).getDonnee() == InfosModelCritereClassementGoalAverage.DONNEE_QUANTITE_OBJECTIF && ((InfosModelCritereClassementGoalAverage)critereClassement).getObjectif().getNom().equals(objectif.getNom())) {
					// Ajouter le critère de classement dans la liste des critères de classement correspondants
					criteresClassement.add(critereClassement);
				}
			}
		}
		
		// Retourner la liste des critères de classement correspondants
		return criteresClassement;
	}

	/**
	 * @see ChangeListener#stateChanged(ChangeEvent)
	 */
	@Override
	public void stateChanged (ChangeEvent event) {
		this.jtf_scores_resultats_pegalite.setEnabled(this.jrb_egalite_activee_oui.isSelected());
		if(this.jrb_egalite_activee_non.isSelected()) {
			this.jtf_scores_resultats_pegalite.setText("0.0");
		}
	}
}
