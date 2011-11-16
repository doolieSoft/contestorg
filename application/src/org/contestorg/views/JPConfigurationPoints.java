package org.contestorg.views;


import java.awt.Window;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.contestorg.common.TrackableList;
import org.contestorg.controlers.ContestOrg;
import org.contestorg.infos.InfosModelCompPhasesQualifsAbstract;
import org.contestorg.infos.InfosModelCompPhasesQualifsObjectif;
import org.contestorg.infos.InfosModelCompPhasesQualifsPoints;
import org.contestorg.infos.InfosModelCompPhasesQualifsVictoires;
import org.contestorg.infos.InfosModelConcours;
import org.contestorg.infos.InfosModelObjectif;
import org.contestorg.interfaces.IFournisseur;
import org.contestorg.interfaces.IListListener;



@SuppressWarnings("serial")
public class JPConfigurationPoints extends JPConfigurationAbstract implements IListListener<InfosModelObjectif>
{
	// Points en fonction des résultats
	protected JTextField jtf_scores_resultats_pvictoire = new JTextField();
	protected JTextField jtf_scores_resultats_pegalite = new JTextField();
	protected JTextField jtf_scores_resultats_pdefaite = new JTextField();

	// TableModel de la liste des objectifs
	private TMObjectifs tm_objectifs;

	// TableModel de la liste des comparateurs
	private TMComparateurs tm_comparateurs;

	// Constructeur
	public JPConfigurationPoints(Window w_parent) {
		// Appeller le constructeur du parent
		super(w_parent);

		// En fonction du résultat
		this.jp_contenu.add(ViewHelper.title("En fonction du résultat", ViewHelper.H1));
		JLabel[] jls_scores_resultat = { new JLabel("Points de victoire : "), new JLabel("Points d'égalité : "), new JLabel("Points de défaite : ") };
		JComponent[] jcs_scores_resultat = { this.jtf_scores_resultats_pvictoire, this.jtf_scores_resultats_pegalite, this.jtf_scores_resultats_pdefaite };
		this.jp_contenu.add(ViewHelper.inputs(jls_scores_resultat, jcs_scores_resultat));
		this.jp_contenu.add(Box.createVerticalStrut(8));
		this.jp_contenu.add(ViewHelper.pinformation("Aucun point n'est attribué en cas de forfait"));

		// Liste des objectifs
		this.jp_contenu.add(ViewHelper.title("Liste des objectifs", ViewHelper.H1));
		this.tm_objectifs = new TMObjectifs(this.w_parent);
		this.tm_objectifs.addValidator(ContestOrg.get().getObjectifsValidator());
		this.jp_contenu.add(new JPTable<InfosModelObjectif>(this.w_parent, this.tm_objectifs, true, true, true, true, true));

		// Ecouter la liste des objectifs
		this.tm_objectifs.addIListListener(this);
		
		// Ordre de classement
		this.jp_contenu.add(ViewHelper.title("Ordre de classement", ViewHelper.H1));
		final TMObjectifs tm_objectifs = this.tm_objectifs;
		this.tm_comparateurs = new TMComparateurs(this.w_parent,new IFournisseur<ArrayList<InfosModelObjectif>>() {
			// Fournir les objectifs
			@Override
			public ArrayList<InfosModelObjectif> get () {
				return tm_objectifs.getModifies();
			}
		});
		this.tm_comparateurs.addValidator(ContestOrg.get().getComparateursValidator());
		this.jp_contenu.add(new JPTable<InfosModelCompPhasesQualifsAbstract>(this.w_parent, this.tm_comparateurs, true, false, true, true, true));
		
		// Placer les valeurs par défaut
		InfosModelConcours defaut = InfosModelConcours.defaut();
		this.jtf_scores_resultats_pvictoire.setText(String.valueOf(defaut.getPointsVictoire()));
		this.jtf_scores_resultats_pegalite.setText(String.valueOf(defaut.getPointsEgalite()));
		this.jtf_scores_resultats_pdefaite.setText(String.valueOf(defaut.getPointsDefaite()));
		
		// Placer des critères de classement par défaut
		this.tm_comparateurs.add(new InfosModelCompPhasesQualifsPoints());
		this.tm_comparateurs.add(new InfosModelCompPhasesQualifsVictoires());
	}

	// Getters
	public float getPointsVictoire () {
		return Float.parseFloat(this.jtf_scores_resultats_pvictoire.getText().trim());
	}
	public float getPointsEgalite () {
		return Float.parseFloat(this.jtf_scores_resultats_pegalite.getText().trim());
	}
	public float getPointsDefaite () {
		return Float.parseFloat(this.jtf_scores_resultats_pdefaite.getText().trim());
	}
	public TrackableList<InfosModelObjectif> getObjectifs() {
		return new TrackableList<InfosModelObjectif>(this.tm_objectifs);
	}
	public TrackableList<InfosModelCompPhasesQualifsAbstract> getComparacteurs() {
		return new TrackableList<InfosModelCompPhasesQualifsAbstract>(this.tm_comparateurs);
	}
	
	// Setters
	public void setPointsVictoire(double points) {
		this.jtf_scores_resultats_pvictoire.setText(String.valueOf(points));
	}
	public void setPointsEgalite(double points) {
		this.jtf_scores_resultats_pegalite.setText(String.valueOf(points));
	}
	public void setPointsDefaite(double points) {
		this.jtf_scores_resultats_pdefaite.setText(String.valueOf(points));
	}
	public void setObjectifs(ArrayList<InfosModelObjectif> objectifs) {
		this.tm_objectifs.fill(objectifs);
	}
	public void setComparateurs(ArrayList<InfosModelCompPhasesQualifsAbstract> comparateurs) {
		this.tm_comparateurs.fill(comparateurs);
	}
	
	// Vérifier la validité des données
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
				float pvictoire = Float.parseFloat(this.jtf_scores_resultats_pvictoire.getText().trim());
				float pegalite = Float.parseFloat(this.jtf_scores_resultats_pegalite.getText().trim());
				float pdefaite = Float.parseFloat(this.jtf_scores_resultats_pdefaite.getText().trim());
				
				// Vérifier que les valeurs sont logique
				if(pvictoire < pegalite) {
					// Message d'erreur
					ViewHelper.derror(this.w_parent, "Points et classement", "Les points de victoire doivent être inférieurs ou égaux aux points d'égalité.");
					erreur = true;
				}
				if(pegalite < pdefaite) {
					// Message d'erreur
					ViewHelper.derror(this.w_parent, "Points et classement", "Les points d'égalité doivent être inférieurs ou égaux aux points de défaite.");
					erreur = true;
				}
			} catch (NumberFormatException exception) {
				// Message d'erreur
				ViewHelper.derror(this.w_parent, "Points et classement", "Les points de victoire, d'égalité et de défaite doivent être des nombres décimales.");
				erreur = true;
			}
		}
		
		// Valider les classements
		if(this.tm_comparateurs.getRowCount() == 0) {
			// Message d'erreur
			ViewHelper.derror(this, "Points et classement", "L'ordre de classement des équipes doit avoir au moins un critère.");
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
		// Modifier les comparateurs correspondants à l'objectif
		for(InfosModelCompPhasesQualifsAbstract comparateur : this.getCompPhasesQualifsObjectif(before)) {
			this.tm_comparateurs.update(this.tm_comparateurs.getRow(comparateur), new InfosModelCompPhasesQualifsObjectif(after));
		}
	}
	@Override
	public void removeEvent (int row, InfosModelObjectif deleted) {
		// Supprimer les comparateurs correspondants à l'objectif
		for(InfosModelCompPhasesQualifsAbstract comparateur : this.getCompPhasesQualifsObjectif(deleted)) {
			this.tm_comparateurs.remove(this.tm_comparateurs.getRow(comparateur));
		}
	}
	
	// Chercher la liste des comparateurs d'objectifs pour un objectif donnée
	private ArrayList<InfosModelCompPhasesQualifsObjectif> getCompPhasesQualifsObjectif(InfosModelObjectif objectif) {
		// Initialiser la liste des comparateurs correspondants
		ArrayList<InfosModelCompPhasesQualifsObjectif> comparateurs = new ArrayList<InfosModelCompPhasesQualifsObjectif>();
		
		// Pour chaque comparateur du TableModel
		for(InfosModelCompPhasesQualifsAbstract comparateur : this.tm_comparateurs) {
			// Vérifier s'il s'agit d'un comparateur d'objectif
			if(comparateur instanceof InfosModelCompPhasesQualifsObjectif) {
				// Vérifier si l'objectif supprimé correspond à l'objectif du comparateur
				if(((InfosModelCompPhasesQualifsObjectif)comparateur).getObjectif().getNom().equals(objectif.getNom())) {
					// Ajouter le comparateur dans la liste des comparateurs correspondants
					comparateurs.add((InfosModelCompPhasesQualifsObjectif)comparateur);
				}
			}
		}
		
		// Retourner la liste des comparateurs correspondants
		return comparateurs;
	}
}
