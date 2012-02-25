package org.contestorg.views;

import java.awt.Window;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.contestorg.common.TrackableList;
import org.contestorg.common.Triple;
import org.contestorg.controllers.ContestOrg;
import org.contestorg.infos.InfosModelEmplacement;
import org.contestorg.infos.InfosModelHoraire;
import org.contestorg.infos.InfosModelLieu;
import org.contestorg.interfaces.ICollector;

/**
 * Boîte de dialogue de création/édition d'un lieu
 */
@SuppressWarnings("serial")
public abstract class JDLieuAbstract extends JDPattern
{

	/** TableModel de la liste des horaires */
	protected TMHoraires tm_horaires;

	/** TableModel de la liste des emplacements */
	protected TMEmplacements tm_emplacements;

	/** Collector d'informations sur le lieu */
	protected ICollector<Triple<InfosModelLieu,TrackableList<InfosModelEmplacement>,TrackableList<InfosModelHoraire>>> collector;

	// Entrées
	
	/** Nom */
	protected JTextField jtf_lieu_nom = new JTextField();
	
	/** Lieu */
	protected JTextField jtf_lieu_lieu = new JTextField();
	
	/** Email */
	protected JTextField jtf_lieu_email = new JTextField();
	
	/** Téléphone */
	protected JTextField jtf_lieu_telephone = new JTextField();
	
	/** Description */
	protected JTextArea jta_lieu_description = new JTextArea();

	/**
	 * Constructeur
	 * @param w_parent fenêtre parent
	 * @param titre titre de la boîte de dialogue
	 * @param collector collecteur des informations du lieu
	 */
	public JDLieuAbstract(Window w_parent, String titre, ICollector<Triple<InfosModelLieu,TrackableList<InfosModelEmplacement>,TrackableList<InfosModelHoraire>>> collector) {
		// Appeller le constructeur du parent
		super(w_parent, titre);

		// Retenir le collector
		this.collector = collector;
		
		// Information sur le lieu
		this.jp_contenu.add(ViewHelper.title("Informations sur le lieu", ViewHelper.H1));
		this.jta_lieu_description.setLineWrap(true);
		this.jta_lieu_description.setWrapStyleWord(true);
		this.jta_lieu_description.setRows(5);
		JLabel[] jls_lieu = { new JLabel("Nom : "), new JLabel("Lieu(s) : "), new JLabel("Email(s) : "), new JLabel("Téléphone(s) : "), new JLabel("Description : ") };
		JComponent[] jcs_lieu = { this.jtf_lieu_nom, this.jtf_lieu_lieu, this.jtf_lieu_email, this.jtf_lieu_telephone, new JScrollPane(this.jta_lieu_description) };
		this.jp_contenu.add(ViewHelper.inputs(jls_lieu, jcs_lieu));
		
		// Liste des emplacements
		this.jp_contenu.add(ViewHelper.title("Liste des horaires", ViewHelper.H1));
		this.tm_horaires = new TMHoraires(this);			
		this.tm_horaires.addValidator(ContestOrg.get().getHorairesValidator());
		this.jp_contenu.add(new JPTable<InfosModelHoraire>(w_parent, this.tm_horaires, 4));
		this.jp_contenu.add(ViewHelper.pinformation("Horaires sur lequels peuvent se dérouler les matchs."));
		
		// Liste des horaires
		this.jp_contenu.add(ViewHelper.title("Liste des emplacements", ViewHelper.H1));
		this.tm_emplacements = new TMEmplacements(this);
		this.tm_emplacements.addValidator(ContestOrg.get().getEmplacementsValidator());
		this.jp_contenu.add(new JPTable<InfosModelEmplacement>(w_parent, this.tm_emplacements, 4));
		this.jp_contenu.add(ViewHelper.pinformation("Emplacements sur lequels peuvent se dérouler les matchs."));
		
		// Pack
		this.pack();
	}

	/**
	 * @see JDPattern#ok()
	 */
	@Override
	protected void ok () {
		// Récupérer les données
		String nom = this.jtf_lieu_nom.getText().trim();
		String lieu = this.jtf_lieu_lieu.getText().trim();
		String email = this.jtf_lieu_email.getText().trim();
		String telephone = this.jtf_lieu_telephone.getText().trim();
		String description = this.jta_lieu_description.getText().trim();

		// Vérifier les données
		boolean erreur = false;
		if(nom.isEmpty()) {
			// Message d'erreur
			erreur = true;
			ViewHelper.derror(this, "Le nom du lieu est vide.");
		}
		if(this.tm_emplacements.size() == 0) {
			// Message d'erreur
			erreur = true;
			ViewHelper.derror(this, "Un lieu doit au moins avoir un emplacement.");
		}
		
		// Envoyer les informations au collector
		if(!erreur) {
			this.collector.collect(new Triple<InfosModelLieu,TrackableList<InfosModelEmplacement>,TrackableList<InfosModelHoraire>>(new InfosModelLieu(nom, lieu, telephone, email, description), new TrackableList<InfosModelEmplacement>(this.tm_emplacements), new TrackableList<InfosModelHoraire>(this.tm_horaires)));
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

}
