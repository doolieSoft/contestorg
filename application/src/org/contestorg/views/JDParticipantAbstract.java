package org.contestorg.views;

import java.awt.Window;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

import org.contestorg.common.Pair;
import org.contestorg.common.Quintuple;
import org.contestorg.common.TrackableList;
import org.contestorg.controllers.ContestOrg;
import org.contestorg.infos.InfosModelConcours;
import org.contestorg.infos.InfosModelParticipant;
import org.contestorg.infos.InfosModelPrix;
import org.contestorg.infos.InfosModelPropriete;
import org.contestorg.infos.InfosModelProprietePossedee;
import org.contestorg.interfaces.ICollector;

/**
 * Boîte de dialogue de création/édition d'un participant
 */
@SuppressWarnings("serial")
public abstract class JDParticipantAbstract extends JDPattern
{	
	/** Collecteur des informations du participant */
	private ICollector<Quintuple<String,String,InfosModelParticipant,TrackableList<Pair<String,InfosModelProprietePossedee>>,TrackableList<String>>> collector;
	
	/** Panel des catégories et poules */
	protected JPCategoriePoule jp_categoriePoule = new JPCategoriePoule();
	
	// Entrées
	
	/** Nom */
	protected JTextField jtf_nom = new JTextField();
	
	/** Stand */
	protected JTextField jtf_stand = new JTextField();
	
	/** Ville */
	protected JTextField jtf_ville = new JTextField();
	
	/** Statut */
	protected JComboBox<String> jcb_statut = new JComboBox<String>();
	
	/** Détails */
	protected JTextArea jta_details = new JTextArea();
	
	/** Prix */
	protected JList<String> jl_prix;
	
	/** Propriétés */
	protected JTextField[] jtfs_proprietes;
	
	/** Anciens prix */
	protected TrackableList<String> prix = new TrackableList<String>();
	
	/** Anciennes propriétés possédées */
	protected TrackableList<Pair<String, InfosModelProprietePossedee>> proprietesPossedees = new TrackableList<Pair<String,InfosModelProprietePossedee>>();

	/**
	 * Constructeur
	 * @param w_parent fenêtre parent
	 * @param titre titre de la boîte de dialogue
	 * @param collector collecteur des informations du participant
	 */
	public JDParticipantAbstract(Window w_parent, String titre, ICollector<Quintuple<String,String,InfosModelParticipant,TrackableList<Pair<String,InfosModelProprietePossedee>>,TrackableList<String>>> collector) {
		// Appeller le constructeur du parent
		super(w_parent, titre);
		
		// Retenir le collector
		this.collector = collector;
		
		// Catégorie et poule
		this.jp_contenu.add(this.jp_categoriePoule);
		
		// Informations sur le participant
		this.jp_contenu.add(ViewHelper.title(ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "Informations sur l'équipe" : "Informations sur le joueur", ViewHelper.H1));
		
		for(InfosModelParticipant.Statut statut : InfosModelParticipant.Statut.values()) {
			this.jcb_statut.addItem(ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? statut.getNomEquipe() : statut.getNomJoueur());
		}
		
		this.jta_details.setLineWrap(true);
		this.jta_details.setWrapStyleWord(true);
		this.jta_details.setRows(5);
		this.jta_details.setColumns(30);
		
		JLabel[] jls_informations = { new JLabel("Nom : "), new JLabel("Stand : "), new JLabel("Ville : "), new JLabel("Statut : "), new JLabel("Détails : ") };
		JComponent[] jcs_informations = { this.jtf_nom, this.jtf_stand, this.jtf_ville, this.jcb_statut, new JScrollPane(this.jta_details) };
		this.jp_contenu.add(ViewHelper.inputs(jls_informations, jcs_informations));

		// Propriétés personnalisées
		ArrayList<InfosModelPropriete> proprietesDisponibles = ContestOrg.get().getCtrlParticipants().getListeProprietes();
		if(proprietesDisponibles.size() > 0) {
			this.jp_contenu.add(ViewHelper.title("Propriétés personnalisées", ViewHelper.H1));
			JLabel[] jls_proprietes = new JLabel[proprietesDisponibles.size()];
			this.jtfs_proprietes = new JTextField[proprietesDisponibles.size()];
			for(int i=0;i<proprietesDisponibles.size();i++) {
				jls_proprietes[i] = new JLabel(proprietesDisponibles.get(i).getNom()+" : ");
				this.jtfs_proprietes[i] = new JTextField();
			}
			this.jp_contenu.add(ViewHelper.inputs(jls_proprietes, this.jtfs_proprietes));
		}
		
		// Prix
		ArrayList<InfosModelPrix> prixDisponibles = ContestOrg.get().getCtrlParticipants().getListePrix();
		if(prixDisponibles.size() > 0) {
			this.jp_contenu.add(ViewHelper.title("Prix remportés",ViewHelper.H1));
			String[] noms = new String[prixDisponibles.size()];
			for(int i=0;i<prixDisponibles.size();i++) {
				noms[i] = prixDisponibles.get(i).getNom();
			}
			this.jl_prix = new JList<String>(noms);
			this.jl_prix.setVisibleRowCount(4);
			this.jl_prix.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			this.jp_contenu.add(new JScrollPane(this.jl_prix));
			this.jp_contenu.add(Box.createVerticalStrut(5));
			this.jp_contenu.add(ViewHelper.pinformation("Maintenez \"ctrl\" enfoncée pour séléctionner plusieurs prix."));
		}
		
		// Pack
		this.pack();
	}
	
	/**
	 * @see Window#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean visible) {
		// Appeller le setVisible du parent
		super.setVisible(visible);
		
		// Focus sur le nom du participant
		if(visible) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run () {
					jtf_nom.requestFocus();
				}
			});
		}
	}

	/**
	 * @see JDPattern#ok()
	 */
	@Override
	protected void ok () {
		// Récupérer les données
		String nom = this.jtf_nom.getText().trim();
		String stand = this.jtf_stand.getText().trim();
		String ville = this.jtf_ville.getText().trim();
		InfosModelParticipant.Statut statut = InfosModelParticipant.Statut.values()[this.jcb_statut.getSelectedIndex()];
		String details = this.jta_details.getText().trim();
		
		// Vérifier les données
		boolean erreur = false;
		if(nom.isEmpty()) {
			// Message d'erreur
			erreur = true;
			ViewHelper.derror(this, ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "Le nom de l'équipe n'est pas précisé." : "Le nom du joueur n'est pas précisé.");
		} else if(!this.checkNomParticipant()) {
			// Erreur
			erreur = true;
			ViewHelper.derror(this, ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "Une équipe existante possède déjà le même nom." : "Un joueur existant possède déjà le même nom.");
		}

		// Vérifier les propriétés de participant
		ArrayList<InfosModelPropriete> proprietesDisponibles = ContestOrg.get().getCtrlParticipants().getListeProprietes();
		for(int i=0;i<proprietesDisponibles.size();i++) {
			try {
				// Récupérer la valeur de la propriété de participant
				String valeur = this.jtfs_proprietes[i].getText().trim();
				
				// Vérifier si la valeur est donnée
				if(proprietesDisponibles.get(i).isObligatoire() && valeur.isEmpty()) {
					// Erreur
					erreur = true;
					ViewHelper.derror(this, "La propriété \""+proprietesDisponibles.get(i).getNom()+"\" est obligatoire.");
				} else if(!valeur.isEmpty()) {
					// Vérifier le "type" de la valeur
					switch(proprietesDisponibles.get(i).getType()) {
						case InfosModelPropriete.TYPE_INT :
							Integer.parseInt(valeur);
							break;
						case InfosModelPropriete.TYPE_FLOAT :
							Float.parseFloat(valeur);
							break;
					}
				}
			} catch(NumberFormatException e) {
				// Erreur
				erreur = true;
				ViewHelper.derror(this, "La propriété \""+proprietesDisponibles.get(i).getNom()+"\" doit être un nombre "+(proprietesDisponibles.get(i).getType() == InfosModelPropriete.TYPE_INT ? "entier" : "décimal")+".");
			}
		}
		
		// Transmettre les données au collector
		if(!erreur) {
			// Mettre à jour la liste des propriétés de participant
			for(int i=0;i<proprietesDisponibles.size();i++) {
				// Récupérer la valeur de la propriété de participant
				String valeur = this.jtfs_proprietes[i].getText().trim();
				
				// Vérifier si la propriété été déjà donnée
				boolean ancien = false; int index = 0;
				for(Pair<String, InfosModelProprietePossedee> proprietePossedee : this.proprietesPossedees) {
					if(proprietePossedee.getFirst().equals(proprietesDisponibles.get(i))) {
						ancien = true;
					} else if(!ancien) {
						index++;
					}
				}
				
				// Vérifier si la propriété est obligatoire
				if(!valeur.isEmpty()) {
					// Ajouter la propriété de participant
					this.proprietesPossedees.add(new Pair<String, InfosModelProprietePossedee>(proprietesDisponibles.get(i).getNom(), new InfosModelProprietePossedee(valeur)));
				} else if(ancien) {
					// Supprimer la propriété de participant
					this.proprietesPossedees.remove(index);
				}
			}

			// Mettre à jour la liste des prix
			ArrayList<InfosModelPrix> prixDisponibles = ContestOrg.get().getCtrlParticipants().getListePrix();
			for(int i=0;i<prixDisponibles.size();i++) {
				// Vérifier si le prix est séléctionné
				boolean nouveau = this.jl_prix.isSelectedIndex(i);
				
				// Vérifier si le prix été déjà séléctionné
				boolean ancien = false; int index = 0;
				for(String prix : this.prix) {
					if(prixDisponibles.get(i).equals(prix)) {
						ancien = true;
					} else if(!ancien) {
						index++;
					}
				}
				
				// Ajouter/Supprimer le prix
				if(nouveau && !ancien) { // Ajouter le prix
					this.prix.add(prixDisponibles.get(i).getNom());
				} else if(!nouveau && ancien) { // Supprimer le prix
					this.prix.remove(index);
				}
			}
			
			// Transmettre les données au collector
			this.collector.collect(new Quintuple<String, String, InfosModelParticipant, TrackableList<Pair<String,InfosModelProprietePossedee>>, TrackableList<String>>(this.jp_categoriePoule.getCategorie(), this.jp_categoriePoule.getPoule(), new InfosModelParticipant(stand, nom, ville, statut, details), this.proprietesPossedees, this.prix));
		}
	}
	
	/**
	 * Vérifier la validité du nom du participant
	 * @return nom du participant valide ?
	 */
	protected abstract boolean checkNomParticipant();

	/**
	 * @see JDPattern#quit()
	 */
	@Override
	protected void quit () {
		// Annuler
		this.collector.cancel();
	}
	
}
